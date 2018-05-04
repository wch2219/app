package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.ShopCatListBean;
import com.xxc.shoppingmall.network.NetConstant;

import java.util.List;

public class AffOrderAdapter extends BaseAdapter {
    private Context ctx;
    private List<ShopCatListBean> catListBeans;

    public AffOrderAdapter(Context ctx, List<ShopCatListBean> catListBeans) {
        this.ctx = ctx;
        this.catListBeans = catListBeans;

    }

    @Override
    public int getCount() {
        return catListBeans.size();
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_afforder, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        ShopCatListBean catListBean = catListBeans.get(position);
        Glide.with(ctx).load(NetConstant.IMGAE_PATH+catListBean.getImgUrl()).apply(new RequestOptions().centerCrop()).into(vh.iv_pic);
        vh.tv_price.setText("¥ "+catListBean.getPrice());
        vh.tv_shopname.setText(catListBean.getName());
        vh.tv_num.setText("x"+catListBean.getNum());
        return convertView;
    }

    private class ViewHolder {
        public View rootView;
        public ImageView iv_pic;
        public TextView tv_shopname;
        public TextView tv_price;
        public TextView tv_num;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_pic = (ImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_shopname = (TextView) rootView.findViewById(R.id.tv_shopname);
            this.tv_price = (TextView) rootView.findViewById(R.id.tv_price);
            this.tv_num = (TextView) rootView.findViewById(R.id.tv_num);
        }

    }
}
