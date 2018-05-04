package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.ProductCatListAdapter;
import com.xxc.shoppingmall.adapter.ProductDescInfoAdapter;
import com.xxc.shoppingmall.db.MyDao;
import com.xxc.shoppingmall.model.Address;
import com.xxc.shoppingmall.model.ProductBannerBean;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.model.ShopCatListBean;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.utils.GlideImageLoader;
import com.xxc.shoppingmall.widget.NoScrollListView;
import com.youth.banner.Banner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class ProductDescActivity extends AbstractPermissionActivity {
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_jian)
    Button btnJian;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.btn_jia)
    Button btnJia;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.btn_joincat)
    Button btnJoincat;
    @BindView(R.id.btn_buy)
    Button btnBuy;
    @BindView(R.id.rl_shopcat)
    RelativeLayout rl_shopcat;
    @BindView(R.id.lv_piclist)
    NoScrollListView lvPicList;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.webPic)
    WebView webPic;
    private ProductListBean.DataBean dataBean;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.activity_product_desc;
    }

    @Override
    public void initData() {
        register();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
        layoutParams.height = displayMetrics.widthPixels;
        banner.setLayoutParams(layoutParams);
        dataBean = (ProductListBean.DataBean) getIntent().getSerializableExtra("date");
        Glide.with(this).load("http://122.114.5.231:8180/api/showImg/" + dataBean.getImgUrl()).into(ivPic);
        tvName.setText(dataBean.getDescription());
        tvPrice.setText("¥ " + dataBean.getPrice());
            List<String> images =  new ArrayList<>();
            images.add(NetConstant.IMGAE_PATH+dataBean.getImgUrl());
        banner.setImages(images);
        banner.setImageLoader(new GlideImageLoader()).setDelayTime(6 * 1000);
        banner.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo userInfo = ShoppingMallApp.getInstance().getUserInfo();
        if (null == userInfo) {
            ToastUtils.showToast(this, "请先登录！！");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        getAddressList(userInfo.getData().getUserId());
        getBannerList();
        getInfoList();
    }

    private void getInfoList() {
        Map<String,Object> map = new HashMap<>();
        map.put("productCode",dataBean.getProductCode());
        Call<ProductBannerBean> call = mHttpApi.getProductInfo(map);
        EasyCallBack<ProductBannerBean> callBack = new EasyCallBack<ProductBannerBean>() {
            @Override
            public void onSuccess(Call<ProductBannerBean> call, ProductBannerBean model) {
                List<ProductBannerBean.DataBean> data = model.getData();
                String content = "";
                for (int i = 0; i < data.size(); i++) {
                    content+="<img src="+(NetConstant.IMGAE_PATH+data.get(i).getDatasource()+"/"+data.get(i).getAttachment())+" width=\"100%\"style=\"vertical-align: middle;\"/>";
                }
                String pichtml= "<html><head><meta charset=\"UTF-8\">\n" +
                        "\t\t<title>商品详情</title></head><body><div line-height:0>"+content+"</div></body></html>";

                webPic.loadData(pichtml,"text/html","utf-8");

//                lvPicList.setAdapter(new ProductDescInfoAdapter(ProductDescActivity.this,data));
            }
        };
        requestApi(call,callBack);
    }


    private void getBannerList() {
        showLoadingDialog();
        Map<String,Object> map = new HashMap<>();
        map.put("productCode",dataBean.getProductCode());
        Call<ProductBannerBean> call = mHttpApi.getProductBanner(map);
        EasyCallBack<ProductBannerBean> callBack = new EasyCallBack<ProductBannerBean>() {
            @Override
            public void onSuccess(Call<ProductBannerBean> call, ProductBannerBean model) {
                List<ProductBannerBean.DataBean> data = model.getData();
                List<String> images = new ArrayList<>();
                for (int i = 0; i < model.getData().size(); i++) {
                    ProductBannerBean.DataBean dataBeanbanner = model.getData().get(i);
                    images.add(NetConstant.IMGAE_PATH+dataBeanbanner.getDatasource()+"/"+dataBeanbanner.getAttachment());
                }
                if (images.size()!= 0) {

                    banner.setImages(images);
                    banner.setImageLoader(new GlideImageLoader()).setDelayTime(6 * 1000);
                    banner.start();
                }
            }
        };
        requestApi(call,callBack);
    }

    @Override
    public void addListeners() {

    }

    private int num = 1;

    @OnClick({R.id.rl_shopcat, R.id.btn_jian, R.id.btn_jia, R.id.btn_joincat, R.id.btn_buy})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.rl_shopcat://购物车
                    intent = new Intent(this,ShopCatActivity.class);
                    startActivity(intent);
                break;
            case R.id.btn_jian:
                if (num == 1) {
                    num = 1;
                } else {
                    num--;
                }
                tvNum.setText(num + "");
                break;
            case R.id.btn_jia:
                num++;
                tvNum.setText(num + "");
                break;
            case R.id.btn_joincat://加入购物车
                MyDao.getInstence(this).queryDate(dataBean, num,this);
                break;
            case R.id.btn_buy://直接购买
                intent = new Intent(this, AffOrderActivity.class);
                List<ShopCatListBean> catListBeans = new ArrayList<>();
                ShopCatListBean catListBean = new ShopCatListBean();
                catListBean.setNum(num);
                catListBean.setShopid(dataBean.getId());
                catListBean.setStatus(dataBean.getStatus());
                catListBean.setProductCode(dataBean.getProductCode());
                catListBean.setPrice(dataBean.getPrice());
                catListBean.setName(dataBean.getName());
                catListBean.setImgUrl(dataBean.getImgUrl());
                catListBean.setDescription(dataBean.getDescription());
                catListBean.setCreateTime(dataBean.getCreateTime());
                catListBean.setUpdateTime(dataBean.getUpdateTime());
                catListBeans.add(catListBean);
                intent.putExtra("date",(Serializable)catListBeans);
                startActivity(intent);
                break;
        }
    }
    private void getAddressList(String userId) {
        Call<Address> call = mHttpApi.getAddressList(userId);
        Callback<Address> callback = new EasyCallBack<Address>() {
            @Override
            public void onSuccess(Call<Address> call, Address model) {
                dismissLoading();
                if (null != model) {
                    for (int i = 0; i < model.getData().size(); i++) {
                        Address.DataBean dataBean = model.getData().get(i);
                        if (dataBean.getIsDefault() == 1) {//默认地址
                            tvAddress.setText(dataBean.getUserAddress());
                        }
                    }
                }
            }
        };
        requestApi(call, callback);
    }
}
