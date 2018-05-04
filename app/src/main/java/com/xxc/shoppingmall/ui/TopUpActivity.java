package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.MerchAccAdapter;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.MerchAccBean;
import com.xxc.shoppingmall.model.TopUpMoneyListBean;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.base.IOnCompressCompleted;
import com.xxc.shoppingmall.widget.NoScrollListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 充值中心
 */
public class TopUpActivity extends AbstractPermissionActivity {
    private static final int REQUEST_UPLOAD_IMG = 101;
    @BindView(R.id.et_vipmoney)
    EditText etVipmoney;
    @BindView(R.id.sp_list)
    Spinner spList;
    @BindView(R.id.tv_totalmoney)
    TextView tvTotalmoney;
    @BindView(R.id.lv_acclist)
    NoScrollListView lvAcclist;
    @BindView(R.id.sp_seletet)
    Spinner spSeletet;
    @BindView(R.id.tv_top_type)
    TextView tvTopType;
    @BindView(R.id.payment_pay_other)
    CheckBox paymentPayOther;
    @BindView(R.id.payment_payid)
    TextView paymentPayid;
    @BindView(R.id.payment_et_pay_other)
    EditText paymentEtPayOther;
    @BindView(R.id.payment_upload)
    TextView paymentUpload;
    @BindView(R.id.payment_submit)
    Button paymentSubmit;
    private List<TopUpMoneyListBean.DataBean> data;
    private MerchAccAdapter merchAccAdapter;
    private String mImagePath;//证书凭证

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.activity_top_up;
    }

    @Override
    public void initData() {
        List<String> list = new ArrayList<>();
        list.add("会员充值");
        list.add("微股东充值");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TopUpActivity.this, android.R.layout.simple_expandable_list_item_1, list);
        spSeletet.setAdapter(arrayAdapter);
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        getToupList(userId);
        getBankList();
    }

    /**
     * 获取银行卡列表
     */
    private void getBankList() {
        Call<MerchAccBean> call = mHttpApi.getMerchAcc();
        EasyCallBack<MerchAccBean> callBack = new EasyCallBack<MerchAccBean>() {
            @Override
            public void onSuccess(Call<MerchAccBean> call, MerchAccBean model) {
                if (model != null) {
                    List<MerchAccBean.DataBean> data = model.getData();
                    if (data != null) {
                        merchAccAdapter = new MerchAccAdapter(TopUpActivity.this, data);
                        lvAcclist.setAdapter(merchAccAdapter);
                    }
                }
            }
        };
        requestApi(call, callBack);
    }

    /**
     * 请求充值金额列表
     *
     * @param userId
     */
    private void getToupList(String userId) {
        Map<String, Object> params = new HashMap<>();
        //传入用户id
        params.put(ParamKey.USERID, userId);
        Call<TopUpMoneyListBean> call = mHttpApi.topUpmoneyList(params);
        EasyCallBack<TopUpMoneyListBean> callBack = new EasyCallBack<TopUpMoneyListBean>() {
            @Override
            public void onSuccess(Call<TopUpMoneyListBean> call, TopUpMoneyListBean model) {
                List<String> list = new ArrayList<>();
                if (model != null) {
                    data = model.getData();
                    for (int i = 0; i < data.size(); i++) {
                        list.add(data.get(i).getMoney() + "元");
                    }

                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TopUpActivity.this, android.R.layout.simple_expandable_list_item_1, list);
                spList.setAdapter(arrayAdapter);

            }
        };
        requestApi(call, callBack);
    }

    /**
     * 选择的充值金额类型
     */
    private int moneyType;

    @Override
    public void addListeners() {
        spList.setOnItemSelectedListener(spnnerListener);
        spSeletet.setOnItemSelectedListener(spnnerSeleteListener);
        lvAcclist.setOnItemClickListener(clickListener);
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

    private AdapterView.OnItemSelectedListener spnnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            moneyType = data.get(i).getType();
            tvTotalmoney.setText(data.get(i).getMoney() + "元");
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private int toptype;
    private AdapterView.OnItemSelectedListener spnnerSeleteListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            toptype = i;//赋值充值类型 0未会员  1为微股东
            if (i == 0) {//会员
                tvTopType.setText("会员充值：");
                spList.setVisibility(View.GONE);
                etVipmoney.setVisibility(View.VISIBLE);
                tvTotalmoney.setText("0 元");
            } else {
                tvTopType.setText("微股东充值：");
                spList.setVisibility(View.VISIBLE);
                etVipmoney.setVisibility(View.GONE);
                tvTotalmoney.setText(data.get(0).getMoney() + "元");
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            merchAccAdapter.setcheckPos(i);
        }
    };



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
    @OnClick({R.id.payment_upload, R.id.payment_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.payment_upload:
                mImagePath = null;
                Intent upload = new Intent(this, UploadImagesActivity.class);
                startActivityForResult(upload, REQUEST_UPLOAD_IMG);
                break;
            case R.id.payment_submit:

                break;
        }
    }
}
