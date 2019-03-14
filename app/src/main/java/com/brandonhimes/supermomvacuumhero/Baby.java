package com.brandonhimes.supermomvacuumhero;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;

public class Baby {

    private Bitmap babySprite;
    private Context mContext;
    private int x;
    private int y;
    private static final int SIZE_DIFFERENCE_FROM_MOM = 120;
    private static final int Y_START = 900 + SIZE_DIFFERENCE_FROM_MOM;
    private int spriteNumber;
    private int spriteId;
    private boolean trashFlag;

    public Baby(Context context) {
        Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        x = ((int)(width * .9));
        y = Y_START;
        babySprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sprite_baby_idle_0);
        mContext = context;
        spriteNumber = 0;
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

    public Bitmap getBabySprite() {
        return babySprite;
    }

    public void setMomSprite(Bitmap momSprite) {
        this.babySprite = momSprite;
    }

    public void throwObject() {
        trashFlag = true;
        spriteNumber = 0;
    }

    public void cycleBabySprite() {
        if(trashFlag) {
            spriteId = mContext.getResources().getIdentifier("sprite_baby_throw_" + spriteNumber, "drawable", mContext.getPackageName());
            babySprite = BitmapFactory.decodeResource(mContext.getResources(), spriteId);
            if(spriteNumber < 5) {
                spriteNumber++;
            } else {
                trashFlag = false;
            }
        } else {
            if(spriteNumber == 0) {
                spriteNumber++;
                babySprite = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sprite_baby_idle_1);
            } else {
                spriteNumber = 0;
                babySprite = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.sprite_baby_idle_0);
            }
        }
    }

    public boolean getTrashFlag() {
        return trashFlag;
    }

    public int getSpriteNumber() {
        return spriteNumber;
    }
}

