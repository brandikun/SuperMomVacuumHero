package com.brandonhimes.supermomvacuumhero;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.view.Display;

public class Player {
    private Bitmap momSprite;
    private Context mContext;
    private int x;
    private int y;
    private int spriteFrame = 0;
    private static final int X_START = 400;
    private static final int Y_START = 900;
    private static final int TOTAL_RUN_FRAMES = 6;
    private int spriteId;

    public enum Direction {
        LEFT, RIGHT, STOPPED_LEFT, STOPPED_RIGHT
    }
    public static final int GROUND_Y = 900;
    public static final int JUMP_APEX = GROUND_Y - 400;
    public static int MAX_X;
    public static int SPRITE_WIDTH;
    public static int SPRITE_HEIGHT;
    private Direction direction;
    private Boolean jumpFlag = false;

    public Player(Context context) {
        x = X_START;
        y = Y_START;
        direction = Direction.STOPPED_RIGHT;
        momSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_mom_right_still);
        mContext = context;

        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        MAX_X = ((int)(size.x * .9));

        SPRITE_HEIGHT = momSprite.getHeight();
        SPRITE_WIDTH = momSprite.getWidth();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Bitmap getMomSprite() {
        return momSprite;
    }

    public void setMomSprite(Bitmap momSprite) {
        this.momSprite = momSprite;
    }

    public void chooseMomSprite() {
        switch(getDirection()) {
            case LEFT:
                if(jumpFlag) {
                    spriteId = mContext.getResources().getIdentifier("sprite_mom_jump_left_0", "drawable", mContext.getPackageName());
                } else {
                    if(spriteFrame < TOTAL_RUN_FRAMES - 1) spriteFrame++;
                    else spriteFrame = 0;
                    spriteId = mContext.getResources().getIdentifier("sprite_mom_left_" + spriteFrame, "drawable", mContext.getPackageName());
                }
                break;
            case RIGHT:
                if(jumpFlag) {
                    spriteId = mContext.getResources().getIdentifier("sprite_mom_jump_right_0", "drawable", mContext.getPackageName());
                } else {
                    if(spriteFrame < TOTAL_RUN_FRAMES - 1) spriteFrame++;
                    else spriteFrame = 0;
                    spriteId = mContext.getResources().getIdentifier("sprite_mom_right_" + spriteFrame, "drawable", mContext.getPackageName());
                }
                break;
            case STOPPED_RIGHT:
                spriteId = mContext.getResources().getIdentifier("sprite_mom_right_still", "drawable", mContext.getPackageName());
                break;
            case STOPPED_LEFT:
                spriteId = mContext.getResources().getIdentifier("sprite_mom_left_still", "drawable", mContext.getPackageName());
                break;
        }
        momSprite = BitmapFactory.decodeResource(mContext.getResources(), spriteId);
    }

    public Boolean getJumpFlag() {
        return jumpFlag;
    }

    public void setJumpFlag(Boolean jumpFlag) {
        this.jumpFlag = jumpFlag;
    }

    public Rect getBounds() {
        return new Rect(x + 20, y, x + SPRITE_WIDTH - 50, y + SPRITE_HEIGHT - 20);
    }

    public int getSpriteFrame() {
        return spriteFrame;
    }
}
