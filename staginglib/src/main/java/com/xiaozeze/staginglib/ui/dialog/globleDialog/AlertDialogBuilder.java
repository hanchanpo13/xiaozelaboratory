package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.ColorInt;

import com.xiaozhu.xzdz.newapp.permission.PermissionRationaleDialog;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 17/9/21 上午10:41
 * @Version: 1.0
 */
public class AlertDialogBuilder extends GlobalDialogBuilder {


    private String mTitle;
    private String mMsg;

    private String positiveBtnText;
    private int positiveBtnTextColor;
    private DialogInterface.OnClickListener positiveButtonClickListener;
    private String negativeBtnText;
    private DialogInterface.OnClickListener negativeButtonClickListener;
    private int negativeBtnColorId;

    AlertDialogBuilder(XZDialogPage dialogInterface) {
        super(dialogInterface);
    }

    public AlertDialogBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }

    public AlertDialogBuilder setMsg(String msg) {
        mMsg = msg;
        return this;
    }

    public AlertDialogBuilder setRightButton(String btnText, Dialog.OnClickListener clickListener) {
        setRightButton(btnText, 0, clickListener);
        return this;
    }

    public AlertDialogBuilder setRightButton(String btnText, @ColorInt int textColorId, Dialog.OnClickListener clickListener) {
        positiveBtnText = btnText;
        positiveBtnTextColor = textColorId;
        positiveButtonClickListener = clickListener;
        return this;
    }

    public AlertDialogBuilder setLeftButton(String btnText, Dialog.OnClickListener clickListener) {
        setLeftButton(btnText, 0, clickListener);
        return this;
    }
    
    public AlertDialogBuilder setLeftButton(String btnText,@ColorInt int textColorId, Dialog.OnClickListener clickListener) {
        negativeBtnText = btnText;
        negativeBtnColorId = textColorId;
        negativeButtonClickListener = clickListener;
        return this;
    }

    @Override
    Dialog builder(Activity ac) {
        return new PermissionRationaleDialog(ac)
                .setTitle(mTitle)
                .setMessage(mMsg)
                .setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (positiveButtonClickListener != null) {
                            positiveButtonClickListener.onClick(mDialogPage, which);
                        }
                    }
                })
                .setPositiveBtnTextColor(positiveBtnTextColor)
                .setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(mDialogPage, which);
                        }
                    }
                })
                .setNegativeBtnTextColor(negativeBtnColorId);
    }
}
