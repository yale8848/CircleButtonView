package com.daoxuehao.circlebuttonlib;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

/**
 * Created by Yale on 2016/9/23.
 */
public  class CircleLine{


    private Paint mPaintLine;
    private float mRadius=0f;
    private int mR = 0;
    private View mView;
    private static final int MAX_ALPHA = 255;
    private static final int INIT_VALUE = -1;

    private int mMaxRadius = INIT_VALUE;
    private int mMinRadius = INIT_VALUE;
    private int mCenterX = INIT_VALUE;
    private int mCenterY = INIT_VALUE;
    private int mRadiusOff = INIT_VALUE;


    public CircleLine(View view,int lineWidth,int color,float startRadiusRatio){
        mView = view;
        mPaintLine = new Paint();
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(color);
        mPaintLine.setStrokeWidth(lineWidth);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setAlpha(MAX_ALPHA);

        mRadius = startRadiusRatio;
    }
    public void setColor(int color){
        mPaintLine.setColor(color);
    }

    private void changeAlpha(){
        if (mR == 0){
            return;
        }
        int of1 = mR - mMinRadius;
        int of2 = mRadiusOff;
        float a = ((float)of1*MAX_ALPHA/of2);
        mPaintLine.setAlpha(MAX_ALPHA-(int)a );
    }
    private void initValue(){
        if (mMinRadius == INIT_VALUE){
            mMinRadius = (int) (mView.getWidth()*mRadius);
        }
        if (mMaxRadius == INIT_VALUE){
            mMaxRadius =mView.getWidth()/2;
        }
        if (mRadiusOff == INIT_VALUE){
            mRadiusOff =(mView.getWidth()/2) - (int) (mView.getWidth()*mRadius);
        }
        if (mCenterX == INIT_VALUE){

            mCenterX = mView.getWidth()/2;
        }
        if (mCenterY == INIT_VALUE){
            mCenterY = mView.getHeight()/2;
        }
    }
    private void changeRadius(){
        if (mR == 0){
            mR = mMinRadius;
        }
        mR+=2;
        if (mR>mMaxRadius){
            mR = 0;
        }
    }
    public void draw(Canvas canvas){

        initValue();
        changeRadius();
        changeAlpha();
        canvas.drawCircle(mCenterX,mCenterY,mR,mPaintLine);
    }
}
