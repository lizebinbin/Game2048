package com.example.mooreli.game2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by MooreLi on 2017/2/10.
 */

public class NumView extends View {
    private Context mContext;
    private int mWidth, mHeight;

    private int mIndexX, mIndexY;
    private int mLocationX,mLocationY;
    private int mValue;
    private int mColor;

    private Paint mPaint;
    private int mTextColor;

    public NumView(Context context, int value) {
        super(context);
        mContext = context;
        mValue = value;
        init();
    }

    public NumView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public NumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(ScreenUtil.sp2px(mContext, 20));
        mTextColor = Color.parseColor("#757575");
        mPaint.setColor(mTextColor);
        updateValue(mValue);
    }

    public void updateValue(int value){
        mValue = value;
        judgeColor();
        this.setBackgroundColor(mColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        String value = mValue + "";
        mPaint.getTextBounds(value, 0, value.length(), rect);
        canvas.drawText(value, mWidth / 2 - rect.width() / 2, mHeight / 2 - rect.height() / 2, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = ScreenUtil.dp2px(mContext, 50);
        mHeight = ScreenUtil.dp2px(mContext, 50);

        setMeasuredDimension(mWidth, mHeight);
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
    }

    public int getmLocationX() {
        return mLocationX;
    }

    public void setmLocationX(int mLocationX) {
        this.mLocationX = mLocationX;
    }

    public int getmLocationY() {
        return mLocationY;
    }

    public void setmLocationY(int mLocationY) {
        this.mLocationY = mLocationY;
    }

    private void judgeColor() {
        switch (mValue) {
            case 2:
                mColor = R.color.color_2;
                break;
            case 4:
                mColor = R.color.color_4;
                break;
            case 8:
                mColor = R.color.color_8;
                break;
            case 32:
                mColor = R.color.color_32;
                break;
            case 64:
                mColor = R.color.color_64;
                break;
            case 128:
                mColor = R.color.color_128;
                break;
            case 256:
                mColor = R.color.color_256;
                break;
            case 512:
                mColor = R.color.color_512;
                break;
            case 1024:
                mColor = R.color.color_1024;
                break;
            case 2048:
                mColor = R.color.color_2048;
                break;
            default:
                mColor = R.color.color_2;
        }
    }
}
