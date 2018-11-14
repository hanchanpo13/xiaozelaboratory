package com.xiaozeze.demo.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.test.xiaozeze.xiaozelaboratory.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @Description: 适配器基类
 * @Author: fengzeyuan
 * @Date: 17/5/4 上午10:55
 * @Version: 1.0
 */
abstract public class XZBaseListAdapter<D> extends BaseAdapter {
    private final LayoutInflater mLayoutInflater;
    private List<D> mList = new ArrayList<>();

    public XZBaseListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public D getItem(int position) {
        if (position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        XZBaseListViewHolder holder;
        if (convertView == null) {
            convertView = getHolder(position, parent).getConvertView();
        }
        Object tag = convertView.getTag(R.string.tag_key);
        holder = (XZBaseListViewHolder) tag;
        holder.setItemData(position);
        return convertView;
    }

    protected abstract XZBaseListViewHolder getHolder(int pos, ViewGroup parent);

    public void refreshList(boolean isAppend, Collection<D> orderInvoice) {
        if (!isAppend) {
            mList.clear();
        }
        mList.addAll(orderInvoice);
        notifyDataSetChanged();
    }

    abstract public class XZBaseListViewHolder {

        private final View convertView;

        public XZBaseListViewHolder(int layoutID, ViewGroup parent) {
            convertView = mLayoutInflater.inflate(layoutID, parent, false);
            convertView.setTag(R.string.tag_key, this);
            initView(convertView);
        }

        public XZBaseListViewHolder(View rootView) {
            convertView = rootView;
            convertView.setTag(R.string.tag_key, this);
            initView(convertView);
        }

        abstract protected void initView(View convertView);

        abstract public void setItemData(int pos);

        private View getConvertView() {
            return convertView;
        }
    }
}
