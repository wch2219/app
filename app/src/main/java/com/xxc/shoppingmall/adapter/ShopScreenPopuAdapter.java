package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxc.shoppingmall.R;

public class ShopScreenPopuAdapter extends BaseAdapter {
    private Context ctx;
    private String[] starname = {"精选箱包", "家居百货", "服装配饰", "视频酒水", "美妆个护", "母婴用品", "汽车用品", "家用电器", "数码办公", "运动户外"};
    private int[] pics = {R.mipmap.xxb, R.mipmap.jjbh, R.mipmap.fzps, R.mipmap.jsyl, R.mipmap.mzgh, R.mipmap.myyp, R.mipmap.icon_cat, R.mipmap.jydq, R.mipmap.smbg, R.mipmap.hwyd};

    public ShopScreenPopuAdapter(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return starname.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_popu_screen, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        vh.iv_pic.setImageResource(pics[position]);
        vh.tv_name.setText(starname[position]);
        return convertView;
    }

    private class ViewHolder {
        public View rootView;
        public ImageView iv_pic;
        public TextView tv_name;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_pic = (ImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
        }

    }
}
