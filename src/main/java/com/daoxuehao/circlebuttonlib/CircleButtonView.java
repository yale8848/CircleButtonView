package com.daoxuehao.circlebuttonlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


/**
 * Created by Yale on 2016/9/22.
 */
public class CircleButtonView extends SurfaceView implements SurfaceHolder.Callback{

    private Paint mPaint;
    private Paint mPaintBG;
    private CircleLine mCircleLine;
    private Paint mPaintText;

    private SurfaceHolder mSurfaceHolder;

    private int mNormalTimeGap;
    private int mPressTimeGapGap;

    private int mGapTime = 100;

    private boolean mIsRun = true;

    private String mText ="";
    private int mNormalColor;
    private int mPressColor;


    public CircleButtonView(Context context) {
        this(context,null);
    }

    public CircleButtonView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs){

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.DXHCircleButtonView);

        int bgColor = typedArray.getColor(R.styleable.DXHCircleButtonView_backgroundColor, Color.WHITE);
        mNormalColor = typedArray.getColor(R.styleable.DXHCircleButtonView_backgroundColor,Color.BLUE);
        mPressColor = typedArray.getColor(R.styleable.DXHCircleButtonView_backgroundColor,Color.RED);
        int textColor = typedArray.getColor(R.styleable.DXHCircleButtonView_textColor,Color.WHITE);

        mText = typedArray.getString(R.styleable.DXHCircleButtonView_text);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.DXHCircleButtonView_textSize,20);
        float circleRadiuRatio = typedArray.getFloat(R.styleable.DXHCircleButtonView_circleRaiusRatio,0.3f);

        int lineWidth = typedArray.getDimensionPixelSize(R.styleable.DXHCircleButtonView_lineWidth,2);

        mNormalTimeGap =  typedArray.getInt(R.styleable.DXHCircleButtonView_normalTimeGap,100);
        mPressTimeGapGap =  typedArray.getInt(R.styleable.DXHCircleButtonView_pressTimeGap,10);

        mGapTime = mNormalTimeGap;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mNormalColor);
        mPaint.setAntiAlias(true);

        mPaintBG = new Paint();
        mPaintBG.setStyle(Paint.Style.FILL);
        mPaintBG.setColor(bgColor);

        mPaintText = new Paint();
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(textColor);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(textSize);

        mCircleLine = new CircleLine(this,lineWidth,mNormalColor,circleRadiuRatio);

        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                mPaint.setColor(mPressColor);
                mCircleLine.setColor(mPressColor);
                mGapTime = mPressTimeGapGap;
                return true;
            }
            case MotionEvent.ACTION_UP:{
                mPaint.setColor(mNormalColor);
                mCircleLine.setColor(mNormalColor);
                mGapTime = mNormalTimeGap;
                return true;
            }

        }
        return super.onTouchEvent(event);
    }

    private void drawText(Canvas canvas, String text){
        if (text == null){
            return;
        }
        Paint.FontMetrics fm = mPaintText.getFontMetrics();
        int textH = (int) Math.ceil(fm.descent - fm.top);
        int textW = (int) mPaintText.measureText(text);
        int x = (getWidth() - textW ) / 2;
        int y = (getWidth() - textH) / 2;
        canvas.drawText(text,x,y-fm.ascent,mPaintText);
    }
    private void start(){
        mIsRun = true;
        while (mIsRun){
            synchronized (mSurfaceHolder){
                Canvas canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    int r = (int) (getWidth()*0.3);
                    int x = getWidth()/2;
                    int y = getHeight()/2;

                    canvas.drawRect(new Rect(0,0,getWidth(),getHeight()),mPaintBG);
                    canvas.drawCircle(x,y,r,mPaint);

                    drawText(canvas,mText);

                    mCircleLine.draw(canvas);
                    Thread.sleep(mGapTime);

                }catch (Exception e){

                }
                finally {
                    if (canvas!=null){
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }

                }
            }
        }

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsRun = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = resolveSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        int h = resolveSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        setMeasuredDimension(w,w);
    }


}
