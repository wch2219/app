package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.king.base.util.LogUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.ProductBannerBean;
import com.xxc.shoppingmall.network.NetConstant;

import java.util.List;

public class ProductDescInfoAdapter extends BaseAdapter {
    private List<ProductBannerBean.DataBean> bannerBeans;
    private Context ctx;

    public ProductDescInfoAdapter(Context ctx, List<ProductBannerBean.DataBean> bannerBeans) {
        this.ctx = ctx;
        this.bannerBeans = bannerBeans;
    }

    @Override
    public int getCount() {
        return bannerBeans.size();
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_desc_pic, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        LogUtils.d(width+"宽");
        ViewGroup.LayoutParams layoutParams = vh.iv_pic.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = (int) (width/1.5);
        vh.iv_pic.setLayoutParams(layoutParams);
        ProductBannerBean.DataBean dataBean = bannerBeans.get(position);
        Glide.with(ctx).load(NetConstant.IMGAE_PATH+dataBean.getDatasource()+"/"+dataBean.getAttachment()).into(vh.iv_pic);
        return convertView;
    }

    private class ViewHolder {
        public View rootView;
        public ImageView iv_pic;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_pic = (ImageView) rootView.findViewById(R.id.iv_pic);
        }

    }
}
