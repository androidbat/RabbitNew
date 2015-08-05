package com.c.commen.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.KeyEvent;
import android.view.View;

import com.yitu.widget.R;

/**
 * android动 ? 绕可移动轴心旋转图 ?
 * @author hanmr//
 *
 */
public class BeadplateMiddle extends View{
    //
    private Bitmap mBitmap = null;//声明Bitmap对象  
    private int x  = 300;  
    private int y = 100;  
    //private float angleA = 0.0f;//声明轴心A转动  
    private float angleB = 0.0f;//声明轴心B转动角度  
    private Matrix mMatrix = new Matrix();//构建矩阵Matrix对象  
    public BeadplateMiddle(Context context) {  //
        super(context);  
        //装载资源  
        mBitmap =((BitmapDrawable) getResources().getDrawable(R.drawable.pan_anecdotes)).getBitmap();
        // ?启线 ?
        new Thread(new DrawThread()).start();  
    }  
    //系统IOC ?始绘 ?
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        Paint mPaint = new Paint();  
        mPaint.setColor(Color.RED);  
        mPaint.setAntiAlias(true);  
        canvas.drawCircle(x, y, 12, mPaint);  
        mMatrix = getMyMatrix(mMatrix,angleB, x, y);  
        canvas.drawBitmap(mBitmap, mMatrix, null);  
    }  
    /**
     * 动 ? 构建旋转矩阵Matrix对象
     * @param matrix   ?要计算的矩阵
     * @param degrees 图片旋转的角度，正 ? 为顺时针，负 ? 为逆时 ?
     * @param pivotX  轴心的X坐标
     * @param pivotY  轴心的Y坐标
     */  
    private Matrix getMyMatrix(Matrix matrix ,float degrees,int pivotX , int pivotY ){  
        //重置Matrix  
        matrix.reset();  
        float cosValue = (float) Math.cos(Math.PI/(180/degrees));   
        float sinValue = (float) Math.sin(Math.PI/(180/degrees));   
        //设置旋转矩阵 ?
        matrix.setValues(   
                new float[]{   
                        cosValue, -sinValue, pivotX,   
                        sinValue, cosValue, pivotY,   
                        0, 0, 1});   
        return matrix;  
    }  
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){  
            x --;  
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){  
            x ++;  
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_UP){  
            y --;  
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){  
            y ++;  
        }else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER){  
            angleB ++;  
        }  
        return true;  
    }  
    private class DrawThread implements Runnable{  
        @Override  
        public void run() {  
            while(!Thread.currentThread().isInterrupted()){  
                try {  
                    Thread.sleep(500);  
                } catch (Exception e) {  
                    Thread.currentThread().interrupt();  
                }  
                //使用PostInvalidate可以直接在线程中更新视图  
                postInvalidate();  
            }  
        }  
    }   
}
