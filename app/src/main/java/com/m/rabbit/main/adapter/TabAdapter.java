package com.m.rabbit.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.c.commen.view.PagerSlidingTabStrip;
import com.m.rabbit.R;

import java.util.ArrayList;

/**
 * 主页tab适配器
 * Created by LOVE on 2015/6/12 012.
 */
public class TabAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.ViewTabProvider {
    private final ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
    private final ArrayList<String> mTitles = new ArrayList<String>();
    private final ArrayList<Integer> mResIds = new ArrayList<Integer>();
    private final ArrayList<Integer> mSelectedIds = new ArrayList<Integer>();
    private final ArrayList<View> mTabs = new ArrayList<View>();
    private Context context;

    public TabAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context.getApplicationContext();
    }

    public void addFragment(Fragment fragment, String title,int resId,int selectId) {
        mFragments.add(fragment);
        mTitles.add(title);
        mResIds.add(resId);
        mSelectedIds.add(selectId);
    }
    public void addFragment(Fragment fragment, int titleId,int resId,int selectId) {
        addFragment(fragment,context.getString(titleId),resId, selectId);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public View getPageView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_tabs_item, null);
        TextView tv = (TextView) view.findViewById(R.id.tab_text);
        ImageView iv = (ImageView) view.findViewById(R.id.tab_img);
        tv.setText(mTitles.get(position));
        iv.setImageResource(mResIds.get(position));
        mTabs.add(position,view);
        return view;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int select(View view, int postion, boolean selected) {
        TextView tv = (TextView) view.findViewById(R.id.tab_text);
        ImageView iv = (ImageView) view.findViewById(R.id.tab_img);
        if (selected){
            tv.setTextColor(Color.parseColor("#333333"));
            iv.setImageResource(mSelectedIds.get(postion));
        }else{
            tv.setTextColor(Color.parseColor("#999999"));
            iv.setImageResource(mResIds.get(postion));
        }
        return 0;
    }

    @Override
    public void onTabItemClick(View item, int position) {

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
