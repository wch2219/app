package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.AffOrderAdapter;
import com.xxc.shoppingmall.db.MyDao;
import com.xxc.shoppingmall.model.Address;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.model.ResultBean;
import com.xxc.shoppingmall.model.ShopCatListBean;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.pay.PayFragment;
import com.xxc.shoppingmall.ui.pay.PayPwdView;
import com.xxc.shoppingmall.widget.CommonTitle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 确认订单
 */
public class AffOrderActivity extends AbstractPermissionActivity implements PayPwdView
        .InputCallBack{
    @BindView(R.id.commtitle)
    CommonTitle commtitle;
    @BindView(R.id.lc_list)
    ListView lcList;
    @BindView(R.id.tv_totalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.btn_gopay)
    Button btnGopay;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_adddesc)
    TextView tvAdddesc;
    @BindView(R.id.ll_seleaddress)
    LinearLayout llSeleaddress;
    @BindView(R.id.tv_totalnum)
    TextView tvTotalnum;
    @BindView(R.id.tv_seleTotalMoney)
    TextView tvSeleTotalMoney;
    private PayFragment mPayFragment;
    private double totalmoney;
    private List<ShopCatListBean> catListBeans;
    private Address.DataBean addressdataBean;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.activity_aff_order;
    }

    @Override
    public void initData() {
        register();
        catListBeans = (List<ShopCatListBean>) getIntent().getSerializableExtra("date");
        lcList.setAdapter(new AffOrderAdapter(this, catListBeans));
        int totalNum = 0;
        totalmoney = 0.0;
        for (int i = 0; i < catListBeans.size(); i++) {
            totalmoney += catListBeans.get(i).getPrice()*catListBeans.get(i).getNum();
           totalNum += catListBeans.get(i).getNum();
        }
        tvTotalnum.setText("共"+ totalNum+"件商品");
        tvSeleTotalMoney.setText("¥ "+ totalmoney);
        tvTotalPrice.setText("¥ "+ totalmoney);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList(ShoppingMallApp.getInstance().getUserInfo().getData().getUserId());
    }

    @Override
    public void addListeners() {

    }
    private void getAddressList(String userId) {
        showLoadingDialog();
        Call<Address> call = mHttpApi.getAddressList(userId);
        Callback<Address> callback = new EasyCallBack<Address>() {
            @Override
            public void onSuccess(Call<Address> call, Address model) {
                dismissLoading();
                if (null != model) {
                    for (int i = 0; i < model.getData().size(); i++) {
                        addressdataBean = model.getData().get(i);
                        if (addressdataBean.getIsDefault() == 1) {//默认地址
                            tvAdddesc.setVisibility(View.VISIBLE);
                            tvAdddesc.setText("收货地址：" + addressdataBean.getUserAddress());
                            tvName.setText("收货人：" + addressdataBean.getName());
                            tvPhone.setText(addressdataBean.getMobile());
                        }
                    }
                } else {
                    tvAdddesc.setVisibility(View.GONE);
                    tvName.setText("请添加收货地址！！");
                }
            }
        };
        requestApi(call, callback);
    }



    @OnClick({R.id.ll_seleaddress, R.id.btn_gopay})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_seleaddress:
                intent = new Intent(this,ManageAddressActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.btn_gopay://跳转支付方式
                Bundle bundle = new Bundle();
                bundle.putString(PayFragment.EXTRA_CONTENT, "结算：¥ " + totalmoney);
                mPayFragment = new PayFragment();
                mPayFragment.setArguments(bundle);
                mPayFragment.setPaySuccessCallBack(AffOrderActivity.this);
                mPayFragment.show(getSupportFragmentManager(), "Pay");
                break;
        }
    }

    @Override
    public void onInputFinish(String result) {
        if (tvAdddesc.getText().toString().equals("请添加收货地址！！")||tvAdddesc.getText().toString().equals("")) {
            ToastUtils.showToast(this,"请添加收货地址");
            return;
        }
        String productCodes="";
        for (int i = 0; i < catListBeans.size(); i++) {
            String productCode = catListBeans.get(i).getProductCode();
            if (TextUtils.isEmpty(productCodes)) {
                productCodes = productCode+":"+catListBeans.get(i).getNum();
            }else{
                productCodes= productCodes+"|"+productCode+":"+catListBeans.get(i).getNum();
            }
        }
        showLoadingDialog();
        Map<String,Object> map = new HashMap<>();
        map.put("userId",ShoppingMallApp.getInstance().getUserInfo().getData().getUserId());
        map.put("productCode",productCodes);
        map.put("integrationNum",(new Double(totalmoney)).intValue());
        map.put("user_address",addressdataBean.getUserAddress());
        map.put("transcationPwd",result);
        map.put("num",catListBeans.size());
        LogUtils.d(map.toString());
        Call<ResultBean> call = mHttpApi.SubmitOrder(map);
        EasyCallBack<ResultBean> callBack = new EasyCallBack<ResultBean>() {
            @Override
            public void onSuccess(Call<ResultBean> call, ResultBean model) {
                boolean success = model.getMsg().isSuccess();
                if (success) {
                    for (int i = 0; i <catListBeans.size() ; i++) {
                        MyDao.getInstence(AffOrderActivity.this).deleteData(catListBeans.get(i).getShopid());
                    }
                    Intent intent = new Intent(AffOrderActivity.this,PaySuccActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        requestApi(call,callBack);
    }
}
