package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.BuildConfig;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import retrofit2.Call;

/**
 * Created by xuxingchen on 2017/11/14.
 * 登录页面
 */
public class LoginActivity extends AbstractPermissionActivity implements View.OnClickListener {
    @BindView(R.id.login_username)
    EditText mLoginUsername;
    @BindView(R.id.login_password)
    EditText mLoginPassword;
    @BindView(R.id.login_btn)
    Button mLoginBtn;
    @BindView(R.id.login_regist)
    TextView mLoginRegist;
    @BindView(R.id.login_reset_pwd)
    TextView mLoginResetPwd;

    @Override
    public void initUIWithPermission() {
        mLoginBtn.setOnClickListener(this);
        mLoginRegist.setOnClickListener(this);
        mLoginResetPwd.setOnClickListener(this);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_login;
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
            case R.id.login_btn:
                String userName = mLoginUsername.getText().toString().trim();
                String pwd = mLoginPassword.getText().toString().trim();
                userName = BuildConfig.DEBUG && TextUtils.isEmpty(userName) ? "18701222933" :
                        userName;
                pwd = BuildConfig.DEBUG && TextUtils.isEmpty(pwd) ? "admin" : pwd;
                if (RegexUtils.isMobileExact(userName)) {
                    Call<LoginResult> call = mHttpApi.userLogin(userName, pwd);
                    EasyCallBack<LoginResult> loginResultEasyCallBack = new
                            EasyCallBack<LoginResult>() {
                                @Override
                                public void onSuccess(Call<LoginResult> call, LoginResult model) {
                                    LogUtils.d(model + "");
                                    dismissLoading();
                                    if (model.getMsg().getCode() == 200) {
                                        ShoppingMallApp.getInstance().setUser(model);
                                        ActivityUtils.finishAllActivities();
                                        ActivityUtils.startActivity(new Intent(LoginActivity.this,
                                                MainActivity.class));
                                    } else {
                                        ToastUtils.showToast(LoginActivity.this, model.getMsg()
                                                .getInfo());
                                    }
                                }
                            };
                    showLoadingDialog("加载中");
                    requestApi(call, loginResultEasyCallBack);
                }
                break;
            case R.id.login_regist:
                Intent regist = new Intent(this, RegistActivity.class);
                startActivity(regist);
                break;
            case R.id.login_reset_pwd:
                Intent resetPwd = new Intent(this, ConfirmAccountActivity.class);
                startActivity(resetPwd);
                break;
        }
    }

}
