package com.test.xiaozeze.xiaozelaboratory.page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.xiaozeze.xiaozelaboratory.R;
import com.test.xiaozeze.xiaozelaboratory.domian.PageInfo;
import com.test.xiaozeze.xiaozelaboratory.uiBase.BaseListAdapter;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {



    private PageListAdapter mAdapter = new PageListAdapter();
    private ListView mPageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mPageListView = findViewById(R.id.id_page_list);
        mPageListView.setOnItemClickListener(this);
        mPageListView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PageInfo item = mAdapter.getItem(position);
        item.startActivity(this);
    }

    private class PageListAdapter extends BaseListAdapter<PageInfo> {

        private TextView mTv;

        public PageListAdapter() {
            reFreshData(false, PageInfo.getPageDispatchList());
        }

        @Override
        public View createItem(ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View rootView = layoutInflater.inflate(R.layout.item_tv, parent, false);
            mTv = rootView.findViewById(R.id.tv_list_item);
            return rootView;
        }

        @Override
        public void bindData(PageInfo data) {
            mTv.setText(data.getPageName());
        }
    }
}
