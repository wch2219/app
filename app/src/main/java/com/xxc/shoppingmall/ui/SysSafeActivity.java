package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuxingchen on 2017/11/24.
 * 账号安全的界面
 */
public class SysSafeActivity extends AbstractPermissionActivity {
    @BindView(R.id.safe_reset_login_pwd)
    RelativeLayout mSafeResetLoginPwd;
    @BindView(R.id.safe_reset_trade_pwd)
    RelativeLayout mSafeResetTradePwd;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_safe;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.safe_reset_login_pwd, R.id.safe_reset_trade_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.safe_reset_login_pwd:
                Intent resetLoginPwd = new Intent(this, ResetPwdWithLoginedActivity.class);
                startActivity(resetLoginPwd);
                break;
            case R.id.safe_reset_trade_pwd:
                Intent resetTradePwd = new Intent(this, ResetTradePwdActivity.class);
                startActivity(resetTradePwd);
                break;
        }
    }
}
