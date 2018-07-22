package com.test.xiaozeze.xiaozelaboratory.page;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test.xiaozeze.xiaozelaboratory.domian.PageInfo;

import java.util.List;

public class MainActivity extends ListActivity {


    private List<PageInfo> mPageDispatchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageDispatchList = PageInfo.getPageDispatchList();
        setListAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mPageDispatchList.size();
            }

            @Override
            public PageInfo getItem(int position) {
                return mPageDispatchList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView;
                if (convertView == null && !(convertView instanceof TextView)) {
                    textView = new TextView(parent.getContext());
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
                textView = (TextView) convertView;

                PageInfo item = getItem(position);
                textView.setText(item.getPageName());
                return convertView;
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        PageInfo item = (PageInfo) getListAdapter().getItem(position);
        item.startActivity(this);
    }
}
