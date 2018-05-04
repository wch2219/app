package com.xxc.shoppingmall.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.BankCard;
import com.xxc.shoppingmall.ui.ManageBankActivity;

import java.util.List;

/**
 * Created by xuxingchen on 2017/11/24.
 */

public class BankCardAdapter extends BaseQuickAdapter<BankCard.DataBean, BankCardAdapter.BankCardViewHolder> {

    private ManageBankActivity mActivity;

    public BankCardAdapter(@Nullable List<BankCard.DataBean> data, ManageBankActivity context) {
        super(R.layout.bank_card_item, data);
        mActivity = context;
    }

    @Override
    protected void convert(BankCardViewHolder helper, BankCard.DataBean item) {
        helper.setText(R.id.bank_name, item.getBankName()).setText(R.id.bank_phone, item.getBankNum())
                .addOnClickListener(R.id.bank_edit_container).addOnClickListener(R.id.bank_delete_container)
                .addOnClickListener(R.id.bank_default_container);
        if (item.getIsDefault() == 1) {
            helper.setTextColor(R.id.bank_default_text, mActivity.getResources().getColor(R.color.color_ff4040))
                    .setImageResource(R.id.bank_default_icon, R.drawable.icon_xuanzhong);
        } else {
            helper.setTextColor(R.id.bank_default_text, mActivity.getResources().getColor(R.color.black_000))
                    .setImageResource(R.id.bank_default_icon, R.drawable.icon_moren);
        }
    }

    static class BankCardViewHolder extends BaseViewHolder {

        public BankCardViewHolder(View view) {
            super(view);
        }
    }
}
