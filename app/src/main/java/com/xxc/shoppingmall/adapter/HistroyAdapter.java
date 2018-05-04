package com.xxc.shoppingmall.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.HistroyData;
import com.xxc.shoppingmall.utils.TimeUtils;

import java.util.List;

/**
 * Created by xuxingchen on 2018/1/23.
 */

public class HistroyAdapter<M extends HistroyData> extends BaseQuickAdapter<M, HistroyAdapter
        .HistroyViewHolder> {

    public static final int TYPE_PAY_HISTORY = 1;
    public static final int TYPE_POINTS_HISTORY = 2;
    public static final int TYPE_SHIELD_HISTORY = 3;

    private int type;

    public HistroyAdapter(int type, @Nullable List<M> data) {
        super(R.layout.histroy_list_item, data);
        this.type = type;
    }

    @Override
    protected void convert(HistroyViewHolder helper, M item) {
        switch (type) {
            case TYPE_PAY_HISTORY:
                helper.setText(R.id.histroy_item_time, TimeUtils.getFormatDay(item.getCreateTime()))
                        .setText(R.id.histroy_item_money, String.valueOf(item.getMoney()));
                if (HistroyData.REJECT_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "被驳回:" + item.getComments());
                } else if (HistroyData.UNCHECKED_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "未审核");
                } else if (HistroyData.PASS_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "审核通过");
                } else if (HistroyData.AWARD_CODE == item.getStatus()) {
//                    helper.setText(R.id.histroy_item_status, "已奖励");
                    helper.setText(R.id.histroy_item_status, "审核通过");
                } else {
                    helper.setText(R.id.histroy_item_status, "状态异常:" + item.getStatus());
                }
                break;
            case TYPE_POINTS_HISTORY:
                helper.setText(R.id.histroy_item_time, TimeUtils.getFormatDay(item.getCreateTime()))
                        .setText(R.id.histroy_item_money, String.valueOf(item.getMoney()));
                if (HistroyData.REJECT_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "被驳回:" + item.getComments());
                } else if (HistroyData.UNCHECKED_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "未处理");
                } else if (HistroyData.PASS_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "已到账");
                } else {
                    helper.setText(R.id.histroy_item_status, "状态异常:" + item.getStatus());
                }
                break;
            case TYPE_SHIELD_HISTORY:
                helper.setText(R.id.histroy_item_time, TimeUtils.getFormatDay(item.getCreateTime()))
                        .setText(R.id.histroy_item_money, String.valueOf(item.getMoney()));
                if (HistroyData.REJECT_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "被驳回:" + item.getComments());
                } else if (HistroyData.UNCHECKED_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "未处理");
                } else if (HistroyData.PASS_CODE == item.getStatus()) {
                    helper.setText(R.id.histroy_item_status, "已到账");
                } else {
                    helper.setText(R.id.histroy_item_status, "状态异常:" + item.getStatus());
                }
                break;
        }
    }

    public static class HistroyViewHolder extends BaseViewHolder {

        public HistroyViewHolder(View view) {
            super(view);
        }
    }
}
