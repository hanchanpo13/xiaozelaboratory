package com.xiaozeze.xziconfont;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Description:IconFont展示类
 * Author: fengzeyuan
 * Date: 2018/7/25 下午2:52
 * Version: 1.0
 */
public class XZIconFontView extends FrameLayout {

    private TextView mTv;

    public XZIconFontView(Context context) {
        this(context, null);
    }

    public XZIconFontView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XZIconFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.icon_font, this);

        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XZIconFontView);
        float size = ta.getDimension(R.styleable.XZIconFontView_iconSize, dp2px(15));
        ColorStateList colorStateList = ta.getColorStateList(R.styleable.XZIconFontView_iconColor);
        CharSequence iconCode = ta.getText(R.styleable.XZIconFontView_iconCode);
        ta.recycle();

        // 设置属性
        mTv = findViewById(R.id.fontViewContent);
        mTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        setIconColor(colorStateList);
        setIcon(iconCode);
    }
    
    public void setIcon(CharSequence iconCode){
        mTv.setText(iconCode);
    }
    
    public void setIcon(@StringRes int iconResId){
        mTv.setText(iconResId);
    }

    public void setIconColor(@ColorRes int iconResId){
        mTv.setTextColor(ContextCompat.getColor(getContext(),iconResId));
    }
    
    public void setIconColorInt(@ColorInt int iconResId){
        mTv.setTextColor(iconResId);
    }

    public void setIconColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            mTv.setTextColor(colorStateList);
        }
    }

    public void setIconSize(int size_dp){
        mTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,dp2px(size_dp));
    }


    int dp2px(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
