package com.c.rabbit.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.c.rabbit.utils.BitmapDecodeUtils;
import com.c.rabbit.DataProvider;

import java.util.List;


public abstract class MBaseAdapter<T> extends BaseAdapter{
	protected List<T> mList=null;
    protected LayoutInflater inflater;
    protected Context mContext;
    protected DataProvider mDataProvider;
    protected Bitmap mDefaultBitmap;
    public MBaseAdapter(List<T> list, Context context){
        mList=list;
        mContext=context;
        mDataProvider = ((RootActivity)context).getDataProvider();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public MBaseAdapter(List<T> list, Context context, int defalutResId){
        this(list,context);
        mDefaultBitmap = BitmapDecodeUtils.decodeResource(context, defalutResId);
    }
    
    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public T getItem(int position) {
        if(mList!=null && mList.size()>position){
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    	if (convertView == null) {
    		convertView = generateView(position, parent);
		}
    	fillValues(position, convertView);
    	return convertView;
    }
    
    public abstract View generateView(int position,ViewGroup parent);
    public abstract void fillValues(int position,View view);
    
}
