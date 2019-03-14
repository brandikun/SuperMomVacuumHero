package com.brandonhimes.supermomvacuumhero;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameView extends SurfaceView implements Runnable {

    private Player player;
    private Baby baby;
    //use volatile boolean to ensure that changes written by any thread are observed
    AtomicBoolean isRunning = new AtomicBoolean(true);
    public Thread mainThread;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private int score;
    AtomicBoolean actionDownFlag = new AtomicBoolean(true);
    Thread loggingThread;
    Bitmap background;
    private int screenWidth;
    private int screenHeight;
    private int backgroundWidth;
    private int backgroundHeight;
    private Rect src;
    private Rect dest;
    private Context context;
    private List<ThrownObject> thrownObjects;
    String endText = "";

    //using 83 to achieve 12 FPS
    public static final int SLEEP_INTERVAL = 83;

    public GameView(Context context) {
        super(context);

        mainThread = new Thread();
        setFocusable(true);
        player = new Player(context);
        baby = new Baby(context);
        paint = new Paint();
        surfaceHolder = getHolder();
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background_0);
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;
        src = new Rect(0,0,background.getWidth()-1, background.getHeight()-1);
        dest = new Rect(0,0, screenWidth-1, screenHeight-1);
        thrownObjects = new ArrayList<>();
        this.context = context;
    }

    @Override
    public void run() {
        int frameCount = 1;
        Random random = new Random();
        MediaPlayer mp = MediaPlayer.create(context, R.raw.zapsplat_foley_footstep_single_wellington_boot_on_wood_001_24971);
        thrownObjects.clear();
        score = 0;
        //game loop
        while(isRunning.get()) {
            if(surfaceHolder.getSurface().isValid()) {
                thrownObjects.removeIf(t -> t.getX() <= -50 || t.isHit());
                for(ThrownObject t : thrownObjects) {
                    t.setX(t.getX() - 35);
                    if(t.getX() <= - 50 && t instanceof Trash) {
                        endText = "YOU LOSE. Tap to play again!";
                        isRunning.set(false);
                    }
                    if(t.getBounds().intersect(player.getBounds())) {
                        if(t instanceof Trash) {
                            t.setHit(true);
                            score++;
                            ((Activity)context).runOnUiThread(() -> ((MainActivity)context).updateScore(score));
                            if(score > 50) {
                                endText = "YOU WIN. Tap to play again!";
                                isRunning.set(true);
                            }
                        } else if(t instanceof Diaper) {
                            endText = "YOU LOSE. Tap to play again!";
                            isRunning.set(false);
                        }
                    }
                }
                if(baby.getTrashFlag()) {
                    baby.cycleBabySprite();
                    if(baby.getSpriteNumber() == 3) {
                        if((random.nextInt(11) + 1) > 3) {
                            Trash trash = new Trash(context);
                            thrownObjects.add(trash);
                        } else {
                            Diaper diaper = new Diaper(context);
                            thrownObjects.add(diaper);
                        }
                    }
                } else {
                    if(frameCount % 3 == 0) {
                        if((random.nextInt(11) + 1) > 8) {
                            baby.throwObject();
                        }
                        baby.cycleBabySprite();
                    }
                }
                canvas = surfaceHolder.lockCanvas();
                canvas.drawBitmap(background, src, dest, paint);
                player.chooseMomSprite();
                if((player.getDirection() == Player.Direction.LEFT || player.getDirection() == Player.Direction.RIGHT) && (player.getSpriteFrame() == 2 || player.getSpriteFrame() == 5) ) {
                    mp.start();
                }
                canvas.drawBitmap(player.getMomSprite(), player.getX(), player.getY(), paint);
                canvas.drawBitmap(baby.getBabySprite(), baby.getX(), baby.getY(), paint);
                for(ThrownObject t : thrownObjects) {
                    if(t instanceof Trash) {
                        canvas.drawBitmap(((Trash)t).getTrashSprite(), t.getX(), t.getY(), paint);
                    } else if(t instanceof Diaper) {
                        canvas.drawBitmap(((Diaper)t).getDiaperSprite(), t.getX(), t.getY(), paint);
                    }
                }
                surfaceHolder.unlockCanvasAndPost(canvas);
                if(frameCount < 12) frameCount++;
                else frameCount = 1;
            }
            try {
                mainThread.sleep(SLEEP_INTERVAL);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        ((Activity)context).runOnUiThread(() -> ((MainActivity)context).setGameEnd(endText));
        try {
            mainThread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        isRunning.set(false);
        try {
            mainThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isRunning.set(true);
        mainThread = new Thread(this);
        mainThread.start();
    }

    class ButtonHoldTask implements Runnable {
        MotionEvent event;
        ButtonHoldTask(MotionEvent event) {
            this.event = event;
        }
        public void run() {
            while(actionDownFlag.get()){
                if(event.getX() > player.getX() + 180 && !(player.getX() >= (Player.MAX_X - Player.SPRITE_WIDTH))) {
                    if(player.getDirection() != Player.Direction.RIGHT) {
                        player.setDirection(Player.Direction.RIGHT);
                    }
                    player.setX(player.getX() + 40);
                } else if(event.getX() < player.getX() + 60){
                    if(player.getDirection() != Player.Direction.LEFT) {
                        player.setDirection(Player.Direction.LEFT);
                    }
                    player.setX(player.getX() - 40);
                } else {
                    actionDownFlag.set(false);
                    player.setDirection(player.getDirection() == Player.Direction.RIGHT ? Player.Direction.STOPPED_RIGHT : Player.Direction.STOPPED_LEFT);
                }
                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        loggingThread = new Thread(new ButtonHoldTask(event));
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            actionDownFlag.set(true);
            loggingThread.start();
        }
        if(event.getAction()==MotionEvent.ACTION_UP){
            actionDownFlag.set(false);
            if(player.getDirection() == Player.Direction.RIGHT)
                player.setDirection(Player.Direction.STOPPED_RIGHT);
            else if(player.getDirection() == Player.Direction.LEFT)
                player.setDirection(Player.Direction.STOPPED_LEFT);
            try {
                loggingThread.join();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public void performJump() {
        player.setJumpFlag(true);
        Thread jumpThread = new Thread(() -> {
            while(player.getY() > Player.JUMP_APEX) {
                player.setY(player.getY() - 80);
                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while(player.getY() < Player.GROUND_Y) {
                player.setY(player.getY() + 50);
                try {
                    Thread.sleep(SLEEP_INTERVAL);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
            player.setJumpFlag(false);
        });
        jumpThread.start();
    }

    public Player getPlayer() {
        return player;
    }
}
