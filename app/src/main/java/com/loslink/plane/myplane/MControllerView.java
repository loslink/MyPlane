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

class MControllerView extends View {

    private Paint baseCirclePaint,barCirclePaint;
    private Context context;
    private float canvasWidth,canvasHeight;
    private float baseCicleRadius=100,baseBarCicleRadius=50;
    private float centerX, centerY;
    private float barCenterX, barCenterY;
    private ControllerListenr controllerListenr;


    public ControllerListenr getControllerListenr() {
        return controllerListenr;
    }

    public void setControllerListenr(ControllerListenr controllerListenr) {
        this.controllerListenr = controllerListenr;
    }

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
        baseCirclePaint.setAntiAlias(true);
        baseCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        baseCirclePaint.setColor(context.getResources().getColor(R.color.control_base));

        barCirclePaint =new Paint();
        barCirclePaint.setAntiAlias(true);
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

        centerX =canvasWidth/2;
        centerY =canvasHeight/2;

        barCenterX =canvasWidth/2;
        barCenterY =canvasHeight/2;

    }

    @Override
    protected void onDraw(Canvas canvas)   {

        canvas.drawCircle(centerX, centerY,baseCicleRadius, baseCirclePaint);
        canvas.drawCircle(barCenterX, barCenterY,baseBarCicleRadius, barCirclePaint);
    }

    private void startAnimation(float fX,float fY){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(fX,canvasWidth/2);
        valueAnimator.setRepeatCount(0);
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                barCenterX =(float)animation.getAnimatedValue();
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
                barCenterY =(float)animation.getAnimatedValue();
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
            touchOutside(x,y);
        }
        dealFinger(dragX,dragY);
        if(event.getAction()==MotionEvent.ACTION_UP){
            startAnimation(dragX,dragY);
        }
        progress();
        return true;
    }

    private void progress(){
        if(controllerListenr!=null){
            float prog=Math.abs(barCenterY - centerY)/baseCicleRadius;
            controllerListenr.updateListener(prog);
        }
    }

    private void touchOutside(float x,float y){
        float y1= centerY;
        float y2=y;
        float x1= centerX;
        float x2=x;
        float a=y1-y2;
        float b=x2-x1;
        float r=baseCicleRadius;
        dragX= (float) (x1+b*r/Math.sqrt(a*a+b*b));
        dragY= (float) (y1-a*r/Math.sqrt(a*a+b*b));
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
        barCenterX =x;
        barCenterY =y;
        invalidate();
    }

    private void showMessage(String s){
        Log.v("showMessage","showMessage: "+s);
    }

    public interface ControllerListenr{
        void updateListener(float progress);
    }
}

