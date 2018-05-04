package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.PopuListAdapter;
import com.xxc.shoppingmall.model.CompanyBank;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.PayMoney;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.base.IOnCompressCompleted;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/19.
 * 充值中心界面
 */
public class PaymentActivity extends AbstractPermissionActivity {

    private static final int REQUEST_UPLOAD_IMG = 101;
//    private static final String MONEY_CODE_KEY = "moneyCode";

    @BindView(R.id.payment_bank_selector)
    RadioGroup mPaymentBankSelector;
    @BindView(R.id.payment_payid)
    TextView mPaymentPayid;
    //    @BindView(R.id.payment_btn_generate_payid)
//    Button mPaymentBtnGeneratePayid;
    @BindView(R.id.payment_submit)
    Button mPaymentSubmit;
    @BindView(R.id.payment_pay_other)
    CheckBox mPaymentPayOther;
    @BindView(R.id.payment_et_pay_other)
    EditText mPaymentEtPayOther;

    @BindView(R.id.tv_selete)
    TextView tvSelete;
    @BindView(R.id.tv_top_type)
    TextView tvTopType;
    @BindView(R.id.tv_moneys)
    TextView tvMoney;
    @BindView(R.id.et_vipmoney)
    EditText etVipmoney;
    @BindView(R.id.tv_totalmoney)
    TextView tvTotalmoney;
    @BindView(R.id.payment_upload)
    TextView mPaymentUpload;
    @BindView(R.id.payment_tip_2)
    TextView paymentTip2;
    @BindView(R.id.payment_tip)
    TextView paymentTip;
    @BindView(R.id.iv_moneydown)
    ImageView iv_moneydown;
    //    private BankCard mBankCard;
    private CompanyBank mCompanyBank;
    private  List<PayMoney.DataBean> mPayMoney;
    private String mBankNum;
    private String mImagePath;
    private PayMoney.DataBean mSelectedMoney;
    private File outFile;
    //    private String submitKey;
    private Map<Integer, String> mAllBanks = new HashMap<>();
    ClipboardManager cm;
    private String money;
    private PopupWindow popupWindow;

