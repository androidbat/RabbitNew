package com.c.commen.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by wg on 2015/8/5.
 */
public class CommenFragment extends Fragment {
    private boolean isActivityCreated;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isActivityCreated = true;
        if (getUserVisibleHint()) {
            onFragmentResume();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isActivityCreated = true;
        if (!isActivityCreated) {
            return;
        }
        if (isVisibleToUser) {
            onFragmentResume();
        }else{
            onFragmentPause();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected void onFragmentResume() {
    }

    protected void onFragmentPause() {
    }
}
