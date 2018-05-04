package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.BankCard;
import com.xxc.shoppingmall.model.ConvertPoints;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.pay.PayFragment;
import com.xxc.shoppingmall.ui.pay.PayPwdView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/19.
 * 积分兑换界面
 */
public class ConvertPointsActivity extends AbstractPermissionActivity implements PayPwdView
        .InputCallBack {

    public static final String ALL_POINTS_KEY = "all_points";
    private static final String POINTS_FORMAT = "积分余额:%d";
    private static final String CAN_CONVERT_POINTS = "%d 元";

    @BindView(R.id.points_select_bank)
    RelativeLayout mPointsSelectBank;
    @BindView(R.id.points_convert_num)
    EditText mPointsConvertNum;
    @BindView(R.id.points_remain)
    TextView mPointsRemain;
    @BindView(R.id.points_cash_all)
    TextView mPointsCashAll;
    @BindView(R.id.points_cash_now)
    Button mPointsCashNow;
    @BindView(R.id.points_bank_num)
    TextView mPointsBankNum;
    @BindView(R.id.points_can_convert)
    TextView mPointsCanConvert;

    private int mAllPoints;
    private BankCard mBankCard;
    //    private List<String> mBankList = new ArrayList<>();
    private PayFragment mPayFragment;
    private int mSelectedBankCardIndex;

    @Override
    public void initUIWithPermission() {
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
                    if (value > mAllPoints) {
                        mPointsConvertNum.setText(String.valueOf(mAllPoints));
                    }
                }
            }
        };
        mPointsConvertNum.addTextChangedListener(watcher);

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_convert_points;
    }

    @Override
    public void initData() {
        mAllPoints = getIntent().getIntExtra(ALL_POINTS_KEY, 0);
        mPointsRemain.setText(String.format(POINTS_FORMAT, mAllPoints));
        mPointsCanConvert.setText(String.format(CAN_CONVERT_POINTS, mAllPoints));
        showLoadingDialog("加载中");
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        getBankCardList(user.getData().getUserId());
    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.points_select_bank, R.id.points_cash_all, R.id.points_cash_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.points_select_bank:
                if (null == mBankCard) {
                    ToastUtils.showToast(this, "暂未请求到银行卡数据");
                    return;
                }
                if (mBankCard.getData().size() <= 0) {
                    ToastUtils.showToast(this, "您还未添加银行卡");
                    return;
                }
                showSingleChoiceDialog("请选择银行卡", mBankCard.getData(), 0, "确定", new MaterialDialog
                        .ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which,
                                               CharSequence text) {
                        mPointsBankNum.setText(text);
                        mSelectedBankCardIndex = which;
                        return true;
                    }
                });
                break;
            case R.id.points_cash_all:
                if (mAllPoints > 0) {
                    mPointsConvertNum.setText(String.valueOf(mAllPoints));
                } else {
                    ToastUtils.showToast(this, "您没有可提现余额");
                }
                break;
            case R.id.points_cash_now:
                String cash = mPointsConvertNum.getText().toString().trim();
                String tempBankNum = mPointsBankNum.getText().toString().trim();
                if (!TextUtils.isEmpty(cash)) {
                    if (TextUtils.isEmpty(tempBankNum)) {
                        ToastUtils.showToast(this, "请先添加银行卡");
                        return;
                    }
                    float cashInt = Float.valueOf(cash);
                    if (cashInt > 0 && 0 == cashInt % 100) {
                        Bundle bundle = new Bundle();
                        bundle.putString(PayFragment.EXTRA_CONTENT, "兑换积分：¥ " + mPointsConvertNum
                                .getText().toString());
                        mPayFragment = new PayFragment();
                        mPayFragment.setArguments(bundle);
                        mPayFragment.setPaySuccessCallBack(ConvertPointsActivity.this);
                        mPayFragment.show(getSupportFragmentManager(), "Pay");
                    } else {
                        ToastUtils.showToast(this, "请输入100的整数倍的积分");
                    }
                } else {
                    ToastUtils.showToast(this, "请输入兑换积分数量,为10的倍数");
                }
                break;
        }
    }

    private void getBankCardList(String userId) {
        Call<BankCard> call = mHttpApi.getBankCardList(userId);
        Callback<BankCard> cardCallback = new EasyCallBack<BankCard>() {
            @Override
            public void onSuccess(Call<BankCard> call, BankCard model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess()) {
                    mBankCard = model;
//                    mBankList.clear();
                    for (int i = 0; i < mBankCard.getData().size(); i++) {
                        BankCard.DataBean bankNum = mBankCard.getData().get(i);
                        if (BankCard.IS_DEFAULT == bankNum.getIsDefault()) {
                            mPointsBankNum.setText(bankNum.toString());
                        }
//                        mBankList.add(StringReplaceUtil.bankCardReplaceWithStar(bankNum
//                                .getBankNum()));
                    }
//                    if (mBankList.size() <= 0) {
                    if (model.getData().size() <= 0) {
                        ToastUtils.showToast(ConvertPointsActivity.this, "请先添加银行卡");
                    } else {
                        if (TextUtils.isEmpty(mPointsBankNum.getText().toString().trim())) {
                            mSelectedBankCardIndex = 0;
                            mPointsBankNum.setText(model.getData().get(0).toString());
                        }
                    }
                } else {
                    ToastUtils.showToast(ConvertPointsActivity.this, null == model ? "请求失败" :
                            model.getMsg().getInfo());
                }
            }
        };

        requestApi(call, cardCallback);

    }

    @Override
    public void onInputFinish(String result) {
        if (null != mPayFragment) {
            mPayFragment.dismiss();
        }
        String convertPoints = mPointsConvertNum.getText().toString().trim();
        if (!TextUtils.isEmpty(convertPoints)) {
            LoginResult user = ShoppingMallApp.getInstance().getUser();
            String userId = user.getData().getUserId();
            showLoadingDialog("加载中");
            convertPoints(convertPoints, mBankCard.getData().get(mSelectedBankCardIndex).toString(),
                    result, userId);
        } else {
            ToastUtils.showToast(this, "请选择银行卡或填入积分金额");
        }
    }

    private void convertPoints(String convertPoints, String selectedBankCard, String result,
                               String userId) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamKey.BANK_NUM, selectedBankCard);
        params.put(ParamKey.INTEGRATION_NUM, convertPoints);
        params.put(ParamKey.TRANSACTION_PASSWORD, result);
        params.put(ParamKey.USERID, userId);
        Call<ConvertPoints> call = mHttpApi.exchangePoints(params);
        Callback<ConvertPoints> callback = new EasyCallBack<ConvertPoints>() {
            @Override
            public void onSuccess(Call<ConvertPoints> call, ConvertPoints model) {
                dismissLoading();
                if (null != model) {
                    if (model.getMsg().isSuccess()) {
                        Runnable task = new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showToast(ConvertPointsActivity.this, "交易成功");
                                finish();
                            }
                        };
                        postDelayed(600, task);
                    } else {
                        ToastUtils.showToast(ConvertPointsActivity.this, model.getMsg().getInfo());
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
