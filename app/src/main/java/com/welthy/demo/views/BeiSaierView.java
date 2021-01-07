package com.welthy.demo.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class BeiSaierView extends View {

    private final String TAG = BeiSaierView.class.getSimpleName();
    private final int INIT_WIDTH = 200;
    private final int INIT_HEIGHT = 200;

    private Paint mPaint,mBackPaint;
    private Path mBeiSaierPath,mBeiSaierBackPath;
    private int measureWidth,measureHeight;

    private int mItemWidth,mItemHeight;
    private int mOffset;

    private ValueAnimator translateAnim;

    public BeiSaierView(Context context) {
        this(context,null);
    }

    public BeiSaierView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public BeiSaierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.GRAY);

        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackPaint.setStrokeWidth(5);
        mBackPaint.setColor(Color.BLUE);

        mBeiSaierPath = new Path();
        mBeiSaierBackPath = new Path();

        mItemWidth = getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                measureWidth = widthSize;
                break;
            default:
                measureWidth = INIT_WIDTH;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                measureHeight = heightSize;
                break;
            default:
                measureHeight = INIT_HEIGHT;
                break;
        }

        mItemWidth = measureWidth;
        mItemHeight = 150;

        setMeasuredDimension(measureWidth,measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mBeiSaierPath.reset();
        Log.d(TAG,"onDraw() mOffset: "+mOffset + "   mItemWidth: "+mItemWidth);
        mBeiSaierPath.moveTo(mOffset - mItemWidth,mItemHeight);
        for (int i = -mItemWidth; i<= mItemWidth + getWidth(); i += mItemWidth) {
            mBeiSaierPath.rQuadTo(mItemWidth/4 ,mItemHeight,mItemWidth/2,0);
            mBeiSaierPath.rQuadTo(mItemWidth/4,-mItemHeight, mItemWidth/2, 0);
        }
        mBeiSaierPath.lineTo(getWidth(),getHeight());
        mBeiSaierPath.lineTo(0,getHeight());
        mBeiSaierPath.close();

        canvas.drawPath(mBeiSaierPath,mPaint);

        mBeiSaierBackPath.reset();
        mBeiSaierBackPath.moveTo(mOffset - mItemWidth + 100, mItemHeight);
        for (int i = -mItemWidth; i<= mItemWidth + getWidth(); i += mItemWidth) {
            mBeiSaierBackPath.rQuadTo(mItemWidth/4 ,mItemHeight,mItemWidth/2,0);
            mBeiSaierBackPath.rQuadTo(mItemWidth/4,-mItemHeight, mItemWidth/2, 0);
        }
        mBeiSaierBackPath.lineTo(getWidth(),getHeight());
        mBeiSaierBackPath.lineTo(0,getHeight());
        mBeiSaierBackPath.close();
        canvas.drawPath(mBeiSaierBackPath,mBackPaint);

    }

    public void startAnim() {
        if (translateAnim == null ) {
            translateAnim = ValueAnimator.ofInt(0,mItemWidth);
            translateAnim.setDuration(3000);
            translateAnim.setRepeatCount(-1);
            translateAnim.setInterpolator(new LinearInterpolator());
            translateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mOffset = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });
        }
        translateAnim.start();
    }

    public void stopAnim() {
        if (translateAnim != null) {
            translateAnim.cancel();
        }
    }
}
