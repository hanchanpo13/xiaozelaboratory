package com.test.xiaozeze.xiaozelaboratory.page;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.test.xiaozeze.xiaozelaboratory.R;
import com.test.xiaozeze.xiaozelaboratory.uiBase.XZBaseListAdapter;
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
    private FontAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page0);
        mFontList = findViewById(R.id.icon_font_list);
        mAdapter = new FontAdapter(this);
        mFontList.setAdapter(mAdapter);
        new ParseHTMLTask().execute("demo_unicode.html");
    }


    class FontAdapter extends XZBaseListAdapter<String> {

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
                    String item = getItem(pos);
                    mFontView.setIcon(item);
                }
            };
            return viewHolder;
        }

    }


    /**
     * 解析HTML
     */
    private class ParseHTMLTask extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(final String... path) {
            List<String> data = new ArrayList<>();
            try {
                InputStream open = getResources().getAssets().open(path[0]);
                String html = Utils.getStringFromInputStream(open);
                Document doc = Jsoup.parse(html);
                Elements tag_li_list = doc.select("div.main").get(0).getElementsByTag("li");
                for (Element element : tag_li_list) {
                    String code4Show = element.child(0).text();
                    String name = element.child(1).text();
                    String code4XML = element.child(2).text();
                    
                    // 
                    data.add(code4Show);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(List<String> data) {
            if (data != null && !data.isEmpty()) {
                mAdapter.refreshList(false, data);
            }
        }
    }

    class IconFontProJect {

        Map<String, List<IconFontInfo>> types = new HashMap<>();

        class IconFontInfo {
            String type;
            String code4Show;
            String name;
            String code4XML;

            public IconFontInfo(String type, String code4Show, String name, String code4XML) {
                this.type = type;
                this.code4Show = code4Show;
                this.name = name;
                this.code4XML = code4XML;
            }
        }

        void add(String type,String code4Show, String name, String code4XML) {
            IconFontInfo info = new IconFontInfo(type, code4Show, name, code4XML);
            if(!types.containsKey(type)){
                types.put(type, new ArrayList<IconFontInfo>());
            }
            List<IconFontInfo> iconFontInfos = types.get(type);
            iconFontInfos.add(info);
        }

        List<IconFontInfo> getList() {
            List<IconFontInfo> list = new ArrayList<>();
            Set<Map.Entry<String, List<IconFontInfo>>> entries = types.entrySet();
            for (Map.Entry<String, List<IconFontInfo>> entry : entries) {
                List<IconFontInfo> infoList = entry.getValue();
                list.addAll(infoList);
            }
            return list;
        }
    }
}
