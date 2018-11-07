package com.test.xiaozeze.homepage;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.xiaozeze.xiaozelaboratory.R;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView mPageListView;
    private PageListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mPageListView = findViewById(R.id.id_page_list);
        mPageListView.setOnItemClickListener(this);
        mAdapter = new PageListAdapter(this);
        mPageListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PageInfo item = mAdapter.getItem(position);
        item.startActivity(this);
    }

    private class PageListAdapter extends XZBaseListAdapter<PageInfo> {


        public PageListAdapter(Context cx) {
            super(cx);
            refreshList(false, PageInfo.getPageDispatchList());
        }


        @Override
        protected XZBaseListViewHolder getHolder(int pos, ViewGroup parent) {
            return new XZBaseListViewHolder(R.layout.item_tv, parent) {
                private TextView mTv;

                @Override
                protected void initView(View convertView) {
                    mTv = convertView.findViewById(R.id.tv_list_item);
                }

                @Override
                public void setItemData(int pos) {
                    PageInfo item = getItem(pos);
                    mTv.setText(item.getPageName());
                }
            };
        }
    }
}
