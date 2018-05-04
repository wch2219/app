package com.xxc.shoppingmall.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.king.base.util.LogUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.widget.OvalImageView;

import java.util.List;

/**
 * 商品列表
 *
 * @param
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context ctx;
    private List<ProductListBean.DataBean> data;

    public ProductAdapter(@Nullable List<ProductListBean.DataBean> data, Context ctx) {
        this.ctx = ctx;
        this.data = data;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, final int position) {
        ProductListBean.DataBean dataBean = data.get(position);
        Glide.with(ctx).load("http://122.114.5.231:8180/api/showImg/"+dataBean.getImgUrl()).apply(new RequestOptions().centerCrop()).into(holder.iv_pic);
        holder.tv_productname.setText(dataBean.getName());
        holder.tv_productname1.setText(dataBean.getDescription());
        holder.tv_productprice.setText("¥ "+dataBean.getPrice());
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {

                    onItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public View rootView;
        public OvalImageView iv_pic;
        public TextView tv_productname;
        public TextView tv_productname1;
        public TextView tv_productprice;

        public ViewHolder(View rootView) {
            super(rootView);
            this.rootView = rootView;
            this.iv_pic = (OvalImageView) rootView.findViewById(R.id.iv_pic);
            this.tv_productname = (TextView) rootView.findViewById(R.id.tv_productname);
            this.tv_productname1 = (TextView) rootView.findViewById(R.id.tv_productname1);
            this.tv_productprice = (TextView) rootView.findViewById(R.id.tv_productprice);
        }

    }
    public interface OnItemClickListener{
        void onClick(int postion);
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }
}
