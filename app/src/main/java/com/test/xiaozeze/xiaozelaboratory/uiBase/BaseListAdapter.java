package com.test.xiaozeze.xiaozelaboratory.uiBase;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author: fengzeyuan
 * Date: 2018/7/23 下午2:31
 * Version: 1.0
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {


    private final List<T> mList = new ArrayList<>();

    @Override
    public final int getCount() {
        return mList.size();
    }

    @Override
    public final T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createItem(parent);
        }
        bindData(getItem(position));
        return convertView;
    }

    public void reFreshData(boolean isAppend, List<T> dataList) {
        if (!isAppend) {
            mList.clear();
        }
        mList.addAll(dataList);
        notifyDataSetChanged();
    }

    public abstract View createItem(ViewGroup parent);

    public abstract void bindData(T data);

}
