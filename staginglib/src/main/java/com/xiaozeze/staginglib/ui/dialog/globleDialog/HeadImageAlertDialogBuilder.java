package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.xiaozhu.xzdz.bizBase.update.XZHeadImageAlertDialog;


/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 17/9/21 上午10:41
 * @Version: 1.0
 */
public class HeadImageAlertDialogBuilder extends GlobalDialogBuilder {


    private String mImageUrl;
    private String mTitle;
    private String mMsg;

    private String posBtntext;
    private DialogInterface.OnClickListener positiveButtonClickListener;

    private String negBtntext;
    private DialogInterface.OnClickListener negativeButtonClickListener;

    HeadImageAlertDialogBuilder(XZDialogPage dialogInterface) {
        super(dialogInterface);
    }


    public HeadImageAlertDialogBuilder setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
        return this;
    }

    public HeadImageAlertDialogBuilder setTitle(String title) {
        mTitle = title;
        return this;
    }

    public HeadImageAlertDialogBuilder setMsg(String msg) {
        mMsg = msg;
        return this;
    }

    public HeadImageAlertDialogBuilder setPositiveButton(String btntext, Dialog.OnClickListener clickListener) {
        posBtntext = btntext;
        positiveButtonClickListener = clickListener;
        return this;
    }

    public HeadImageAlertDialogBuilder setNegativeButton(String btntext, Dialog.OnClickListener clickListener) {
        negBtntext = btntext;
        negativeButtonClickListener = clickListener;
        return this;
    }

    @Override
    Dialog builder(Activity ac) {
        XZHeadImageAlertDialog imageAlertDialog = new XZHeadImageAlertDialog(ac);
        imageAlertDialog
                .setImage(mImageUrl)
                .setTitle(mTitle)
                .setContent(mMsg)
                .setPositiveButton(posBtntext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(positiveButtonClickListener != null){
                            positiveButtonClickListener.onClick(dialog, which);
                        }
                    }
                })
                .setNegativeButton(negBtntext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        negativeButtonClickListener.onClick(dialog, which);
                    }
                });
        return imageAlertDialog;
    }


}
