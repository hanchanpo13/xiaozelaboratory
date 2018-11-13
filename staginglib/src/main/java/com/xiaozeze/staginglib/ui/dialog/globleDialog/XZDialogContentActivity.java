
package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import static com.xiaozeze.staginglib.ui.dialog.globleDialog.XZActivityLifecycleInterface.dialogPageMap;


/**
 * @Description: 动态权限申请透明activity
 * @Author: fengzeyuan
 * @Date: 17/6/25 上午1:15
 * @Version: 1.0
 */
public final class XZDialogContentActivity extends Activity {


    private int mCode ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取回调对象对应编码号
        mCode = getIntent().getIntExtra(XZActivityLifecycleInterface.class.getCanonicalName(), -1);
        
        if (dialogPageMap.get(mCode) != null) {
            dialogPageMap.get(mCode).onCreate(this);
        } else {
            finish();
        }
    }


    /**
     * 权限设置面板，弹窗设置面板，"系统设置"权限获取面版结果返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (dialogPageMap.get(mCode) != null) {
            dialogPageMap.get(mCode).onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogPageMap.get(mCode) != null) {
            dialogPageMap.get(mCode).onDestroy();
        }
    }
    
}
