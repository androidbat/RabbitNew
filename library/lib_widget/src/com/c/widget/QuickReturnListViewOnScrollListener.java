package com.c.widget;

import java.util.Dictionary;
import java.util.Hashtable;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * Created by etiennelawlor on 7/10/14.
 */
public class QuickReturnListViewOnScrollListener implements AbsListView.OnScrollListener {

	// region Member Variables
	private int mMinFooterTranslation;
	private int mMinHeaderTranslation;
	private int mPrevScrollY = 0;
	private int mHeaderDiffTotal = 0;
	private int mFooterDiffTotal = 0;
	private View mHeader;
	private View mFooter;
	private QuickReturnType mQuickReturnType;
	private boolean mCanSlideInIdleScrollState = false;

	// endregion

	// region Constructors
	public QuickReturnListViewOnScrollListener(QuickReturnType quickReturnType, View headerView, int headerTranslation, View footerView, int footerTranslation) {
		mQuickReturnType = quickReturnType;
		mHeader = headerView;
		mMinHeaderTranslation = headerTranslation;
		mFooter = footerView;
		mMinFooterTranslation = footerTranslation;
	}
	
	private OnScrollListener mOriginalScrollListener;
    public void setOnScrollListener(OnScrollListener l) {
        mOriginalScrollListener = l;
    }

	// endregion

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// Log.d(getClass().getSimpleName(), "onScrollStateChanged() : scrollState - "+scrollState);

