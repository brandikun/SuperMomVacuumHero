package com.brandonhimes.supermomvacuumhero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Trash extends ThrownObject{
    private Bitmap trashSprite;
    private Context mContext;

    public Trash(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        trashSprite = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.trash_0);
    }

    public Bitmap getTrashSprite() {
        return trashSprite;
    }
}
