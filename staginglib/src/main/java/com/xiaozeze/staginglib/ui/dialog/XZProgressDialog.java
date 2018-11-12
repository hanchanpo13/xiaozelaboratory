package com.xiaozeze.staginglib.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaozeze.staginglib.utils.StringUtils;
import com.xiaozeze.staginglib.R;


/**
 * @Description:进度展示的dialog
 * @Author: fengzeyuan
 * @Date: 17/9/22 下午7:04
 * @Version: 1.0
 */
public class XZProgressDialog extends Dialog {

    private String mTitle;
    private String mContent;

    private ProgressBar mUpdatePgb;
    private TextView mUpdatePgbTv;

    private String mNegBtnText;
    private OnClickListener leftBtnListener;

    private String mPosBtnText;
    private OnClickListener rightBtnListener;

    public XZProgressDialog(@NonNull Context context) {
        super(context, R.style.progressDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        WindowManager.LayoutParams wmParams = getWindow().getAttributes();
//        wmParams.format = PixelFormat.TRANSLUCENT;  //内容半透明
//        wmParams.alpha = 0.2f;    //调节透明度，1.0最大
//        getWindow().setAttributes(wmParams);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.xz_progress_dialog_layout, null);

        TextView mTitleTv = view.findViewById(R.id.uptate_download_title);
        TextView mContentTv = view.findViewById(R.id.uptate_download_content);
        mUpdatePgb = view.findViewById(R.id.uptate_download_pgb);
        mUpdatePgbTv = view.findViewById(R.id.update_download_pgbtv);
        TextView mUpdateLeft = view.findViewById(R.id.uptate_download_left);
        TextView mUpdateRight = view.findViewById(R.id.uptate_download_right);
        View mViewLine = view.findViewById(R.id.view_line);

        // 标题
        if (!StringUtils.isEmpty(mTitle)) {
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(mTitle);
        }

        // 内容
        if (!StringUtils.isEmpty(mContent)) {
            mContentTv.setVisibility(View.VISIBLE);
            mContentTv.setText(mContent);
        }

        // 左边按钮
        if (!StringUtils.isEmpty(mNegBtnText)) {
            mUpdateLeft.setVisibility(View.VISIBLE);
            mUpdateLeft.setText(mNegBtnText);
            mUpdateLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leftBtnListener != null) {
                        leftBtnListener.onClick(XZProgressDialog.this, v.getId());
                    }
                }
            });
        }

        // 右边按钮
        if (!StringUtils.isEmpty(mPosBtnText)) {
            mUpdateRight.setVisibility(View.VISIBLE);
            mUpdateRight.setText(mPosBtnText);
            mUpdateRight.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (rightBtnListener != null) {
                        rightBtnListener.onClick(XZProgressDialog.this, v.getId());
                    }
                }
            });
        } else {
            mViewLine.setVisibility(View.GONE);
            mUpdateRight.setVisibility(View.GONE);
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = Utils.dip2px(XZApplication.getInstance(), 315);
        setContentView(view, lp);
    }

    public XZProgressDialog setTitle(String title) {
        mTitle = title;
        return this;
    }

    public XZProgressDialog setContent(String content) {
        mContent = content;
        return this;
    }


    public XZProgressDialog setPositiveButton(String btnText, OnClickListener clickListener) {
        mPosBtnText = btnText;
        rightBtnListener = clickListener;
        return this;
    }

    public XZProgressDialog setNegativeButton(String btnText, OnClickListener clickListener) {
        leftBtnListener = clickListener;
        mNegBtnText = btnText;
        return this;
    }

    /**
     * 设置进度
     *
     * @param progress 0-100
     */
    public void setProgress(int max, int progress, String text) {
        if (isShowing()) {
            mUpdatePgb.setMax(max);
            mUpdatePgb.setProgress(progress);
            mUpdatePgbTv.setText(text);
        }
    }
}
