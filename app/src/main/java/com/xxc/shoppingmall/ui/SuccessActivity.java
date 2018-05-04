package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;

import butterknife.BindView;

/**
 * Created by xuxingchen on 2017/11/23.
 * 交易成功和充值成功的界面
 */
public class SuccessActivity extends AbstractPermissionActivity implements View.OnClickListener {

    /**
     * 显示类型 1 交易成功  2  充值成功
     */
    public static final String SHOW_TYPE = "show_type";

    public static final int SHOW_PAY = 1;
    public static final int SHOW_TRANSACTION = 2;

    @BindView(R.id.action_root_view)
    LinearLayout mActionRootView;
    @BindView(R.id.action_title)
    CommonTitle mActionTitle;
    @BindView(R.id.success_msg_hint)
    TextView mSuccessMsgHint;

    @Override
    public void initUIWithPermission() {
        Intent intent = getIntent();
        int type = intent.getIntExtra(SHOW_TYPE, SHOW_PAY);
        if (type == SHOW_PAY) {
            mActionTitle.setTitle("充值成功");
            mSuccessMsgHint.setText("订单交易成功");
            Button paySuccess = (Button) LayoutInflater.from(this).inflate(R.layout
                    .action_bottom_payment, mActionRootView,false);
            paySuccess.setOnClickListener(this);
            mActionRootView.addView(paySuccess);

        } else if (type == SHOW_TRANSACTION) {
            mActionTitle.setTitle("交易成功");
            mSuccessMsgHint.setText("交易成功!");
            View container = LayoutInflater.from(this).inflate(R.layout
                    .action_bottom_transaction, mActionRootView);
            Button transaction_continue = (Button) container.findViewById(R.id.transaction_continue);
            Button payment_payid = (Button) container.findViewById(R.id.payment_go_home);
            transaction_continue.setOnClickListener(this);
            payment_payid.setOnClickListener(this);
        } else {
            finish();
        }
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_action_success;
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
            case R.id.pay_bottom_back:
                finish();
                break;
            case R.id.transaction_continue:
                finish();
                break;
            case R.id.payment_go_home:
                ActivityUtils.finishToActivity(MainActivity.class, false);
                break;
        }
    }
}
