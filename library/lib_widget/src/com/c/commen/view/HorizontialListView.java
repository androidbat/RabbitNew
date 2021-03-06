package com.c.commen.view;

import java.util.LinkedList;
import java.util.Queue;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

import com.c.commen.view.EasygoAnimation.KXAnimationListener;

public class HorizontialListView extends AdapterView<ListAdapter> implements KXAnimationListener {
    private static final String TAG = "Test";

    protected ListAdapter mAdapter;
    protected Scroller mScroller;

    private GestureDetector mGesture;
    private Queue<View> mRemovedViewQueue = new LinkedList<View>();

    private OnItemSelectedListener mOnItemSelected;
    private OnItemClickListener mOnItemClicked;
    private OnItemLongClickListener mOnItemLongClicked;

    private int mLeftViewIndex = -1;
    private int mRightViewIndex = 0;
    private int mCurrentX;
    private int mNextX;
    private int mMaxX = Integer.MAX_VALUE;
    private int mDisplayOffset = 0;

    private boolean mDataChanged = false;
    
    private OnScrollLister onScrollLister;
    private OnCurrentXpositionListener currentXpositionListener;
    
    boolean isFling = false;

    public HorizontialListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void setOnScrollLister(OnScrollLister onScrollLister ){
        this.onScrollLister  = onScrollLister;
    }
    
    public OnScrollLister getOnScrollLister(){
        return this.onScrollLister;
    }
    
    public OnCurrentXpositionListener getCurrentXpositionListener() {
        return currentXpositionListener;
    }

    public void setCurrentXpositionListener(OnCurrentXpositionListener currentXpositionListener) {
        this.currentXpositionListener = currentXpositionListener;
    }

    private synchronized void initView() {
        mLeftViewIndex = -1;
        mRightViewIndex = 0;
        mDisplayOffset = 0;
        mCurrentX = 0;
        mNextX = 0;
        mMaxX = Integer.MAX_VALUE;
        mScroller = new Scroller(getContext());
        mGesture = new GestureDetector(getContext(), mOnGesture);
    }

