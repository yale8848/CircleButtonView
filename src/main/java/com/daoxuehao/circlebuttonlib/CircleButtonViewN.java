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

import com.daoxuehao.circlebuttonlib.util.SizeUtil;


/**
 * Created by Yale on 2016/9/22.
 */
public class CircleButtonViewN extends SurfaceView implements SurfaceHolder.Callback{

    private Paint mPaint;
    private Paint mPaintLine;
    private Paint mPaintBG;
    private Paint mPaintText;

    private SurfaceHolder mSurfaceHolder;

    private String mText ="";
    private int mNormalColor;
    private int mEmptyColor;

    private int LINE_WIDTH = 2;//3dp
    private int TEXT_SIZE = 16;//3dp
    private float mCircleRatio = 0.35f;


    public CircleButtonViewN(Context context) {
        this(context,null);
    }

    public CircleButtonViewN(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleButtonViewN(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs){

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.DXHCircleButtonView);

        int bgColor = typedArray.getColor(R.styleable.DXHCircleButtonView_backgroundColor, Color.WHITE);
        mNormalColor = typedArray.getColor(R.styleable.DXHCircleButtonView_normalColor,Color.BLUE);
        int textColor = typedArray.getColor(R.styleable.DXHCircleButtonView_textColor,Color.WHITE);

        mText = typedArray.getString(R.styleable.DXHCircleButtonView_text);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.DXHCircleButtonView_textSize,SizeUtil.sp2px(getContext(),TEXT_SIZE));
        mCircleRatio = typedArray.getFloat(R.styleable.DXHCircleButtonView_circleRaiusRatio,mCircleRatio);

        boolean isSetZOrderOnTop = typedArray.getBoolean(R.styleable.DXHCircleButtonView_setZOrderOnTop,true);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mNormalColor);
        mPaint.setAntiAlias(true);


        mPaintLine = new Paint();
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setColor(mNormalColor);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStrokeWidth(SizeUtil.dip2px(getContext(),LINE_WIDTH));


        mPaintBG = new Paint();
        mPaintBG.setStyle(Paint.Style.FILL);
        mPaintBG.setColor(bgColor);

        mPaintText = new Paint();
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setColor(textColor);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(textSize);


        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);

        setZOrderOnTop(isSetZOrderOnTop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                press();
                return true;
            }
            case MotionEvent.ACTION_UP:{
                normal();
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

    private void press(){
        synchronized (mSurfaceHolder){
            int r = (int) (getWidth()*mCircleRatio);
            int x = getWidth()/2;
            int y = getHeight()/2;
            int max = (getWidth()/2)-2*SizeUtil.dip2px(getContext(),LINE_WIDTH);
            for(int i = r;i<max;){
                Canvas canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    canvas.drawRect(new Rect(0,0,getWidth(),getHeight()),mPaintBG);
                    canvas.drawCircle(x,y,i,mPaint);
                    canvas.drawCircle(x,y,(getWidth()/2)-SizeUtil.dip2px(getContext(),LINE_WIDTH),mPaintLine);
                    drawText(canvas,mText);

                }catch (Exception e){
                }
                finally {
                    if (canvas!=null){
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }

                }
                i += 5;
            }


        }

    }

    private void normal(){
            synchronized (mSurfaceHolder){
                Canvas canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    int r = (int) (getWidth()*mCircleRatio);
                    int x = getWidth()/2;
                    int y = getHeight()/2;

                    canvas.drawRect(new Rect(0,0,getWidth(),getHeight()),mPaintBG);
                    canvas.drawCircle(x,y,r,mPaint);
                    canvas.drawCircle(x,y,(getWidth()/2)-SizeUtil.dip2px(getContext(),LINE_WIDTH),mPaintLine);
                    drawText(canvas,mText);



                }catch (Exception e){

                }
                finally {
                    if (canvas!=null){
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }

                }
            }

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        normal();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int w = resolveSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        int h = resolveSize(getSuggestedMinimumHeight(),heightMeasureSpec);
        setMeasuredDimension(w,w);
    }


}
