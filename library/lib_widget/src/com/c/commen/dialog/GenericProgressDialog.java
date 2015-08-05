package com.c.commen.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yitu.widget.R;


public class GenericProgressDialog extends AlertDialog {
    private ProgressBar  mProgress;
    private TextView     mMessageView;
    private CharSequence mMessage;
    private boolean      mIndeterminate;
    private boolean      mProgressVisiable;
    private int mLayoutId;

    public GenericProgressDialog(Context context) {
        super(context/*,R.style.Float*/);
    }

    public GenericProgressDialog(Context context, int layoutId) {
        super(context);
        mLayoutId = layoutId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mLayoutId != 0){
            setContentView(R.layout.dialog_progress);
        }else{
            setContentView(mLayoutId);
        }
        mProgress = (ProgressBar) findViewById(android.R.id.progress);
        mMessageView = (TextView) findViewById(R.id.dialog_msg_tv);

        setMessageAndView();
        setIndeterminate(mIndeterminate);
    }

    private void setMessageAndView() {
        mMessageView.setText(mMessage);

        if (mMessage == null || "".equals(mMessage)) {
            mMessageView.setVisibility(View.GONE);
        }

        mProgress.setVisibility(mProgressVisiable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMessage(CharSequence message) {
        mMessage = message;
    }

    /**
     * 圈圈可见性设置
     * @param progressVisiable 是否显示圈圈
     */
    public void setProgressVisiable(boolean progressVisiable) {
        mProgressVisiable = progressVisiable;
    }

    public void setIndeterminate(boolean indeterminate) {
        if (mProgress != null) {
            mProgress.setIndeterminate(indeterminate);
        } else {
            mIndeterminate = indeterminate;
        }
    }
}
