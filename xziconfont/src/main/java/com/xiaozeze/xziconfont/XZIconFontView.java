package com.xiaozeze.xziconfont;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntRange;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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
    private TextBound mIconBound = new TextBound();

    private TextView mTextView;
    private TextBound mTextBound = new TextBound();

    public XZIconFontView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XZIconFontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 初始化视图
        View.inflate(getContext(), R.layout.icon_font, this);
        mIconView = findViewById(R.id.fontViewContent);
        mIconView.setMovementMethod(IconFontLinkMovementMethod.getInstance());
        // 解析XML属性

        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.XZIconFontView);
        // 图标相关
        mIconBound.orientation = ta.getInt(R.styleable.XZIconFontView_icon_rotation, LEFT);
        mIconBound.mText = ta.getText(R.styleable.XZIconFontView_iconCode);
        mIconBound.mColor = ta.getColor(R.styleable.XZIconFontView_iconColor, Color.BLACK);
        mIconBound.mSize = ta.getDimension(R.styleable.XZIconFontView_iconSize, dp2px(15));
        mIconBound.mStyle = ta.getInt(R.styleable.XZIconFontView_iconBold, Typeface.NORMAL);
        int iconOffset_left = (int) ta.getDimension(R.styleable.XZIconFontView_iconOffset_left, 0);
        int iconOffset_top = (int) ta.getDimension(R.styleable.XZIconFontView_iconOffset_top, 0);
        int iconOffset_right = (int) ta.getDimension(R.styleable.XZIconFontView_iconOffset_right, 0);
        int iconOffset_bottom = (int) ta.getDimension(R.styleable.XZIconFontView_iconOffset_bottom, 0);

        // 文字相关
        mTextBound.orientation = ta.getInt(R.styleable.XZIconFontView_text_Orientation, LEFT);
        mTextBound.mText = ta.getText(R.styleable.XZIconFontView_font_Text);
        mTextBound.mColor = ta.getColor(R.styleable.XZIconFontView_font_TextColor, mIconBound.mColor);
        mTextBound.mSize = ta.getDimension(R.styleable.XZIconFontView_font_TextSize, mIconBound.mSize + dp2px(2));
        mTextBound.mStyle = ta.getInt(R.styleable.XZIconFontView_font_TextStyle, Typeface.NORMAL);
        mTextBound.textPadding = (int) ta.getDimension(R.styleable.XZIconFontView_textPadding, 0);
        ta.recycle();

        // 初始化IconView
        mIconView.setPadding(iconOffset_left,iconOffset_top,iconOffset_right,iconOffset_bottom);
        rendererIconView();

        // 初始化 TextView
        if (mTextBound.isValid()) {
            rendererTextView();
        }
    }

    private void rendererIconView() {
        mIconView.setRotation(mIconBound.orientation);
        mIconView.setText(mIconBound.mText);
        mIconView.setTextColor(mIconBound.mColor);
        mIconView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mIconBound.mSize);
        Typeface typeface = Typeface.create(mIconView.getTypeface(), mTextBound.mStyle);
        mIconView.setTypeface(typeface);
    }


    /**
     * 创建并设置TextView视图
     */
    private void rendererTextView() {
        // 初次创建，加入到父布局中
        if (mTextView == null) {
            mTextView = new TextView(getContext());
            mTextView.setMovementMethod(IconFontLinkMovementMethod.getInstance());
            mTextView.setId(R.id.tv_id);
            addView(mTextView, -2, -2);
        }
        LayoutParams icon_lp = new LayoutParams(-2, -2);
        boolean showIt = !TextUtils.isEmpty(mTextBound.mText);
        if (showIt) {
            // 描述位置关系
            LayoutParams text_lp = new LayoutParams(-2, -2);
            // init LayoutParams
            switch (mTextBound.orientation) {
                case LEFT:
                    text_lp.addRule(CENTER_VERTICAL, TRUE);
                    icon_lp.addRule(CENTER_VERTICAL, TRUE);

                    icon_lp.addRule(RIGHT_OF, mTextView.getId());
                    icon_lp.leftMargin = mTextBound.textPadding;
                    break;
                case TOP:
                    icon_lp.addRule(CENTER_HORIZONTAL, TRUE);
                    text_lp.addRule(ALIGN_PARENT_TOP, TRUE);
                    text_lp.addRule(CENTER_HORIZONTAL, TRUE);

                    icon_lp.addRule(BELOW, mTextView.getId());
                    break;
                case RIGHT:
                    text_lp.addRule(CENTER_VERTICAL, TRUE);
                    icon_lp.addRule(CENTER_VERTICAL, TRUE);

                    text_lp.addRule(RIGHT_OF, mIconView.getId());
                    icon_lp.leftMargin = mTextBound.textPadding;

                    break;
                case BOTTOM:
                    icon_lp.addRule(CENTER_HORIZONTAL, TRUE);
                    text_lp.addRule(CENTER_HORIZONTAL, TRUE);

                    text_lp.addRule(BELOW, mIconView.getId());
                    break;
            }
            mTextView.setLayoutParams(text_lp);

            // 设置text样式
            mTextView.setText(mTextBound.mText);
            mTextView.setTextColor(mTextBound.mColor);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextBound.mSize);
            mTextView.setTypeface(Typeface.create(Typeface.SANS_SERIF, mTextBound.mStyle));
        }
        mIconView.setLayoutParams(icon_lp);
        mTextView.setVisibility(showIt ? VISIBLE : GONE);
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
        mIconBound.mText = iconCode;
        rendererIconView();
    }


    /**
     * 设置icon大小
     *
     * @param size
     */
    public void setIconSize(int size) {
        mIconBound.mSize = size;
        rendererIconView();
    }

    /**
     * 设置icon颜色
     *
     * @param iconColor
     */
    public void setIconColor(@ColorInt int iconColor) {
        mIconBound.mColor = iconColor;
        rendererIconView();
    }

    /**
     * 设置icon颜色
     *
     * @param iconColorId
     */
    public void setIconColorRes(@ColorRes int iconColorId) {
        mIconBound.mColor = ContextCompat.getColor(getContext(), iconColorId);
        rendererIconView();
    }

    /**
     * 设置icon是否粗体
     *
     * @param isBold
     */
    public void setIconBold(boolean isBold) {
        mIconBound.mStyle = isBold ? Typeface.BOLD : Typeface.NORMAL;
        rendererIconView();
    }

    /**
     * 设置icon的偏转角度
     *
     * @param rotation
     */
    public void setIconRotation(int rotation) {
        mIconBound.orientation = rotation;
        rendererIconView();
    }

    /**
     * 动态设置是否展示icon
     *
     * @param visible
     */
    public void setIconVisibility(boolean visible) {
        mIconView.setVisibility(visible ? VISIBLE : GONE);
    }

