package com.c.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yitu.widget.R;

public class ListFootLoadHelper implements OnScrollListener, OnClickListener{

	private boolean isLoadEnable;// 开启或者关闭加载更多功能
	private boolean isLoadFull;
	public boolean isHasFullFooter = false ;

	private TextView des_tv;
	private ProgressBar loading;
	private View footer;
	private int pageSize = 10;
	private String mFullString;

	public void setFullString(String mFullString) {
		this.mFullString = mFullString;
	}


	private OnFootLoading onLoadListener;
	private ListView mListView;
	
	public ListFootLoadHelper(){
		
	}
	public ListFootLoadHelper(ListView listview,int pageSize){
		this.mListView = listview;
		this.pageSize = pageSize;
	}
	
	public void setList(ListView listview,int pageSize){
		this.mListView = listview;
		this.pageSize = pageSize;
	}
	
	public void setLoadEnble(boolean enble){
		if (mListView == null) {
			return;
		}
		if (enble) {
			if (footer != null) {
				mListView.removeFooterView(footer);
			}
			if (mBottomView != null){
				mListView.removeFooterView(mBottomView);
				mBottomView = null;
			}
			footer = LayoutInflater.from(mListView.getContext().getApplicationContext()).inflate(R.layout.list_foot_bar, null);
			loading = (ProgressBar) footer.findViewById(R.id.loading);
			des_tv = (TextView) footer.findViewById(R.id.des_tv);
			loading.setVisibility(View.GONE);
			des_tv.setText(R.string.release_to_load);
			mListView.setOnScrollListener(this);
			mListView.addFooterView(footer);
			isLoadFull = false;
		}else{
			if (footer != null) {
				mListView.removeFooterView(footer);
			}
			if (mFullView != null) {
				mListView.removeFooterView(mFullView);
			}

			mListView.setOnScrollListener(null);
			footer = null;
		}
		isLoadEnable = enble;
	}

	/*
	 * 定义下拉刷新接口
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnFootLoading {
		public void onLoad();
	}

	public boolean isLoading(){
		return loading != null && loading.getVisibility()==View.VISIBLE;
	}

	public void setOnLoadListener(OnFootLoading onLoadListener) {
		this.onLoadListener = onLoadListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch ( scrollState ){
		case OnScrollListener.SCROLL_STATE_FLING:
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			try {
				if (!isLoadFull && isLoadEnable && mListView.getLastVisiblePosition() >= mListView.getPositionForView(footer)) {
					if (loading.getVisibility() != View.VISIBLE) {
						loading.setVisibility(View.VISIBLE);
						des_tv.setText(R.string.more_loading);
						if (onLoadListener != null) {
							onLoadListener.onLoad();
						}
					}
				}
			} catch (Exception e) {
			}
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
	
	/**
	 * 这个方法是根据结果的大小来决定footer显示的。
	 * <p>
	 * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
	 * </p>
	 * 
	 * @param resultSize
	 */
	public void setResultSize(int resultSize) {
		try {
			if (resultSize == -1) {
                showNetError();
            }else if (resultSize >= 0 && resultSize < pageSize) {
                showLoadedTotal();
            } else if (resultSize >= pageSize) {
                showLoadMore();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showNetError(){
		if (loading.getVisibility() == View.VISIBLE){
			loading.setVisibility(View.GONE);
			des_tv.setText(R.string.load_failed);
			if(footer != null)
				footer.setOnClickListener(this);
		}
	}

	public void showLoadMore(){
		setLoadEnble(true);
		isLoadFull = false;
		des_tv.setText(R.string.release_to_load);
		loading.setVisibility(View.GONE);
		if(footer != null)
			footer.setOnClickListener(null);
	}

	private View mBottomView;
	public void showLoadedTotal(){
		isLoadFull = true;
		if (mFullString != null) {
			if (des_tv != null)
				des_tv.setText(mFullString);
		}else{
			if (des_tv != null)
				des_tv.setText(R.string.load_full);
		}
		setLoadEnble(false);
		if ( isHasFullFooter && mBottomView == null){
			mBottomView = new View(mListView.getContext());
			mBottomView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,dip2px(mListView.getContext(),10)));
			mListView.addFooterView(mBottomView);
		}
	}

	public static int dip2px(Context context, float dpValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}


	private int totalSize;
	public void setTotalSize(int totalSize){
		this.totalSize = totalSize;
	}
	public void setCurrentSize(int currentSize) {
		try {
			if (currentSize == -1) {
                showNetError();
			}else if(totalSize <= pageSize){
				showLoadedTotal();
            }else if (currentSize >= totalSize) {
                showLoadedTotal();
            } else if (currentSize < totalSize) {
                showLoadMore();
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private View mFullView;
	public void setFullView(View view){
		mFullView = view;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public void onClick(View v) {
		loading.setVisibility(View.VISIBLE);
		des_tv.setText(R.string.more_loading);
		if (onLoadListener != null) {
			onLoadListener.onLoad();
		}
	}
	
}
