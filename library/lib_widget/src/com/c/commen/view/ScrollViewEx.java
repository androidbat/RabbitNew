package com.c.commen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ScrollViewEx extends ScrollView {
    private OnScrollListener onScrollListener;

    public ScrollViewEx(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ScrollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewEx(Context context) {
        super(context);
    }

    @Override
    public boolean arrowScroll(int direction) {
        return super.arrowScroll(direction);
    }

    /**
     * 设置滚动接口
     * 
     * @param onScrollListener
     */
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
            // System.out.println("------滚动到最下方------");
            if (onScrollListener != null) {
                onScrollListener.onScrollToBottom();
            }
        } else {
            // System.out.println("没有到最下方");
        }
        if (onScrollListener != null) {
            System.out.println("t===="+t);
            onScrollListener.onScroll(t,oldt<t);
        }
    }

    /**
     * 
     * 滚动的回调接 ?
     * 
     */
    public interface OnScrollListener {
        /**
         * 回调方法 ? 返回MyScrollView滑动的Y方向距离
         * 
         * @param scrollY
         *             ?
         */
        public void onScroll(int scrollY,boolean isUp);

        public void onScrollToBottom();
    }
}
