package com.m.view;


import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingLayout {
	private Context mContext;
	private View mView;
	private View mLoadingLayout;
//	private ProgressBar mProgressBar;
	private ProgressBar mProgressBar;
	private ImageView mImageShow;
	public TextView mTextDes;
	private int mState;
	private static final int ERROR = 1;
	private static final int SUCCESS = 2;
	private static final int EMPTY = 3;
	private static final int LOADING = 4;

	private View mCustomLoadingView;
	private int mLoadingId;
	private int mErrorId;
	private int mEmptyId;
	
	private OnClickListener mErrorClickListener;
	private OnClickListener mNullClickListener;
	
	public void setErrorClickListener(OnClickListener mErrorClickListener) {
		this.mErrorClickListener = mErrorClickListener;
	}
	
	public void setDataNullClickListener(OnClickListener mNullClickListener) {
		this.mNullClickListener = mNullClickListener;
	}

	public LoadingLayout setLoadingId(int mLoadingId) {
		this.mLoadingId = mLoadingId;
		return this;
	}

	public LoadingLayout setErrorId(int mErrorId) {
		this.mErrorId = mErrorId;
		return this;
	}

	public LoadingLayout setEmptyId(int mEmptyId) {
		this.mEmptyId = mEmptyId;
		return this;
	}

	private String net_error = "亲，网络不给力哦\n点击屏幕重试";
	private String no_data = "暂无数据";


	public LoadingLayout(Context context,View view){
		mContext = context;
		mView = view;
		addView();
	}
	
	public View getBackGround(){
		return mLoadingLayout;
	}

	protected void addCustomLoadingView(View view){
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent instanceof RelativeLayout || parent instanceof FrameLayout) {
			parent.addView(view,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		}
		mCustomLoadingView = view;
	}

	private void addView() {
		ViewGroup parent = (ViewGroup) mView.getParent();
		mLoadingLayout = LayoutInflater.from(mContext).inflate(R.layout.loading_layout,parent, false);
		
		if (parent instanceof RelativeLayout || parent instanceof FrameLayout) {
			parent.addView(mLoadingLayout,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		}else{
			FrameLayout frameLayout = new FrameLayout(mContext);
			frameLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			parent.removeView(mView);
			frameLayout.addView(mView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			frameLayout.addView(mLoadingLayout,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
			parent.addView(frameLayout,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		}
		
		
		mProgressBar = (ProgressBar) mLoadingLayout.findViewById(R.id.progressbar);
		mImageShow = (ImageView) mLoadingLayout.findViewById(R.id.iv_show);
		mTextDes = (TextView) mLoadingLayout.findViewById(R.id.tv_des);
	}
	
	public void hide(){
		showView(SUCCESS,null);
	}
	
	public void showError(){
		showView(ERROR,null);
	}
	
	public void showLoading(){
		showView(LOADING,null);
	}
	
	public void showLoading(String msg){
		showView(LOADING,msg);
	}
	
	public void showEmpty(String des){
		if (TextUtils.isEmpty(des)){
			des = no_data;
		}
		showView(EMPTY,des);
	}

	private void hideLoadingView(){
		if (mCustomLoadingView != null){
			mCustomLoadingView.setVisibility(View.GONE);
		}else{
			mProgressBar.setVisibility(View.GONE);
		}
	}
	
	private void showView(int state,String des){
		mLoadingLayout.setVisibility(View.VISIBLE);
		mLoadingLayout.setOnClickListener(null);
		if (mImageShow.getAnimation() != null) {
			mImageShow.getAnimation().cancel();
		}
		switch (state) {
		case ERROR:
			if (mLoadingId == 0 ) {
				hideLoadingView();
			}
			setImage(mErrorId);
			mLoadingLayout.setOnClickListener(mErrorClickListener);
			mTextDes.setVisibility(View.VISIBLE);
			mTextDes.setText(net_error);
			break;
		case EMPTY:
			if (mLoadingId == 0 ) {
				hideLoadingView();
			}
			mLoadingLayout.setOnClickListener(mNullClickListener);
			setImage(mEmptyId);
			mTextDes.setVisibility(View.VISIBLE);
			mTextDes.setText(des);
			break;
		case LOADING:
			setImage(mLoadingId);
			if (des != null && !des.equals("")) {
				mTextDes.setVisibility(View.VISIBLE);
				mTextDes.setText(des);
			}else{
				mTextDes.setVisibility(View.GONE);
			}
			if (mLoadingId == 0 ) {
				if (mCustomLoadingView != null){
					mCustomLoadingView.setVisibility(View.VISIBLE);
				}else{
					mProgressBar.setVisibility(View.VISIBLE);
				}
			}else{
				mImageShow.startAnimation(getRotateAnimation());
			}
			break;
		case SUCCESS:
			mLoadingLayout.setVisibility(View.GONE);
			hideLoadingView();
			break;

		default:
			break;
		}
	}

	private void setImage(int resId) {
		if (resId > 0 ) {
			mImageShow.setVisibility(View.VISIBLE);
			mImageShow.setImageResource(resId);
		}else{
			mImageShow.setVisibility(View.GONE);
		}
	}
	
	private static Animation getRotateAnimation() {
		final RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
		rotateAnimation.setDuration(1500);		
		rotateAnimation.setInterpolator(new LinearInterpolator());
		rotateAnimation.setRepeatCount(Animation.INFINITE);		
		return rotateAnimation;
	}
	
	
}
