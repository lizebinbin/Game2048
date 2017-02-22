package com.example.mooreli.game2048;

import android.content.Context;

/**
 * Created by MooreLi on 2017/2/13.
 */

public class Number {
    private Context mContext;
    private int mIndexX, mIndexY;
    private int mValue;
    private int mColor = -1;
    private int mLocationX, mLocationY;

    public Number(Context context) {
        mContext = context;
    }

    public Number(Context context , int mIndexX, int mIndexY, int mValue) {
        mContext = context;
        this.mIndexX = mIndexX;
        this.mIndexY = mIndexY;
        this.mValue = mValue;
        judgeColor();
    }

    public int getIndexX() {
        return mIndexX;
    }

    public void setIndexX(int indexX) {
        this.mIndexX = indexX;
    }

    public int getIndexY() {
        return mIndexY;
    }

    public void setIndexY(int indexY) {
        this.mIndexY = indexY;
    }

    public int getmValue() {
        return mValue;
    }

    public void setmValue(int mValue) {
        this.mValue = mValue;
        judgeColor();
    }

    public int getmColor() {
        return mColor;
    }

    public int getmLocationY() {
        return mLocationY;
    }

    public void setmLocationY(int mLocationY) {
        this.mLocationY = mLocationY;
    }

    public int getmLocationX() {
        return mLocationX;
    }

    public void setmLocationX(int mLocationX) {
        this.mLocationX = mLocationX;
    }

    private void judgeColor() {
        switch (mValue) {
            case 2:
                mColor = mContext.getResources().getColor(R.color.color_2);
                break;
            case 4:
                mColor = mContext.getResources().getColor(R.color.color_4);
                break;
            case 8:
                mColor = mContext.getResources().getColor(R.color.color_8);
                break;
            case 32:
                mColor = mContext.getResources().getColor(R.color.color_32);
                break;
            case 64:
                mColor = mContext.getResources().getColor(R.color.color_64);
                break;
            case 128:
                mColor = mContext.getResources().getColor(R.color.color_128);
                break;
            case 256:
                mColor = mContext.getResources().getColor(R.color.color_256);
                break;
            case 512:
                mColor = mContext.getResources().getColor(R.color.color_512);
                break;
            case 1024:
                mColor = mContext.getResources().getColor(R.color.color_1024);
                break;
            case 2048:
                mColor = mContext.getResources().getColor(R.color.color_2048);
                break;
            default:
                mColor = mContext.getResources().getColor(R.color.color_2);
        }
    }
}
