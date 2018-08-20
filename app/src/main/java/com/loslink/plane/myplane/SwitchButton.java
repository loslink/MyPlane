package com.loslink.plane.myplane;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

class SwitchButton extends View {

    private Paint baseCirclePaint;
    private Context context;
    private float canvasWidth,canvasHeight;

    private float buttonWidth,buttonHeight;

    public SwitchButton(Context context) {
        this(context,null);
        init();
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init() {
        baseCirclePaint =new Paint();
        baseCirclePaint.setAntiAlias(true);
        baseCirclePaint.setStyle(Paint.Style.STROKE);
        baseCirclePaint.setStrokeWidth(10);
        baseCirclePaint.setColor(context.getResources().getColor(R.color.colorAccent));

        buttonWidth=dip2px(80);
        buttonHeight=dip2px(30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasWidth=w;
        canvasHeight=h;


    }

    @Override
    protected void onDraw(Canvas canvas)   {

//        canvas.drawCircle(centerX, centerY,baseCicleRadius, baseCirclePaint);
//        canvas.drawCircle(barCenterX, barCenterY,baseBarCicleRadius, barCirclePaint);
        drawButton(canvas);
    }

    private void drawButton(Canvas canvas){

        float radius=buttonHeight/2;
        Path path=new Path();

//        RectF rectF=new RectF(canvasWidth/2-buttonWidth/2,
//                canvasHeight/2-buttonHeight/2,
//                canvasWidth/2+buttonWidth/2,
//                canvasHeight/2+buttonHeight/2);
//        path.addRoundRect(rectF,radius,radius,Path.Direction.CCW);


        RectF rectF2=new RectF(canvasWidth/2-buttonWidth/2+radius,
                canvasHeight/2-buttonHeight/2,
                canvasWidth/2+buttonWidth/2-radius,
                canvasHeight/2+buttonHeight/2);
        path.addCircle(canvasWidth/2-buttonWidth/2+radius,canvasHeight/2,radius,Path.Direction.CCW);
        path.addRect(rectF2,Path.Direction.CCW);
        path.addCircle(canvasWidth/2+buttonWidth/2-radius,canvasHeight/2,radius,Path.Direction.CCW);

        canvas.drawPath(path,baseCirclePaint);

    }

    private void startAnimation(float fX,float fY){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(fX,canvasWidth/2);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
//                barCenterX =(float)animation.getAnimatedValue();
                invalidate();
            }
        });

    }

    public int dip2px(int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX(0);
        float y = event.getY(0);

        return true;
    }

}

