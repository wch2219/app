package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.ShopCatListBean;
import com.xxc.shoppingmall.network.NetConstant;

import java.util.List;

public class ShopCatAdapter extends BaseAdapter {
    private Context ctx;
    private List<ShopCatListBean> catListBeans;

    public ShopCatAdapter(Context ctx, List<ShopCatListBean> catListBeans) {
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
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_shopcat, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        final ShopCatListBean catListBean = catListBeans.get(position);
        Glide.with(ctx).load(NetConstant.IMGAE_PATH+catListBean.getImgUrl()).apply(new RequestOptions().centerCrop()).into(vh.iv_pic);
        vh.tv_name.setText(catListBean.getName());
        vh.tv_price.setText("¥ "+catListBean.getPrice());
        vh.tv_num.setText(""+catListBean.getNum());
        vh.cb_check.setChecked(catListBean.isCheck());
        vh.btn_jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numChangeListener.jiaChange(catListBean.getShopid(),catListBean.getNum());
            }
        });
        vh.btn_jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numChangeListener.jianChange(catListBean.getShopid(),catListBean.getNum());
            }
        });

        return convertView;
    }

   private class ViewHolder {
        public View rootView;
        public CheckBox cb_check;
        public ImageView iv_pic;
        public TextView tv_name;
        public TextView tv_price;
        public TextView tv_num;
        public Button btn_jia;
        public Button btn_jian;
        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.cb_check = (CheckBox) rootView.findViewById(R.id.cb_check);
            this.iv_pic = (ImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.tv_price = (TextView) rootView.findViewById(R.id.tv_price);
            this.tv_num = (TextView) rootView.findViewById(R.id.tv_num);
            this.btn_jia = (Button) rootView.findViewById(R.id.btn_jia);
            this.btn_jian = (Button) rootView.findViewById(R.id.btn_jian);
        }

    }
    public interface NumChangeListener{
        void jiaChange(int shopid,int num);
        void jianChange(int shopid,int num);
    }
    private NumChangeListener numChangeListener;
    public void setNumChangeListener(NumChangeListener numChangeListener){
        this.numChangeListener = numChangeListener;
    }
}
