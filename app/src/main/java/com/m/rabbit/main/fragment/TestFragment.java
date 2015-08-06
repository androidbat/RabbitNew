package com.m.rabbit.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TestFragment extends Fragment {

    private String title_Str=null;
    private TextView title;

    public TestFragment(String title_Str) {
        this.title_Str = title_Str;

    }

    public TestFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView title = new TextView(getActivity());

        if (title_Str != null) {
            title.setText(title_Str);
        }

        return title;

    }
}
