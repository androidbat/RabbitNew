package com.c.commen.view;


import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class EasygoAnimation implements Runnable {

    public interface KXAnimationListener {
        public void onStart(int fromValue,int toValue);

        public void onUpdate(int currentValue);

        public void onComplete();
    }

    public static class SimpleKXAnimationListener implements KXAnimationListener {
        @Override
        public void onStart(int fromValue,int toValue) {
        }

        @Override
        public void onUpdate(int currentValue) {
        };

        @Override
        public void onComplete() {
        }
    }

    private static int ANIMATION_FPS = 1000 / 20;

    private Interpolator interpolator;

    private final int toValue;

    private final int fromValue;

    private boolean continueRunning = true;

    private long startTime = -1;

    private int currentValue = -1;

    private long mDuration;

    private final Handler handler = new Handler();

    private final KXAnimationListener mListener;

    /**
     * Ĭ  duration=1000
     * 
     * @param from
     * @param to
     * @param listener
     */
    public EasygoAnimation(int from, int to, KXAnimationListener listener) {
        this(from, to, 1000, listener);
    }

    public EasygoAnimation(int from, int to, long duration, KXAnimationListener listener) {
        if (listener == null) {
            throw new NullPointerException("the listener should not be null");
        }
        this.fromValue = from;
        this.toValue = to;        
        mDuration = duration;
        mListener = listener;
    }
    
    private void ensureInterpolator() {
        if(interpolator == null) {
            this.interpolator = new AccelerateDecelerateInterpolator();
        }
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    @Override
    public void run() {
        if (startTime == -1) {
            startTime = System.currentTimeMillis();
            mListener.onUpdate(fromValue);
            mListener.onStart(fromValue, toValue);
            continueRunning = true;
            ensureInterpolator();
        } else {
            long normalizedTime = (1000 * (System.currentTimeMillis() - startTime)) / mDuration;
            normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

            final int deltaY = Math.round((fromValue - toValue)
                    * interpolator.getInterpolation(normalizedTime / 1000f));
            this.currentValue = fromValue - deltaY;

            mListener.onUpdate(currentValue);
        }

        if (continueRunning && toValue != currentValue) {
            handler.postDelayed(this, ANIMATION_FPS);
        } else {
            continueRunning = false;
            mListener.onComplete();
        }
    }

    public void stop(boolean hold) {
        this.continueRunning = false;
        handler.removeCallbacks(this);
        if (!hold) {
            mListener.onUpdate(toValue);
        }
    }

    public void stop() {
        stop(true);
    }

    public boolean isRunning() {
        return continueRunning;
    }
}