// ----------------------- text -----------------------------------------

    /**
     * 获取text样式数据
     *
     * @return
     */
    public TextBound getTextBound() {
        return mTextBound.clone();
    }

    /**
     * 设置icon附属文字信息
     *
     * @param textBound
     * @return
     */
    public void setTextThem(TextBound textBound) {
        if (!mTextBound.equals(textBound)) {
            mTextBound = textBound;
            rendererTextView();
        }
    }

    /**
     * 设置文字
     *
     * @param text
     * @param orientation
     */
    public void setText(CharSequence text, @IntRange(from = LEFT, to = BOTTOM) int orientation) {
        mTextBound.mText = text;
        mTextBound.orientation = orientation;
        rendererTextView();
    }

    /**
     * 设置文字间距
     *
     * @param spacing
     */
    public void setTextSpacing(float spacing) {
        if (Build.VERSION.SDK_INT >= 21) {
            mTextView.setLetterSpacing(spacing);
        }
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(@ColorInt int color) {
        mTextBound.mColor = color;
        rendererTextView();
    }

    /**
     * 设置文字颜色
     *
     * @param colorRes
     */
    public void setTextColorRes(@ColorRes int colorRes) {
        setTextColor(ContextCompat.getColor(getContext(),colorRes));
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


    private int dp2px(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    public class TextBound implements Cloneable {

        private float mSize = dp2px(15);
        // 样式相关信息
        private CharSequence mText;
        @ColorInt
        private int mColor = Color.BLACK;
        private int mStyle = Typeface.NORMAL;

        // 位置相关信息
        private int orientation = LEFT;
        private int textPadding;


        public void setOrientation(int orientation) {
            this.orientation = orientation;
        }


        public void setText(CharSequence text) {
            mText = text;
        }


        public void setSize(float size) {
            mSize = size;
        }

        public void setColor(int color) {
            mColor = color;
        }

        public void setStyle(@IntRange(from = Typeface.NORMAL, to = Typeface.BOLD_ITALIC) int style) {
            mStyle = style;
        }

        public void setTextPadding(int textPadding) {
            this.textPadding = textPadding;
        }

        private boolean isValid() {
            return !TextUtils.isEmpty(mText);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TextBound textBound = (TextBound) o;

            if (orientation != textBound.orientation) return false;
            if (Float.compare(textBound.mSize, mSize) != 0) return false;
            if (mColor != textBound.mColor) return false;
            if (mStyle != textBound.mStyle) return false;
            if (textPadding != textBound.textPadding) return false;
            return mText.equals(textBound.mText);
        }

        @Override
        public int hashCode() {
            int result = orientation;
            result = 31 * result + mText.hashCode();
            result = 31 * result + (mSize != +0.0f ? Float.floatToIntBits(mSize) : 0);
            result = 31 * result + mColor;
            result = 31 * result + mStyle;
            result = 31 * result + textPadding;
            return result;
        }

        @Override
        protected TextBound clone() {
            TextBound bound;
            try {
                bound = (TextBound) super.clone();
                return bound;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return new TextBound();
        }
    }

    /**
     * @Description: 解决高亮点击和父控件点击事件冲突问题
     * @auther: fengzeyuan
     * @Date: 2018/10/16 下午1:42
     */

    public static class IconFontLinkMovementMethod extends LinkMovementMethod {


        private static class Holder {
            public static final IconFontLinkMovementMethod instance = new IconFontLinkMovementMethod();
        }

        private IconFontLinkMovementMethod() {
        }

        public static IconFontLinkMovementMethod getInstance() {
            return Holder.instance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            boolean b = super.onTouchEvent(widget, buffer, event);
            //解决点击事件冲突问题
            if (!b && event.getAction() == MotionEvent.ACTION_UP) {
                ViewParent parent = widget.getParent();//处理widget的父控件点击事件
                if (parent instanceof ViewGroup) {
                    return ((ViewGroup) parent).performClick();
                }
            }
            return b;
        }
    }
}
