package com.c.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LinearLayoutEx extends LinearLayout{

    public LinearLayoutEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinearLayoutEx(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mOriginalHeight = getMeasuredHeight();
        System.out.println("widthMeasureSpec="+widthMeasureSpec+" heightMeasureSpec="+heightMeasureSpec+"  mOriginalHeight="+mOriginalHeight);
    }

}
