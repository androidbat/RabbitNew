package com.c.commen.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yitu.widget.R;

/**
 * Created by wg on 2015/8/5.
 */
public class DialogHelper {
    private Activity mActivity;
    private Context mContext;
    private Dialog mDialog;
    private AlertDialog.Builder mBuidler;
    private Params mParams;
    public static int mProgressLayoutId;
    public static int mLayoutId;

    public static void init(int layoutId,int progressLayoutId){
        mLayoutId = layoutId;
        mProgressLayoutId = progressLayoutId;
    }

    public DialogHelper(Activity activity) {
        this.mActivity = activity;
        mParams = new Params();
        mContext = activity.getApplicationContext();
    }

    public DialogHelper setTitle(String title){
        mParams.title = title;
        return this;
    }
    public DialogHelper setTitle(int resId){
        mParams.title = mContext.getString(resId);
        return this;
    }
    public DialogHelper setPositiveButton(String positive,DialogInterface.OnClickListener positiveListener){
        mParams.positive = positive;
        mParams.positiveListener = positiveListener;
        return this;
    }

    public DialogHelper setPositiveButton(int positiveId,DialogInterface.OnClickListener positiveListener){
        return setPositiveButton(mContext.getString(positiveId),positiveListener);
    }

    public DialogHelper setNegativeButton(String negative,final DialogInterface.OnClickListener negativeListener){
        mParams.negative = negative;
        mParams.negativeListener = negativeListener;
        return this;
    }

    public DialogHelper setNegativeButton(int negativeId,DialogInterface.OnClickListener negativeListener){
        return setNegativeButton(mContext.getString(negativeId),negativeListener);
    }

    public DialogHelper setMessage(String msg){
        mParams.msg = msg;
        return this;
    }
    public DialogHelper setMessage(int msgId){
        return setMessage(mContext.getString(msgId));
    }

    public void show(){
        if (mActivity == null || mActivity.isFinishing()) {
            return;
        }
        if (mLayoutId != 0 ){
            LayoutInflater mInflater = LayoutInflater.from(mActivity.getApplicationContext());
            View view=mInflater.inflate(mLayoutId, null);
            TextView dialog_title_tv = (TextView) view.findViewById(R.id.dialog_title_tv);
            View dialog_title_layout = view.findViewById(R.id.dialog_title_layout);
            TextView dialog_msg_tv = (TextView) view.findViewById(R.id.dialog_msg_tv);
            TextView dialog_positive_tv = (TextView) view.findViewById(R.id.dialog_positive_tv);
            TextView dialog_negative_tv = (TextView) view.findViewById(R.id.dialog_negative_tv);
            if (mParams.title != null) {
                dialog_title_tv.setText(mParams.title);
            }else{
                if (dialog_title_layout != null){
                    dialog_title_layout.setVisibility(View.GONE);
                }else {
                    dialog_title_tv.setVisibility(View.GONE);
                }
            }
            if (mParams.msg != null) {
                dialog_msg_tv.setText(mParams.msg);
            }
            if (mParams.positive != null) {
                dialog_positive_tv.setText(mParams.positive);
                dialog_positive_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mParams.positiveListener != null) {
                            mParams.positiveListener.onClick(mDialog,0);
                        }else{

                        }
                    }
                });
            }
            if (mParams.negative != null) {
                dialog_negative_tv.setVisibility(View.VISIBLE);
                dialog_negative_tv.setText(mParams.negative);
                dialog_negative_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mParams.negativeListener != null) {
                            mParams.negativeListener.onClick(mDialog,0);
                        }else{
                            mDialog.dismiss();
                        }
                    }
                });
            }else{
                dialog_negative_tv.setVisibility(View.GONE);
            }
            mDialog = new Dialog(mActivity, R.style.dialog);
            mDialog.setContentView(view);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            if (mParams.title != null) {
                builder.setTitle(mParams.title);
            }
            if (mParams.msg != null) {
                builder.setMessage(mParams.msg);
            }
            if (mParams.positive != null) {
                builder.setPositiveButton(mParams.positive, mParams.positiveListener);
            }
            if (mParams.negative != null) {
                builder.setNegativeButton(mParams.negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mParams.negativeListener != null) {
                            mParams.negativeListener.onClick(dialog,which);
                        }else{
                            mDialog.dismiss();
                        }
                    }
                });
            }
            mDialog = builder.create();
        }

        mDialog.setCanceledOnTouchOutside(mParams.isCanceledOnTouchOutside);
        mDialog.setCancelable(mParams.isCancelable);
        mDialog.show();
    }

    public Dialog getDialog(){
       return mDialog;
    }

    public void isShow(){
        mDialog.isShowing();
    }

    public void dismiss(){
        mDialog.dismiss();
    }
    public DialogHelper setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside){
        mParams.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
        return this;
    }
    public DialogHelper setCancelable(boolean isCancelable){
        mParams.isCancelable = isCancelable;
        mDialog.setCancelable(isCancelable);
        return this;
    }

    private static class Params{
        public String title;
        public String msg;
        public String positive;
        public String negative;
        public boolean isCanceledOnTouchOutside;
        public boolean isCancelable;
        public DialogInterface.OnClickListener positiveListener;
        public DialogInterface.OnClickListener negativeListener;
    }

}
