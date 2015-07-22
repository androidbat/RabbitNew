package com.yitu.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

public class PullView extends FrameLayout implements OnGestureListener, AnimationListener {
    private GestureDetector gestureDetector;
    private TextView title;
    private Animation animationUp;
    private Animation animationDown;
    private ImageView arrow;
    private ProgressBar progressBar;

    private Flinger flinger;
    public static int MAX_LENGHT = 0;
    private int multiple = 2;
    private int paddingTop = 0;
    private UpdateHandle updateHandle;
    private boolean isAutoScroller;
    private String updateData = "";
    private boolean isIgnore = false;

    private final int state_init = 0;
    private final int state_close = 1;
    private final int state_open = 2;
    private final int state_open_max = 3;
    private final int state_open_release = 4;
    private final int state_open_max_release = 5;
    private final int state_update = 6;

    private int state = state_init;

    private OnMeasureCallback measureCallback = null;
    public void setOnMeasureListener(OnMeasureCallback callback)
    {
        this.measureCallback = callback;
    }
    public PullView(Context context) {
        super(context);
        init();
        initLayout();
    }

    public PullView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
        initLayout();
    }

    private void init() {
        MAX_LENGHT = getResources().getDimensionPixelSize(R.dimen.updatebar_height);
        flinger = new Flinger();
        gestureDetector = new GestureDetector(this);
        gestureDetector.setIsLongpressEnabled(true);
        setDrawingCacheEnabled(false);
        setBackgroundDrawable(null);
        setClipChildren(false);
        state = state_close;
        if("".equals(updateData)){
            updateData = getCurDate();
        }
    }

    private void initLayout() {
        animationUp = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_up);
        animationUp.setAnimationListener(this);
        animationDown = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_down);
        animationDown.setAnimationListener(this);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.update_bar, null);
        addView(view);

        arrow = (ImageView) findViewById(R.id.update_bar_img);
        progressBar = (ProgressBar) findViewById(R.id.update_bar_pro);

        title = (TextView) findViewById(R.id.update_bar_title);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isAutoScroller) {
            return true;
        }
        boolean isRelease = false;
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (state == state_open || state == state_open_max) {
                isRelease = release();
            }
        }
        if (isRelease) {
            return isRelease;
        } else {
            if (!isIgnore && gestureDetector.onTouchEvent(ev)) {
                return true;
            } else {
                return super.dispatchTouchEvent(ev);
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        StringBuilder builder = new StringBuilder();
        View view_0 = getChildAt(0);
        View view_1 = getChildAt(1);
        switch (state) {
        case state_close:
            view_0.setVisibility(INVISIBLE);
            view_1.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
            arrow.setImageResource(R.drawable.ic_arrow_down);
            if (paddingTop > 0) {
                paddingTop = 0;
            }
//            if(getChildAt(1) instanceof ListView ){
//                ListView listView=(ListView)getChildAt(1);
//                ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
//            }
            break;
        case state_open:
            view_0.setVisibility(VISIBLE);
            view_0.offsetTopAndBottom(-MAX_LENGHT - paddingTop - view_0.getTop());
            view_1.offsetTopAndBottom(-paddingTop - view_1.getTop());
            progressBar.setVisibility(INVISIBLE);
            arrow.setVisibility(VISIBLE);
            builder.append(getContext().getString(R.string.pull_to_refresh_pull)).append("\n")
                    .append(getContext().getString(R.string.pull_to_refresh_at)).append(updateData);
            title.setText(builder.toString());
            break;
        case state_open_max:
            view_0.setVisibility(VISIBLE);
            view_0.offsetTopAndBottom(-MAX_LENGHT - paddingTop - view_0.getTop());
            view_1.offsetTopAndBottom(-paddingTop - view_1.getTop());
            progressBar.setVisibility(INVISIBLE);
            arrow.setVisibility(VISIBLE);
            builder.append(getContext().getString(R.string.pull_to_refresh_release)).append("\n")
                    .append(getContext().getString(R.string.pull_to_refresh_at)).append(updateData);
            title.setText(builder.toString());
            break;
        case state_open_release:
            break;
        case state_open_max_release:
            break;
        case state_update:
            view_0.setVisibility(VISIBLE);
            view_0.offsetTopAndBottom(-MAX_LENGHT - paddingTop - view_0.getTop());
            view_1.offsetTopAndBottom(-paddingTop - view_1.getTop());
            progressBar.setVisibility(VISIBLE);
            arrow.setVisibility(INVISIBLE);
            builder.append(getContext().getString(R.string.pull_to_refresh_refreshing)).append("\n")
                    .append(getContext().getString(R.string.pull_to_refresh_at)).append(updateData);
            title.setText(builder.toString());
            break;
        }
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            getChildAt(0).layout(left, -paddingTop, right, MAX_LENGHT - paddingTop);
            getChildAt(1).layout(left, -paddingTop, right, getMeasuredHeight() - paddingTop);
        }
    }

    private boolean move(float distanceY, boolean updateScroll) {
        if (state != state_update) {

            paddingTop += distanceY;
            if (paddingTop >= 0) {
                paddingTop = 0;
                state = state_close;
            }
            if (state != state_open_release && state != state_open_max_release) {
                if (Math.abs(paddingTop) > MAX_LENGHT * multiple) {
                    if (state != state_open_max) {
                        state = state_open_max;
                        arrow.startAnimation(animationUp);
                    }
                } else {
                    if (state != state_open && paddingTop != 0) {
                        if (state == state_open_max) {
                            arrow.startAnimation(animationDown);
                        }
                        state = state_open;
                    }
                }
            } else if (state == state_open_release) {
                state = state_open;
            } else if (state == state_open_max_release) {
                state = state_update;
                if (updateHandle != null) {
                    updateHandle.onUpdate();
                }
            }
        } else {
            paddingTop += distanceY;
            if (updateScroll) {
                if (paddingTop > 0) {
                    paddingTop = -MAX_LENGHT;
                }
            } else {
                if (paddingTop > 0) {
                    paddingTop = 0;
                } else if (Math.abs(paddingTop) > MAX_LENGHT) {
                    paddingTop = -MAX_LENGHT;
                }
            }
        }

        if (Math.abs(paddingTop) > 0 && state != state_update) {
            return true;
        } else {
            return false;
        }
    }

    private boolean release() {
        if (paddingTop >= 0 && isAutoScroller) {
            return false;
        }

        if (Math.abs(paddingTop) >= MAX_LENGHT * multiple) {
            state = state_open_max_release;
            scrollToUpdate();
        } else {
            state = state_open_release;
            scrollToClose();
        }

        if (Math.abs(paddingTop) > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void scrollToClose() {
        flinger.startUsingDistance(-paddingTop, 300);
    }

    private void scrollToUpdate() {
        flinger.startUsingDistance(-paddingTop - MAX_LENGHT, 300);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        View view = getChildAt(1);
        if (view instanceof AdapterView) {
            AdapterView adapterView = (AdapterView) view;
            if (adapterView.getFirstVisiblePosition() == 0) {
                if (paddingTop != 0 || adapterView.getChildAt(0) == null || adapterView.getChildAt(0).getTop() == 0) {
                    return move(distanceY, false);
                }
            }
        } else {
            return move(distanceY, false);
        }

        return false;

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if ((state == state_close) || (state == state_open) || (state == state_open_release)) {
            arrow.setImageResource(R.drawable.ic_arrow_down);
        } else {
            arrow.setImageResource(R.drawable.ic_arrow_up);
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    class Flinger implements Runnable {
        private int lastFlingX;
        private Scroller scroller;

        public Flinger() {
            scroller = new Scroller(getContext());
        }

        private void startCommon() {
            removeCallbacks(this);
        }

        public void run() {
            move(-lastFlingX, true);
            if (scroller.computeScrollOffset()) {
                lastFlingX = scroller.getCurrX();
                post(this);
            } else {
                isAutoScroller = false;
                removeCallbacks(this);
            }
        }

        public void startUsingDistance(int dx, int duration) {
            startCommon();
            lastFlingX = 0;
            scroller.startScroll(0, 0, -dx, 0, duration);
            isAutoScroller = true;
            post(this);
        }
    }

    public interface UpdateHandle {
        public abstract void onUpdate();
    }
    
    private int mUpdateState = UPDATE_IDLE_STATE;
    public static final int UPDATE_START_STATE = 0;
    public static final int UPDATE_END_STATE = 1;
    public static final int UPDATE_IDLE_STATE = 3;
    public boolean isUpdating() {
    	return mUpdateState == UPDATE_START_STATE;
    }
    
    public boolean isUpdated() {
    	return mUpdateState == UPDATE_END_STATE;
    }

    public void startUpdate() {
    	mUpdateState = UPDATE_START_STATE;
        paddingTop = -(MAX_LENGHT * multiple + 1);
        release();
        setUpdateData();
    }

    public void endUpdate() {
    	mUpdateState = UPDATE_END_STATE;
        updateData = getCurDate();
        if (paddingTop != 0) {
            scrollToClose();
        }
        state = state_close;
    }

    public void setUpdateHandle(UpdateHandle updateHandle) {
        this.updateHandle = updateHandle;
    }

    public void setUpdateData() {
        this.updateData = getCurDate();
    }

    public String getCurDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        return simpleDateFormat.format(date.getTime());
    }

    public void setIgnore(boolean isIgnore) {
        this.isIgnore = isIgnore;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(measureCallback!=null)
        {
            measureCallback.onMeasured(heightMeasureSpec);
        }
    }
    
    public interface OnMeasureCallback
    {
        void onMeasured(int heightMeasureSpec);
    }
}
