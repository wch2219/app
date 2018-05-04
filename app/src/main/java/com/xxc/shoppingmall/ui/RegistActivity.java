package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.Invite;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/15.
 * 注册界面
 */
public class RegistActivity extends AbstractPermissionActivity implements View.OnClickListener {
    @BindView(R.id.title)
    CommonTitle mTitle;
    @BindView(R.id.regist_yaoqingma)
    EditText mRegistYaoqingma;
    @BindView(R.id.regist_phone)
    EditText mRegistPhone;
    @BindView(R.id.regist_next)
    Button mRegistNext;

    @Override
    public void initUIWithPermission() {
        mRegistNext.setOnClickListener(this);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_regist;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regist_next:
                String inviteCode = mRegistYaoqingma.getText().toString().trim();
                String phone = mRegistPhone.getText().toString().trim();
                if (RegexUtils.isMobileExact(phone)) {
                    checkInvit(inviteCode, phone);
                } else {
                    ToastUtils.showToast(this, "请输入手机号或邀请码");
                }
                break;
        }
    }

    private void checkInvit(final String inviteCode, final String phone) {
        Call<Invite> call = mHttpApi.checkInvitNum(inviteCode);
        Callback<Invite> callback = new EasyCallBack<Invite>() {
            @Override
            public void onSuccess(Call<Invite> call, Invite model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess() && model.getData()) {
                    Intent fastReg = new Intent(RegistActivity.this, PhoneVerActivity.class);
                    fastReg.putExtra(PhoneVerActivity.REGIST_PHONE_KEY, phone);
                    fastReg.putExtra(PhoneVerActivity.VISIT_NUM_KEY, inviteCode);
                    startActivity(fastReg);
                    finish();
                } else {
                    ToastUtils.showToast(RegistActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("正在校验邀请码。。。");
        requestApi(call, callback);
    }
}
