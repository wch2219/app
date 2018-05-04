package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.TicketsResult;
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
 * Created by xuxingchen on 2017/11/19.
 * 商标股界面
 */
public class VoucherTicketsActivity extends AbstractPermissionActivity {

    public static final String TICKETS_KEY = "ticket";

    @BindView(R.id.ticket_et_num)
    EditText mTicketEtNum;
    @BindView(R.id.ticket_et_target)
    EditText mTicketEtTarget;
    @BindView(R.id.ticket_et_pwd)
    EditText mTicketEtPwd;
    @BindView(R.id.tickets_confirm)
    Button mTicketsConfirm;

    private int mAllTickets;

    @Override
    public void initUIWithPermission() {
        mAllTickets = getIntent().getIntExtra(TICKETS_KEY, 0);
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
                    if (value > mAllTickets) {
                        mTicketEtNum.setText(String.valueOf(mAllTickets));
                    }
                }
            }
        };
        mTicketEtNum.addTextChangedListener(watcher);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_voucher_tickets;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.tickets_confirm)
    public void onViewClicked() {
        String ticketNum = mTicketEtNum.getText().toString().trim();
        String userAccount = mTicketEtTarget.getText().toString().trim();
        String pwd = mTicketEtPwd.getText().toString().trim();
        if (!TextUtils.isEmpty(ticketNum) && !TextUtils.isEmpty(userAccount)) {
            int tempNum = Integer.valueOf(ticketNum);
            if (tempNum <= 0) {
                ToastUtils.showToast(this, "转出数量需大于0");
                return;
            }
            if (!TextUtils.isEmpty(pwd)) {
                LoginResult user = ShoppingMallApp.getInstance().getUser();
                String userId = user.getData().getUserId();
                showLoadingDialog();
                exchangeTickets(ticketNum, userAccount, pwd, userId);
            } else {
                ToastUtils.showToast(this, "请填写交易密码");
            }
        } else {
            ToastUtils.showToast(this, "请填写转出数量及目标账号");
        }
    }

    private void exchangeTickets(String ticketNum, String userAccount, String pwd, String userId) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamKey.TICKET_NUM, ticketNum);
        params.put(ParamKey.USER_ACCOUNT, userAccount);
        params.put(ParamKey.TRANSACTION_PASSWORD, pwd);
        params.put(ParamKey.USERID, userId);
        Call<TicketsResult> call = mHttpApi.exchangeTickets(params);

        Callback<TicketsResult> callback = new EasyCallBack<TicketsResult>() {
            @Override
            public void onSuccess(Call<TicketsResult> call, TicketsResult model) {
                dismissLoading();
                if (null != model) {
                    if (model.getMsg().isSuccess()) {
                        Runnable task = new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(VoucherTicketsActivity.this, "兑换成功");
                                finish();
                            }
                        };
                        postDelayed(600, task);
                    } else {
                        ToastUtils.showToast(VoucherTicketsActivity.this, model.getMsg().getInfo());
                    }
                }
            }
        };
        requestApi(call, callback);
    }

    @Override
    public void finish() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}
