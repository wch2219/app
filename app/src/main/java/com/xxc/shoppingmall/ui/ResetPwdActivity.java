package com.xxc.shoppingmall.ui;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.model.VerResult;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/15.
 * 重置密码界面
 */
public class ResetPwdActivity extends AbstractPermissionActivity implements View.OnClickListener {
    @BindView(R.id.title)
    CommonTitle mTitle;
    @BindView(R.id.reset_phone)
    EditText mResetPhone;
    @BindView(R.id.reset_ver_num)
    EditText mResetVerNum;
    @BindView(R.id.reset_get_ver_num)
    Button mResetGetVerNum;
    @BindView(R.id.reset_set_new_pwd)
    EditText mResetSetNewPwd;
    @BindView(R.id.reset_confirm_new_pwd)
    EditText mResetConfirmNewPwd;
    @BindView(R.id.reset_btn_confirm)
    Button mResetBtnConfirm;

    private boolean isInSending = false;
    private CountDownTimer mCountDownTimer;

    @Override
    public void initUIWithPermission() {
        mResetGetVerNum.setOnClickListener(this);
        mResetBtnConfirm.setOnClickListener(this);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_reset_pwd;
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
            case R.id.reset_get_ver_num:
                String phone = mResetPhone.getText().toString().trim();
                if (RegexUtils.isMobileExact(phone)) {
                    sendVerNum(phone);
                } else {
                    ToastUtils.showToast(this, "请输入正确的手机号");
                }
                break;
            case R.id.reset_btn_confirm:
                String tel = mResetPhone.getText().toString().trim();
                String ver = mResetVerNum.getText().toString().trim();
                String pwd = mResetSetNewPwd.getText().toString().trim();
                String confirmPwd = mResetConfirmNewPwd.getText().toString().trim();

                if (!TextUtils.isEmpty(tel) && !TextUtils.isEmpty(ver) && !TextUtils.isEmpty(pwd)
                        && !TextUtils.isEmpty(confirmPwd)) {
                    if (pwd.length() < 10) {
                        ToastUtils.showToast(this, "密码太短");
                        return;
                    }
                    if (SetPwdActivity.isNumeric(pwd)) {
                        ToastUtils.showToast(this, "密码不能为纯数字");
                        return;
                    }
                    if (SetPwdActivity.isAllEnglishChar(pwd)) {
                        ToastUtils.showToast(this, "密码不能为纯英文");
                        return;
                    }
                    if (pwd.equals(confirmPwd)) {
                        showLoadingDialog("提交中...");
                        submitResetPwd(tel, ver, pwd);
                    } else {
                        ToastUtils.showToast(this, "两次密码不一致");
                    }
                } else {
                    ToastUtils.showToast(this, "请填写手机号,验证码,密码,并确认密码");
                }
                break;
        }
    }

    private void submitResetPwd(String tel, String ver, String pwd) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USER_NAME, tel);
        params.put(ParamKey.VERIFICATIONCODE, ver);
        params.put(ParamKey.PASSWORD, pwd);
        params.put(ParamKey.MOBILE, tel);
        Call<SubmitOrder> call = mHttpApi.submitPersonInfo(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                dismissLoading();
                if (null != model) {
                    if (model.getMsg().isSuccess()) {
                        postDelayed(1500, new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                ToastUtils.showToast(ResetPwdActivity.this, "重置密码成功");
                            }
                        });
                    } else {
                        ToastUtils.showToast(ResetPwdActivity.this, model.getMsg().getInfo());
                    }
                } else {
                    ToastUtils.showToast(ResetPwdActivity.this, "请求失败");
                }
            }
        };
        requestApi(call, callback);
    }


    private void sendVerNum(String phone) {
        if (isInSending) {
            ToastUtils.showToast(this, "验证码发送中");
        } else {
            EasyCallBack<VerResult> callBack = new EasyCallBack<VerResult>() {
                @Override
                public void onSuccess(Call<VerResult> call, VerResult model) {
                    if (model.getMsg().isSuccess()) {
                        LogUtils.d(model + "");
                        ToastUtils.showToast(ResetPwdActivity.this, "验证码已发送");
                        isInSending = true;
                        mCountDownTimer = new CountDownTimer(60 * 1000, 999) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                mResetGetVerNum.setText(millisUntilFinished / 1000 + "");
                            }

                            @Override
                            public void onFinish() {
                                isInSending = false;
                                mResetGetVerNum.setText("获取验证码");
                            }
                        };
                        mCountDownTimer.start();
                    }
                }

                @Override
                public void onFailure(Call<VerResult> call, Throwable t) {
                    super.onFailure(call, t);
                    isInSending = false;
                    ToastUtils.showToast(ResetPwdActivity.this, "请求失败");
                }
            };
            Call<VerResult> call = mHttpApi.getVerNum(phone);
            requestApi(call, callBack);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCountDownTimer) {
            mCountDownTimer.cancel();
        }
    }
}
