package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.adapter.ProductCatListAdapter;
import com.xxc.shoppingmall.adapter.ShopCatAdapter;
import com.xxc.shoppingmall.db.MyDao;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.model.ShopCatListBean;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;
import com.xxc.shoppingmall.widget.NoScrollGridView;
import com.xxc.shoppingmall.widget.NoScrollListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;

/**
 * 我的购物车
 */
public class ShopCatActivity extends AbstractPermissionActivity implements AdapterView.OnItemClickListener,
        ShopCatAdapter.NumChangeListener {
    @BindView(R.id.commtitle)
    CommonTitle commtitle;
    @BindView(R.id.cb_allcheck)
    CheckBox cbAllcheck;
    @BindView(R.id.lv_list)
    NoScrollListView lvList;
    @BindView(R.id.tv_totalPrice)
    TextView tvTotalPrice;
    @BindView(R.id.btn_gopay)
    Button btnGopay;
    @BindView(R.id.ll_entry)
    LinearLayout llEntry;
    @BindView(R.id.ll_noentry)
    LinearLayout llNoentry;
     @BindView(R.id.lv_listtuijian)
     NoScrollGridView lvListTuijian;

    private List<ShopCatListBean> catListBeans;
    private ShopCatAdapter shopCatAdapter;
    private List<ProductListBean.DataBean> data;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.activity_shop_cat;
    }

    @Override
    public void initData() {
        register();



        Map<String, Object> params = new HashMap<>();
        //传入用户id
        params.put(ParamKey.PAGE, 1);
        params.put(ParamKey.LIMIT, 10);
        Call<ProductListBean> call = mHttpApi.GetProductList(params);
        EasyCallBack<ProductListBean> callBack = new EasyCallBack<ProductListBean>() {
            @Override
            public void onSuccess(Call<ProductListBean> call, ProductListBean model) {
                data = model.getData();
                ProductCatListAdapter picListAdapter = new ProductCatListAdapter(ShopCatActivity.this, data);
                lvListTuijian.setAdapter(picListAdapter);
            }
        };
        requestApi(call, callBack);
    }

    @Override
    protected void onResume() {
        catListBeans = MyDao.getInstence(this).quaryAllData();
        if (catListBeans.size() == 0) {
            cbAllcheck.setVisibility(View.GONE);
            llEntry.setVisibility(View.VISIBLE);
        }else{
            cbAllcheck.setVisibility(View.VISIBLE);
            llEntry.setVisibility(View.GONE);
        }

        shopCatAdapter = new ShopCatAdapter(this, catListBeans);
        lvList.setAdapter(shopCatAdapter);
        shopCatAdapter.setNumChangeListener(this);
        super.onResume();
    }

    @Override
    public void addListeners() {
        lvList.setOnItemClickListener(this);

        cbAllcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setAllType(isChecked);
            }
        });
        lvListTuijian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductListBean.DataBean dataBean = data.get(position);
                Intent intent = new Intent(ShopCatActivity.this,ProductDescActivity.class);
                intent.putExtra("date",dataBean);
                startActivity(intent);
            }
        });
    }

    private double totalPrice;

    private void setAllType(boolean isChecked) {
        totalPrice = 0.0;
        for (int i = 0; i < catListBeans.size(); i++) {
            ShopCatListBean catListBean = catListBeans.get(i);
            catListBean.setCheck(isChecked);
            MyDao.getInstence(this).upDataCheck(catListBean.getShopid(), isChecked, this);
            if (isChecked) {

                totalPrice += catListBean.getPrice() * catListBean.getNum();
            } else {
                totalPrice = 0.0;
            }
        }
        tvTotalPrice.setText("¥ " + totalPrice);
        shopCatAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.btn_gopay)
    public void onViewClicked() {
        List<ShopCatListBean> ListBeans = new ArrayList<>();
        for (int i = 0; i < catListBeans.size(); i++) {
            if (catListBeans.get(i).isCheck()) {
                ListBeans.add(catListBeans.get(i));
            }
        }
        if (ListBeans.size() == 0) {
            return;
        }
        Intent intent = new Intent(this, AffOrderActivity.class);
        intent.putExtra("date", (Serializable) ListBeans);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ShopCatListBean catListBean = catListBeans.get(position);
        catListBean.setCheck(!catListBean.isCheck());
        MyDao.getInstence(this).upDataCheck(catListBean.getShopid(), !catListBean.isCheck(), this);
        changeItemUi();
        shopCatAdapter.notifyDataSetChanged();
    }

    private void changeItemUi() {
        int chekNum = 0;
        totalPrice = 0.0;
        for (int i = 0; i < catListBeans.size(); i++) {
            if (catListBeans.get(i).isCheck()) {
                chekNum++;
                totalPrice += catListBeans.get(i).getPrice() * catListBeans.get(i).getNum();

            }
        }
        tvTotalPrice.setText("¥ " + totalPrice);
        if (chekNum == catListBeans.size()) {
            cbAllcheck.setChecked(true);
        } else {
            cbAllcheck.setChecked(false);
        }

    }

    @Override
    public void jiaChange(int shopid, int num) {
        MyDao.getInstence(this).upData(shopid, num + 1, this);
        catListBeans = MyDao.getInstence(this).quaryAllData();
        changeItemUi();

        shopCatAdapter = new ShopCatAdapter(this, catListBeans);
        shopCatAdapter.setNumChangeListener(this);
        lvList.setAdapter(shopCatAdapter);
    }

    @Override
    public void jianChange(int shopid, int num) {
        if (num == 1) {
            return;
        }
        MyDao.getInstence(this).upData(shopid, num - 1, this);
        catListBeans = MyDao.getInstence(this).quaryAllData();
        changeItemUi();
        shopCatAdapter = new ShopCatAdapter(this, catListBeans);
        shopCatAdapter.setNumChangeListener(this);
        lvList.setAdapter(shopCatAdapter);
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < catListBeans.size(); i++) {
            ShopCatListBean catListBean = catListBeans.get(i);
            MyDao.getInstence(this).upDataCheck(catListBean.getShopid(), false, this);
        }
        super.onDestroy();
    }

}