		if (scrollState == SCROLL_STATE_IDLE && mCanSlideInIdleScrollState) {

			int midHeader = -mMinHeaderTranslation / 2;
			int midFooter = mMinFooterTranslation / 2;

			switch (mQuickReturnType) {
			case HEADER:
				if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
					ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
					anim.setDuration(80);
					anim.start();
					mHeaderDiffTotal = 0;
				} else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
					ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
					anim.setDuration(80);
					anim.start();
					mHeaderDiffTotal = mMinHeaderTranslation;
				}
				break;
			case FOOTER:
				if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
					ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
					anim.setDuration(80);
					anim.start();
					mFooterDiffTotal = 0;
				} else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
					ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
					anim.setDuration(80);
					anim.start();
					mFooterDiffTotal = -mMinFooterTranslation;
				}
				break;
			case BOTH:
				if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
					ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
					anim.setDuration(80);
					anim.start();
					mHeaderDiffTotal = 0;
				} else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
					ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
					anim.setDuration(80);
					anim.start();
					mHeaderDiffTotal = mMinHeaderTranslation;
				}

				if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
					ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
					anim.setDuration(80);
					anim.start();
					mFooterDiffTotal = 0;
				} else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
					ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
					anim.setDuration(80);
					anim.start();
					mFooterDiffTotal = -mMinFooterTranslation;
				}
				break;
			case TWITTER:
				if (-mHeaderDiffTotal > 0 && -mHeaderDiffTotal < midHeader) {
					ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), 0);
					anim.setDuration(80);
					anim.start();
					mHeaderDiffTotal = 0;
				} else if (-mHeaderDiffTotal < -mMinHeaderTranslation && -mHeaderDiffTotal >= midHeader) {
					ObjectAnimator anim = ObjectAnimator.ofFloat(mHeader, "translationY", mHeader.getTranslationY(), mMinHeaderTranslation);
					anim.setDuration(80);
					anim.start();
					mHeaderDiffTotal = mMinHeaderTranslation;
				}

				if (-mFooterDiffTotal > 0 && -mFooterDiffTotal < midFooter) { // slide up
					ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), 0);
					anim.setDuration(80);
					anim.start();
					mFooterDiffTotal = 0;
				} else if (-mFooterDiffTotal < mMinFooterTranslation && -mFooterDiffTotal >= midFooter) { // slide down
					ObjectAnimator anim = ObjectAnimator.ofFloat(mFooter, "translationY", mFooter.getTranslationY(), mMinFooterTranslation);
					anim.setDuration(80);
					anim.start();
					mFooterDiffTotal = -mMinFooterTranslation;
				}
				break;
			}

		}
		
		if (mOriginalScrollListener != null) {
			mOriginalScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView listview, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		int scrollY = getScrollY(listview);
		int diff = mPrevScrollY - scrollY;

		// Log.d(getClass().getSimpleName(), "onScroll() : scrollY - "+scrollY);
		// Log.d(getClass().getSimpleName(), "onScroll() : diff - "+diff);
		// Log.d(getClass().getSimpleName(), "onScroll() : mMinHeaderTranslation - "+mMinHeaderTranslation);
		// Log.d(getClass().getSimpleName(), "onScroll() : mMinFooterTranslation - "+mMinFooterTranslation);

		if (diff != 0) {
			switch (mQuickReturnType) {
			case HEADER:
				if (diff < 0) { // scrolling down
					mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);
				} else { // scrolling up
					mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
				}

				mHeader.setTranslationY(mHeaderDiffTotal);
				break;
			case FOOTER:
				if (diff < 0) { // scrolling down
					mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
				} else { // scrolling up
					mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
				}

				mFooter.setTranslationY(-mFooterDiffTotal);
				break;
			case BOTH:
				if (diff < 0) { // scrolling down
					mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);
					mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
				} else { // scrolling up
					mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
					mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
				}

				mHeader.setTranslationY(mHeaderDiffTotal);
				mFooter.setTranslationY(-mFooterDiffTotal);
				break;
			case TWITTER:
				if (diff < 0) { // scrolling down
					if (scrollY > -mMinHeaderTranslation)
						mHeaderDiffTotal = Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation);

					if (scrollY > mMinFooterTranslation)
						mFooterDiffTotal = Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation);
				} else { // scrolling up
					mHeaderDiffTotal = Math.min(Math.max(mHeaderDiffTotal + diff, mMinHeaderTranslation), 0);
					mFooterDiffTotal = Math.min(Math.max(mFooterDiffTotal + diff, -mMinFooterTranslation), 0);
				}

				mHeader.setTranslationY(mHeaderDiffTotal);
				mFooter.setTranslationY(-mFooterDiffTotal);
			default:
				break;
			}
		}

		mPrevScrollY = scrollY;
		
		if (mOriginalScrollListener != null) {
			mOriginalScrollListener.onScroll(listview, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public void setCanSlideInIdleScrollState(boolean canSlideInIdleScrollState) {
		mCanSlideInIdleScrollState = canSlideInIdleScrollState;
	}
	
	private static Dictionary<Integer, Integer> sListViewItemHeights = new Hashtable<Integer, Integer>();
	public static int getScrollY(AbsListView lv) {
		View c = lv.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = lv.getFirstVisiblePosition();
		int scrollY = -(c.getTop());
		// int scrollY = 0;

		sListViewItemHeights.put(lv.getFirstVisiblePosition(), c.getHeight());

		// if(scrollY>0)
		// Log.d("QuickReturnUtils", "getScrollY() : -(c.getTop()) - "+ -(c.getTop()));
		// else
		// Log.i("QuickReturnUtils", "getScrollY() : -(c.getTop()) - "+ -(c.getTop()));

		if (scrollY < 0)
			scrollY = 0;

		for (int i = 0; i < firstVisiblePosition; ++i) {
			// Log.d("QuickReturnUtils", "getScrollY() : i - "+i);

			// Log.d("QuickReturnUtils", "getScrollY() : sListViewItemHeights.get(i) - "+sListViewItemHeights.get(i));

			if (sListViewItemHeights.get(i) != null) // (this is a sanity check)
				scrollY += sListViewItemHeights.get(i); // add all heights of the views that are gone

		}

		// Log.d("QuickReturnUtils", "getScrollY() : scrollY - "+scrollY);

		return scrollY;
	}

	/**
	 * Created by etiennelawlor on 7/10/14.
	 */
	public static enum QuickReturnType {
	    HEADER,
	    FOOTER,
	    BOTH,
	    GOOGLE_PLUS,
	    TWITTER
	}
}
