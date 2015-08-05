package com.c.commen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by hanmr on 2015/6/24.
 */
public class YTGridView extends GridView {
    public YTGridView(Context paramContext){
        super(paramContext);
    }

    public YTGridView(Context paramContext, AttributeSet paramAttributeSet){
        super(paramContext, paramAttributeSet);
    }

    public YTGridView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec( Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
