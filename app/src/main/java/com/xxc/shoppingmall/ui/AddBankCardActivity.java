package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.BankCard;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.NumberUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/24.
 * 添加银行卡界面
 */
public class AddBankCardActivity extends AbstractPermissionActivity {

    public static final String BANK_DATA_KEY = "bank_data";

    @BindView(R.id.card_et_owner)
    EditText mCardEtOwner;
    @BindView(R.id.card_et_phone)
    EditText mCardEtPhone;
    @BindView(R.id.card_et_card_num)
    EditText mCardEtCardNum;
    @BindView(R.id.add_bank_card_confirm)
    Button mAddBankCardConfirm;

    private BankCard.DataBean mDataBean;

    @Override
    public void initUIWithPermission() {
        mDataBean = (BankCard.DataBean) getIntent().getSerializableExtra(BANK_DATA_KEY);
        if (null != mDataBean) {
            mCardEtOwner.setText(mDataBean.getBankName());
            mCardEtCardNum.setText(String.valueOf(mDataBean.getBankNum()));
        }
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_add_bank_card;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.add_bank_card_confirm)
    public void onViewClicked() {
        String bankName = mCardEtOwner.getText().toString().trim();
        String bankNum = mCardEtCardNum.getText().toString().trim();
        if (!TextUtils.isEmpty(bankName) && !TextUtils.isEmpty(bankNum)) {
            String bankNumCheckResult = NumberUtils.luhmCheck(bankNum);
            if (!"true".equalsIgnoreCase(bankNumCheckResult)) {
                ToastUtils.showToast(this,bankNumCheckResult);
                return;
            }
            addBankCardInfo(bankName, bankNum, 0, mDataBean);
        } else {
            ToastUtils.showToast(this, "请完善开户行及银行卡卡号信息");
        }
    }

    private void addBankCardInfo(String bankName, String bankNum, int isDefault, BankCard.DataBean dataBean) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.BANK_NUM, bankNum);
        params.put(ParamKey.BANK_NAME, bankName);
        params.put(ParamKey.IS_DEFAULT, 1 == isDefault ? 1 : 0);
        if (null != dataBean) {
            params.put("id", dataBean.getId());
        }
        Call<SubmitOrder> call = mHttpApi.updateBankCard(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess()) {
                    ToastUtils.showToast(AddBankCardActivity.this, "已提交");
                    postDelayed(1200, new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = getIntent();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    ToastUtils.showToast(AddBankCardActivity.this, null == model ? "请求失败" : model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("信息提交中。。。");
        requestApi(call, callback);
    }
}
