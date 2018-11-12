package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.app.Dialog;

/**
 * @Description:
 * @Author: fengzeyuan
 * @Date: 17/9/21 上午10:40
 * @Version: 1.0
 */
public abstract class GlobalDialogBuilder<T extends Dialog> {

    XZDialogPage mDialogPage;

    GlobalDialogBuilder(XZDialogPage dialogInterface) {
        mDialogPage = dialogInterface;
    }

    abstract T builder(Activity ac);

    public final void show() {
        mDialogPage.showPage();
    }
}