    @Override
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mOnItemSelected = listener;
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOnItemClicked = listener;
    }

    @Override
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        mOnItemLongClicked = listener;
    };

    private DataSetObserver mDataObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            synchronized (HorizontialListView.this) {
                mDataChanged = true;
            }
            invalidate();
            requestLayout();
        }

        @Override
        public void onInvalidated() {
            reset();
            invalidate();
            requestLayout();
        }

    };

    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public View getSelectedView() {
        // unimplement
        return null;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataObserver);
        reset();
    }

    private synchronized void reset() {
        initView();
        removeAllViewsInLayout();
        requestLayout();
    }

    @Override
    public void setSelection(int position) {
        // unimplement
    }

    private void addAndMeasureChild(final View child, int viewPos) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        }

        addViewInLayout(child, viewPos, params, true);
        child.measure(MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));
    }

    @Override
    protected synchronized void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mAdapter == null) {
            return;
        }

        if (mDataChanged) {
            int oldCurrentX = mCurrentX;
            initView();
            removeAllViewsInLayout();
            mNextX = oldCurrentX;
            mDataChanged = false;
        }

        if (mScroller.computeScrollOffset()) {
            int scrollx = mScroller.getCurrX();
            mNextX = scrollx;
        }

        if (mNextX < 0) {
            mNextX = 0;
            mScroller.forceFinished(true);
        }
        if (mMaxX>=0 && mNextX > mMaxX) {
            mNextX = mMaxX;
            mScroller.forceFinished(true);
        }

        int dx = mCurrentX - mNextX;

        removeNonVisibleItems(dx);
        fillList(dx);
        positionItems(dx);

        mCurrentX = mNextX;

        if (!mScroller.isFinished()) {
            if(onScrollLister!=null){
                onScrollLister.onScrollStateChanged(this, OnScrollLister.SCROLL_STATE_FLING);
            }
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });

        }else{
            if(isFling){
                if(onScrollLister!=null){
                    onScrollLister.onScrollStateChanged(this, OnScrollLister.SCROLL_STATE_IDLE);
                }
            }
        }
        
        if(currentXpositionListener != null){
           currentXpositionListener.onCurrentXChanged(mCurrentX);
        }
    }

    private void fillList(final int dx) {
        int edge = 0;
        View child = getChildAt(getChildCount() - 1);
        if (child != null) {
            edge = child.getRight();
        }
        fillListRight(edge, dx);

        edge = 0;
        child = getChildAt(0);
        if (child != null) {
            edge = child.getLeft();
        }
        fillListLeft(edge, dx);

    }

    private void fillListRight(int rightEdge, final int dx) {
        while (rightEdge + dx < getWidth() && mRightViewIndex < mAdapter.getCount()) {
            View child = mAdapter.getView(mRightViewIndex, mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, -1);
            rightEdge += child.getMeasuredWidth();

            if (mRightViewIndex == mAdapter.getCount() - 1) {
                mMaxX = mCurrentX + rightEdge - getWidth();
            }
            mRightViewIndex++;
        }

    }

    private void fillListLeft(int leftEdge, final int dx) {
        while (leftEdge + dx > 0 && mLeftViewIndex >= 0) {
            View child = mAdapter.getView(mLeftViewIndex, mRemovedViewQueue.poll(), this);
            addAndMeasureChild(child, 0);
            leftEdge -= child.getMeasuredWidth();
            mLeftViewIndex--;
            mDisplayOffset -= child.getMeasuredWidth();
        }
    }

    private void removeNonVisibleItems(final int dx) {
        View child = getChildAt(0);
        while (child != null && child.getRight() + dx <= 0) {
            mDisplayOffset += child.getMeasuredWidth();
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mLeftViewIndex++;
            child = getChildAt(0);

        }

        child = getChildAt(getChildCount() - 1);
        while (child != null && child.getLeft() + dx >= getWidth()) {
            mRemovedViewQueue.offer(child);
            removeViewInLayout(child);
            mRightViewIndex--;
            child = getChildAt(getChildCount() - 1);
        }
    }

    private void positionItems(final int dx) {
        if (getChildCount() > 0) {
            mDisplayOffset += dx;
            int left = mDisplayOffset;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                child.layout(left, 0, left + childWidth, child.getMeasuredHeight());
                left += childWidth;
            }
        }
    }

    public synchronized void scrollTo(int x) {
        Log.e(TAG, "[HListView|scrollTo](" + mNextX + ",0" + ") - (" + (x - mNextX) + "0)");
        mScroller.startScroll(mNextX, 0, x - mNextX, 0);
        requestLayout();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = mGesture.onTouchEvent(ev);
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            onRelease();
        }
        return handled;
    }
    
    private DecelerateInterpolator interpolator = new DecelerateInterpolator(1.1f);
    private EasygoAnimation animation;
    private void onRelease() {
        
        
        if (mNextX <= 0) {
            animation = new EasygoAnimation(mNextX, 0, 500, this);
            animation.setInterpolator(interpolator);
            mScroller.forceFinished(true);
            post(animation);
        } else 
        if (mNextX >= mMaxX) {
            animation = new EasygoAnimation(mNextX, mMaxX,  500, this);
            animation.setInterpolator(interpolator);
            mScroller.forceFinished(true);
            post(animation);
        } else {
            requestLayout();
        }
//        requestLayout();
    }

    protected boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        synchronized (HorizontialListView.this) {
            isFling = true;
            Log.e(TAG, "[HListView|onFling]");
            mScroller.fling(mNextX, 0, (int) -velocityX, 0, 0, mMaxX, 0, 0);
        }
        
        return true;
    }

    protected boolean onDown(MotionEvent e) {
        isFling = false;
        if(animation != null && animation.isRunning()) {
            animation.stop(true);
            animation = null;
        }
        mScroller.forceFinished(true);
        return true;
    }
    
    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return HorizontialListView.this.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return HorizontialListView.this.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            getParent().requestDisallowInterceptTouchEvent(true);

            synchronized (HorizontialListView.this) {               
                isFling = true;
                mNextX += (int) distanceX;
                if(mNextX < 0 || mNextX > mMaxX) {
                    mNextX -= distanceX / 2;
                }
            }
            requestLayout();

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Rect viewRect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop();
                int bottom = child.getBottom();
                viewRect.set(left, top, right, bottom);
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (mOnItemClicked != null) {
                        mOnItemClicked.onItemClick(HorizontialListView.this, child, mLeftViewIndex + 1 + i,
                                mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    if (mOnItemSelected != null) {
                        mOnItemSelected.onItemSelected(HorizontialListView.this, child, mLeftViewIndex + 1 + i,
                                mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Rect viewRect = new Rect();
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                int left = child.getLeft();
                int right = child.getRight();
                int top = child.getTop();
                int bottom = child.getBottom();
                viewRect.set(left, top, right, bottom);
                if (viewRect.contains((int) e.getX(), (int) e.getY())) {
                    if (mOnItemLongClicked != null) {
                        mOnItemLongClicked.onItemLongClick(HorizontialListView.this, child, mLeftViewIndex + 1 + i,
                                mAdapter.getItemId(mLeftViewIndex + 1 + i));
                    }
                    break;
                }

            }
        };
    };

    public int getFirstVisibleViewIndex(){
        return mLeftViewIndex+1;
    }
    
    public void removeToLeft(){
        setSelectionFromLeft(0);
    }
    
    public void setSelectionFromLeft(int x){
        setSelectionFromLeft(x,true);
    }
    
    public void setSelectionFromLeft(int x,boolean relayout){
        if(x<0){
            x = 0;
        }
        mCurrentX = mNextX = x;
        if(relayout){
            requestLayout();
        }
    }
    
    public int getCurrentXPosition(){
        return mCurrentX;
    }
    
    public interface  OnCurrentXpositionListener{
        public void onCurrentXChanged(int newX);
    }
    
    public  interface OnScrollLister{
        public static int SCROLL_STATE_IDLE = 0;

        public static int SCROLL_STATE_TOUCH_SCROLL = 1;

        public static int SCROLL_STATE_FLING = 2;

        public void onScrollStateChanged(HorizontialListView horizontialListView, int scrollState);
    }
    @Override
    public void onStart(int fromValue, int toValue) {
        
    }

    @Override
    public void onUpdate(int currentValue) {
        mNextX = currentValue;
        requestLayout();
    }

    @Override
    public void onComplete() {
        
    }

}