    @Override
    public void initUIWithPermission() {
        cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Set<Integer> keys = mAllBanks.keySet();
                for (Integer key : keys) {
                    if (key == checkedId) {
                        mBankNum = mAllBanks.get(key);
                    }
                }
                ClipData clip = ClipData.newPlainText("复制成功", mBankNum);
                cm.setPrimaryClip(clip);
                ToastUtils.showToast(PaymentActivity.this, "已复制银行卡号");
            }
        };
        mPaymentBankSelector.setOnCheckedChangeListener(listener);

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_payment;
    }

    @Override
    public void initData() {
        outFile = new File(ShoppingMallApp.OUT_PATH, "proof.jpg");
//        submitKey = ;
        mPaymentPayid.setText(String.valueOf(System.currentTimeMillis()));
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        LogUtils.d(ShoppingMallApp.getInstance().getUserInfo().getData().getRoleType()+"用户角色");
        String userid = user.getData().getUserId();
        showLoadingDialog("加载中");
        getBankCardList();

        Log.e("userid", "initData: "+userid );
        getPayMoneyList(userid);
    }

    private List<String> moneys = new ArrayList<>();

    private void getPayMoneyList(String userid) {
        moneys.clear();
        Call<PayMoney> call = mHttpApi.queryPayMoneyList(userid);
        Callback<PayMoney> callback = new EasyCallBack<PayMoney>() {
            @Override
            public void onSuccess(Call<PayMoney> call, PayMoney model) {
                if (null != model && model.getMsg().isSuccess()) {
                        if (mPaymentPayOther.isChecked()) {//为他人充值
                            mPayMoney = model.getData();
                            if (mPayMoney.size() != 0) {
                                moneyType = mPayMoney.get(0).getMoney();
                                mSelectedMoney = mPayMoney.get(0);
                            }
                            List<PayMoney.DataBean> data = model.getData();
                            for (int i = 0; i < data.size(); i++) {
                                moneys.add(data.get(i).getMoney() + "元");

                            }

                        }else{ //为自己充值

                            List<PayMoney.DataBean> data1 = model.getData();
                            mPayMoney = model.getData();


                            int roleType = ShoppingMallApp.getInstance().getUserInfo().getData().getRoleType();//角色级别
                            switch (roleType) {
                                case 0://会员
                                    mPayMoney = data1;

                                    if (mPayMoney.size() != 0) {
                                        moneyType = mPayMoney.get(0).getMoney();
                                        mSelectedMoney = mPayMoney.get(0);
                                    }
                                    for (int i = 0; i < data1.size(); i++) {
                                        moneys.add(data1.get(i).getMoney() + "元");
                                    }
                                    break;
                                case 1://初级微股东
                                    data1.remove(0);
                                    mPayMoney = data1;

                                        if (mPayMoney.size() != 0) {
                                            moneyType = mPayMoney.get(0).getMoney();
                                            mSelectedMoney = mPayMoney.get(0);
                                        }
                                        for (int i = 0; i < data1.size(); i++) {
                                            moneys.add(data1.get(i).getMoney() + "元");
                                        }


                                    break;

                                case 2://中级级微股东
                                    data1.remove(0);
                                    data1.remove(0);
                                    mPayMoney = data1;

                                        if (mPayMoney.size() != 0) {
                                            moneyType = mPayMoney.get(0).getMoney();
                                            mSelectedMoney = mPayMoney.get(0);
                                        }
                                        for (int i = 0; i < data1.size(); i++) {
                                            moneys.add(data1.get(i).getMoney() + "元");
                                        }
                                    break;

                                case 3://高级级微股东

                                    break;
                            }


                        }

                } else {
                    ToastUtils.showToast(PaymentActivity.this, null == model ? "请求失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }

    @Override
    public void addListeners() {
        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton
                .OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mPaymentEtPayOther.setVisibility(View.VISIBLE);
                    mPaymentEtPayOther.setText("");
                    getPayMoneyList(ShoppingMallApp.getInstance().getUserInfo().getData().getUserId());
                } else {
                    mPaymentEtPayOther.setVisibility(View.GONE);
                    mPaymentEtPayOther.setText("");
                    getPayMoneyList(ShoppingMallApp.getInstance().getUserInfo().getData().getUserId());
                }
                toptype = 0;
                tvTopType.setText("会员充值：");
                tvMoney.setVisibility(View.GONE);
                tvSelete.setText("会员充值");
                iv_moneydown.setVisibility(View.GONE);
                etVipmoney.setVisibility(View.VISIBLE);
                tvTotalmoney.setText("0 元");
            }
        };
        mPaymentPayOther.setOnCheckedChangeListener(changeListener);
        etVipmoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvTotalmoney.setText(charSequence + "元");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    private void getBankCardList() {
        Call<CompanyBank> call = mHttpApi.queryCompanyBankList();
        Callback<CompanyBank> callback = new EasyCallBack<CompanyBank>() {
            @Override
            public void onSuccess(Call<CompanyBank> call, CompanyBank model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess()) {
                    mCompanyBank = model;
                    mPaymentBankSelector.removeAllViews();
                    mAllBanks.clear();
                    for (int i = 0; i < mCompanyBank.getData().size(); i++) {
                        CompanyBank.DataBean bankNum = mCompanyBank.getData().get(i);
                        RadioButton radioButton = new RadioButton(PaymentActivity.this);
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup
                                .LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        radioButton.setLayoutParams(params);
                        if (i == 0) {
                            mBankNum = bankNum.getBankNum();
                            radioButton.setChecked(true);
                        }
                        Integer id = generateViewId();
                        radioButton.setId(id);
                        mAllBanks.put(id, bankNum.getBankNum());
                        radioButton.setText(bankNum.toString());
                        mPaymentBankSelector.addView(radioButton);
                    }
                } else {
                    ToastUtils.showToast(PaymentActivity.this, null == model ? "请求失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }

    @OnClick({R.id.payment_upload, R.id.payment_submit, R.id.tv_selete, R.id.tv_moneys})
    public void onViewClicked(View view) {
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        final String userId = user.getData().getUserId();
        switch (view.getId()) {
//            case R.id.payment_btn_generate_payid:
//                if (!TextUtils.isEmpty(submitKey)) {
//                    long time = Long.valueOf(submitKey);
//                    if ((System.currentTimeMillis() - time) > 15 * 60 * 1000) {
//                        submitKey = String.valueOf(System.currentTimeMillis());
//                        SharedPreferencesUtils.put(this, MONEY_CODE_KEY, submitKey);
//                    } else {
//                        ToastUtils.showToast(this, "汇款码有效期(15分钟)内,无需重新生成");
//                    }
//                } else {
//                    submitKey = String.valueOf(System.currentTimeMillis());
//                    SharedPreferencesUtils.put(this, MONEY_CODE_KEY, submitKey);
//                }
//                mPaymentPayid.setText(submitKey);
//                break;

            case R.id.tv_selete:
                int roleType = ShoppingMallApp.getInstance().getUserInfo().getData().getRoleType();//角色级别
                if (!mPaymentPayOther.isChecked()&&roleType == 3) {//选择为自己充值

                    return;
                }
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return;
                }
                popuType = 0;
                List<String> list = new ArrayList<>();

                    list.add("会员充值");
                    list.add("微股东充值");

                showPopu(list, tvSelete);
                break;
            case R.id.tv_moneys:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    return;
                }
                popuType = 1;

                showPopu(moneys, etVipmoney);
                break;
            case R.id.payment_upload:
                mImagePath = null;
                Intent upload = new Intent(this, UploadImagesActivity.class);
                startActivityForResult(upload, REQUEST_UPLOAD_IMG);
                break;
            case R.id.payment_submit:
//                String submitUserId = mPaymentPayid.getText().toString().trim();

                if (toptype == 0) {//会员充值
                    money = etVipmoney.getText().toString().trim();//会员充值金额
                    if (TextUtils.isEmpty(money)) {
                        ToastUtils.showToast(PaymentActivity.this, "充值金额不能为空");
                        return;
                    }
                    if (Long.valueOf(money) % 100 != 0) {
                        ToastUtils.showToast(PaymentActivity.this, "充值金额为100的倍数");
                        return;
                    }
                } else {
                    money = moneyType;//选择的微股东充值金额
                }
                if (null != mImagePath) {
                    if (null == mBankNum) {
                        ToastUtils.showToast(this, "请选择商家账户");
                        return;
                    }
                    final String targetUser = mPaymentEtPayOther.getText().toString().trim();

                    if (mPaymentPayOther.isChecked() && TextUtils.isEmpty(targetUser)) {
                        ToastUtils.showToast(this, "请输入对方账号");
                        return;

                    }
                    if (!TextUtils.isEmpty(money)) {
                        showLoadingDialog("订单提交中。。。", false);
                        compressImage(mImagePath, outFile.getAbsolutePath(), new
                                IOnCompressCompleted() {
                                    @Override
                                    public void completed(String outPath, File outFile) {

                                        LogUtils.d("file图片：" + outFile);
                                        uploadOrder(userId, money, mBankNum,
                                                outFile.getAbsolutePath(), targetUser);
                                    }
                                });
                    } else {
                        ToastUtils.showToast(this, "请填入充值金额，并生成汇款码");
                    }
                } else {
                    ToastUtils.showToast(this, "请先选取汇款凭证");
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            String path = data.getStringExtra(UploadImagesActivity.IMAGE_PATH);
            if (!TextUtils.isEmpty(path)) {
                ToastUtils.showToast(this, "已提交");
                mImagePath = path;
            }
        }
    }

    /**
     * @param userId     用户id
     * @param money      充值的金额
     * @param bankNum    银行卡号
     * @param filePath   汇款凭证图片
     * @param targetUser 他人账号(输入就传不输入不传）
     *                   isUpgrade   会员传0  微股东传1
     *                   rechargeCode   当前时间
     */
    private void uploadOrder(String userId, String money, String bankNum,
                             String filePath, String targetUser) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);//用户id
        params.put(ParamKey.COMPANY_BANK, bankNum);//银行卡号
        params.put(ParamKey.RECHARGE_MONEY, money);//金额
        params.put(ParamKey.ISUPGRADE, toptype);//充值方式0为会员1为微股东
        if (!TextUtils.isEmpty(targetUser)) {
            params.put(ParamKey.TARGET_USER, targetUser);//是否为他人充值
        }
        /*
         * userid
         * */
        params.put(ParamKey.RECHARGE_CODE, String.valueOf(System.currentTimeMillis()));//汇款码传当前时间
        final File file = new File(filePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        Call<SubmitOrder> call = mHttpApi.submitOrder(params, requestBody);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                dismissLoading();
                if (null != model) {
                    if (model.getMsg().isSuccess()) {
//                        submitKey = null;
                        mPaymentPayOther.setChecked(false);
                        mPaymentEtPayOther.setText("");
                        mImagePath = null;
                        mPaymentPayid.setText(String.valueOf(System.currentTimeMillis()));
//                        SharedPreferencesUtils.put(PaymentActivity.this, MONEY_CODE_KEY, "");
                        ToastUtils.showToast(PaymentActivity.this, "提交成功");
                        Intent success = new Intent(PaymentActivity.this, SuccessActivity.class);
                        success.putExtra(SuccessActivity.SHOW_TYPE, SuccessActivity.SHOW_PAY);
                        startActivity(success);
                    } else {
                        ToastUtils.showToast(PaymentActivity.this, model.getMsg().getInfo());
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

    /**
     * 选择的充值金额
     */
    private String moneyType;
    private int toptype;//充值类型

    public void showPopu(List<String> sours, View parent) {
        View view = LayoutInflater.from(PaymentActivity.this).inflate(R.layout.popu_list, null);
        popupWindow = new PopupWindow(view, parent.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        LogUtils.d("显示：" + popupWindow.isShowing());
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        ListView lv_list = view.findViewById(R.id.lv_list);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });
        lv_list.setAdapter(new PopuListAdapter(PaymentActivity.this, sours));
        lv_list.setOnItemClickListener(popuClick);
        popupWindow.showAsDropDown(parent, 0, 3);
        LogUtils.d("显示2：" + popupWindow.isShowing());
    }



    //0选择会员活微股东 1选择充值金额
    private int popuType;
    private AdapterView.OnItemClickListener popuClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (popuType == 0) {
                if (i == 0) {//会员
                    toptype = 0;
                    tvTopType.setText("会员充值：");
                    tvMoney.setVisibility(View.GONE);
                    tvSelete.setText("会员充值");
                    iv_moneydown.setVisibility(View.GONE);
                    etVipmoney.setVisibility(View.VISIBLE);
                    tvTotalmoney.setText("0 元");
                    popupWindow.dismiss();
                } else {//微股东
                    toptype = 1;
                    tvSelete.setText("微股东充值");
                    tvTopType.setText("微股东充值：");
                    iv_moneydown.setVisibility(View.VISIBLE);
                    tvMoney.setVisibility(View.VISIBLE);
                    etVipmoney.setVisibility(View.GONE);
                    if(null!=mPayMoney&&mPayMoney.size()>0){
                        tvTotalmoney.setText(mPayMoney.get(0).getMoney() + "元");
                        tvMoney.setText(mPayMoney.get(0).getMoney() + "元");
                    }

                    popupWindow.dismiss();
                }
            } else {
                moneyType = mPayMoney.get(i).getMoney();
                mSelectedMoney = mPayMoney.get(i);
                if(null!=mPayMoney&&mPayMoney.size()>0){
                    tvMoney.setText(mPayMoney.get(i).getMoney() + "元");
                    tvTotalmoney.setText(mPayMoney.get(i).getMoney() + "元");
                }

                popupWindow.dismiss();
            }

        }
    };
}
