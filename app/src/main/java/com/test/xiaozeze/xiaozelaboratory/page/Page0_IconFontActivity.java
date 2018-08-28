package com.test.xiaozeze.xiaozelaboratory.page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.test.xiaozeze.xiaozelaboratory.R;
import com.test.xiaozeze.xiaozelaboratory.domian.IconFontInfo;
import com.test.xiaozeze.xiaozelaboratory.uiBase.XZBaseListAdapter;
import com.test.xiaozeze.xiaozelaboratory.utils.ShareUtils;
import com.test.xiaozeze.xiaozelaboratory.utils.SpannableStringUtils;
import com.test.xiaozeze.xiaozelaboratory.utils.TranslateUtil;
import com.test.xiaozeze.xiaozelaboratory.utils.Utils;
import com.xiaozeze.xziconfont.XZIconFontView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Page0_IconFontActivity extends AppCompatActivity {

    private GridView mFontList;
    private Button btn_share;
    private XZIconFontView mFontView;
    private FontAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page0);
        mFontView = findViewById(R.id.icon_font_view);
        mFontList = findViewById(R.id.icon_font_list);
        btn_share = findViewById(R.id.btn_share);
        mAdapter = new FontAdapter(this);
        mFontList.setAdapter(mAdapter);
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtils.sendFileByOtherApp(Page0_IconFontActivity.this, "");
            }
        });
        testIconFontMix();
        new ParseHTMLTask().execute("demo_unicode.html");
    }

    private void testIconFontMix() {
        String icon = getResources().getString(R.string.iconf_common_arrowTriangle);
        //速订是一种更高效的预订方式：房客下单付款后即完成预订，不再需要您的确认。了解更多
        SpannableStringBuilder spannableInfo= SpannableStringUtils
                .getBuilder("速订").setBold()
                .append("是一种更高效的预订方式：房客下单付款后即完成预订，")
                .append("不再需要您的确认。").setForegroundColor(Color.RED).setBold()
                .append("了解更多 ")
                .append(icon).setTextSize(13)
                .create();
//        mFontView.setIconContent(spannableInfo);
        mFontView.setIconContent("了解更多",icon);
        mFontView.setOnClickListener(new View.OnClickListener() {
            boolean isVisib;

            @Override
            public void onClick(View v) {
                isVisib = !isVisib;
                mFontView.setIconVisibility(isVisib);
            }
        });
    }


    class FontAdapter extends XZBaseListAdapter<IconFontInfo> {

        public FontAdapter(Context context) {
            super(context);
        }

        @Override
        protected XZBaseListViewHolder getHolder(int pos, ViewGroup parent) {
            XZBaseListViewHolder viewHolder = new XZBaseListViewHolder(R.layout.item_icon_font, parent) {
                private XZIconFontView iconFontView;
                private TextView iconFontCode;
                private TextView iconFontDesc;

                @Override
                protected void initView(View view) {
                    iconFontView = view.findViewById(R.id.icon_font_view);
                    iconFontCode = view.findViewById(R.id.icon_font_code);
                    iconFontDesc = view.findViewById(R.id.icon_font_desc);
                }

                @Override
                public void setItemData(int pos) {
                    IconFontInfo item = getItem(pos);
                    iconFontView.setIconContent(null,item.code4Show);
                    iconFontCode.setText(item.code4XML);
                    iconFontDesc.setText(String.format("%s\n%s", item.type, item.name));
                }
            };
            return viewHolder;
        }

    }


    /**
     * 解析HTML
     */
    private class ParseHTMLTask extends AsyncTask<String, Void, List<IconFontInfo>> {
        @Override
        protected void onPreExecute() {
            IconFontInfo.clear();
        }

        @Override
        protected List<IconFontInfo> doInBackground(final String... path) {
            try {
                InputStream open = getResources().getAssets().open(path[0]);
                String html = Utils.getStringFromInputStream(open);
                Document doc = Jsoup.parse(html);
                Elements tag_li_list = doc.select("div.main").get(0).getElementsByTag("li");
                for (Element element : tag_li_list) {
                    String[] allName = element.child(1).text().split("_");
                    if (allName.length == 3) {
                        String code4Show = element.child(0).text();
                        String code4XML = element.child(2).text();
                        IconFontInfo.add(String.format("%s_%s", allName[0], allName[1]), allName[2], code4Show, code4XML);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return IconFontInfo.getList();
        }

        @Override
        protected void onPostExecute(List<IconFontInfo> data) {
            if (data != null && !data.isEmpty()) {
                mAdapter.refreshList(false, data);
                new TranslateTask().execute();
            }
        }
    }

    /**
     * 翻译
     */
    private class TranslateTask extends AsyncTask<Void, Void, Boolean> {

        List<IconFontInfo> eroList = new ArrayList<>();

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (!IconFontInfo.types.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                Set<Map.Entry<String, List<IconFontInfo>>> entries = IconFontInfo.types.entrySet();
                for (Map.Entry<String, List<IconFontInfo>> entry : entries) {
                    String type = entry.getKey();
                    if (!TextUtils.isEmpty(type)) {
                        sb.append(String.format("<!-- %s -->\n", type));
                        List<IconFontInfo> fontInfos = entry.getValue();
                        for (IconFontInfo fontInfo : fontInfos) {
                            String allName4XML = TranslateUtil.translate("zh", "en", fontInfo.name);
                            if (!TextUtils.isEmpty(allName4XML)) {
                                allName4XML = getHumpText(allName4XML);
                                sb.append(String.format("<string name=\"%s_%s\">%s</string>", type, allName4XML, fontInfo.code4XML)).append("\n");
                            } else {
                                Log.i("javen", fontInfo.name + "翻译失败");
                            }
                        }
                        sb.append(String.format("\n\n", type));
                    }

                }

                if (sb.length() > 0) {
                    //  TODO 将sb写入文件  
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean isOK) {
            if (isOK) {
                btn_share.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 转驼峰
         *
         * @param text
         * @return
         */
        public String getHumpText(String text) {
            StringBuilder sb = new StringBuilder();
            String[] split = text.split(" ");
            for (String s : split) {
                if (!Character.isUpperCase(s.charAt(0))) {
                    s = new StringBuilder().append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
                }else {
                    s = new StringBuilder().append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
                }
                sb.append(s);
            }
            return sb.toString();
        }
    }


}
