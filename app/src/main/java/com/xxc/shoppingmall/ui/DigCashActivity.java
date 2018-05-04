package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.GscPriceBen;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.ResultBean;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.pay.PayFragment;
import com.xxc.shoppingmall.ui.pay.PayPwdView;
import com.xxc.shoppingmall.widget.CommonTitle;
import com.xxc.shoppingmall.widget.Rotate3dAnimation;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/12/7.
 */

public class DigCashActivity extends AbstractPermissionActivity implements RadioGroup.OnCheckedChangeListener,PayPwdView
        .InputCallBack {

    private static final int REQUEST_MONEY = 100;

    @BindView(R.id.dig_cash_total_num)
    TextView mDigCashTotalNum;
    @BindView(R.id.dig_cash_bonus)
    TextView mDigCashBonus;
    @BindView(R.id.dig_cash_title)
    CommonTitle mDigCashTitle;
    @BindView(R.id.dig_cash_unit)
    TextView digCashUnit;
    @BindView(R.id.rb_usableIntege)
    RadioButton rbUsableIntege;
    @BindView(R.id.rb_totalIntege)
    RadioButton rbTotalIntege;
    @BindView(R.id.rg_check)
    RadioGroup rgCheck;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_userconvernum)
    TextView tvUserconvernum;
    @BindView(R.id.tv_stocknum)
    TextView tvStocknum;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.iv_gsc)
    ImageView iv_gsc;
    private UserInfo info;
    private PayFragment mPayFragment;
    private float gscPrice;

    @Override
    public void initUIWithPermission() {
        info = ShoppingMallApp.getInstance().getUserInfo();
        refreshUi(info);
    }

    private void refreshUi(UserInfo info) {
        mDigCashTotalNum.setText(String.valueOf(info.getData().getHold_gsc_num()));
        mDigCashBonus.setText(String.valueOf(info.getData().getGsc_num()));
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_dig_cash;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        getPersonInfo(user);
    }
    private void getPersonInfo(LoginResult user) {
        Call<UserInfo> call = mHttpApi.getUserInfo(user.getData().getUserId());
        Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
            @Override
            public void onSuccess(Call<UserInfo> call, UserInfo model) {
                LogUtils.d(model + "");
                dismissLoading();
                if (model != null) {
                    if (model.getMsg().isSuccess()) {

                        ShoppingMallApp.getInstance().setUserInfo(model);
                        getGscPrice();
                        refreshUi(model);
                    }
                }
            }
        };
        showLoadingDialog();
        requestApi(call, callback);
    }
    @Override
    public void initData() {
        rotateOnYCoordinate();
    }

    private void getGscPrice() {
        Map<String,Object> map = new HashMap<>();
        Call<GscPriceBen> call = mHttpApi.getGscPrice(map);
        EasyCallBack<GscPriceBen> callBack = new EasyCallBack<GscPriceBen>() {
            @Override
            public void onSuccess(Call<GscPriceBen> call, GscPriceBen model) {

                String data = model.getData();
                LogUtils.d("价格："+data);
                gscPrice = Float.valueOf(data);
                tvPrice.setText(gscPrice+"");
            }
        };
        requestApi(call,callBack);
    }

    @Override
    public void addListeners() {
        rgCheck.setOnCheckedChangeListener(this);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent digMoney = new Intent(DigCashActivity.this, DigMoneyActivity.class);
                startActivityForResult(digMoney, REQUEST_MONEY);
            }
        };
        mDigCashTitle.setRightClickListener(clickListener);
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = s.toString().trim();

                if (TextUtils.isEmpty(num)) {
                    tvStocknum.setText("0");
                    tvUserconvernum.setText("0");
                    return;
                }
                double nums = Double.valueOf(num);
                if (duiType == 2) {
                    String totalInteg = ShoppingMallApp.getInstance().getUserInfo().getData().getHoldIntNum()+"";
                    LogUtils.d(totalInteg);
                    if (nums >Double.valueOf(totalInteg)) {
                        etNum.setText(totalInteg.substring(0,totalInteg.lastIndexOf(".")));
                        etNum.setSelection(totalInteg.substring(0,totalInteg.lastIndexOf(".")).length());
                        tvStocknum.setText((double) (nums*0.015)+"");
                        tvUserconvernum.setText(((double) (nums*0.75/gscPrice))+"");

                    }
                }else{
                    String userIntege = ShoppingMallApp.getInstance().getUserInfo().getData().getIntegration()+"";
                    if (nums >Double.valueOf(userIntege)) {
                        etNum.setText(userIntege.substring(0,userIntege.lastIndexOf(".")));
                        etNum.setSelection(userIntege.substring(0,userIntege.lastIndexOf(".")).length());
                    }
                }
                tvStocknum.setText((double) (Integer.valueOf(etNum.getText().toString())*0.015)+"");
                tvUserconvernum.setText(((double) (Integer.valueOf(etNum.getText().toString())*0.75/gscPrice))+"");
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            refreshUi(ShoppingMallApp.getInstance().getUserInfo());
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_usableIntege:
                duiType = 1;
                break;
            case R.id.rb_totalIntege:
                duiType = 2;
                break;

        }
    }

    private int duiType = 1;//兑换方式1 可用积分  2总积分

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("兑换须知！！！");
        builder.setMessage("1.我自愿参与积分兑换已了解兑换规则\n2.我自愿承担GSC交易的相关风险");
        builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String num = etNum.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    ToastUtils.showToast(DigCashActivity.this, "请输入兑换数量");
                    return;
                }
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString(PayFragment.EXTRA_CONTENT, "兑换积分：¥ " + etNum
                        .getText().toString());
                mPayFragment = new PayFragment();
                mPayFragment.setArguments(bundle);
                mPayFragment.setPaySuccessCallBack(DigCashActivity.this);
                mPayFragment.show(getSupportFragmentManager(), "Pay");



            }
        });
        builder.show();
    }

    @Override
    public void onInputFinish(String result) {
        String num = etNum.getText().toString().trim();
        if (TextUtils.isEmpty(num)) {
            ToastUtils.showToast(DigCashActivity.this, "请输入兑换数量");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userId", info.getData().getUserId());
        params.put("transactionPassword", result);
        if (duiType == 1) {
            params.put("integration", Float.valueOf(num));
        } else {
            params.put("holdIntNum", Float.valueOf(num));
        }
        params.put("integrationType", duiType);
        Call<ResultBean> call = mHttpApi.converItgetoGsc(params);
        EasyCallBack<ResultBean> callBack = new EasyCallBack<ResultBean>() {
            @Override
            public void onSuccess(Call<ResultBean> call, ResultBean model) {
                dismissLoading();
                ResultBean.MsgBean msg = model.getMsg();
                if (msg.isSuccess()) {
                    etNum.setText("");
                    mPayFragment.dismiss();
                    LoginResult user = ShoppingMallApp.getInstance().getUser();
                    getPersonInfo(user);
                    ToastUtils.showToast(DigCashActivity.this,"兑换成功");
                }else{
                    ToastUtils.showToast(DigCashActivity.this,"兑换失败，请重新提交");
                }
            }
        };
        showLoadingDialog();
        requestApi(call,callBack);
    }

    // 以X轴为轴心旋转
    private void rotateOnYCoordinate() {
        Rotate3dAnimation animation = new Rotate3dAnimation();
        animation.setRepeatCount(Animation.INFINITE);
        iv_gsc.startAnimation(animation);
    }
}
