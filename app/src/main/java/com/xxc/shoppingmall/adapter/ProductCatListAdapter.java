package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.widget.OvalImageView;

import java.util.List;

public class ProductCatListAdapter extends BaseAdapter {
    private Context ctx;
    private List<ProductListBean.DataBean> data;
    public ProductCatListAdapter(Context ctx, List<ProductListBean.DataBean> data) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public int getCount() {
        return 2;
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_prodesc_pic, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        ProductListBean.DataBean dataBean = data.get(position);
        Glide.with(ctx).load(NetConstant.IMGAE_PATH+dataBean.getImgUrl()).apply(new RequestOptions().centerCrop()).into(vh.iv_pic);
        vh.tv_productname.setText(dataBean.getName());
        vh.tv_productname1.setText(dataBean.getDescription());
        vh.tv_productprice.setText("¥ "+dataBean.getPrice());
        return convertView;
    }

    private class ViewHolder{
        public View rootView;
        public OvalImageView iv_pic;
        public TextView tv_productname;
        public TextView tv_productname1;
        public TextView tv_productprice;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_pic = (OvalImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_productname = (TextView) rootView.findViewById(R.id.tv_productname);
            this.tv_productname1 = (TextView) rootView.findViewById(R.id.tv_productname1);
            this.tv_productprice = (TextView) rootView.findViewById(R.id.tv_productprice);
        }

    }
}
