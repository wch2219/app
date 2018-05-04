package com.xxc.shoppingmall.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.download.DownloadListener;
import com.tencent.bugly.beta.download.DownloadTask;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.BannerModel;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.NoticeModel;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.AccountSettingActivity;
import com.xxc.shoppingmall.ui.ConvertPointsActivity;
import com.xxc.shoppingmall.ui.DigCashActivity;
import com.xxc.shoppingmall.ui.MyTeamActivity;
import com.xxc.shoppingmall.ui.OfflineMallActivity;
import com.xxc.shoppingmall.ui.PaymentActivity;
import com.xxc.shoppingmall.ui.RecommendActivity;
import com.xxc.shoppingmall.ui.VoucherTicketsActivity;
import com.xxc.shoppingmall.ui.base.BaseLazyFragment;
import com.xxc.shoppingmall.utils.GlideImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/17.
 * 主界面---首页
 */
public class HomeFragment extends BaseLazyFragment {

    private static final int REQUEST_PAY = 100;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.home_unlogin_image_header)
    ImageView homeUnloginImageHeader;
    @BindView(R.id.rl_unlogin)
    RelativeLayout rlUnlogin;
    @BindView(R.id.home_login_image_header)
    ImageView homeLoginImageHeader;
    @BindView(R.id.home_v_type)
    ImageView homeVType;
    @BindView(R.id.tv_headbottom_name)
    TextView tvHeadbottomName;
    @BindView(R.id.tv_acc)
    TextView tvAcc;
    @BindView(R.id.tv_att_type)
    TextView tvAttType;
    @BindView(R.id.tv_capp_num)
    TextView tvCappNum;
    @BindView(R.id.tv_surplus)
    TextView tvSurplus;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.tv_noti)
    TextView tvNoti;
    @BindView(R.id.tv_internum)
    TextView tvInternum;
    @BindView(R.id.ll_inteag)
    LinearLayout llInteag;
    @BindView(R.id.tv_shangbiaonum)
    TextView tvShangbiaonum;
    @BindView(R.id.rl_shangbiao)
    RelativeLayout rlShangbiao;
    @BindView(R.id.tv_fuwuquan)
    TextView tvFuwuquan;
    @BindView(R.id.ll_gouwuquan)
    LinearLayout llGouwuquan;
    @BindView(R.id.rl_gouwuquan)
    RelativeLayout rlGouwuquan;
    @BindView(R.id.rl_myteam)
    RelativeLayout rlMyteam;
    @BindView(R.id.tv_shuzihuobi)
    TextView tvShuzihuobi;
    @BindView(R.id.rl_shuzihuobi)
    RelativeLayout rlShuzihuobi;
    @BindView(R.id.rl_shitishop)
    RelativeLayout rlShitishop;
    @BindView(R.id.rl_tuijian)
    RelativeLayout rlTuijian;
    Unbinder unbinder;


    private UserInfo mUserInfo;
    private boolean huiyuanclick = true;

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_home;
    }

    @Override
    public void initUI() {


    }

    @Override
    public void onResume() {
        super.onResume();
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        if (null != user) {
            rlUnlogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            getPersonInfo(user);
        } else {
            rlUnlogin.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
        }
        getBanners();
        getNotices();

    }

    @Override
    public void initData() {
        Beta.checkUpgrade();
//        Beta.installTinker(this);
    }

    @Override
    public void addListeners() {


    }

    private void getPersonInfo(LoginResult user) {
        Call<UserInfo> call = mHttpApi.getUserInfo(user.getData().getUserId());
        Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
            @Override
            public void onSuccess(Call<UserInfo> call, UserInfo model) {
                LogUtils.d(model + "");
                if (model != null) {
                    if (model.getMsg().isSuccess()) {
                        ShoppingMallApp.getInstance().setUserInfo(model);
                        refreshHomeUi(model);
                    }
                }
            }
        };
        requestApi(call, callback);
    }

    private void refreshHomeUi(UserInfo info) {
        mUserInfo = info;
        String url = NetConstant.IMGAE_PATH + info.getData().getAvatarUrl();
        if (TextUtils.isEmpty(info.getData().getIdCard())) {//未认证
            rlUnlogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            tvHeadbottomName.setText("未认证");
            tvAttType.setVisibility(View.GONE);
            loadAvatar(url, homeUnloginImageHeader);
        } else {
            tvHeadbottomName.setText(info.getData().getNickName());
            tvAttType.setVisibility(View.VISIBLE);
            loadAvatar(url, homeLoginImageHeader);
        }
        tvAcc.setText(String.format("账号:%s", info.getData().getUserName()));

        if (TextUtils.isEmpty(info.getData().getHoldIntNum()+"")) {
            tvCappNum.setText("0");
        } else {
            tvCappNum.setText(info.getData().getHoldIntNum()+"");
        }
        if (!TextUtils.isEmpty(info.getData().getDepIntNum())) {

            tvSurplus.setText(info.getData().getDepIntNum());
        }
        tvShangbiaonum.setText(String.format("%.2f", info.getData().getTicket()));//商标股
        tvFuwuquan.setText(String.format("%.2f", info.getData().getShopcoin()));
        tvShuzihuobi.setText(String.format("%.2f", info.getData().getGsc_num()));
        tvInternum.setText(String.format("%.2f", info.getData().getIntegration()));
//        if (UserInfo.V_TYPE_0 == info.getData().getRoleType()) {
//            huiyuanclick = false;
//            llGouwuquan.setBackgroundResource(R.color.color_42ffffff);
//        } else {
//            huiyuanclick = true;
//            llGouwuquan.setBackgroundResource(R.color.color_ccffffff);
//        }
        switch (info.getData().getRoleType()) {
            case UserInfo.V_TYPE_0:
                homeVType.setVisibility(View.GONE);
                break;
            case UserInfo.V_TYPE_1:
                homeVType.setVisibility(View.VISIBLE);
                homeVType.setImageResource(R.drawable.home_header_v_1);
                break;
            case UserInfo.V_TYPE_2:
                homeVType.setVisibility(View.VISIBLE);
                homeVType.setImageResource(R.drawable.home_header_v_2);
                break;
            case UserInfo.V_TYPE_3:
                homeVType.setVisibility(View.VISIBLE);
                homeVType.setImageResource(R.drawable.home_header_v_3);
                break;
        }
    }

    private void getBanners() {
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.LIMIT, 5);
        params.put(ParamKey.PAGE, 0);
        Call<BannerModel> call = mHttpApi.getHomeBanner(params);
        Callback<BannerModel> callback = new EasyCallBack<BannerModel>() {
            @Override
            public void onSuccess(Call<BannerModel> call, BannerModel model) {

                if (model != null) {
                    if (model.getMsg().isSuccess()) {
                        List<String> images = new ArrayList<>();
                        for (int i = 0; i < model.getData().size(); i++) {
                            BannerModel.DataBean dataBean = model.getData().get(i);
                            images.add(dataBean.getImgUrl());
                            LogUtils.d(dataBean.getImgUrl()+":bann");
                        }
                        banner.setImages(images);
                        banner.setImageLoader(new GlideImageLoader()).setDelayTime(6 * 1000);
                        banner.start();
                    }
                }
            }
        };
        requestApi(call, callback);
    }

    private void getNotices() {
        Call<NoticeModel> call = mHttpApi.getNotices(1);
        Callback<NoticeModel> callback = new EasyCallBack<NoticeModel>() {
            @Override
            public void onSuccess(Call<NoticeModel> call, NoticeModel model) {
                if (model != null) {
                    if (model.getMsg().isSuccess() && null != getContext()) {
                        StringBuffer buffer = new StringBuffer();
                        for (int i = 0; i < model.getData().size(); i++) {
                            String notice = model.getData().get(i).toString();
                            if (TextUtils.isEmpty(buffer)) {
                                buffer.append(notice);
                            } else {
                                buffer.append("    " + notice);
                            }
                        }
                        tvNoti.setText(buffer);
                        setTextMarquee(tvNoti);
                    }
                }
            }
        };
        requestApi(call, callback);
    }
    public static void setTextMarquee(TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }


    @OnClick({R.id.home_login_image_header, R.id.home_unlogin_image_header, R.id.ll_inteag, R.id.rl_shangbiao, R.id.rl_gouwuquan, R.id.rl_myteam, R.id.rl_shuzihuobi, R.id.rl_shitishop, R.id.rl_tuijian})
    public void onViewClicked(View view) {
        LoginResult user = checkLogin();
        Intent intent;
        switch (view.getId()) {

            case R.id.home_login_image_header://头像
                if ((null != user) && (null != mUserInfo)) {
                    Intent accountSetting = new Intent(getContext(), AccountSettingActivity.class);
                    startActivity(accountSetting);
                } else {
                    ToastUtils.showToast(getContext(), "用户详情获取失败");
                }
                break;
            case R.id.home_unlogin_image_header:
                if ((null != user) && (null != mUserInfo)) {
                    Intent accountSetting = new Intent(getContext(), AccountSettingActivity.class);
                    startActivity(accountSetting);
                } else {
                    ToastUtils.showToast(getContext(), "用户详情获取失败");
                }
                break;
            case R.id.ll_inteag://积分

                if (null != user) {
                    if (null != mUserInfo) {
                        if (TextUtils.isEmpty(mUserInfo.getData().getIdCard())) {
                            ToastUtils.showToast(getContext(), "请先进行实名认证");
                        } else {

                            Intent convertPoints = new Intent(getContext(),
                                    ConvertPointsActivity.class);
                            convertPoints.putExtra(ConvertPointsActivity.ALL_POINTS_KEY, (int)
                                    mUserInfo.getData().getIntegration());
                            startActivityForResult(convertPoints, REQUEST_PAY);

                        }
                    } else {
                        ToastUtils.showToast(getContext(), "用户详情获取失败");
                    }
                }
                break;
            case R.id.rl_shangbiao://商标股
                if (null != user) {
                    if (null != mUserInfo) {
                        Intent tickets = new Intent(getContext(), VoucherTicketsActivity.class);
                        tickets.putExtra(VoucherTicketsActivity.TICKETS_KEY, (int) mUserInfo
                                .getData().getTicket());
                        startActivityForResult(tickets, REQUEST_PAY);
                    } else {
                        ToastUtils.showToast(getContext(), "用户详情获取失败");
                    }
                }
                break;
            case R.id.rl_gouwuquan://购物券

                if (null != user && huiyuanclick) {
                    if (null != mUserInfo) {

//                        if (!huiyuanclick) {
//
//
//                            ToastUtils.showToast(getContext(), "不可充值");
//                        } else {

                            intent = new Intent(getActivity(), PaymentActivity.class);
                            startActivity(intent);
                      //  }
                    } else {
                        ToastUtils.showToast(getContext(), "用户详情获取失败");
                    }
                }
                break;
            case R.id.rl_myteam://我的团队
                if (null != user) {
                    if (null != mUserInfo) {
                        intent = new Intent(getActivity(), MyTeamActivity.class);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(getContext(), "用户详情获取失败");
                    }
                }
                break;
            case R.id.rl_shuzihuobi://数字货币

                if (null != user) {
                    if (null != mUserInfo) {
                        Intent digCash = new Intent(getContext(), DigCashActivity.class);
                        startActivityForResult(digCash, REQUEST_PAY);
                    } else {
                        ToastUtils.showToast(getContext(), "用户详情获取失败");
                    }
                }
                break;
            case R.id.rl_shitishop://实体店
                if (null != user) {
                    if (null != mUserInfo) {
                        Intent offlineMall = new Intent(getContext(), OfflineMallActivity.class);
                        startActivity(offlineMall);
                    } else {
                        ToastUtils.showToast(getContext(), "用户详情获取失败");
                    }
                }
                break;
            case R.id.rl_tuijian://推荐
                if (null != user) {
                    if (null != mUserInfo) {
                        Intent recommend = new Intent(getContext(), RecommendActivity.class);
                        startActivity(recommend);
                    } else {
                        ToastUtils.showToast(getContext(), "用户详情获取失败");
                    }
                }
                break;
        }
    }
}
