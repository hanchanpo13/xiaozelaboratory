#### **IconFont概述**

- 是什么：

  > IocnFont 是一种使用自定义字体库替换现有图片Icon的技术手段。



- 为什么：

  > 使用此方案好处如下:

   	1. 减少资源体积，每个iconFont图标理论上只用2字节
   	2. 可以无限放大且不变虚
   	3. 符合元素设计理念，节省UI工作量，同时减少因为图标颜色、大小问题，而需要放置多套图问题；省时省工还节约空间。
   	4. 符合元素设计思想，从开发到大前端均可以复用使用



### 怎么办

	>待解决的问题

1. 传统集成IconFont方案开发时无法在布局文件中实时预览
2. 使用原生提供的TextView渲染图标，在完成图文混排时十分麻烦
3. 安卓O提供的XML加载字体库技术，存在版本向下兼容问题



#### 解决方案：XZIconFontView



> 使用

- 定义一个

```xml
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"/>
```

java方式省略



- 设置一个图标

```xml
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" 
    app:iconCode="@string/common_arrowheadTriangle"/>
```

```java
String icon = getResources().getString(R.string.common_arrowheadTriangle);
mFontView.setIcon(icon);

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
```



- 图标颜色设置

```xml
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:iconCode="@string/common_arrowheadTriangle"
    app:iconColor="#ff4081"/>
```

```java
mFontView.setIconColor(Color.parseColor("#ff4081"));

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
```



- 图标边上追加文字

```xml
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:iconCode="@string/common_arrowheadTriangle"
    app:font_Text="文字右"
    app:text_Orientation="right"/>
```

```java
// 支持左上右下四个方位
mFontView.setText("文字右",XZIconFontView.RIGHT);
```



- 文字颜色设置

```xml
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:iconCode="@string/common_arrowheadTriangle"
    app:font_Text="文字右"
    app:text_Orientation="right"
    app:font_TextColor="#ff4081"/>
```

```java 
mFontView.setTextColor(Color.parseColor("#ff4081"));
```



- 对富文本的动态支持

```java
//速订是一种更高效的预订方式：房客下单付款后即完成预订，不再需要您的确认。了解更多
SpannableStringBuilder spannableInfo = SpannableStringUtils
        .getBuilder("速订").setBold()
        .append("是一种更高效的预订方式：房客下单付款后即完成预订，")
        .append("不再需要您的确认。").setForegroundColor(Color.RED).setBold()
        .append("了解更多 ")
        .append(icon).setTextSize(13)
        .create();
mFontView.setIcon(spannableInfo);
```



- 图标加粗

```XML
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"                                           			app:iconCode="@string/common_arrowheadTriangle"
    app:iconBold="yes"/>
```

```java
mFontView.setIconBold(true);
```



- 图标方向设置

```xml
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"                                           			app:iconCode="@string/common_arrowheadTriangle"
    app:iconBold="yes"
    app:icon_rotation="rotation_90"/>
```



```java
mFontView.setIconRotation(90)
```



- 文字样式设置(粗体、斜体、粗斜体)

```xml
<com.xiaozeze.xziconfont.XZIconFontView
    android:id="@+id/icon_font_view"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"                                           			app:iconCode="@string/common_arrowheadTriangle"
    app:font_TextStyle="bold"/>
```

```java 
XZIconFontView.TextBound textBound = mFontView.getTextBound();
// Typeface.NORMAL,Typeface.BOLD,Typeface.ITALIC,Typeface.BOLD_ITALIC
textBound.setStyle(Typeface.BOLD);
```

















