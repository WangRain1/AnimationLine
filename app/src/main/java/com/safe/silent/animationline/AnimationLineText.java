/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.safe.silent.animationline;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class AnimationLineText extends View {

    public AnimationLineText(Context context) {
        this(context, null);
    }

    public AnimationLineText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    Context context;
    public AnimationLineText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    Paint mPaint;
    Path mPath;
    private void init() {

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(6);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(100);
        mPaint.setAntiAlias(true);
        mPath = new Path();
        startAnimation();
    }

    List<Float> mSimples = new ArrayList<>();

    /**
     * 采样图像中就有变化的数据【-4，4】这是x值
     */
    private void simple() {
        ValueAnimator animator = ValueAnimator.ofFloat(-4, 4);
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSimples.add((float) animation.getAnimatedValue());
                invalidate();
            }
        });
        animator.start();
    }

    boolean isStart = false;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isStart){
            return;
        }
        canvas.translate(getWidth()/2,getHeight()/2);

        mPath.reset();
        for (int i = 0;i<mSimples.size();i++){
            if (i==0){
                mPath.moveTo(mSimples.get(i)*130,getSinPoint(mSimples.get(i))*100);
            }else {
                mPath.lineTo(mSimples.get(i)*130,getSinPoint(mSimples.get(i))*100);
            }
        }
        canvas.drawTextOnPath("w a n g  r a i n 1",mPath,200,0,mPaint);
        if (isShow){
            canvas.drawPath(mPath,mPaint);
        }
    }

    float offset = 0;
    ValueAnimator mAnimator;
    private void startAnimation(){

        mAnimator = ValueAnimator.ofFloat(0, (float) (2*Math.PI));
        mAnimator.setDuration(1000);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    private float getSinPoint(float x) {

        return (float) (4/(4+Math.pow(x,2))*Math.sin(0.2*x + offset));
    }

    public void start(){
        isStart = true;
        simple();
        mAnimator.start();
        mAnimator.setRepeatCount(100);
    }

    boolean isShow = false;
    public void showLine(){

        isShow = !isShow;
    }
}
