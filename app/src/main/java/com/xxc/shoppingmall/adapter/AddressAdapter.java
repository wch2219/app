package com.xxc.shoppingmall.adapter;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.Address;
import com.xxc.shoppingmall.ui.ManageAddressActivity;

import java.util.List;

/**
 * Created by xuxingchen on 2017/11/24.
 */

public class AddressAdapter extends BaseQuickAdapter<Address.DataBean, AddressAdapter.AddressViewHolder> {

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener;
    private ManageAddressActivity mActivity;

    public AddressAdapter(@Nullable List<Address.DataBean> data, ManageAddressActivity context) {
        super(R.layout.manage_address_item, data);
        mActivity = context;
    }

    @Override
    protected void convert(AddressViewHolder helper, final Address.DataBean item) {
        helper.setText(R.id.address_receiver_name, item.getName()).setText(R.id.address_receiver_phone, item
                .getMobile()).setText(R.id.address_tv_detail, item.getUserAddress()).addOnClickListener(R.id
                .address_edit_container).addOnClickListener(R.id.address_delete_container).addOnClickListener(R.id
                .address_default_container);
        if (item.getIsDefault() == 1) {
            helper.setTextColor(R.id.address_default_text, mActivity.getResources().getColor(R.color.color_ff4040))
                    .setImageResource(R.id.address_default_icon, R.drawable.icon_xuanzhong);
        } else {
            helper.setTextColor(R.id.address_default_text, mActivity.getResources().getColor(R.color.black_000))
                    .setImageResource(R.id.address_default_icon, R.drawable.icon_moren);
        }
    }


    static class AddressViewHolder extends BaseViewHolder {
        public AddressViewHolder(View view) {
            super(view);
        }
    }
}
