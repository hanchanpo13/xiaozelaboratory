package com.xiaozeze.xziconfont;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
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

    public XZIconFontView(Context context) {
        this(context, null);
    }

    public XZIconFontView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
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
        TextView tv = findViewById(R.id.fontViewContent);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        if (colorStateList != null) {
            tv.setTextColor(colorStateList);
        }
        tv.setText(iconCode);
    }


    int dp2px(int dp) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
