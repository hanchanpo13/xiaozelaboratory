package com.xiaozeze.xziconfont;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Description:IconFont展示类
 * Author: fengzeyuan
 * Date: 2018/7/25 下午2:52
 * Version: 1.0
 */
public class XZIconFontView extends RelativeLayout {

    /**
     * 左TextView
     */
    public static final int LEFT = 0x00;
    /**
     * 上TextView
     */
    public static final int TOP = LEFT + 1;
    /**
     * 右TextView
     */
    public static final int RIGHT = TOP + 1;
    /**
     * 下TextView
     */
    public static final int BOTTOM = RIGHT + 1;

    private TextView mIconView;

    private SparseArray<TextBound> mBoundsMap = new SparseArray<>(5);

    public XZIconFontView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XZIconFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化视图
        View.inflate(getContext(), R.layout.icon_font, this);
        mIconView = findViewById(R.id.fontViewContent);
        // 解析XML属性
        TextBound iconBound = new TextBound();
        int leftPadding = 0;
        int topPadding = 0;
        int rightPadding = 0;
        int bottomPadding = 0;

        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XZIconFontView);
        // 图标相关
        iconBound.mText = ta.getText(R.styleable.XZIconFontView_iconCode);
        iconBound.mColor = ta.getColor(R.styleable.XZIconFontView_iconColor, Color.BLACK);
        iconBound.mSize = ta.getDimension(R.styleable.XZIconFontView_iconSize, dp2px(15));
        iconBound.mStyle = ta.getInt(R.styleable.XZIconFontView_iconBold, Typeface.NORMAL);
        leftPadding = (int) ta.getDimension(R.styleable.XZIconFontView_textLeftPadding, 0);
        topPadding = (int) ta.getDimension(R.styleable.XZIconFontView_textTopPadding, 0);
        rightPadding = (int) ta.getDimension(R.styleable.XZIconFontView_textRightPadding, 0);
        bottomPadding = (int) ta.getDimension(R.styleable.XZIconFontView_textBottomPadding, 0);

        // 文字相关

        //  左
        TextBound textBound = new TextBound();
        textBound.mText = ta.getText(R.styleable.XZIconFontView_font_leftText);
        textBound.mColor = ta.getColor(R.styleable.XZIconFontView_font_leftTextColor, iconBound.mColor);
        textBound.mSize = ta.getDimension(R.styleable.XZIconFontView_font_leftTextSize, iconBound.mSize + dp2px(2));
        textBound.mStyle = ta.getInt(R.styleable.XZIconFontView_font_leftTextStyle, Typeface.NORMAL);
        mBoundsMap.put(LEFT, textBound);

        // 上
        textBound = new TextBound();
        textBound.mText = ta.getText(R.styleable.XZIconFontView_font_topText);
        textBound.mColor = ta.getColor(R.styleable.XZIconFontView_font_topTextColor, iconBound.mColor);
        textBound.mSize = ta.getDimension(R.styleable.XZIconFontView_font_topTextSize, iconBound.mSize + dp2px(2));
        textBound.mStyle = ta.getInt(R.styleable.XZIconFontView_font_topTextStyle, Typeface.NORMAL);
        mBoundsMap.put(TOP, textBound);

        // 右
        textBound = new TextBound();
        textBound.mText = ta.getText(R.styleable.XZIconFontView_font_rightText);
        textBound.mColor = ta.getColor(R.styleable.XZIconFontView_font_rightTextColor, iconBound.mColor);
        textBound.mSize = ta.getDimension(R.styleable.XZIconFontView_font_rightTextSize, iconBound.mSize + dp2px(2));
        textBound.mStyle = ta.getInt(R.styleable.XZIconFontView_font_rightTextStyle, Typeface.NORMAL);
        mBoundsMap.put(RIGHT, textBound);

        // 下
        textBound = new TextBound();
        textBound.mText = ta.getText(R.styleable.XZIconFontView_font_bottomText);
        textBound.mColor = ta.getColor(R.styleable.XZIconFontView_font_bottomTextColor, iconBound.mColor);
        textBound.mSize = ta.getDimension(R.styleable.XZIconFontView_font_bottomTextSize, iconBound.mSize + dp2px(2));
        textBound.mStyle = ta.getInt(R.styleable.XZIconFontView_font_bottomTextStyle, Typeface.NORMAL);
        mBoundsMap.put(BOTTOM, textBound);
        ta.recycle();

        initView(iconBound, leftPadding, topPadding, rightPadding, bottomPadding);


    }

    private void initView(TextBound iconBound, int leftPadding, int topPadding, int rightPadding, int bottomPadding) {
        TextBound textBound;// 设置图标的属性
        mIconView.setText(iconBound.mText);
        mIconView.setTextColor(iconBound.mColor);
        mIconView.setTextSize(TypedValue.COMPLEX_UNIT_PX, iconBound.mSize);
        mIconView.getPaint().setFakeBoldText(Typeface.BOLD == iconBound.mStyle);
        mIconView.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        // text 相关
        textBound = mBoundsMap.get(LEFT);
        if (textBound.isValid()) {
            getTextView(LEFT);
            textBound.createAndInitText(LEFT);
        }

        textBound = mBoundsMap.get(TOP);
        if (textBound.isValid()) {
            textBound.createAndInitText(TOP);
        }

        textBound = mBoundsMap.get(RIGHT);
        if (textBound.isValid()) {
            textBound.createAndInitText(RIGHT);
        }

        textBound = mBoundsMap.get(BOTTOM);
        if (textBound.isValid()) {
            textBound.createAndInitText(BOTTOM);
        }
    }

    /**
     * 设置信息
     *
     * @param iconCodeRes
     */
    public void setIcon(@StringRes int iconCodeRes) {
        setIcon(getResources().getString(iconCodeRes));
    }

    /**
     * 设置信息
     *
     * @param iconCode icon信息
     */
    public void setIcon(CharSequence iconCode) {
        //  文字渲染
        mIconView.setText(iconCode);
    }


    /**
     * 动态设置是否展示icon
     *
     * @param visible
     */
    public void setIconVisibility(boolean visible) {
        mIconView.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * @param witchOne {@link #LEFT}, {@link #TOP}, {@link #RIGHT}, {@link #BOTTOM}
     * @return
     */
    public TextView getTextView(@IntRange(from = LEFT, to = BOTTOM) int witchOne) {
        TextBound textBound = mBoundsMap.get(witchOne);
        if (textBound == null) {
            textBound = new TextBound();
            textBound.mColor = mIconView.getTextColors().getDefaultColor();
            textBound.mSize = mIconView.getTextSize();
            mBoundsMap.put(witchOne, textBound);
        }
        if (textBound.view == null) {
            textBound.createAndInitText(witchOne);
        }
        return textBound.view;
    }

    /**
     * 设置你默认选择器
     *
     * @param colorSelector
     */
    public void setDefaultColor(ColorStateList colorSelector) {
        // 设置颜色选择器
        if (colorSelector != null) {
            mIconView.setTextColor(colorSelector);
        } else {
            mIconView.setTextColor(Color.BLACK);
        }
    }

//    /**
//     * 重定义文字大小、颜色、样式
//     *
//     * @param bound 文本
//     * @return
//     */
//    private SpannableStringBuilder getSpanStr(TextBound bound) {
//        SpannableStringBuilder spannableString = new SpannableStringBuilder();
//        // 文字
//        if (bound != null && !TextUtils.isEmpty(bound.mText)) {
//            spannableString.append(bound.mText);
//            // 色值
//            ForegroundColorSpan colorSpan = new ForegroundColorSpan(bound.mColor);
//            spannableString.setSpan(colorSpan, 0, bound.mText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            // 字号设置
//            AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan((int) bound.mSize);
//            spannableString.setSpan(absoluteSizeSpan, 0, bound.mText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            // 字体设置
//            StyleSpan styleSpan = new StyleSpan(bound.mStyle);
//            spannableString.setSpan(styleSpan, 0, bound.mText.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//        }
//        return spannableString;
//    }


    int dp2px(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public class TextBound {

        private CharSequence mText;
        private float mSize = dp2px(15);
        private @ColorInt int mColor = Color.BLACK;
        private int mStyle = Typeface.NORMAL;
        private TextView view;

        private boolean isValid() {
            return !TextUtils.isEmpty(mText);
        }

        /**
         * 创建并设置TextView视图
         * @param witchOne {@link #LEFT}, {@link #TOP}, {@link #RIGHT}, {@link #BOTTOM}
         */
        private void createAndInitText(int witchOne) {
            // createView
            view = new TextView(getContext());
            addView(view, -2, -2);
            // init LayoutParams
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) view.getLayoutParams();
            switch (witchOne) {
                case LEFT:
                    lp.addRule(RelativeLayout.LEFT_OF, mIconView.getId());
                    break;
                case TOP:
                    lp.addRule(RelativeLayout.ABOVE, mIconView.getId());
                    break;
                case RIGHT:
                    lp.addRule(RelativeLayout.RIGHT_OF, mIconView.getId());
                    break;
                case BOTTOM:
                    lp.addRule(RelativeLayout.BELOW, mIconView.getId());
                    break;
            }
            lp.addRule(witchOne % 2 == 0 ? RelativeLayout.CENTER_VERTICAL : RelativeLayout.CENTER_HORIZONTAL);
            view.setText(mText);
            view.setTextColor(mColor);
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSize);

            TextPaint textPaint = view.getPaint();
            textPaint.setFakeBoldText(Typeface.BOLD == mStyle || Typeface.BOLD_ITALIC == mStyle);// 粗体
            textPaint.setTextSkewX(Typeface.ITALIC == mStyle || Typeface.BOLD_ITALIC == mStyle ? -0.5f : 0);//float类型参数，负数表示右斜，整数左斜

        }
    }
}
