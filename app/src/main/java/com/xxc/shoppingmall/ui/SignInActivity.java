package com.xxc.shoppingmall.ui;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.sunfusheng.marqueeview.MarqueeView;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.SignInDateAdapter;
import com.xxc.shoppingmall.model.ClickSignBean;
import com.xxc.shoppingmall.model.SignBottomBean;
import com.xxc.shoppingmall.model.SignBottomSlBena;
import com.xxc.shoppingmall.model.SignTopBean;
import com.xxc.shoppingmall.model.TodaySignBean;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 签到界面
 */
public class SignInActivity extends AbstractPermissionActivity {
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.rl_sign)
    RelativeLayout rlSign;
    @BindView(R.id.ll_today_sign)
    LinearLayout llTodaySign;
    @BindView(R.id.ll_lianxu)
    LinearLayout llLianxu;
    @BindView(R.id.tv_total_inte)
    TextView tvTotalInte;
    @BindView(R.id.tv_jifen)
    TextView tvJifen;
    @BindView(R.id.tv_signtype)
    TextView tvSigntype;
    @BindView(R.id.tv_lianxu)
    TextView tvLianxu;
    private List<SignBottomSlBena> slBenas;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.activity_sign_in;
    }

    @Override
    public void initData() {
        ButterKnife.bind(this);
        slBenas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SignBottomSlBena slBena = new SignBottomSlBena();
            slBena.setIntegrationNum("0");
            slBena.setSignedLastTime(getOldDate(i - 2));
            slBena.setStatus(0);
            slBenas.add(slBena);
        }
        GridLayoutManager manager = new GridLayoutManager(SignInActivity.this, 5);
        rvList.setLayoutManager(manager);
        rvList.setAdapter(new SignInDateAdapter(slBenas));
        getListData();
    }
    /**
     * 获取当前界面数据展示
     */
    private void getListData() {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Map<String, Object> params = new HashMap<>();
        //传入用户id
        params.put(ParamKey.USERID, userId);
        Call<SignTopBean> call = mHttpApi.SignTop(params);
        Callback<SignTopBean> callback = new EasyCallBack<SignTopBean>() {
            @Override
            public void onSuccess(Call<SignTopBean> call, SignTopBean model) {
                List<SignTopBean.DataBean> data = model.getData();
                List<String> info = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    info.add(data.get(i).getNickname() + "签到成功获得" + data.get(i).getIntegrationNum() + "积分");
                }
                marqueeView.startWithList(info);
                getBottom();
                getToday();

            }
        };
        //发起请求
        requestApi(call, callback);
    }

    /**
     * 获取今日
     */
    private void getToday() {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        LogUtils.i(userId + "用户id");
        Map<String, Object> params = new HashMap<>();
        //传入用户id
        params.put(ParamKey.USERID, userId);
        Call<TodaySignBean> call = mHttpApi.TodaySign(params);
        EasyCallBack<TodaySignBean> callBack = new EasyCallBack<TodaySignBean>() {
            @Override
            public void onSuccess(Call<TodaySignBean> call, TodaySignBean model) {
                List<TodaySignBean.DataBean> data = model.getData();
                if (data != null && data.size() != 0) {
                    int status = data.get(0).getStatus();
                    todaystatus = status;
                    if (status == 1) {//已签到
                        rlSign.setBackgroundResource(R.mipmap.icon_signjinbi);
                        llTodaySign.setVisibility(View.VISIBLE);
                        tvJifen.setText(data.get(0).getJifei());
                        tvLianxu.setText(data.get(0).getDay()+"");
                        llLianxu.setVisibility(View.VISIBLE);
                        tvTotalInte.setText("已领取积分："+data.get(0).getZongjifei());
                    }
                }
            }
            @Override
            public void onFailure(Call<TodaySignBean> call, Throwable t) {
               LogUtils.d("err:"+t.getMessage());
            }
        };
        requestApi(call, callBack);
    }
    private int todaystatus;
    private void getBottom() {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        LogUtils.i(userId + "用户id");
        Map<String, Object> params = new HashMap<>();
        //传入用户id
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.NOWDAY, getOldDate(0));
        params.put(ParamKey.NEXTDAY, getOldDate(-2));
        Call<SignBottomBean> call = mHttpApi.SignBottom(params);
        EasyCallBack<SignBottomBean> callBack = new EasyCallBack<SignBottomBean>() {
            @Override
            public void onSuccess(Call<SignBottomBean> call, SignBottomBean model) {
                List<SignBottomBean.DataBean> data = model.getData();
                //循环遍历去重  并把 真实数据保存到集合
                for (int j = 0; j < data.size(); j++) {
                    for (int i = 0; i < slBenas.size(); i++) {
                        if (slBenas.get(i).getSignedLastTime().equals(data.get(j).getTime())) {
                            slBenas.get(i).setIntegrationNum(data.get(j).getJifei());
                            slBenas.get(i).setStatus(data.get(j).getState());
                        } else {
                            continue;
                        }
                    }
                }
                GridLayoutManager manager = new GridLayoutManager(SignInActivity.this, 5);
                rvList.setLayoutManager(manager);
                rvList.setAdapter(new SignInDateAdapter(slBenas));
            }
        };
        requestApi(call, callBack);
    }

    @Override
    public void addListeners() {

    }
    @Override
    public void onStart() {
        super.onStart();
        marqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        marqueeView.stopFlipping();
    }

    /**
     * 点击签到
     */
    @OnClick(R.id.rl_sign)
    public void onViewClicked() {
        if (todaystatus == 1) {

            return;
        }
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Map<String, Object> params = new HashMap<>();
        //传入用户id
        params.put(ParamKey.USERID, userId);
        //当前年月日
        long l = System.currentTimeMillis();
        Date date = new Date(l);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        params.put("nowtime", dateFormat.format(date));
        Call<ClickSignBean> call = mHttpApi.signClick(params);
        Callback<ClickSignBean> callback = new EasyCallBack<ClickSignBean>() {
            @Override
            public void onSuccess(Call<ClickSignBean> call, ClickSignBean model) {
                ClickSignBean.MsgBean msg = model.getMsg();
                if (msg.isSuccess()) {
                    ToastUtils.showToast(SignInActivity.this, "签到成功");
                    getToday();
                    getBottom();
                } else
                    ToastUtils.showToast(SignInActivity.this, "签到失败");
            }
        };
        //发起请求
        requestApi(call, callback);
    }

    /**
     * 获取前n天日期、后n天日期
     *
     * @param distanceDay 前几天 如获取前7天日期则传-7即可；如果后7天则传7
     * @return
     */
    public static String getOldDate(int distanceDay) {
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_MONTH, distanceDay);
        Date time = ca.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        LogUtils.d(format.format(time));
        return format.format(time);
    }

}
