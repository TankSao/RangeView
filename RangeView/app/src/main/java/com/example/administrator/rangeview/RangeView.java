package com.example.administrator.rangeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2018/11/16.
 */

public class RangeView extends View{
    private Paint paintBackground;//背景线的画笔
    private Paint paintCircle;//起始点圆环的画笔
    private Paint paintWhileCircle;//起始点内圈白色区域的画笔
    private Paint paintText;//起始点数值的画笔
    private Paint paintConnectLine;//起始点连接线的画笔

    private int height = 0;//控件的高度
    private int width = 0;//控件的宽度

    private float centerVertical = 0;//y轴的中间位置

    private float backlineWidth = 5;//底线的宽度

    private float marginhorizontal = 35;//横向边距

    private float marginTop = 50;//文字距顶部的距离

    private float pointStart = 0;//起点的X轴位置

    private float pointEnd = 0;//始点的Y轴位置

    private float circleRadius = 15;//起始点圆环的半径

    private float numStart = 0;//数值的开始值

    private float numEnd = 0;//数值的结束值

    private int textSize = 30;//文字的大小

    private boolean isRunning = false;//是否可以滑动

    private boolean isStart = true;//起点还是终点 true：起点；false：终点。

    private  RangeChangeListener listener;//范围变化监听

    public  void setOnRangeChangeListener(RangeChangeListener listener){
        this.listener = listener;
    }


    public RangeView(Context context) {
        super(context);
        init();
    }

    public RangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获取控件的宽高、中线位置、起始点、起始数值
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        centerVertical = height / 2;

        pointStart = marginhorizontal;
        pointEnd = width - marginhorizontal;

        numStart = getProgressNum(pointStart);

        numEnd = getProgressNum(pointEnd);

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //如果点击的点在第一个圆内就是起点,并且可以滑动
                if (event.getX() >= (pointStart - circleRadius) && event.getX() <= (pointStart + circleRadius)) {
                    isRunning = true;
                    isStart = true;

                    pointStart = event.getX();
                    //如果点击的点在第二个圆内就是终点,并且可以滑动
                } else if (event.getX() <= (pointEnd + circleRadius) && event.getX() >= (pointEnd - circleRadius)) {
                    isRunning = true;
                    isStart = false;

                    pointEnd = event.getX();
                } else {
                    //如果触控点不在圆环内，则不能滑动
                    isRunning = false;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (isRunning) {
                    if (isStart) {
                        //起点滑动时，重置起点的位置和进度值
                        pointStart = event.getX();
                        numStart = getProgressNum(pointStart);
                    } else {
                        //始点滑动时，重置始点的位置和进度值
                        pointEnd = event.getX();
                        numEnd = getProgressNum(pointEnd);
                    }

                    flushState();//刷新状态
                }

                break;
            case MotionEvent.ACTION_UP:

                flushState();
                break;
        }

        return true;
    }

    /**
     * 刷新状态和屏蔽非法值
     */
    private void flushState() {

        //起点非法值
        if (pointStart <= marginhorizontal) {
            pointStart = marginhorizontal;
        }
        //终点非法值
        if (pointEnd > width - marginhorizontal) {
            pointEnd = width - marginhorizontal;
        }

        //防止起点位置大于终点位置（规定：如果起点位置大于终点位置，则将起点位置放在终点位置前面,即：终点可以推着起点走，而起点不能推着终点走）
        if (pointStart + circleRadius > pointEnd - circleRadius) {

            pointStart = pointEnd - 2 * circleRadius;

        }

        //防止终点把起点推到线性范围之外
        if (pointEnd < marginhorizontal + 2 * circleRadius) {
            pointEnd = marginhorizontal + 2 * circleRadius;
            pointStart = marginhorizontal;
        }
        numStart = getProgressNum(pointStart);

        invalidate();//这个方法会导致onDraw方法重新绘制

    }

    //进度范围
    float beginNum = 0;
    float endNum = 1000;

    //计算进度数值
    private float getProgressNum(float progress) {
        float num =  (int) progress / (width - 2 * marginhorizontal) * (endNum - beginNum);
        if(num>=endNum){
            num = endNum;
        }
        if(num<=beginNum){
            num = beginNum;
        }
        if(progress<=marginhorizontal){
            return 0;
        }
        return num;

    }

    //初始化画笔
    private void init() {

        paintBackground = new Paint();
        paintBackground.setColor(getResources().getColor(R.color.colorBackline));
        paintBackground.setStrokeWidth(backlineWidth);
        paintBackground.setAntiAlias(true);

        paintCircle = new Paint();
        paintCircle.setColor(getResources().getColor(R.color.colorTipCircle));
        paintCircle.setStrokeWidth(backlineWidth);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);

        paintWhileCircle = new Paint();
        paintWhileCircle.setColor(getResources().getColor(R.color.white));
        paintCircle.setStyle(Paint.Style.FILL);
        paintWhileCircle.setAntiAlias(true);

        paintText = new Paint();
        paintText.setColor(getResources().getColor(R.color.colorTipText));
        paintText.setTextSize(textSize);
        paintText.setAntiAlias(true);

        paintConnectLine = new Paint();
        paintConnectLine.setColor(getResources().getColor(R.color.colorTipCircle));
        paintConnectLine.setStrokeWidth(backlineWidth + 5);
        paintConnectLine.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        //背景线
        canvas.drawLine(marginhorizontal, centerVertical, width - marginhorizontal, centerVertical, paintBackground);
        //起点位置的外圈圆
        canvas.drawCircle(pointStart, centerVertical, circleRadius, paintCircle);
        //起点位置的内圈圆
        canvas.drawCircle(pointStart, centerVertical, circleRadius - backlineWidth, paintWhileCircle);
        //终点位置的外圈圆
        canvas.drawCircle(pointEnd, centerVertical, circleRadius, paintCircle);
        //终点位置的内圈圆
        canvas.drawCircle(pointEnd, centerVertical, circleRadius - backlineWidth, paintWhileCircle);
        //起始点连接线
        canvas.drawLine(pointStart + circleRadius, centerVertical, pointEnd - circleRadius, centerVertical, paintConnectLine);
        //监听回调
        if(listener!=null){
            listener.onRangeChange(numStart,numEnd);
        }

    }
}
