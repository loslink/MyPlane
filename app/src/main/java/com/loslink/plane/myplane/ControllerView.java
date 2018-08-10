package com.loslink.plane.myplane;

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

class ControllerView extends View {

    private Paint baseCirclePaint,barCirclePaint;
    private Context context;
    private float canvasWidth,canvasHeight;
    private float baseCicleRadius=100,baseBarCicleRadius=50;
    private float leftCenterX,leftCenterY,rightCenterX,rightCenterY;
    private float leftBarCenterX,leftBarCenterY,rightBarCenterX,rightBarCenterY;

    public ControllerView(Context context) {
        this(context,null);
        init();
    }

    public ControllerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public ControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ControllerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

        leftCenterX=canvasWidth/4;
        leftCenterY=canvasHeight/2;

        rightCenterX=canvasWidth*3/4;
        rightCenterY=canvasHeight/2;

        leftBarCenterX=canvasWidth/4;
        leftBarCenterY=canvasHeight/2;

        rightBarCenterX=canvasWidth*3/4;
        rightBarCenterY=canvasHeight/2;
    }

    @Override
    protected void onDraw(Canvas canvas)   {

        canvas.drawCircle(leftCenterX,leftCenterY,baseCicleRadius, baseCirclePaint);
        canvas.drawCircle(rightCenterX,rightCenterY,baseCicleRadius, baseCirclePaint);

        canvas.drawCircle(leftBarCenterX,leftBarCenterY,baseBarCicleRadius, barCirclePaint);
        canvas.drawCircle(rightBarCenterX,rightBarCenterY,baseBarCicleRadius, barCirclePaint);
    }

    public int dip2px(int dipValue) {
        float reSize = context.getResources().getDisplayMetrics().density;
        return (int) ((dipValue * reSize) + 0.5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_POINTER_1_DOWN:
                showMessage("第一个手指按下");
                break;
            case MotionEvent.ACTION_POINTER_1_UP:
                showMessage("第一个手指抬起");
                break;
            case MotionEvent.ACTION_POINTER_2_DOWN:
                showMessage("第二个手指按下");
                break;
            case MotionEvent.ACTION_POINTER_2_UP:
                showMessage("第二个手指抬起");
                break;
            case MotionEvent.ACTION_POINTER_3_DOWN:
                showMessage("第三个手指按下");
                break;
            case MotionEvent.ACTION_POINTER_3_UP:
                showMessage("第三个手指抬起");
                break;
            default:
                showMessage("只有一个手指");
                break;
        }

        return true;
    }

    private void showMessage(String s){
//        Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
//        toast.show();
        Log.v("showMessage","showMessage: "+s);
    }
}

