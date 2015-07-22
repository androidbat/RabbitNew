package com.tonicartos.widget.stickygridheaders;

import java.util.LinkedList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public class DragGridView extends StickyGridHeadersGridView {
	
	private static final int MOVE_DURATION = 300;
    private static final int SMOOTH_SCROLL_AMOUNT_AT_EDGE = 8;
    
	private DynamicGridAdapterInterface mDynamicGridAdapterInterface;
	
	private int mSmoothScrollAmountAtEdge = 0;
	private int mEdgeSpace = 0;
	private long mOldHeadId = -1;
	private long mNewHeadId = -1;
	private int edage = 0;
	private int headHeight;
	public DragGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 init(context);
	}

	public DragGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DragGridView(Context context) {
		super(context);
		init(context);
	}
	

	private void init(Context context) {
		super.setOnScrollListener(mScrollListener);
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mSmoothScrollAmountAtEdge = (int) (SMOOTH_SCROLL_AMOUNT_AT_EDGE * metrics.density + 0.5f);
        headHeight = (int) (40 * metrics.density + 0.5f);
        edage = (int) (4 * metrics.density + 0.5f);
        mEdgeSpace = (int) (10 * metrics.density + 0.5f);
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
    
    private int i = -1;
    private boolean isBackNull;
    private void swipe(int oldPosition,int newPosition,boolean isUp){
    	if(mDragPosition != newPosition && newPosition != AdapterView.INVALID_POSITION && mAnimationEnd){
    		mAnimationEnd = false;
    		isBackNull = false;
    		long oldHeadId = -1;
			long newHeadId = -1;
			try {
				oldHeadId = mHeadData.get(oldPosition).head;
				newHeadId = mHeadData.get(newPosition).head;
			} catch (Exception e) {
				e.printStackTrace();
			}
    		
    		mOldHeadId = oldHeadId;
    		mNewHeadId = newHeadId;
	    	if (mDynamicGridAdapterInterface != null) {
	    		if (isUp) {
	    			int j = mDynamicGridAdapterInterface.appEnd(oldPosition, newPosition);
	    			Log.d("head", "newPosition" + newPosition +" mOldHeadId "+mOldHeadId +" mNewHeadId "+mNewHeadId);
	    			if (oldHeadId != newHeadId) {
	    				newPosition +=1+j;
	    				if (oldHeadId > newHeadId) {
//	    					oldPosition +=1;
						}
					}
	    			Log.d("head", "newPosition" + newPosition);
				}else{
					int i = mDynamicGridAdapterInterface.reorderItems(oldPosition, newPosition);
					if (i ==1) {
						newPosition +=1;
					}else if(i==-1){
						
					}
					
					if (i == -1) {
						isBackNull = true;
					}else{
						isBackNull = false;
					}
				}
	    		if (newHeadId == oldHeadId) {
	    			i = 0;
	    		}else if (newHeadId < oldHeadId) {
	    			i = 1;
	    		}else if (newHeadId > oldHeadId){
	    			newPosition -= 1;
	    			i = 2;
	    		}
			}
	    	
	    	final int np = newPosition;
	    	final int op = oldPosition;
	    	mDragPosition = np;
			final ViewTreeObserver observer = getViewTreeObserver();
			observer.addOnPreDrawListener(new OnPreDrawListener() {
				@Override
				public boolean onPreDraw() {
					if(mMobileView != null)
						mMobileView.setVisibility(View.VISIBLE);
					Log.d("head", " op "+op +" np "+np+" mDragPosition "+mDragPosition);
					try {
						mAnimationEnd = true;
						mMobileView = mHeadData.get(np).view;
						mMobileView.setVisibility(View.INVISIBLE);
						observer.removeOnPreDrawListener(this);
						animateReorder(op, np);
					} catch (Exception e) {
						mAnimationEnd = true;
						e.printStackTrace();
					}
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
        
//        int pad = dip2px(getContext(), 5);
        b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight());

        BitmapDrawable drawable = new BitmapDrawable(getResources(), b);

        mHoverCellOriginalBounds = new Rect(left-edage, top-edage, left + w+edage, top + h+edage);
        mHoverCellCurrentBounds = new Rect(mHoverCellOriginalBounds);

        drawable.setBounds(mHoverCellCurrentBounds);

        return drawable;
    }
    
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	switch (event.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:
	            mDownX = (int) event.getX();
	            mDownY = (int) event.getY();
	            mActivePointerId = event.getPointerId(0);
	            break;
	        case MotionEvent.ACTION_MOVE:

	            mLastEventY = (int) event.getY();
	            mLastEventX = (int) event.getX();
	            int deltaY = mLastEventY - mDownY;
	            int deltaX = mLastEventX - mDownX;
	            if (mCellIsMobile) {
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
        	
            mLastEventY = (int) event.getY();
            mLastEventX = (int) event.getX();
            int deltaY = mLastEventY - mDownY;
            int deltaX = mLastEventX - mDownX;
            if (mCellIsMobile) {
//            	Log.d("head"," deltaX " +deltaX);
                mHoverCellCurrentBounds.offsetTo(mHoverCellOriginalBounds.left + deltaX + mTotalOffsetX,
                        mHoverCellOriginalBounds.top + deltaY + mTotalOffsetY);
                mHoverCell.setBounds(mHoverCellCurrentBounds);
                invalidate();
                handleCellSwitch(false);
                mIsMobileScrolling = false;
                handleMobileCellScroll();
                return true;
            }
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
        	touchEventsEnded();
        	break;
        default:
            break;
    	}
    	return super.onTouchEvent(event);
    }
    
    
    private boolean isLastOut;
    private void handleCellSwitch(boolean isUp) {
    	try {
			final int deltaYTotal = mHoverCellCurrentBounds.centerY();
			final int deltaXTotal = mHoverCellCurrentBounds.centerX();
			
			if (!mAnimationEnd) {
				return;
			}
			int pos = -1;
			final int size = mOriViews.size();
			isLastOut = false;
			for (int i=size-1;i>=0;i--) {
				int position = mOriViews.keyAt(i);
				View view = mOriViews.get(position);
				
				if (pointInView(deltaXTotal,deltaYTotal,view,position,isUp)) {
					pos = position;
					break;
				}
			}
			
			if (pos != -1 ) {
				Log.d("head"," handleCellSwitc "+mDragPosition +" pos "+pos +" isLastOut "+isLastOut);
				swipe(mDragPosition,pos,isLastOut);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private boolean pointInView(int deltaXTotal, int deltaYTotal,View view,int position,boolean isUp) {
		
		if (view == null) {
			return false;
		}
		
		if (view.getAlpha() == 0) {
			if (view.getTop()+mEdgeSpace < deltaYTotal && view.getTop()+view.getHeight() > deltaYTotal+mEdgeSpace) {
				Log.d("head"," 111111111111111 ");
				return true;
			}
		}
		
		if (view.getLeft()+mEdgeSpace < deltaXTotal && view.getTop()+mEdgeSpace < deltaYTotal) {
			if (view.getTop()+view.getHeight() > deltaYTotal+mEdgeSpace) {
				if (view.getLeft()+view.getWidth() > deltaXTotal+mEdgeSpace) {
					Log.d("head"," 222222222222222222222 " +view.hashCode());
					return true;
				}else if (view.getLeft()+view.getWidth()*2 > deltaXTotal+mEdgeSpace) {//好位置
					HeadData data = mHeadData.get(position);
					HeadData data2 = mHeadData.get(position+1);
					if (data != null) {
						if (data2 == null || data2.headFiretPosition != data.headFiretPosition) {
							Log.d("head"," 333333333333333333333 " +view.hashCode());
							//添加新位置
							if (position != mDragPosition ) {
								isLastOut = true;
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean mIsMobileScrolling;
    private void handleMobileCellScroll() {
        mIsMobileScrolling = handleMobileCellScroll(mHoverCellCurrentBounds);
    }

    public boolean handleMobileCellScroll(Rect r) {
    	if (!mAnimationEnd) {
			return false;
		}
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
		
		if (mMobileView != null) {
			mMobileView.setVisibility(View.VISIBLE);
		}
		if (mMobileView != null && (mCellIsMobile || mIsWaitingForScrollFinish)) {
			mHoverCellCurrentBounds.offsetTo(mMobileView.getLeft(), mMobileView.getTop());
		}
		mIsWaitingForScrollFinish = false;
		mCellIsMobile = false;
		mHoverCell = null;
		mIsMobileScrolling = false;
		
		if (mScrollState != OnScrollListener.SCROLL_STATE_IDLE) {
			mIsWaitingForScrollFinish = true;
			return;
		}
		invalidate();
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
		mAnimationEnd = false;
		List<Animator> resultList = new LinkedList<Animator>();
		try {
			final long oldHeadId = mHeadData.get(oldPosition).head;
			final long newHeadId = mHeadData.get(newPosition).head;
			Log.d("head"," oldHeadId "+oldHeadId +" newHeadId "+newHeadId +" oldPosition "+oldPosition+" newPostion "+newPosition);
			int sePositin = 0;
			if (isForward || i == 2) {
				for (int pos = oldPosition; pos < newPosition; pos++) {
					HeadData headData = mHeadData.get(pos);
					View view = headData.view;
					long headId = headData.head;
					
					if (mOldHeadId != - 1 && mOldHeadId != headId) {
						break;
					}
					mOldHeadId = headId;
					Log.d("head"," old pos "+pos +" headId "+headId +" first "+headData.headFiretPosition);
					if ((pos +1 - headData.headFiretPosition) % mNumColumns == 0) {
						resultList.add(createTranslationAnimations(view,
								- view.getWidth() * (mNumColumns - 1), 0,
								view.getHeight(), 0));
					} else {
						resultList.add(createTranslationAnimations(view,
								view.getWidth(), 0, 0, 0));
					}
				} 
				Log.d("head","------------------------------- " +i);
				for (int pos = newPosition+1; i == 2  && pos < getAdapter().getCount(); pos++) {
					HeadData headData = mHeadData.get(pos);
					Log.d("head","------------------------------22");
					if (headData == null || (mNewHeadId != -1 && mNewHeadId != headData.head)) {
						break;
					}
					View view = headData.view;
					mNewHeadId = headData.head;
					Log.d("head"," new pos "+pos +" headId "+mNewHeadId +" first "+headData.headFiretPosition);
					if ((pos + mNumColumns-headData.headFiretPosition) % mNumColumns == 0) {
						resultList.add(createTranslationAnimations(view,
								view.getWidth() * (mNumColumns - 1), 0,
								-view.getHeight(), 0));
					} else {
						resultList.add(createTranslationAnimations(view,
								-view.getWidth(), 0, 0, 0));
					}
				}
			} else {//向上拖
				//新位置动画
				for (int pos = newPosition; pos <= oldPosition; pos++) {
					HeadData headData = mHeadData.get(pos);
					View view = headData.view;
					long headId = headData.head;
					Log.d("head"," new pos  "+pos +" headId "+headId +" oldHeadId "+oldHeadId+" first "+headData.headFiretPosition);
					
					if (newHeadId != headId) {
						break;
					}
					if ((pos -headData.headFiretPosition) % mNumColumns == 0) {
						resultList.add(createTranslationAnimations(view,
								view.getWidth() * (mNumColumns - 1), 0,
								-view.getHeight(), 0));
					} else {
						resultList.add(createTranslationAnimations(view,
								-view.getWidth(), 0,0 , 0));
					}
				}
				
				int start = oldPosition+1;
				if (isBackNull) {
					start = oldPosition;
				}
				Log.d("head", " oldPosition start " + start);
				//旧位置动画
				for (int pos = start; i== 1 && pos < getAdapter().getCount(); pos++) {
					HeadData headData = mHeadData.get(pos);
					if (headData == null || (mOldHeadId != -1 && mOldHeadId != headData.head)) {
						Log.d("head", " mOldHeadId  " + mOldHeadId +" !=  headData.head "+headData.head);
						break;
					}
					View view = headData.view;
					Log.d("head"," old pos "+pos +" headId "+mOldHeadId +" first "+headData.headFiretPosition);
					if ((pos+1-headData.headFiretPosition) % mNumColumns == 0) {
						resultList.add(createTranslationAnimations(view,
								-view.getWidth() * (mNumColumns - 1), 0,
								view.getHeight(), 0));
					} else {
						resultList.add(createTranslationAnimations(view,
								view.getWidth(), 0, 0, 0));
					}
				}
			}
		} catch (Exception e) {
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
		
		Log.d("head"," mOriViews.size "+mOriViews.size() +" mHeadData "+mHeadData.size());
		
//		System.out.println(" 1111111111111111111");
//		String a =""; 
//		for (int j = 0; j < mViews.size(); j++) {
//			int key = mViews.keyAt(j);
//			a += " key "+key;
//		}
//		System.out.println(" key "+key);
	}
	
	public void clearItemViews(){
		mHeadData.clear();
		mOriViews.clear();
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

            checkAndHandleFirstVisibleCellChange();
            checkAndHandleLastVisibleCellChange();

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
            if (mCurrentScrollState == SCROLL_STATE_IDLE) {
                if (mCellIsMobile && mIsMobileScrolling) {
                    handleMobileCellScroll();
                } else if (mIsWaitingForScrollFinish) {
                    touchEventsEnded();
                }
            }
        }

        /**
         * Determines if the gridview scrolled up enough to reveal a new cell at the
         * top of the list. If so, then the appropriate parameters are updated.
         */
        public void checkAndHandleFirstVisibleCellChange() {
            if (mCurrentFirstVisibleItem != mPreviousFirstVisibleItem) {
                if (mCellIsMobile && mDragPosition != INVALID_ID) {
                	Log.d("head"," checkAndHandleFirstVisibleCellChange ");
                    handleCellSwitch(false);
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
                	Log.d("head"," checkAndHandleLastVisibleCellChange ");
                    handleCellSwitch(false);
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
    	public int headFiretPosition;//当前position组中的第一个position
    	
    	@Override
    	public int hashCode() {
    		return super.hashCode();
    	}
    	
    }
}
