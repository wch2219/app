package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.pay.PayFragment;
import com.xxc.shoppingmall.ui.pay.PayPwdView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2018/1/23.
 */

public class DigMoneyActivity extends AbstractPermissionActivity implements PayPwdView
        .InputCallBack {

    @BindView(R.id.dig_money_num)
    EditText mDigMoneyNum;
    @BindView(R.id.dig_money_address)
    EditText mDigMoneyAddress;
    @BindView(R.id.dig_money_now)
    Button mDigMoneyNow;

    private int myDigMoney;

    @Override
    public void initUIWithPermission() {

    }
    @Override
    public int layoutRes() {
        return R.layout.layout_dig_money;
    }
    @Override
    public void initData() {
        UserInfo info = ShoppingMallApp.getInstance().getUserInfo();
        if (null != info) {
            myDigMoney = (int) info.getData().getGsc_num();
        } else {
            ToastUtils.showToast(this, "正在获取您的提币信息");
            Callback<UserInfo> infoCallback = new EasyCallBack<UserInfo>() {
                @Override
                public void onSuccess(Call<UserInfo> call, UserInfo model) {
                    if (model != null) {
                        if (model.getMsg().isSuccess()) {
                            myDigMoney = (int) model.getData().getGsc_num();
                            ShoppingMallApp.getInstance().setUserInfo(model);
                        }
                    }
                }
            };
            getPersonInfo(infoCallback);
        }
    }

    @Override
    public void addListeners() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputContent = s.toString().trim();
                if (!TextUtils.isEmpty(inputContent)) {
                    int value = Integer.valueOf(inputContent);
                    if (value > myDigMoney) {
                        mDigMoneyNum.setText(String.valueOf(myDigMoney));
                    }
                }
            }
        };
        mDigMoneyNum.addTextChangedListener(watcher);
    }
    @OnClick(R.id.dig_money_now)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putString(PayFragment.EXTRA_CONTENT, "提币：¥ " + mDigMoneyNum
                .getText().toString());
        PayFragment  mPayFragment = new PayFragment();
        mPayFragment.setArguments(bundle);
        mPayFragment.setPaySuccessCallBack(DigMoneyActivity.this);
        mPayFragment.show(getSupportFragmentManager(), "Pay");

    }
    @Override
    public void onInputFinish(String result) {
        String mAddress = mDigMoneyAddress.getText().toString().trim();
        String numString = mDigMoneyNum.getText().toString().trim();
        if (!TextUtils.isEmpty(numString) && !TextUtils.isEmpty(mAddress)) {

            final int num = Integer.valueOf(numString);
            String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
            Map<String, Object> params = new HashMap<>();
            params.put(ParamKey.USERID, userId);
            params.put(ParamKey.AMOUNT, num);
            params.put(ParamKey.TRANSACTION_PASSWORD, result);
            params.put(ParamKey.INTEGRATION_NUM, numString);
            params.put(ParamKey.MONEY_ADDRESS, mAddress);
            Call<SubmitOrder> call = mHttpApi.submitDigMoney(params);
            Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
                @Override
                public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                    if (null != model && model.getMsg().isSuccess()) {
                        Callback<UserInfo> userInfoCallback = new EasyCallBack<UserInfo>() {
                            @Override
                            public void onSuccess(Call<UserInfo> call, UserInfo model) {
                                dismissLoading();
                                if (model != null) {
                                    if (model.getMsg().isSuccess()) {
                                        ShoppingMallApp.getInstance().setUserInfo(model);
                                        ToastUtils.showToast(DigMoneyActivity.this, "提交成功");
                                        postDelayed(800, new Runnable() {
                                            @Override
                                            public void run() {
                                                setResult(Activity.RESULT_OK);
                                                finish();
                                            }
                                        });
                                    }
                                }
                            }
                        };
                        getPersonInfo(userInfoCallback);
                    } else {
                        dismissLoading();
                        String msg = model.getMsg().getInfo();
                        ToastUtils.showToast(DigMoneyActivity.this, msg);
                    }
                }
            };
            showLoadingDialog("正在提交提币申请");
            requestApi(call, callback);
        } else {
            ToastUtils.showToast(this, "请输入提币信息,然后提交!");
        }
    }
}
