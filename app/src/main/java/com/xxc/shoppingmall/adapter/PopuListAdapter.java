package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xxc.shoppingmall.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class PopuListAdapter extends BaseAdapter {
    private Context ctx;
    private List<String> source;

    public PopuListAdapter(Context ctx, List<String> sources) {
        this.ctx = ctx;
        this.source = sources;
    }

    @Override
    public int getCount() {
        return source.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.text_list, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
        vh = (ViewHolder) view.getTag();
        }
        vh.text_list.setText(source.get(i));
        return view;
    }

    private class ViewHolder {
        public View rootView;
        public TextView text_list;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.text_list = (TextView) rootView.findViewById(R.id.text_list);
        }

    }
}
