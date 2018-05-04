package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.MerchAccBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/25 0025.
 */

public class MerchAccAdapter extends BaseAdapter {
    private Context ctx;
    private List<MerchAccBean.DataBean> data;

    public MerchAccAdapter(Context ctx, List<MerchAccBean.DataBean> data) {
        super();
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    private int checkpos = -1;
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.item_mechacc, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        }else{
            vh = (ViewHolder) view.getTag();
        }
        MerchAccBean.DataBean dataBean = data.get(i);
        if (i == checkpos) {
            vh.cb_check.setChecked(true);
        }else{
            vh.cb_check.setChecked(false);
        }
        String start = dataBean.getBankNum().substring(0, 3);
        String end = dataBean.getBankNum().substring(dataBean.getBankNum().length() - 4);
        vh.tv_acc.setText(dataBean.getOwner()+"\t\t"+start+"***"+end);
        return view;
    }

    public void setcheckPos(int i) {
    this.checkpos = i;
    notifyDataSetChanged();
    }

    private class ViewHolder {
        public View rootView;
        public CheckBox cb_check;
        public TextView tv_acc;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.cb_check = (CheckBox) rootView.findViewById(R.id.cb_check);
            this.tv_acc = (TextView) rootView.findViewById(R.id.tv_acc);
        }

    }
}
