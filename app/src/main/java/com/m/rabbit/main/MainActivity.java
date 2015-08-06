package com.m.rabbit.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.c.commen.view.PagerSlidingTabStrip;
import com.m.rabbit.R;
import com.m.rabbit.base.BaseActivity;
import com.m.rabbit.main.adapter.TabAdapter;
import com.m.rabbit.main.fragment.TestFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends BaseActivity {
    @InjectView(R.id.pager_tabs)
    PagerSlidingTabStrip pager_tabs;
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        setupViewPager(viewpager);

    }

    private void setupViewPager(ViewPager viewpager) {
        TabAdapter adapter = new TabAdapter(getSupportFragmentManager(),this);
        adapter.addFragment(new TestFragment("首页"), "首页",R.drawable.main_ic_find,R.drawable.main_ic_home_selected);
        adapter.addFragment(new TestFragment("发现"), "发现",R.drawable.main_ic_find,R.drawable.main_ic_find_seleted);
        adapter.addFragment(new TestFragment("我"), "我",R.drawable.main_ic_my,R.drawable.main_ic_my_selected);
        viewpager.setAdapter(adapter);
        pager_tabs.setViewPager(viewpager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
