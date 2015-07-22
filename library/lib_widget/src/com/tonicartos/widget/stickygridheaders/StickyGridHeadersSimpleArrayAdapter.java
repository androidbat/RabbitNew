/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tonicartos.widget.stickygridheaders;

import java.util.Arrays;
import java.util.List;

import com.tonicartos.widget.stickygridheaders.DragGridView.HeadData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Tonic Artos
 * @param <T>
 */
public class StickyGridHeadersSimpleArrayAdapter<T> extends BaseAdapter implements
        StickyGridHeadersSimpleAdapter,DynamicGridAdapterInterface {
    protected static final String TAG = StickyGridHeadersSimpleArrayAdapter.class.getSimpleName();

    private int mHeaderResId;

    private LayoutInflater mInflater;

    private int mItemResId;

    private List<T> mItems;

    public StickyGridHeadersSimpleArrayAdapter(Context context, List<T> items, int headerResId,
            int itemResId) {
        init(context, items, headerResId, itemResId);
    }

    public StickyGridHeadersSimpleArrayAdapter(Context context, T[] items, int headerResId,
            int itemResId) {
        init(context, Arrays.asList(items), headerResId, itemResId);
    }
    
    private DragGridView mDragGridView;
    public void setDragGridView(DragGridView dragGridView){
    	mDragGridView = dragGridView;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public long getHeaderId(int position) {
        T item = getItem(position);
        CharSequence value;
        if (item instanceof CharSequence) {
            value = (CharSequence)item;
        } else {
            value = item.toString();
        }

        long l = value.subSequence(0, 1).charAt(0);
        return l;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder)convertView.getTag();
        }

        T item = getItem(position);
        CharSequence string;
        if (item instanceof CharSequence) {
            string = (CharSequence)item;
        } else {
            string = item.toString();
        }

        // set header text as first char in string
        holder.textView.setText(string.subSequence(0, 1));

        return convertView;
    }

    @Override
    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        T item = getItem(position);
        if (item instanceof CharSequence) {
            holder.textView.setText((CharSequence)item);
        } else {
            holder.textView.setText(item.toString());
        }
        
        if (mDragGridView != null) {
        	mDragGridView.addItemView(position,new HeadData(position, convertView));
		}
        return convertView;
    }

    private void init(Context context, List<T> items, int headerResId, int itemResId) {
        this.mItems = items;
        this.mHeaderResId = headerResId;
        this.mItemResId = itemResId;
        mInflater = LayoutInflater.from(context);
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public TextView textView;
    }

	@Override
	public int reorderItems(int firstIndex, int secondIndex) {
		Object firstObject = mItems.get(firstIndex);
		Object secondObject = mItems.get(secondIndex);
		mItems.set(firstIndex, (T) secondObject);
		mItems.set(secondIndex, (T) firstObject);
		notifyDataSetChanged();
		return 0;
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canReorder(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getHeadId(int head) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int appEnd(int originalPosition, int newPosition) {
		// TODO Auto-generated method stub
		return 0;
	}
}
