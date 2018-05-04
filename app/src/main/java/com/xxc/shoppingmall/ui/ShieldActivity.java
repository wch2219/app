package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
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
import com.xxc.shoppingmall.model.ConvertShield;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.UserInfo;
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
 * 卖盾页面
 */
public class ShieldActivity extends AbstractPermissionActivity {

    public static final String SHIELD_KEY = "shield_key";
    private static final String CAN_SELL_FORMAT = "可卖数量:%d";
    private static final String CAN_SELL_MONEY = "可卖价值:%.2f 元";
    private static final String DUN_PRICE = "单价:%s 元";

    @BindView(R.id.shield_can_sell_num)
    TextView mShieldCanSellNum;
    @BindView(R.id.shield_plan_sell)
    EditText mShieldPlanSell;
    @BindView(R.id.shield_price)
    TextView mShieldPrice;
    @BindView(R.id.shield_password)
    EditText mShieldPassword;
    @BindView(R.id.shield_values)
    TextView mShieldValues;
    @BindView(R.id.shield_bank_num)
    TextView mShieldBankNum;
    @BindView(R.id.shield_select_bank)
    RelativeLayout mShieldSelectBank;
    @BindView(R.id.shield_confirm)
    Button mShieldConfirm;

    private UserInfo mUserInfo;
    private BankCard mBankCard;
    //    private List<String> mBankList = new ArrayList<>();
    private int mBankCardIndex;

    @Override
    public void initUIWithPermission() {
        mUserInfo = (UserInfo) getIntent().getSerializableExtra(SHIELD_KEY);
        refreshUI();
    }

    private void refreshUI() {
        mShieldCanSellNum.setText(String.format(CAN_SELL_FORMAT, (int) mUserInfo.getData().getDong
                ()));
        mShieldPrice.setText(String.format(DUN_PRICE, mUserInfo.getData().getDongPrice()));
        float price = Float.valueOf(mUserInfo.getData().getDongPrice());
        float allMoney = price * mUserInfo.getData().getDong();
        mShieldValues.setText(String.format(CAN_SELL_MONEY, allMoney));

        mShieldPlanSell.setText("");
        mShieldPassword.setText("");

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
                    if (value > mUserInfo.getData().getDong()) {
                        mShieldPlanSell.setText(String.valueOf((int) mUserInfo.getData().getDong
                                ()));
                    }
                }
            }
        };
        mShieldPlanSell.addTextChangedListener(watcher);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_sell_shield;
    }

    @Override
    public void initData() {
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        String userid = user.getData().getUserId();
        showLoadingDialog("加载中");
        getBankCardList(userid);
    }

    @Override
    public void addListeners() {

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
                            mBankCardIndex = i;
                            mShieldBankNum.setText(bankNum.toString());
                        }
//                        mBankList.add(bankNum.getBankName() + "(" + StringReplaceUtil
//                                .bankCardReplaceWithStar(bankNum.getBankNum()) + ")");
                    }

//                    if (mBankList.size() <= 0) {
                    if (model.getData().size() <= 0) {
                        ToastUtils.showToast(ShieldActivity.this, "请先添加银行卡");
                    } else {
                        if (TextUtils.isEmpty(mShieldBankNum.getText().toString().trim())) {
                            mBankCardIndex = 0;
                            mShieldBankNum.setText(model.getData().get(0).toString());
                        }
                    }
                } else {
                    ToastUtils.showToast(ShieldActivity.this, null == model ? "请求失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        requestApi(call, cardCallback);
    }

    @OnClick({R.id.shield_select_bank, R.id.shield_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shield_select_bank:
                if (null != mBankCard && mBankCard.getData().size() > 0) {
                    showSingleChoiceDialog("请选择银行卡", mBankCard.getData(), mBankCardIndex, "确定",
                            new MaterialDialog
                                    .ListCallbackSingleChoice() {
                                @Override
                                public boolean onSelection(MaterialDialog dialog, View itemView,
                                                           int which,
                                                           CharSequence text) {
                                    mBankCardIndex = which;
                                    mShieldBankNum.setText(text);
                                    return true;
                                }
                            });
                } else {
                    ToastUtils.showToast(this, "请先添加银行卡");
                }
                break;
            case R.id.shield_confirm:
                if (null != mBankCard && mBankCard.getData().size() > 0) {
                    String num = mShieldPlanSell.getText().toString().trim();
                    String pwd = mShieldPassword.getText().toString().trim();
                    String bankNum = mBankCard.getData().get(mBankCardIndex).getBankNum();
                    LoginResult user = ShoppingMallApp.getInstance().getUser();
                    String userId = user.getData().getUserId();
                    if (!TextUtils.isEmpty(num) && !TextUtils.isEmpty(pwd)) {
                        int intNum = Integer.parseInt(num);
                        if (mUserInfo.getData().getDong() < 1) {
                            ToastUtils.showToast(this, "您的盾数太少,快去赚盾吧～");
                            return;
                        }
                        if (intNum > mUserInfo.getData().getDong()) {
                            ToastUtils.showToast(this, "您输入的数量已超出，请重新输入～");
                            return;
                        }
                        exchangeShield(num, pwd, bankNum, userId);
                    } else {
                        ToastUtils.showToast(this, "请输入兑换数量及密码");
                    }
                }
                break;
        }
    }

    private void exchangeShield(final String num, String pwd, String bankNum, String userId) {
        Map<String, String> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.TRANSACTION_PASSWORD, pwd);
        params.put(ParamKey.BANK_NUM, bankNum);
        params.put(ParamKey.AMOUNT, num);
        Call<ConvertShield> call = mHttpApi.exchangeShields(params);
        Callback<ConvertShield> callback = new EasyCallBack<ConvertShield>() {
            @Override
            public void onSuccess(Call<ConvertShield> call, ConvertShield model) {
                if (null != model) {
                    if (model.getMsg().isSuccess()) {
                        Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
                            @Override
                            public void onSuccess(Call<UserInfo> call, UserInfo model) {
                                dismissLoading();
                                if (model != null) {
                                    if (model.getMsg().isSuccess()) {
                                        mUserInfo = model;
                                        ShoppingMallApp.getInstance().setUserInfo(model);
                                        refreshUI();
                                        Intent sell = new Intent(ShieldActivity.this,
                                                SuccessActivity.class);
                                        sell.putExtra(SuccessActivity.SHOW_TYPE, SuccessActivity
                                                .SHOW_TRANSACTION);
                                        startActivity(sell);
                                    } else {
                                        ToastUtils.showToast(ShieldActivity.this, null == model ?
                                                "请求失败" : model.getMsg().getInfo());
                                    }
                                }
                            }
                        };
                        getPersonInfo(callback);
                    } else {
                        dismissLoading();
                        ToastUtils.showToast(ShieldActivity.this, TextUtils.isEmpty(model.getMsg
                                ().getInfo()) ? "请求失败" : model.getMsg().getInfo());
                    }
                }
            }
        };
        showLoadingDialog("正在提交...");
        requestApi(call, callback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != ShoppingMallApp.getInstance().getUser() && null == ShoppingMallApp.getInstance()
                .getUserInfo()) {
            Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
                @Override
                public void onSuccess(Call<UserInfo> call, UserInfo model) {
                    if (model != null) {
                        if (model.getMsg().isSuccess()) {
                            mUserInfo = model;
                            ShoppingMallApp.getInstance().setUserInfo(model);
                            refreshUI();
                        }
                    }
                }
            };
            getPersonInfo(callback);
        }
    }

    @Override
    public void finish() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}
