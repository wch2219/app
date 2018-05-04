package com.xxc.shoppingmall.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.OrderBean;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.utils.GlideExtra;
import com.xxc.shoppingmall.utils.ImageLoader;

import java.util.List;

/**
 * Created by xuxingchen on 2017/11/25.
 * 订单页面适配器
 */
public class OrderListAdapter extends BaseMultiItemQuickAdapter<OrderBean.DataBean, OrderListAdapter
        .OrderViewHolder> {

    private static final String NUM_PRICE_FORMAT = "共%s件商品 合记:¥%.2f";
    private Activity mActivity;
    private GlideExtra mGlideExtra;
    private int[] imageview = new int[]{R.id.order_item_img, R.id.order_item_img2, R.id
            .order_item_img3};

    public OrderListAdapter(@Nullable List<OrderBean.DataBean> data, Activity activity) {
        super(data);
        addItemType(OrderBean.ORDER_ONE, R.layout.order_list_item);
        addItemType(OrderBean.ORDER_TWO, R.layout.order_list_item_type_two);
        mActivity = activity;
        mGlideExtra = new GlideExtra();
        mGlideExtra.scaleType = GlideExtra.CENTER_CROP;
        mGlideExtra.showCircle = GlideExtra.NO_CIRCLE;
        mGlideExtra.placeHolderRes = R.drawable.img_morenshangpin;
    }

    @Override
    protected void convert(OrderViewHolder helper, OrderBean.DataBean item) {
//        helper.setText(R.id.order_item_des,item.get)

        switch (helper.getItemViewType()) {
            case OrderBean.ORDER_ONE:
                helper.setText(R.id.order_item_des, item.getProducts().get(0).getDescription())
                        .setText(R.id.order_item_price, String.valueOf(item.getIntegrationNum()))
                        .setText(R.id.order_item_goods_money, String.format(NUM_PRICE_FORMAT, item
                                .getProducts().get(0).getNum(), Float.valueOf(item
                                .getIntegrationNum())));

                ImageLoader.loadImage(mActivity, (ImageView) helper.getView(R.id.order_item_img),
                        NetConstant.IMGAE_PATH + item.getProducts().get(0).getImgUrl(),
                        mGlideExtra);
                if (item.getStatus() == 1) {
                    helper.setText(R.id.order_item_confirm_goods, "确认收货");
                    helper.addOnClickListener(R.id.order_item_confirm_goods);
                } else {
                    helper.setText(R.id.order_item_confirm_goods, "已完成");
                }
                break;
            case OrderBean.ORDER_TWO:
                helper.setText(R.id.order_item_goods_money, String.format(NUM_PRICE_FORMAT, item
                        .getProducts().size(), Float.valueOf(item.getIntegrationNum())));
                for (int i = 0; i < 3; i++) {
                    if (i < item.getProducts().size()) {
                        ImageLoader.loadImage(mActivity, (ImageView) helper.getView(imageview[i]),
                                NetConstant.IMGAE_PATH + item.getProducts().get(i).getImgUrl(),
                                mGlideExtra);
                    } else {
                        helper.getView(imageview[i]).setVisibility(View.INVISIBLE);
                    }
                }
                if (item.getStatus() == 1) {
                    helper.setText(R.id.order_item_confirm_goods, "确认收货");
                    helper.addOnClickListener(R.id.order_item_confirm_goods);
                } else {
                    helper.setText(R.id.order_item_confirm_goods, "已完成");
                }
                break;
        }
    }

    static class OrderViewHolder extends BaseViewHolder {
        public OrderViewHolder(View view) {
            super(view);
        }
    }
}
