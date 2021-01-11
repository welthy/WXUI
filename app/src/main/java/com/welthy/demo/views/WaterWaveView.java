/**
 * WaterWaveView, use BeiSaier path to draw a water wave effect
 */

package com.welthy.demo.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class WaterWaveView extends View {

    private final String TAG = WaterWaveView.class.getSimpleName();
    private final int INIT_WIDTH = 200;
    private final int INIT_HEIGHT = 200;

    private Paint mPaint,mBackPaint;
    private Path mBeiSaierPath,mBeiSaierBackPath;
    private int measureWidth,measureHeight;

    private int mItemWidth,mItemHeight;
    private int mOffset;
    private volatile boolean mInit = false;

    private ValueAnimator translateAnim;

    public WaterWaveView(Context context) {
        this(context,null);
    }

    public WaterWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public WaterWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        //前景水波
        mBeiSaierPath = new Path();
        //背景水波
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

        //Path路线画之前要重置，否则会包含之前的信息。因为Path是对象成员变量
        mBeiSaierPath.reset();
        //移动到初始点。需要距离View的左边界多出一个波长的距离，为了使View在移动结束到下次移动开始时连贯。
        mBeiSaierPath.moveTo(mOffset - mItemWidth,mItemHeight);
        //画1个周期的水波
        for (int i = -mItemWidth; i<= mItemWidth + getWidth(); i += mItemWidth) {
            mBeiSaierPath.rQuadTo(mItemWidth/4 ,mItemHeight,mItemWidth/2,0);
            mBeiSaierPath.rQuadTo(mItemWidth/4,-mItemHeight, mItemWidth/2, 0);
        }
        //连接View底部区域，最后封口，形成水槽
        mBeiSaierPath.lineTo(getWidth(),getHeight());
        mBeiSaierPath.lineTo(0,getHeight());
        mBeiSaierPath.close();

        canvas.drawPath(mBeiSaierPath,mPaint);

        //画背景波纹
        mBeiSaierBackPath.reset();
        //为了不和前景波纹重合，需要向右平移一段距离
        mBeiSaierBackPath.moveTo(mOffset - 2 * mItemWidth + mItemWidth/4, mItemHeight);
        for (int i = -mItemWidth; i<= mItemWidth + getWidth(); i += mItemWidth) {
            mBeiSaierBackPath.rQuadTo(mItemWidth/4, mItemHeight,mItemWidth/2,0);
            mBeiSaierBackPath.rQuadTo(mItemWidth/4, -mItemHeight, mItemWidth/2, 0);
        }
        mBeiSaierBackPath.lineTo(getWidth(),getHeight());
        mBeiSaierBackPath.lineTo(0,getHeight());
        mBeiSaierBackPath.close();
        canvas.drawPath(mBeiSaierBackPath,mBackPaint);
        mInit = true;
    }

    public void startAnim() {
        if (translateAnim == null ) {
            translateAnim = ValueAnimator.ofInt(0,mItemWidth);
            translateAnim.setDuration(3000);
            translateAnim.setRepeatCount(-1);
            translateAnim.setInterpolator(new FastOutSlowInInterpolator());
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
