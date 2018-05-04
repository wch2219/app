package com.xxc.shoppingmall.ui;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
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
 * Created by xuxingchen on 2017/11/25.
 * 修改交易密码界面
 */
public class ResetTradePwdActivity extends AbstractPermissionActivity {
    @BindView(R.id.reset_trade_pwd_new)
    EditText mResetTradePwdNew;
    @BindView(R.id.reset_trade_pwd_confirm)
    EditText mResetTradePwdConfirm;
    @BindView(R.id.reset_trade_pwd_confirm_submit)
    Button mResetTradePwdConfirmSubmit;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_reset_trade_pwd;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.reset_trade_pwd_confirm_submit)
    public void onViewClicked() {
        String pwd = mResetTradePwdNew.getText().toString().trim();
        String confirmPwd = mResetTradePwdConfirm.getText().toString().trim();
        if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(confirmPwd)) {
            if (pwd.equalsIgnoreCase(confirmPwd)) {
                LoginResult info = ShoppingMallApp.getInstance().getUser();
                Map<String, Object> params = new HashMap<>();
                params.put(ParamKey.USERID, info.getData().getUserId());
                params.put(ParamKey.TRANSACTION_PASSWORD, pwd);
                updatePassword(params);
            } else {
                ToastUtils.showToast(this, "两次输入的密码需一致才能继续提交");
            }
        } else {
            ToastUtils.showToast(this, "请填写新的密码并确认");
        }
    }

    public void updatePassword(Map<String, Object> params) {
        Call<SubmitOrder> call = mHttpApi.submitPersonInfo(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess()) {
                    ToastUtils.showToast(ResetTradePwdActivity.this, "修改成功");
                    postDelayed(600, new Runnable() {
                        @Override
                        public void run() {
                            postDelayed(600, new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            });
                        }
                    });
                } else {
                    ToastUtils.showToast(ResetTradePwdActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("提交中。。。");
        requestApi(call, callback);
    }
}
