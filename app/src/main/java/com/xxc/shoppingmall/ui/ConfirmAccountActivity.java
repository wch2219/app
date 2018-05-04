package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.AccountBank;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/18.
 * 重置密码前的确认账号银行卡界面
 */
public class ConfirmAccountActivity extends AbstractPermissionActivity {
    @BindView(R.id.confirm_account)
    EditText mConfirmAccount;
    @BindView(R.id.reset_phone)
    EditText mConfiemBankNum;
    @BindView(R.id.reset_btn_confirm)
    Button mResetBtnConfirm;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_confirm_account;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }


    @OnClick(R.id.reset_btn_confirm)
    public void onViewClicked() {
        String phone = mConfirmAccount.getText().toString().trim();
        String bankNum = mConfiemBankNum.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(bankNum)) {
            checkPhoneAndBank(phone, bankNum);
        }
    }

    private void checkPhoneAndBank(String phone, String bankNum) {
        Call<AccountBank> call = mHttpApi.checkAccountBank(phone, bankNum);
        Callback<AccountBank> callback = new EasyCallBack<AccountBank>() {
            @Override
            public void onSuccess(Call<AccountBank> call, AccountBank model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess() && model.isData()) {
                    Intent resetPwd = new Intent(ConfirmAccountActivity.this, ResetPwdActivity
                            .class);
                    startActivity(resetPwd);
                    finish();
                } else {
                    ToastUtils.showToast(ConfirmAccountActivity.this, null == model ? "请求失败" :
                            model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("检测中...");
        requestApi(call, callback);
    }


}
