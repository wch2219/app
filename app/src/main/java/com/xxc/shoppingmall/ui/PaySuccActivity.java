package com.xxc.shoppingmall.ui;

import android.os.Bundle;
import android.widget.Button;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付成功
 */
public class PaySuccActivity extends AbstractPermissionActivity {
    @BindView(R.id.commtitle)
    CommonTitle commtitle;
    @BindView(R.id.btn_back)
    Button btnBack;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.activity_pay_succ;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        close();
        finish();
    }
}
