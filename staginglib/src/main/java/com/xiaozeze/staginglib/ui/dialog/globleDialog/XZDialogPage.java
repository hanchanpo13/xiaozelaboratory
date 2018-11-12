
package com.xiaozeze.staginglib.ui.dialog.globleDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.xiaozeze.staginglib.R;


public class XZDialogPage<T extends Dialog> implements DialogInterface {

    private final int instanceTag;
    private Context context;

    private OnActivityForResultListener resultListener;
    private OnCancelListener mOnCancelListener;
    
    private GlobalDialogBuilder<T> mDialogBuilder;

    private static final int STATE_PREPARE = 0;
    private static final int STATE_SHOW = 1;
    private static final int STATE_DISMISS = 2;
    private int mCurState = STATE_DISMISS;

    public XZDialogPage(Context cx) {
        this.context = cx;
        instanceTag = hashCode();
    }

    private Activity activity;

    private T mDialog;

    public T getDialog() {
        return mDialog;
    }


    private XZActivityLifecycleInterface mLifecycleInterface = new XZActivityLifecycleInterface() {
        @Override
        public void onCreate(Activity activity) {
            XZDialogPage.this.activity = activity;
            mDialog = mDialogBuilder.builder(activity);
            mDialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (mOnCancelListener != null) {
                        mOnCancelListener.onCancel(dialog);
                    }
                    dismiss();
                }
            });
            mDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dismiss();
                }
            });
            mDialog.setCancelable(mOnCancelListener != null);
            mDialog.setCanceledOnTouchOutside(mOnCancelListener != null);
            mDialog.show();
            mCurState = STATE_SHOW;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            onPageResult(requestCode, resultCode, data);
        }

        @Override
        public void onDestroy() {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
            mCurState = STATE_DISMISS;
            dialogPageMap.remove(instanceTag);
            XZDialogPage.this.activity = null;
        }
    };

    /**
     * 如有页面跳转返回，此方法回调
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        数据
     */
    private void onPageResult(int requestCode, int resultCode, Intent data) {
        if (resultListener != null) {
            resultListener.onActivityForResult(requestCode, resultCode, data);
            resultListener = null;
        }
        mCurState = STATE_SHOW;
        dismiss();
    }

    /**
     * 普通全局提示框构造器
     *
     * @return 对应构造器
     */
    public AlertDialogBuilder getAlertDialogBuilder() {
        AlertDialogBuilder alertDialogBuilder = new AlertDialogBuilder(this);
        mDialogBuilder = alertDialogBuilder;
        return alertDialogBuilder;
    }

    /**
     * 列表全局提示框构造器
     *
     * @return
     */
    public ListDialogBuilder getListDialogBuilder() {
        ListDialogBuilder listDialogBuilder = new ListDialogBuilder(this);
        mDialogBuilder = listDialogBuilder;
        return listDialogBuilder;
    }


    /**
     * 头部图片全局提示框构造器
     *
     * @return 对应构造器
     */
    public HeadImageAlertDialogBuilder getHeadImageAlertDialogBuilder() {
        HeadImageAlertDialogBuilder alertDialogBuilder = new HeadImageAlertDialogBuilder(this);
        mDialogBuilder = alertDialogBuilder;
        return alertDialogBuilder;
    }

    /**
     * 列表全局提示框构造器
     *
     * @return 对应构造器
     */
    public ProgressDialogBuilder getProgressDialogBuilder() {
        ProgressDialogBuilder listDialogBuilder = new ProgressDialogBuilder(this);
        mDialogBuilder = listDialogBuilder;
        return listDialogBuilder;
    }

    @Override
    public void cancel() {
        dismiss();
    }

    @Override
    public void dismiss() {
        if (mCurState == STATE_SHOW) {
            if (activity != null && !activity.isFinishing()) {
                mCurState = STATE_PREPARE;
                activity.finish();
            } else {
                mCurState = STATE_DISMISS;
            }
        }
    }

    /**
     * 显示dialog
     */
    public void showPage() {
        if (mCurState == STATE_DISMISS && mDialogBuilder != null) {
            mCurState = STATE_PREPARE;
            dialogPageMap.put(instanceTag, mLifecycleInterface);
            Intent intent = new Intent(context, XZDialogContentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(XZActivityLifecycleInterface.class.getCanonicalName(), instanceTag);
            context.startActivity(intent);
        }

    }

    /**
     * 全局dialog是否显示
     *
     * @return 是否显示
     */
    public boolean isShowing() {
        return mDialog != null && mDialog.isShowing() && mCurState == STATE_SHOW;
    }


    /**
     * ForResult方式打开一个activity
     *
     * @param intent         意图
     * @param requestCode    请求码
     * @param resultListener 返回的回调
     */
    public void startActivityForResult(Intent intent, int requestCode,
                                       OnActivityForResultListener resultListener) {
        if (activity != null && !activity.isFinishing()) {
            // 只关闭dialog，不关闭容器Activity
            mCurState = STATE_PREPARE;
            mDialog.dismiss();
            this.resultListener = resultListener;
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(R.anim.enter_right_left, R.anim.exit_right_left);
        }
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        mOnCancelListener = onCancelListener;
    }

    /**
     * ForResult方式打开的activity被finish后的监听
     */
    public interface OnActivityForResultListener {
        /**
         * @param requestCode 请求码
         * @param resultCode  结果码
         * @param data        包含数据的意图
         */
        void onActivityForResult(int requestCode, int resultCode, Intent data);
    }
}
