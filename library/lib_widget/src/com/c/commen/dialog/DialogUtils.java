package com.c.commen.dialog;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * provide dialog
 * 
 */
public class DialogUtils {

    public static final int SOURCE_POSITIVE = -5;
    public static final int SOURCE_NEUTRAL = -6;
    public static final int SOURCE_NEGATIVE = -7;
	public static final int SOURCE_RADIO_BUTTON = -10;

    /**
     * 等待的对话框
     *
     * @param context
     * @param title 标题
     * @param msg 内容
     */
    public static AlertDialog getProgressDialog(Context context, String title, String msg) {
        AlertDialog mProgressDialog;
        if (DialogHelper.mProgressLayoutId != 0){
            mProgressDialog = new GenericProgressDialog(context,DialogHelper.mProgressLayoutId);
        }else{
            ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            mProgressDialog = progressDialog;
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(msg);
    	mProgressDialog.setCancelable(true);
    	mProgressDialog.setCanceledOnTouchOutside(false);
    	return mProgressDialog;
    }

    /**
	 * 单选列表对话框
	 * @param context
	 * @param title
	 *            标题
	 * @param data
	 *            list数据
	 * @param positiveButtonText
	 *            正向按钮的文字描，没有则不创建这个按钮
	 * @param neutralButtonText
	 *            中间按钮的文字描，没有则不创建这个按钮
	 * @param negativeButtonText
	 *            反向按钮的文字描 ，没有则不创建这个按钮
	 * @param onClickListener
	 *            监听onClick函数被触发的事件
	 */
	public static AlertDialog getRadioButtonDialog(Context context, String title, String[] data,int checkItem, String positiveButtonText, String neutralButtonText,
			String negativeButtonText, final DialogOnClickListener onClickListener) {
		if (null == data || data.length == 0) {
			return null;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if(title!=null){
			builder.setTitle(title);
		}
		builder.setSingleChoiceItems(data, checkItem, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				onClickListener.onDialogClick(dialog, which, SOURCE_RADIO_BUTTON);
			}
		});
		setButtons(builder, positiveButtonText, neutralButtonText, negativeButtonText, onClickListener);
		return builder.create();
	}

    public interface DialogOnClickListener {
        void onDialogClick(DialogInterface dialog, int whichButton, int source);
    }

    /**
     *
     * @param builder
     * @param positiveButtonText
     * @param neutralButtonText
     * @param negativeButtonText
     * @param onClickListener
     */
    private static void setButtons(AlertDialog.Builder builder, String positiveButtonText, String neutralButtonText, String negativeButtonText,
            final DialogOnClickListener onClickListener) {

        if (null != positiveButtonText && positiveButtonText.trim().length() > 0) {
            builder.setPositiveButton(positiveButtonText, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (null != onClickListener) {
                        onClickListener.onDialogClick(dialog, whichButton, SOURCE_POSITIVE);
                    }
                }
            });
        }

        if (null != neutralButtonText && neutralButtonText.trim().length() > 0) {
            builder.setNeutralButton(neutralButtonText, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (null != onClickListener) {
                        onClickListener.onDialogClick(dialog, whichButton, SOURCE_NEUTRAL);
                    }
                }
            });
        }

        if (null != negativeButtonText && negativeButtonText.trim().length() > 0) {
            builder.setNegativeButton(negativeButtonText, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (null != onClickListener) {
                        onClickListener.onDialogClick(dialog, whichButton, SOURCE_NEGATIVE);
                    }
                }
            });
        }
    }

}
