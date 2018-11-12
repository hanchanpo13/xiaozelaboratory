
package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xiaozhu.xzdz.R;
import com.xiaozhu.xzdz.application.Util;
import com.xiaozhu.xzdz.bizBase.tools.XZAlertDialog;
import com.xiaozhu.xzdz.publish.adapter.PublishPopWindowCommonAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 列表dialog
 * @Author: fengzeyuan
 * @Date: 17/9/21 上午10:41
 * @Version: 1.0
 */
public class ListDialogBuilder extends GlobalDialogBuilder {

    private String mBtnStr;
    private ArrayList<String> itemStr = new ArrayList<>();

    private XZAlertDialog.OnDialogItemClickListener onItemClickListener;


    ListDialogBuilder(XZDialogPage dialogInterface) {
        super(dialogInterface);
    }

    public ListDialogBuilder setBtnStr(String btnStr) {
        mBtnStr = btnStr;
        return this;
    }

    public ListDialogBuilder setItemStr(List<String> itemStr) {
        this.itemStr.clear();
        this.itemStr.addAll(itemStr);
        return this;
    }

    public ListDialogBuilder setOnItemClickListener(XZAlertDialog.OnDialogItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    private Dialog getListDialog(Activity ac) {

        final Dialog mDialog = new Dialog(ac, R.style.progressDialogStyle);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window dialogWindow = mDialog.getWindow();
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.BOTTOM);
            // dialog动画
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        }

        View view = View.inflate(ac, R.layout.publish_pop_window_view, null);
        TextView btn = view.findViewById(R.id.lv_pop_window_btn);
        if (!TextUtils.isEmpty(mBtnStr)) {
            btn.setText(mBtnStr);
        }
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        ListView lv = view.findViewById(R.id.lv_pop_window);
        PublishPopWindowCommonAdapter adapter = new PublishPopWindowCommonAdapter(ac, itemStr);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, mDialog, view, position, id);
                }
            }
        });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = Util.getScreenWidth();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(view, lp);
        return mDialog;
    }

    @Override
    public Dialog builder(Activity ac) {
        return getListDialog(ac);
    }
}
