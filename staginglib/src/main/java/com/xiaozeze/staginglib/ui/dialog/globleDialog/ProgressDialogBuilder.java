
package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.xiaozhu.xzdz.bizBase.update.XZProgressDialog;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 17/9/26 下午4:13
 * @Version: 1.0
 */
public class ProgressDialogBuilder extends GlobalDialogBuilder {

    private String mTitle;
    private String mMsg;

    private String posBtntext;
    private DialogInterface.OnClickListener positiveButtonClickListener;

    private String negBtntext;
    private DialogInterface.OnClickListener negativeButtonClickListener;

    ProgressDialogBuilder(XZDialogPage dialogInterface) {
        super(dialogInterface);
    }


    public ProgressDialogBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }

    public ProgressDialogBuilder setMsg(String msg) {
        mMsg = msg;
        return this;
    }

    public ProgressDialogBuilder setPositiveButton(String btntext, Dialog.OnClickListener clickListener) {
        posBtntext = btntext;
        positiveButtonClickListener = clickListener;
        return this;
    }

    public ProgressDialogBuilder setNegativeButton(String btntext, Dialog.OnClickListener clickListener) {
        negBtntext = btntext;
        negativeButtonClickListener = clickListener;
        return this;
    }


    @Override
    Dialog builder(Activity ac) {
        XZProgressDialog progressDialog = new XZProgressDialog(ac);
        progressDialog
                .setTitle(mTitle)
                .setContent(mMsg)
                .setPositiveButton(posBtntext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (positiveButtonClickListener != null) {
                            positiveButtonClickListener.onClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(negBtntext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(dialog, which);
                        }
                    }
                });
        return progressDialog;
    }

}
