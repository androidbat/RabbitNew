package com.c.commen.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;

import com.c.commen.dialog.DialogUtils;
import com.yitu.widget.R;

/**
 * Created by wg on 2015/8/5.
 */
public class CommenActivity extends AppCompatActivity {
    private AlertDialog progressDialog;
    public Dialog showPregrossDialog(String msg){
        if (progressDialog == null) {
            if (msg == null) {
                msg = getString(R.string.loading);
            }
            progressDialog = DialogUtils.getProgressDialog(this, null, msg);
        }else{
            progressDialog.setMessage(msg);
        }
        try {
            if (!isFinishing() && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        } catch (Exception e) {
        }
        return progressDialog;
    }

    /**]
     *
     * @return 返回是是否显示
     */
    public boolean dismissPregross(){
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
