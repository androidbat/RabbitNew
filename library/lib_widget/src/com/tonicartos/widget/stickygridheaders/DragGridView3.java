package com.tonicartos.widget.stickygridheaders;

import java.util.LinkedList;
import java.util.List;

import android.R.integer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;

public class DragGridView3 extends StickyGridHeadersGridView {
	
	private static final int MOVE_DURATION = 300;
    private static final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 8;
    
	private DynamicGridAdapterInterface mDynamicGridAdapterInterface;
	
	private int mSmoothScrollAmountAtEdge = 0;
	public DragGridView3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 init(context);
	}

	public DragGridView3(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DragGridView3(Context context) {
		super(context);
		init(context);
	}
	

	private void init(Context context) {
		super.setOnScrollListener(mScrollListener);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mSmoothScrollAmountAtEdge = (int) (SMOOTH_SCROLL_AMOUNT_AT_EDGE * metrics.density + 0.5f);
	}


	private Rect mHoverCellCurrentBounds;
    private Rect mHoverCellOriginalBounds;
    private BitmapDrawable mHoverCell;
    private boolean mCellIsMobile;
    
    private int mTotalOffsetY = 0;
    private int mTotalOffsetX = 0;

    private int mDownX = -1;
    private int mDownY = -1;
    private int mLastEventY = -1;
    private int mLastEventX = -1;
    private int mActivePointerId = -1;
    private static final int INVALID_ID = -1;
	private View mMobileView;
	private int mDragPosition;
    
    public void startDrag(View item,int postion){
    	item.setVisibility(View.INVISIBLE);
    	mHoverCell = getAndAddHoverView(item);
    	mCellIsMobile = true;
    	Log.d("draglist"," startDrag ");
    	mMobileView = item;
    	mDragPosition = postion;
    	invalidate();
    }
    
    private void swipe(final int oldPosition,final int newPosition){
    	if(mDragPosition != newPosition && newPosition != AdapterView.INVALID_POSITION && mAnimationEnd){
    		
	    	if (mDynamicGridAdapterInterface != null) {
				mDynamicGridAdapterInterface.reorderItems(oldPosition, newPosition);
			}
			final ViewTreeObserver observer = getViewTreeObserver();
			observer.addOnPreDrawListener(new OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					if(mMobileView != null)
						mMobileView.setVisibility(View.VISIBLE);
					mMobileView = mHeadData.get(newPosition).view;
					mMobileView.setVisibility(View.INVISIBLE);
					observer.removeOnPreDrawListener(this);
					animateReorder(oldPosition, newPosition);
					mDragPosition = newPosition;
					return true;
				}
			});
    	}
    }
    
    @Override
    public void setAdapter(ListAdapter adapter) {
    	mHeadData.clear();
    	mOriViews.clear();
    	if (adapter instanceof DynamicGridAdapterInterface) {
			mDynamicGridAdapterInterface = (DynamicGridAdapterInterface) adapter;
		}
    	super.setAdapter(adapter);
    }
	
    private BitmapDrawable getAndAddHoverView(View v) {

        int w = v.getWidth();
        int h = v.getHeight();
//        int top = ((View)(v.getParent())).getTop();
        int top = v.getTop();
        int left = v.getLeft();

        Bitmap b = getBitmapFromView(v);

        BitmapDrawable drawable = new BitmapDrawable(getResources(), b);

        Log.d("draglist"," left "+left +" top " +top +" w "+w+" h " + h);
        
        mHoverCellOriginalBounds = new Rect(left, top, left + w, top + h);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);

        drawable.setBounds(mHoverCellCurrentBounds);

        return drawable;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	Log.d("draglist"," onInterceptTouchEvent " +event.getAction());
    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:
	            mDownX = (int) event.getX();
	            mDownY = (int) event.getY();
	            mActivePointerId = event.getPointerId(0);
	            break;
	        case MotionEvent.ACTION_MOVE:
//	            if (mActivePointerId == INVALID_ID) {
//	                break;
//	            }

