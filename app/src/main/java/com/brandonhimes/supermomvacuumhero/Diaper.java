package com.brandonhimes.supermomvacuumhero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Diaper extends ThrownObject{
    private Bitmap diaperSprite;
    private Context mContext;

    public Diaper(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        diaperSprite = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.diaper_0);
    }

    public Bitmap getDiaperSprite() {
        return diaperSprite;
    }
}
