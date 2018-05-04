package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.VerResult;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * Created by xuxingchen on 2017/11/16.
 * 验证码界面
 */
public class PhoneVerActivity extends AbstractPermissionActivity {

    public static final String VISIT_NUM_KEY = "visitNum";
    public static final String REGIST_PHONE_KEY = "phone";

    private static final String HINT_FORMAT = "请输入%s收到的验证码";

    @BindView(R.id.title)
    CommonTitle mTitle;
    @BindView(R.id.fast_regist_phone_hint)
    TextView mFastRegistPhoneHint;
    @BindView(R.id.fast_regist_ver_num)
    EditText mFastRegistVerNum;
    @BindView(R.id.fast_regist_get_ver)
    Button mFastRegistGetVer;
    @BindView(R.id.fast_regist_next)
    Button mFastRegistNext;

    private String mPhone;
    private String mVisit;

    private boolean isInSending = false;
    private VerResult mVerResult;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_phone_ver;
    }

    @Override
    public void initData() {
        mPhone = getIntent().getStringExtra(REGIST_PHONE_KEY);
        mVisit = getIntent().getStringExtra(VISIT_NUM_KEY);
        mFastRegistPhoneHint.setText(String.format(HINT_FORMAT, mPhone));
    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.fast_regist_get_ver, R.id.fast_regist_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fast_regist_get_ver:
                if (isInSending) {
                    ToastUtils.showToast(this, "验证码发送中");
                } else {
                    EasyCallBack<VerResult> callBack = new EasyCallBack<VerResult>() {
                        @Override
                        public void onSuccess(Call<VerResult> call, VerResult model) {
                            if (model.getMsg().isSuccess()) {
                                LogUtils.d(model + "");
                                ToastUtils.showToast(PhoneVerActivity.this, "验证码已发送");
                                isInSending = true;
                                mVerResult = model;
                                CountDownTimer countDownTimer = new CountDownTimer(30 * 1000, 999) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        mFastRegistGetVer.setText(millisUntilFinished / 1000 +
                                                "S后重新发送");
                                    }

                                    @Override
                                    public void onFinish() {
                                        isInSending = false;
                                        mFastRegistGetVer.setText("验证码");
                                    }
                                };
                                countDownTimer.start();
                            }
                        }

                        @Override
                        public void onFailure(Call<VerResult> call, Throwable t) {
                            super.onFailure(call, t);
                            isInSending = false;
                            ToastUtils.showToast(PhoneVerActivity.this, "请求失败");
                        }
                    };
                    Call<VerResult> call = mHttpApi.getVerNum(mPhone);
                    requestApi(call, callBack);
                }
                break;
            case R.id.fast_regist_next:
                String verNum = mFastRegistVerNum.getText().toString().trim();
                if (TextUtils.isEmpty(verNum)) {
                    ToastUtils.showToast(this, "请输入验证码");
                } else {
                    if (verNum.length() != 4) {
                        ToastUtils.showToast(this, "验证码长度为4位");
                        return;
                    }
                    if (null != mVerResult && null != mVerResult.getData()) {
                        String verCode = mVerResult.getData().getVerificationCode();
                        if (!TextUtils.isEmpty(verCode) && !verCode.equals(verNum)) {
                            ToastUtils.showToast(this, "验证码不正确,请重新输入");
                            return;
                        }
                    }
                    Intent setPwd = new Intent(this, SetPwdActivity.class);
                    setPwd.putExtra(SetPwdActivity.VER_KEY, verNum);
                    setPwd.putExtra(SetPwdActivity.VISIT_NUM_KEY, mVisit);
                    setPwd.putExtra(SetPwdActivity.PHONE_NUM, mPhone);
                    startActivity(setPwd);
                    finish();
                }
                break;
        }
    }
}
