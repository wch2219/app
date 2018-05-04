package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.Address;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/24.
 * 添加地址界面
 */
public class AddAddressActivity extends AbstractPermissionActivity {

    public static final String ADDRESS_KEY = "address";

    @BindView(R.id.add_address_et_receiver)
    EditText mAddAddressEtReceiver;
    @BindView(R.id.add_address_et_phone)
    EditText mAddAddressEtPhone;
    @BindView(R.id.add_address_details)
    EditText mAddAddressDetails;
    @BindView(R.id.add_address_cb_default)
    CheckBox mAddAddressCbDefault;
    @BindView(R.id.add_address_confirm)
    Button mAddAddressConfirm;

    private Address.DataBean mAddress;

    @Override
    public void initUIWithPermission() {
        mAddress = (Address.DataBean) getIntent().getSerializableExtra(ADDRESS_KEY);
        if (null != mAddress) {
            mAddAddressEtReceiver.setText(mAddress.getName());
            mAddAddressEtPhone.setText(mAddress.getMobile());
            mAddAddressDetails.setText(mAddress.getUserAddress());
            mAddAddressCbDefault.setChecked(mAddress.getIsDefault() == 1);
        }
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_add_address;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.add_address_confirm)
    public void onViewClicked() {
        String receiver = mAddAddressEtReceiver.getText().toString().trim();
        String phone = mAddAddressEtPhone.getText().toString().trim();
        String addressDetail = mAddAddressDetails.getText().toString().trim();
        if (!TextUtils.isEmpty(receiver) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(addressDetail)) {
            updateAddress(receiver, phone, addressDetail);
        } else {
            ToastUtils.showToast(this, "请将相关信息填写完整后再提交");
        }
    }

    private void updateAddress(String receiver, String phone, String address) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.USER_ADDRESS, address);
        params.put(ParamKey.IS_DEFAULT, mAddAddressCbDefault.isChecked() ? 1 : 0);
        params.put(ParamKey.MOBILE, phone);
        params.put(ParamKey.NAME, receiver);
        if (null != mAddress) {
            params.put("id", mAddress.getId());
        }
        Call<SubmitOrder> call = mHttpApi.updateAddress(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    ToastUtils.showToast(AddAddressActivity.this, "已提交");
                    postDelayed(1200, new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = getIntent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    ToastUtils.showToast(AddAddressActivity.this, null == model ? "请求失败" : model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }
}
