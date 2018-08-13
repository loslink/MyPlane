package com.loslink.plane.myplane;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

class MControllerView extends View {

    private Paint baseCirclePaint,barCirclePaint;
    private Context context;
    private float canvasWidth,canvasHeight;
    private float baseCicleRadius=100,baseBarCicleRadius=50;
    private float leftCenterX,leftCenterY;
    private float leftBarCenterX,leftBarCenterY;

    public MControllerView(Context context) {
        this(context,null);
        init();
    }

    public MControllerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public MControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public void init() {
        baseCirclePaint =new Paint();
        baseCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        baseCirclePaint.setColor(context.getResources().getColor(R.color.control_base));

        barCirclePaint =new Paint();
        barCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        barCirclePaint.setColor(context.getResources().getColor(R.color.control_bar));

        baseCicleRadius=dip2px(100);
        baseBarCicleRadius=dip2px(50);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasWidth=w;
        canvasHeight=h;

        leftCenterX=canvasWidth/2;
        leftCenterY=canvasHeight/2;

        leftBarCenterX=canvasWidth/2;
        leftBarCenterY=canvasHeight/2;

    }

    @Override
    protected void onDraw(Canvas canvas)   {

        canvas.drawCircle(leftCenterX,leftCenterY,baseCicleRadius, baseCirclePaint);
        canvas.drawCircle(leftBarCenterX,leftBarCenterY,baseBarCicleRadius, barCirclePaint);
    }

    private void startAnimation(float fX,float fY){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(fX,canvasWidth/2);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftBarCenterX=(float)animation.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator valueAnimatorY=ValueAnimator.ofFloat(fY,canvasHeight/2);
        valueAnimatorY.setRepeatCount(0);
        valueAnimatorY.setInterpolator(new AccelerateInterpolator());
        valueAnimatorY.start();
        valueAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftBarCenterY=(float)animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    public int dip2px(int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    float dragX,dragY;

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX(0);
        float y = event.getY(0);
        if(isDragAirea(x,y)){
            dragX=x;
            dragY=y;
        }else {

        }
        dealFinger(dragX,dragY);
        if(event.getAction()==MotionEvent.ACTION_UP){
            startAnimation(dragX,dragY);
        }
        return true;
    }

    private boolean isDragAirea(float x,float y){
        double distance=Math.sqrt((x-(canvasWidth/2))*(x-(canvasWidth/2))+(y-(canvasHeight/2))*(y-(canvasHeight/2)));
        if(distance <= baseCicleRadius){
            return true;
        }
        return false;
    }

    private void dealFinger(float x,float y){
        showMessage(x+"  "+y);
        leftBarCenterX=x;
        leftBarCenterY=y;
        invalidate();
    }

    private void showMessage(String s){
        Log.v("showMessage","showMessage: "+s);
    }
}

