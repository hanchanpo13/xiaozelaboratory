package com.test.xiaozeze.xiaozelaboratory.page;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.test.xiaozeze.xiaozelaboratory.R;
import com.test.xiaozeze.xiaozelaboratory.uiBase.XZBaseListAdapter;
import com.test.xiaozeze.xiaozelaboratory.utils.ShareUtils;
import com.test.xiaozeze.xiaozelaboratory.utils.TranslateUtil;
import com.test.xiaozeze.xiaozelaboratory.utils.Utils;
import com.xiaozeze.xziconfont.XZIconFontView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Page0_IconFontActivity extends AppCompatActivity {

    private GridView mFontList;
    private Button btn_share;
    private FontAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page0);
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
        new ParseHTMLTask().execute("demo_unicode.html");
    }


    class FontAdapter extends XZBaseListAdapter<IconFontInfo> {

        public FontAdapter(Context context) {
            super(context);
        }

        @Override
        protected XZBaseListViewHolder getHolder(int pos, ViewGroup parent) {
            XZBaseListViewHolder viewHolder = new XZBaseListViewHolder(new XZIconFontView(parent.getContext())) {
                private XZIconFontView mFontView;

                @Override
                protected void initView(View convertView) {
                    mFontView = (XZIconFontView) convertView;
                    mFontView.setIconColorInt(Color.BLACK);
                    mFontView.setIconSize(50);
                }

                @Override
                public void setItemData(int pos) {
                    IconFontInfo item = getItem(pos);
                    mFontView.setIcon(item.code4Show);
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
                    if (allName.length == 2) {
                        String code4Show = element.child(0).text();
                        String code4XML = element.child(2).text();
                        IconFontInfo.add(allName[0], allName[1], code4Show, code4XML);
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

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (!IconFontInfo.types.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                Set<Map.Entry<String, List<IconFontInfo>>> entries = IconFontInfo.types.entrySet();
                for (Map.Entry<String, List<IconFontInfo>> entry : entries) {
//                    String type = TranslateUtil.translate("zh", "en", entry.getKey());
                    String type = entry.getKey();
                    List<IconFontInfo> fontInfos = entry.getValue();
                    for (IconFontInfo fontInfo : fontInfos) {
                        String allName4XML = TranslateUtil.translate("zh", "en", fontInfo.name);
                        sb.append(String.format("<string name=\"%s_%s\">%s</string>", type, allName4XML, fontInfo.code4XML)).append("\n");
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
    }


    // 数据处理
    static class IconFontInfo {

        static Map<String, List<IconFontInfo>> types = new HashMap<>();

        static void add(String type, String name, String code4Show, String code4XML) {
            IconFontInfo info = new IconFontInfo(type, code4Show, name, code4XML);
            if (!types.containsKey(type)) {
                types.put(type, new ArrayList<IconFontInfo>());
            }
            List<IconFontInfo> iconFontInfos = types.get(type);
            iconFontInfos.add(info);
        }

        static List<IconFontInfo> getList() {
            List<IconFontInfo> list = new ArrayList<>();
            Set<Map.Entry<String, List<IconFontInfo>>> entries = types.entrySet();
            for (Map.Entry<String, List<IconFontInfo>> entry : entries) {
                List<IconFontInfo> infoList = entry.getValue();
                list.addAll(infoList);
            }
            return list;
        }

        public static void clear() {
            types.clear();
        }

//        _________________________________________________________________________

        String type;
        String name;
        String code4Show;
        String code4XML;

        public IconFontInfo(String type, String name, String code4Show, String code4XML) {
            this.type = type;
            this.name = name;
            this.code4Show = code4Show;
            this.code4XML = code4XML;
        }
    }
}