//	            int pointerIndex = event.findPointerIndex(mActivePointerId);

	            mLastEventY = (int) event.getY();
	            mLastEventX = (int) event.getX();
	            int deltaY = mLastEventY - mDownY;
	            int deltaX = mLastEventX - mDownX;
	            if (mCellIsMobile) {
	            	Log.d("draglist"," deltaX " +deltaX);
	                mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left + deltaX + mTotalOffsetX,
	                        mHoverCellOriginalBounds.top + deltaY + mTotalOffsetY);
	                mHoverCell.setBounds(mHoverCellCurrentBounds);
	                invalidate();
	                handleCellSwitch();
	                mIsMobileScrolling = false;
	                handleMobileCellScroll();
	                return true;
	            }
	            break;
	    	}
    	return super.onInterceptTouchEvent(event);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//    	Log.d("draglist"," onTouchEvent " +event.getAction());
    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            mDownX = (int) event.getX();
            mDownY = (int) event.getY();
            mActivePointerId = event.getPointerId(0);

            break;

        case MotionEvent.ACTION_MOVE:
//            if (mActivePointerId == INVALID_ID) {
//                break;
//            }

//            int pointerIndex = event.findPointerIndex(mActivePointerId);

            mLastEventY = (int) event.getY();
            mLastEventX = (int) event.getX();
            int deltaY = mLastEventY - mDownY;
            int deltaX = mLastEventX - mDownX;
            if (mCellIsMobile) {
//            	Log.d("draglist"," deltaX " +deltaX);
                mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left + deltaX + mTotalOffsetX,
                        mHoverCellOriginalBounds.top + deltaY + mTotalOffsetY);
                mHoverCell.setBounds(mHoverCellCurrentBounds);
                invalidate();
                handleCellSwitch();
                mIsMobileScrolling = false;
                handleMobileCellScroll();
                return false;
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
        	touchEventsEnded();
        	break;
