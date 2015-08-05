package com.c.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.yitu.widget.R;

import java.util.ArrayList;

/**
 * Created by wg on 2015/4/21.
 */
public class CircleHoleView extends View {

    public CircleHoleView(Context context) {
        super(context);
    }

    public CircleHoleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleHoleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleHoleView, defStyleAttr, 0);
        if (a != null){
            x = (int) a.getDimension(R.styleable.CircleHoleView_x, 0);
            y = (int) a.getDimension(R.styleable.CircleHoleView_y, 0);
            right = (int) a.getDimension(R.styleable.CircleHoleView_right, -1);
            mRadius = (int) a.getDimension(R.styleable.CircleHoleView_radio, 0);
            bottom = (int) a.getDimension(R.styleable.CircleHoleView_bottom, 0);
            color = a.getColor(R.styleable.CircleHoleView_bg_color,color);
            a.recycle();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Paint paint=new Paint();
        paint.setAlpha(0);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        Bitmap bitmap= Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempcCanvas=new Canvas(bitmap);
        tempcCanvas.drawColor(color);
        if (right >= 0 )
            x = getWidth()-right;
        if (bottom >= 0 )
            y = getHeight()-bottom;
        if (mHoles == null){
            tempcCanvas.drawCircle(x, y, mRadius, paint);
        }else{
            for(Hole hole:mHoles){
                tempcCanvas.drawCircle(hole.x, hole.y, hole.radius, paint);
            }
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    public int x,right=-1,y,mRadius = 100,bottom = -1,color = Color.parseColor("#df000000");

    public void setRadius(int x,int y,int radius){
        mRadius = radius;
        this.x = x;
        this.y = y;
        invalidate();
    }

    private ArrayList<Hole> mHoles;
    public void add(Hole hole){
        if (mHoles == null)
            mHoles = new ArrayList<Hole>();
        mHoles.add(hole);
    }

    public static class Hole{
        public int x;
        public int y;
        public int radius;

        public Hole(int x, int y, int radius) {
            this.x = x;
            this.y = y;
            this.radius = radius;
        }
    }

}
