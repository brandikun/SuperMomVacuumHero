package com.brandonhimes.supermomvacuumhero;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;

public class ThrownObject {
    private Context mContext;
    private int x;
    private int y;
    private boolean hit;

    public ThrownObject(Context mContext) {
        this.mContext = mContext;
        Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        x = (int)(size.x * .9) - 50;
        y = (int)(size.y * .75);
    }

    public ThrownObject() {
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

    public Rect getBounds() {
        return new Rect(x , y, x + 100, y + 100);
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}
