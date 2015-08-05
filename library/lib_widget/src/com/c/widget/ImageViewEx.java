package com.c.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

public class ImageViewEx extends ImageView{
    private static final String TAG="ImageViewEx";
    private BitmapEx mBitmapEx=null;
    private Paint mPaint = new Paint();
    private int mScreenWidth;
    private float sourceHeight=0;
    private float currentHeight=0;
     //如果没有以下这三个变量，动画效果错乱
    private Matrix savedMatrix = new Matrix();
    private OnBitmapChange mOnBitmapChange;
    
    public ImageViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewEx(Context context) {
        super(context);
    }
    
    public void init(Bitmap bm,OnBitmapChange listener){
        if(bm==null){
            mBitmapEx=null;
        }
        if(mBitmapEx==null){//第一次初始化
            initBmp(bm,listener);
            invalidate();//绘制
        }else{
            initBmp(bm,listener);
            invalidate();//绘制
            //替换图片,淡入淡出
//            showAnimation(this,500,1,0.0f,bm,listener);
        }
        
    }
    
    private void initBmp(Bitmap bm,OnBitmapChange listener){
        mOnBitmapChange=listener;
        mPaint.setAntiAlias(true);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        mScreenWidth = dm.widthPixels;
        mBitmapEx=getBitmapEx(bm);
        sourceHeight=mBitmapEx.bm.getHeight();
//        printfMatrix(mBitmapEx.matrix);
        currentHeight=sourceHeight;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmapEx==null){
            return;
        }
        canvas.save();  
        canvas.drawBitmap(mBitmapEx.bm, mBitmapEx.matrix, null);
        canvas.restore();
    }
    public boolean isMinHeight(){
        if(currentHeight<=sourceHeight){
            lastDy=0;
            return true;
        }else{
            return false;
        }
    }
    long time=0;
    /**
     * 
     * @param zoomIn 放大
     */
    public void startScale(boolean zoomIn){
        if(mBitmapEx==null){
            return;
        }
        savedMatrix.set(mBitmapEx.matrix);
        changeMatrixValue(zoomIn);
        printfMatrix(mBitmapEx.matrix);
//        LogManager.d(TAG, "---------------------------------------");
        invalidate();
    }
    float lastDy=0;
    public void startScale(float dy){
        if(mBitmapEx==null){
            return;
        }
        savedMatrix.set(mBitmapEx.matrix);
        changeMatrixValue(dy-lastDy);
//        changeMatrixValue(zoomIn);
//        printfMatrix(mBitmapEx.matrix);
        lastDy=dy;
//        LogManager.d(TAG, "---------------------------------------");
        invalidate();
    }
    //恢复原来的样 ?
    public void restore(long delayMillis){
        if(mBitmapEx==null){
            return;
        }
        if(currentHeight>sourceHeight){
            mHandler.sendEmptyMessageDelayed(START, delayMillis);
        }
    }
    private void changeMatrixValue(float dy){
        float step=dy/currentHeight;
        Log.d(TAG, "step----------------->"+step);
        if(step>0.02f){
            step=0.02f;
        }else if(step<-0.02f){
            step=-0.02f;
        }
        float values[]=new float[9];
        mBitmapEx.matrix.getValues(values);
        values[0]=values[0]+step;
        values[4]=values[4]+step;
        if(values[0]>1.5){
            values[0]=1.5f;
            values[4]=1.5f;
        }
        if(values[0]<1){
            values[0]=1f;
            values[4]=1f;
        }
        values[2]=-(mScreenWidth*values[0]-mScreenWidth)/2;
        mBitmapEx.matrix.setValues(values);
        currentHeight=sourceHeight*values[4];
        mOnBitmapChange.onBitmapChanged((int)(currentHeight));
    }
    private void changeMatrixValue(boolean zoomIn){
        float step=0.02f;
        if(!zoomIn){
            //缩小
            step=-step*5;
        }
        float values[]=new float[9];
        mBitmapEx.matrix.getValues(values);
        values[0]=values[0]+step;
        values[4]=values[4]+step;
        if(values[0]>1.5){
            values[0]=1.5f;
            values[4]=1.5f;
        }
        if(values[0]<1){
            values[0]=1f;
            values[4]=1f;
        }
        values[2]=-(mScreenWidth*values[0]-mScreenWidth)/2;
        mBitmapEx.matrix.setValues(values);
        currentHeight=sourceHeight*values[4];
        mOnBitmapChange.onBitmapChanged((int)(currentHeight));
    }
    private BitmapEx getBitmapEx(Bitmap bm){
        BitmapEx bme=new BitmapEx();
        bme.bm=bm;
        float dx=0;
        float dy=0;
        bme.matrix=getMatrix(dx,dy);
        return bme;
    }
    
    private Matrix getMatrix(float dx,float dy){
        Matrix matrix=new Matrix();
        float values[]=new float[9];
        matrix.getValues(values);
        values[2]=dx;
        values[5]=dy;
        matrix.setValues(values);
        return matrix;
    }
    
    private void printfMatrix(Matrix matrix){
        float values[]=new float[9];
        matrix.getValues(values);
        for(int i=0;i<values.length;i++){
//            LogManager.d(TAG, values[i]+"");
        }
    }
    
    public static class BitmapEx{
        private Bitmap bm;
        private Matrix matrix;
    }
    public interface OnBitmapChange{
        public void onBitmapChanged(int newHeight);
    }
    private static final int START=1;
    private static final int STOP=2;
    Handler mHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
            case START:
                changeMatrixValue(false);
                restore(10);
                break;
            case STOP:
                
                break;
            }
        }
        
    };
    
    
    private void showAnimation(final View view, long duration, final float fromAlpha, final float toAlpha,final Bitmap bm,final OnBitmapChange listener) {
        AnimationSet as = new AnimationSet(false);
        Animation myAnimation_Translate = new AlphaAnimation(fromAlpha, toAlpha);
        as.addAnimation(myAnimation_Translate);
        as.setFillAfter(true);
        as.setDuration(duration);
        as.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(toAlpha==0.0 && bm!=null){
                    initBmp(bm,listener);
                    //替换图片,淡入淡出
                    showAnimation(ImageViewEx.this,500,0.0f,1,null,null);
                    System.out.println("fromAlpha-->"+fromAlpha+"  toAlpha-->"+toAlpha);
                }
            }
        });
        view.setAnimation(as);
        view.startAnimation(as);
    }
}
