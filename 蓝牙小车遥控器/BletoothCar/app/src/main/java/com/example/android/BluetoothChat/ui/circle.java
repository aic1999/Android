package com.example.android.BluetoothChat.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;



public class circle extends View {
    private  int col= Color.WHITE;
    private int RockerCircleX =70;//=50 ;//230
    private int RockerCircleY =70;//= 50;
    private int RockerCircleR =40;//= 20;//150
    private String up="Z",down="Z";
    //摇杆的X,Y坐标以及摇杆的半径
    private float SmallRockerCircleX=70;// = 50;
    private float SmallRockerCircleY=70;// = 50;
    private float SmallRockerCircleR=25;// = 10;
    private Paint paint;
    float du;
    public circle(Context context) {
        super(context);
        init();
    }

    public void setUp(String up){
        this.up=up;
    }

    public void init(){
        paint=new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {

       canvas.drawColor(00000000);
     //   canvas.drawColor(Color.WHITE);

        //设置透明度
      //  paint.setColor(0x70000000);
        paint.setColor(Color.GRAY);
        //绘制摇杆背景
        canvas.drawCircle(RockerCircleX, RockerCircleY, RockerCircleR, paint);
        paint.setColor(0x70ff0000);
        //绘制摇杆
        canvas.drawCircle(SmallRockerCircleX, SmallRockerCircleY, SmallRockerCircleR, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
      //  super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(RockerCircleR*2+(int)SmallRockerCircleR*2+10,10+RockerCircleR*2+(int)SmallRockerCircleR*2);
    }


        @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            // 当触屏区域不在活动范围内
            if (Math.sqrt(Math.pow((RockerCircleX - (int) event.getX()), 2) + Math.pow((RockerCircleY - (int) event.getY()), 2))  >= RockerCircleR) {
                //得到摇杆与触屏点所形成的角度
                double tempRad = getRad(RockerCircleX, RockerCircleY, event.getX(), event.getY());
                //保证内部小圆运动的长度限制
                du= (float) -(Math.toDegrees(tempRad));

                getXY(RockerCircleX, RockerCircleY, RockerCircleR, tempRad);
            } else {//如果小球中心点小于活动区域则随着用户触屏点移动即可
                SmallRockerCircleX = (int) event.getX();
                SmallRockerCircleY = (int) event.getY();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            //当释放按键时摇杆要恢复摇杆的位置为初始位置
            SmallRockerCircleX = 70;
            SmallRockerCircleY = 70;
        }
        invalidate();
       // return true;
            return super.onTouchEvent(event);//哇去，这个666啊

    }

    public double getRad(float px1, float py1, float px2, float py2) {
        //得到两点X的距离
        float x = px2 - px1;
        //得到两点Y的距离
        float y = py1 - py2;
        //算出斜边长
        float xie = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        //得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
        float cosAngle = x / xie;
        //通过反余弦定理获取到其角度的弧度
        float rad = (float) Math.acos(cosAngle);
        //注意：当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
        if (py2 < py1) {
            rad = -rad;
        }

//        MainActivity mc=new MainActivity();
//        if(0<cosAngle&&cosAngle<90){
//            mc.top_send(getUp());
//        }else{
//            mc.bottom_send(getDown());
//        }

        return rad;
    }
    public void getXY(float centerX, float centerY, float R, double rad) {
        //获取圆周运动的X坐标
        SmallRockerCircleX = (float) (R * Math.cos(rad)) + centerX;
        //获取圆周运动的Y坐标
        SmallRockerCircleY = (float) (R * Math.sin(rad)) + centerY;
    }
    public float getDu(){
        return du;
    }
}
