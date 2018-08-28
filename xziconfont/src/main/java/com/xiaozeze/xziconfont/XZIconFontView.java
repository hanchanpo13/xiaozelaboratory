package com.xiaozeze.xziconfont;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
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
    private TextStyle mIconTextStyle = new TextStyle();
    private TextStyle mIconStyle = new TextStyle();

    public XZIconFontView(Context context) {
        this(context, null);
    }

    public XZIconFontView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XZIconFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // UI渲染
        View.inflate(getContext(), R.layout.icon_font, this);
        mTv = findViewById(R.id.fontViewContent);

        // 数据初始化
        mIconTextStyle.mSize = dp2px(15);
        mIconStyle.mSize = dp2px(15);

        // 解析XML属性
        if (attrs != null) {
            // 获取自定义属性
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XZIconFontView);
            // 全局选择器
            ColorStateList mColorSelector = ta.getColorStateList(R.styleable.XZIconFontView_defaultColor);
            float size = ta.getDimension(R.styleable.XZIconFontView_size, dp2px(15));

            // 文字相关
            mIconTextStyle.mText = ta.getText(R.styleable.XZIconFontView_iconText);
            mIconTextStyle.mSize = size;
            mIconTextStyle.mColor = ta.getColor(R.styleable.XZIconFontView_iconTextColor, Color.BLACK);
            mIconTextStyle.mStyle = ta.getInt(R.styleable.XZIconFontView_iconTextStyle, Typeface.NORMAL);

            // 图标相关
            mIconStyle.mText = ta.getText(R.styleable.XZIconFontView_iconCode);
            mIconStyle.mSize = size;
            mIconStyle.mColor = ta.getColor(R.styleable.XZIconFontView_iconColor, Color.BLACK);
            mIconStyle.mStyle = ta.getInt(R.styleable.XZIconFontView_iconBold, Typeface.NORMAL);

            ta.recycle();

            // 设置颜色选择器
            setDefaultColor(mColorSelector);
            setIconContent(getStyleSpan(mIconTextStyle, mIconStyle));
        }
    }

    /**
     * 动态设置是否展示icon
     *
     * @param visible
     */
    public void setIconVisibility(boolean visible) {
        if (!TextUtils.isEmpty(mIconTextStyle.mText) && !TextUtils.isEmpty(mIconStyle.mText)) {
            mTv.setText(getStyleSpan(mIconTextStyle, visible ? mIconStyle : null));
        }
    }

    /**
     * 设置信息
     *
     * @param iconText 文字信息
     * @param iconCode icon信息
     */
    public void setIconContent(CharSequence iconText, CharSequence iconCode) {
        //  文字渲染
        mIconTextStyle.mText = iconText;
        mIconStyle.mText = iconCode;
        mTv.setText(getStyleSpan(mIconTextStyle, mIconStyle));
    }


    /**
     * 设置信息与
     *
     * @param iconContent
     */
    public void setIconContent(SpannableStringBuilder iconContent) {
        if (iconContent != null) {
            mIconTextStyle.mText = null;
            mIconStyle.mText = null;
        }
        mTv.setText(iconContent);
    }

    /**
     * 设置默认颜色
     *
     * @param color
     */
    public void setDefaultColor(@ColorInt int color) {
        mTv.setTextColor(color);
    }

    /**
     * 设置你默认选择器
     *
     * @param colorSelector
     */
    public void setDefaultColor(ColorStateList colorSelector) {
        // 设置颜色选择器
        if (colorSelector != null) {
            mTv.setTextColor(colorSelector);
        } else {
            mTv.setTextColor(Color.BLACK);
        }
    }


    /**
     * 重定义文字大小、颜色、样式
     *
     * @param text 文本
     * @param icon
     * @return
     */
    private SpannableStringBuilder getStyleSpan(TextStyle text, TextStyle icon) {

        SpannableStringBuilder spannableString = new SpannableStringBuilder();

        int start = 0;
        int end;
        // 文字
        if (text != null && !TextUtils.isEmpty(text.mText)) {
            spannableString.append(text.mText);
            end = start + text.mText.length();
            // 色值
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(text.mColor);
            spannableString.setSpan(colorSpan, 0, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // 字号设置
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan((int) text.mSize);
            spannableString.setSpan(absoluteSizeSpan, 0, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // 字体设置
            StyleSpan styleSpan = new StyleSpan(text.mStyle);
            spannableString.setSpan(styleSpan, 0, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            start = end;
        }
        // icon
        if (icon != null && !TextUtils.isEmpty(icon.mText)) {
            end = start + icon.mText.length();
            spannableString.append(icon.mText);
            // 色值
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(icon.mColor);
            spannableString.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // 字号设置
            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan((int) icon.mSize);
            spannableString.setSpan(absoluteSizeSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            // 字体设置
            StyleSpan styleSpan = new StyleSpan(icon.mStyle);
            spannableString.setSpan(styleSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return spannableString;
    }


    int dp2px(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private class TextStyle {
        private CharSequence mText;
        private float mSize = 30;
        private int mColor = Color.BLACK;
        private int mStyle = Typeface.NORMAL;
    }
}
