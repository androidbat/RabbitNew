/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.c.commen.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;


public class ObservableListView extends ListView {
    private static final String TAG = ObservableListView.class.getSimpleName();

    private ObservableScrollViewCallbacks mCallbacks;
    private int mPrevFirstVisiblePosition;
    private int mPrevFirstVisibleChildHeight = -1;
    private int mPrevScrolledChildrenHeight;
    private SparseIntArray mChildrenHeights;
    private int mPrevScrollY;
    private int mScrollY;
    private ScrollState mScrollState;
    private boolean mFirstScroll;
    private boolean mDragging;
    private boolean isRequestLayout;

    private OnScrollListener mOriginalScrollListener;
    private OnScrollListener mScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mOriginalScrollListener != null) {
                mOriginalScrollListener.onScrollStateChanged(view, scrollState);
            }
//            Log.d("scroll", " ------ scrollState"+scrollState);
            
            if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            	mDragging = true;
			}else if (scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				if (!isRequestLayout) {
					mDragging = true;
				}
			}else if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				isRequestLayout = false;
			}
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (mOriginalScrollListener != null) {
                mOriginalScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
            // AbsListView#invokeOnItemScrollListener calls onScrollChanged(0, 0, 0, 0)
            // on Android 4.0+, but Android 2.3 is not. (Android 3.0 is unknown)
            // So call it with onScrollListener.
            onScrollChanged();
        }
    };

    public ObservableListView(Context context) {
        super(context);
        init();
    }

    public ObservableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ObservableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    @Override
    public void setAdapter(ListAdapter adapter) {
    	super.setAdapter(adapter);
    	mDragging = false;
    }
    
    @Override
    public void smoothScrollToPosition(int position) {
    	mDragging = false;
    	super.smoothScrollToPosition(position);
    }
    
    @Override
    public void requestLayout() {
    	mDragging = false;
    	isRequestLayout = true;
    	super.requestLayout();
    }
    
	@Override
	public void smoothScrollToPositionFromTop(int position, int offset,
			int duration) {
		mDragging = false;
		super.smoothScrollToPositionFromTop(position, offset, duration);
	}
    
    @Override
    public void setSelection(int position) {
    	mDragging = false;
    	super.setSelection(position);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mCallbacks != null) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
//                    Log.v(TAG, "onTouchEvent: ACTION_DOWN");
                    mFirstScroll  = mDragging = true;
                    mCallbacks.onDownMotionEvent();
                    isRequestLayout = false;
                    break;
                case MotionEvent.ACTION_MOVE:
//                	mDragging = true;
                	break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
//                    Log.v(TAG, "onTouchEvent: ACTION_UP|ACTION_CANCEL");
//                    mDragging = false;
                    mCallbacks.onUpOrCancelMotionEvent(mScrollState);
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        // Don't set l to super.setOnScrollListener().
        // l receives all events through mScrollListener.
        mOriginalScrollListener = l;
    }

    public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
        mCallbacks = listener;
    }

    public int getCurrentScrollY() {
        return mScrollY;
    }

    private void init() {
        mChildrenHeights = new SparseIntArray();
        super.setOnScrollListener(mScrollListener);
    }

    private void onScrollChanged() {
        if (mCallbacks != null) {
            if (getChildCount() > 0) {
                int firstVisiblePosition = getFirstVisiblePosition();
                for (int i = getFirstVisiblePosition(), j = 0; i <= getLastVisiblePosition(); i++, j++) {
                    if (mChildrenHeights.indexOfKey(i) < 0 || getChildAt(j).getHeight() != mChildrenHeights.get(i)) {
                        mChildrenHeights.put(i, getChildAt(j).getHeight());
                    }
                }

                View firstVisibleChild = getChildAt(0);
                if (firstVisibleChild != null) {
                    if (mPrevFirstVisiblePosition < firstVisiblePosition) {
                        // scroll down
                        int skippedChildrenHeight = 0;
                        if (firstVisiblePosition - mPrevFirstVisiblePosition != 1) {
//                            Log.v(TAG, "Skipped some children while scrolling down: " + (firstVisiblePosition - mPrevFirstVisiblePosition));
                            for (int i = firstVisiblePosition - 1; i > mPrevFirstVisiblePosition; i--) {
                                if (0 < mChildrenHeights.indexOfKey(i)) {
                                    skippedChildrenHeight += mChildrenHeights.get(i);
//                                    Log.v(TAG, "Calculate skipped child height at " + i + ": " + mChildrenHeights.get(i));
                                } else {
//                                    Log.v(TAG, "Could not calculate skipped child height at " + i);
                                    // Approximate each item's height to the first visible child.
                                    // It may be incorrect, but without this, scrollY will be broken
                                    // when scrolling from the bottom.
                                    skippedChildrenHeight += firstVisibleChild.getHeight();
                                }
                            }
                        }
                        mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                    } else if (firstVisiblePosition < mPrevFirstVisiblePosition) {
                        // scroll up
                        int skippedChildrenHeight = 0;
                        if (mPrevFirstVisiblePosition - firstVisiblePosition != 1) {
//                            Log.v(TAG, "Skipped some children while scrolling up: " + (mPrevFirstVisiblePosition - firstVisiblePosition));
                            for (int i = mPrevFirstVisiblePosition - 1; i > firstVisiblePosition; i--) {
                                if (0 < mChildrenHeights.indexOfKey(i)) {
                                    skippedChildrenHeight += mChildrenHeights.get(i);
//                                    Log.v(TAG, "Calculate skipped child height at " + i + ": " + mChildrenHeights.get(i));
                                } else {
//                                    Log.v(TAG, "Could not calculate skipped child height at " + i);
                                    // Approximate each item's height to the first visible child.
                                    // It may be incorrect, but without this, scrollY will be broken
                                    // when scrolling from the bottom.
                                    skippedChildrenHeight += firstVisibleChild.getHeight();
                                }
                            }
                        }
                        mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                    } else if (firstVisiblePosition == 0) {
                        mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
                    }
                    if (mPrevFirstVisibleChildHeight < 0) {
                        mPrevFirstVisibleChildHeight = 0;
                    }
                    mScrollY = mPrevScrolledChildrenHeight - firstVisibleChild.getTop();
                    mPrevFirstVisiblePosition = firstVisiblePosition;

//                    Log.v(TAG, "first: " + firstVisiblePosition + " scrollY: " + mScrollY + " first height: " + firstVisibleChild.getHeight() + " first top: " + firstVisibleChild.getTop());

                    if (mPrevScrollY < mScrollY) {
                        //down
                        mScrollState = ScrollState.UP;
                    } else if (mScrollY < mPrevScrollY) {
                        //up
                        mScrollState = ScrollState.DOWN;
                    } else {
                        mScrollState = ScrollState.STOP;
                    }
//                    if (!isNoTouchEvent) {
                    	mCallbacks.onScrollChanged(mScrollY, mFirstScroll, mDragging,mScrollState);
//					}
                    if (mFirstScroll) {
                    	mFirstScroll = false;
                    }
                    mPrevScrollY = mScrollY;
                } else {
//                    Log.v(TAG, "first: " + firstVisiblePosition);
                }
            }
        }
    }
}