//            touchEventsEnded();
//
//            if (mUndoSupportEnabled) {
//                if (mCurrentModification != null && !mCurrentModification.getTransitions().isEmpty()) {
//                    mModificationStack.push(mCurrentModification);
//                    mCurrentModification = new DynamicGridModification();
//                }
//            }
//
//            if (mHoverCell != null) {
//                if (mDropListener != null) {
//                    mDropListener.onActionDrop();
//                }
//            }
//            break;
//
//        case MotionEvent.ACTION_CANCEL:
//            touchEventsCancelled();
//
//            if (mHoverCell != null) {
//                if (mDropListener != null) {
//                    mDropListener.onActionDrop();
//                }
//            }
//            break;
//
//        case MotionEvent.ACTION_POINTER_UP:
//            /* If a multitouch event took place and the original touch dictating
//             * the movement of the hover cell has ended, then the dragging event
//             * ends and the hover cell is animated to its corresponding position
//             * in the listview. */
//            pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >>
//                    MotionEvent.ACTION_POINTER_INDEX_SHIFT;
//            final int pointerId = event.getPointerId(pointerIndex);
//            if (pointerId == mActivePointerId) {
//                touchEventsEnded();
//            }
//            break;

        default:
            break;
    	}
    	return super.onTouchEvent(event);
    }
    
    private void handleCellSwitch() {
    	final int deltaYTotal = mHoverCellCurrentBounds.centerY();
    	final int deltaXTotal = mHoverCellCurrentBounds.centerX();
    	
    	int pos = -1;
    	final int size = mHeadData.size();
//    	System.out.println(" getChildCount "+getChildCount() +" handleCellSwitch " + pos);
//    	System.out.println(" mViews.size() "+size +" views.size() "+mOriViews.size());
    	for (int i=size-1;i>=0;i--) {
    		int position = mHeadData.keyAt(i);
        	View view = mHeadData.get(position).view;
        	
        	if (pointInView(deltaXTotal,deltaYTotal,view)) {
        		pos = position;
        		break;
			}
		}
    	
//    	for (int i = 0; i < mHeadData.size(); i++) {
//			int key = mHeadData.keyAt(i);
//			System.out.println(" key "+key +" | "+mHeadData.get(key).view.hashCode());
//		}
    	
//    	System.out.println(" handleCellSwitch " + pos);
        if (pos != -1 ) {
			swipe(mDragPosition,pos);
		}
    }

	private boolean pointInView(int deltaXTotal, int deltaYTotal, View view) {
		if (view.getLeft() < deltaXTotal && view.getTop() < deltaYTotal) {
			if ((view.getLeft()+view.getWidth() > deltaXTotal && view.getTop()+view.getHeight() > deltaYTotal)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean mIsMobileScrolling;
    private void handleMobileCellScroll() {
        mIsMobileScrolling = handleMobileCellScroll(mHoverCellCurrentBounds);
    }

    public boolean handleMobileCellScroll(Rect r) {
//    	if (!mAnimationEnd) {
//			return false;
//		}
        int offset = computeVerticalScrollOffset();
        int height = getHeight();
        int extent = computeVerticalScrollExtent();
        int range = computeVerticalScrollRange();
        int hoverViewTop = r.top;
        int hoverHeight = r.height();

        if (hoverViewTop <= 0 && offset > 0) {
            smoothScrollBy(-mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        if (hoverViewTop + hoverHeight >= height && (offset + extent) < range) {
            smoothScrollBy(mSmoothScrollAmountAtEdge, 0);
            return true;
        }

        return false;
    }

	private void touchEventsEnded(){
		if (mMobileView != null && (mCellIsMobile || mIsWaitingForScrollFinish)) {
			
			mIsWaitingForScrollFinish = false;
			
			 if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) {
	                mIsWaitingForScrollFinish = true;
	                return;
	            }
			 
			mCellIsMobile = false;
			mHoverCell = null;
			invalidate();
			
			if (mMobileView != null) {
				mMobileView.setVisibility(View.VISIBLE);
			}
		}
    }
    
    /**
     * Returns a bitmap showing a screenshot of the view passed in.
     */
    private Bitmap getBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }
    
    @Override
    protected void dispatchDraw(Canvas canvas) {
    	super.dispatchDraw(canvas);
    	if (mHoverCell != null) {
            mHoverCell.draw(canvas);
        }
    }
	
    private int mNumColumns = 3;
    private boolean mAnimationEnd = true;
    private void animateReorder(final int oldPosition, final int newPosition) {
		boolean isForward = newPosition > oldPosition;
		
		long oldHeadId = mHeadData.get(oldPosition).head;
		long newHeadId = mHeadData.get(newPosition).head;
		
		Log.d("head"," oldHeadId "+oldHeadId +" newHeadId "+newHeadId +" oldPosition "+oldPosition);
		
		List<Animator> resultList = new LinkedList<Animator>();
		try {
			if (isForward) {
				for (int pos = oldPosition; pos < newPosition; pos++) {
					View view = mHeadData.get(pos).view;
					if ((pos + 1) % mNumColumns == 0) {
						resultList.add(createTranslationAnimations(view,
								- view.getWidth() * (mNumColumns - 1), 0,
								view.getHeight(), 0));
					} else {
						resultList.add(createTranslationAnimations(view,
								view.getWidth(), 0, 0, 0));
					}
				}
			} else {
				for (int pos = oldPosition; pos > newPosition; pos--) {
					View view = mHeadData.get(pos).view;
					if ((pos + mNumColumns) % mNumColumns == 0) {
						resultList.add(createTranslationAnimations(view,
								view.getWidth() * (mNumColumns - 1), 0,
								-view.getHeight(), 0));
					} else {
						resultList.add(createTranslationAnimations(view,
								-view.getWidth(), 0, 0, 0));
					}
				}
			}
		} catch (Exception e) {
			Toast.makeText(getContext(), "erro ", 0).show();
			e.printStackTrace();
		}

		AnimatorSet resultSet = new AnimatorSet();
		resultSet.playTogether(resultList);
		resultSet.setDuration(300);
		resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
		resultSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				mAnimationEnd = false;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mAnimationEnd = true;
			}
		});
		resultSet.start();
	}
	
	private AnimatorSet createTranslationAnimations(View view, float startX,
			float endX, float startY, float endY) {
		ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
				startX, endX);
		ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
				startY, endY);
		AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.playTogether(animX, animY);
		return animSetXY;
	}
	
	private SparseArray<HeadData> mHeadData = new SparseArray<HeadData>();
	private SparseArray<View> mOriViews = new SparseArray<View>();
	public void addItemView(int position, HeadData convertView) {
//		if (mViews.get(position) != null && views.get(position) != null) {
			mHeadData.remove(position);
			mOriViews.remove(position);
//		}
		final int i = mOriViews.indexOfValue(convertView.view);
		if (i != -1) {
			mHeadData.removeAt(i);
			mOriViews.removeAt(i);
		}
		
		mOriViews.put(position, convertView.view);
		mHeadData.put(position, convertView);
		
		System.out.println(" mOriViews.size "+mOriViews.size() +" mHeadData "+mHeadData.size());
		
//		System.out.println(" 1111111111111111111");
//		String a =""; 
//		for (int j = 0; j < mViews.size(); j++) {
//			int key = mViews.keyAt(j);
//			a += " key "+key;
//		}
//		System.out.println(" key "+key);
	}
	
	 private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	 private boolean mIsWaitingForScrollFinish = false;
	private OnScrollListener mScrollListener = new OnScrollListener() {

        private int mPreviousFirstVisibleItem = -1;
        private int mPreviousVisibleItemCount = -1;
        private int mCurrentFirstVisibleItem;
        private int mCurrentVisibleItemCount;
        private int mCurrentScrollState;

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            mCurrentFirstVisibleItem = firstVisibleItem;
            mCurrentVisibleItemCount = visibleItemCount;

            mPreviousFirstVisibleItem = (mPreviousFirstVisibleItem == -1) ? mCurrentFirstVisibleItem
                    : mPreviousFirstVisibleItem;
            mPreviousVisibleItemCount = (mPreviousVisibleItemCount == -1) ? mCurrentVisibleItemCount
                    : mPreviousVisibleItemCount;

//            checkAndHandleFirstVisibleCellChange();
//            checkAndHandleLastVisibleCellChange();

            mPreviousFirstVisibleItem = mCurrentFirstVisibleItem;
            mPreviousVisibleItemCount = mCurrentVisibleItemCount;
            if (mUserScrollListener != null) {
                mUserScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            mCurrentScrollState = scrollState;
            mScrollState = scrollState;
            isScrollCompleted();
            if (mUserScrollListener != null) {
                mUserScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        /**
         * This method is in charge of invoking 1 of 2 actions. Firstly, if the gridview
         * is in a state of scrolling invoked by the hover cell being outside the bounds
         * of the gridview, then this scrolling event is continued. Secondly, if the hover
         * cell has already been released, this invokes the animation for the hover cell
         * to return to its correct position after the gridview has entered an idle scroll
         * state.
         */
        private void isScrollCompleted() {
//            if (mCurrentVisibleItemCount > 0 && mCurrentScrollState == SCROLL_STATE_IDLE) {
//                if (mCellIsMobile && mIsMobileScrolling) {
//                    handleMobileCellScroll();
//                } else if (mIsWaitingForScrollFinish) {
//                    touchEventsEnded();
//                }
//            }
        }

        /**
         * Determines if the gridview scrolled up enough to reveal a new cell at the
         * top of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleFirstVisibleCellChange() {
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem) {
                if (mCellIsMobile && mDragPosition != INVALID_ID) {
                    handleCellSwitch();
                }
            }
        }

        /**
         * Determines if the gridview scrolled down enough to reveal a new cell at the
         * bottom of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleLastVisibleCellChange() {
            int currentLastVisibleItem = mCurrentFirstVisibleItem + mCurrentVisibleItemCount;
            int previousLastVisibleItem = mPreviousFirstVisibleItem + mPreviousVisibleItemCount;
            if (currentLastVisibleItem != previousLastVisibleItem) {
                if (mCellIsMobile && mDragPosition != INVALID_ID) {
                    handleCellSwitch();
                }
            }
        }
    };
    
    private OnScrollListener mUserScrollListener;
    @Override
    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.mUserScrollListener = scrollListener;
    }
    
    public static class HeadData{
    	
    	public HeadData(int positon, View view) {
			super();
			this.positon = positon;
			this.view = view;
		}
		public int positon;
    	public View view;
    	public long head;
    	public int headFiretPosition;
    	
    	@Override
    	public int hashCode() {
    		return super.hashCode();
    	}
    	
    }
}
