package com.hyc.puzzlegame.bean;

import android.graphics.Bitmap;

/**
 * Created by hyc on 2017/3/12 10:20
 */

public class ImagePiece {

    private Bitmap bitmap;

    private int position;


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
