package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.UserTransationAdapter;
import com.xxc.shoppingmall.model.QueryWater;
import com.xxc.shoppingmall.model.UserTransationResult;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.base.CommonWebBrowser;
import com.xxc.shoppingmall.utils.TimeUtils;
import com.xxc.shoppingmall.widget.CommonTitle;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/25.
 * 盾收支账本
 */
public class ShieldDetailsActivity extends AbstractPermissionActivity {

    public static final String LIMIT = "10";
    private static final String YEAR_FORMAT = "%s年";
    private static final String MONTH_FROMAT = "%s月";

    @BindView(R.id.shield_details_year)
    TextView mShieldDetailsYear;
    @BindView(R.id.shield_details_month)
    TextView mShieldDetailsMonth;
    @BindView(R.id.shield_date_container)
    RelativeLayout mShieldDateContainer;
    @BindView(R.id.shield_tv_out)
    TextView mShieldTvOut;
    @BindView(R.id.shield_out_container)
    RelativeLayout mShieldOutContainer;
    @BindView(R.id.shield_tv_income)
    TextView mShieldTvIncome;
    @BindView(R.id.shield_income_container)
    RelativeLayout mShieldIncomeContainer;
    @BindView(R.id.details_list)
    RecyclerView mDetailsList;
    @BindView(R.id.easylayout)
    EasyRefreshLayout mEasyRefreshLayout;
    @BindView(R.id.shield_title)
    CommonTitle mShieldTitle;

    private UserTransationAdapter mAdapter;
    private int mPage = 1;//页数，用于获取数据

    private String yyyyDD;

    @Override
    public void initUIWithPermission() {
        mShieldDetailsYear.setText(String.format(YEAR_FORMAT, TimeUtils.getThisYear()));
        mShieldDetailsMonth.setText(String.format(MONTH_FROMAT, TimeUtils.getMonth()));
        mDetailsList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserTransationAdapter(this, 2);
        mDetailsList.setAdapter(mAdapter);
        mShieldTitle.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help = new Intent(ShieldDetailsActivity.this, HelpActivity.class);
                help.putExtra(CommonWebBrowser.SHOW_TITLE, true);
                help.putExtra(CommonWebBrowser.BROWSER_URL, NetConstant.HELP_URL_SHIELD);
                help.putExtra(CommonWebBrowser.BROWSER_TITLE, getString(R.string.help_title_hint));
                startActivity(help);
            }
        });
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_shield_details;
    }

    @Override
    public void initData() {
        mPage = 1;
        yyyyDD = TimeUtils.getThisMonth();
        loadData(true, mPage + "", yyyyDD);
        android.util.Log.i("yyyyDD",""+yyyyDD);
        queryWater(yyyyDD);
    }

    @Override
    public void addListeners() {
        mEasyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                mPage++;
                loadData(false, mPage + "", yyyyDD);
            }

            @Override
            public void onRefreshing() {
                mPage = 1;
                loadData(true, mPage + "", yyyyDD);
            }
        });
    }

    @OnClick(R.id.shield_date_container)
    public void onViewClicked() {
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH);
        picker.setDividerColor(getResources().getColor(R.color.color_ff4040));
        picker.setTextColor(getResources().getColor(R.color.color_ff4040));
        picker.setCancelTextColor(getResources().getColor(R.color.color_ff4040));
        picker.setSubmitTextColor(getResources().getColor(R.color.color_ff4040));
        picker.setTopLineColor(getResources().getColor(R.color.color_ff4040));
        picker.setLabelTextColor(getResources().getColor(R.color.color_ff4040));
        picker.setPressedTextColor(getResources().getColor(R.color.color_ff4040));
        DatePicker.OnYearMonthPickListener onDatePickListener = new DatePicker
                .OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                mShieldDetailsYear.setText(String.format(YEAR_FORMAT, year));
                mShieldDetailsMonth.setText(String.format(MONTH_FROMAT, month));
                mPage = 1;
                yyyyDD = year + month;
                loadData(true, mPage + "", yyyyDD);
                queryWater(yyyyDD);
            }
        };
        picker.setOnDatePickListener(onDatePickListener);
        picker.setCanceledOnTouchOutside(false);
        picker.setRangeStart(2000, 1);
        Calendar cal = Calendar.getInstance();
        //当前年
        int year = cal.get(Calendar.YEAR);
        //当前月
        int month = (cal.get(Calendar.MONTH)) + 1;
        //当前月的第几天：即当前日
        int day = cal.get(Calendar.DAY_OF_MONTH);
        picker.setSelectedItem(year, month);
        picker.setRangeEnd(year, month);
        picker.show();
    }

    private void loadData(final boolean isFresh, String page, String yyyyDD) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Call<UserTransationResult> resultCall = mHttpApi.getUserTransationResult("2",
                userId, yyyyDD, LIMIT, page);
        EasyCallBack<UserTransationResult> easyCallBack = new EasyCallBack<UserTransationResult>() {
            @Override
            public void onSuccess(Call<UserTransationResult> call, UserTransationResult model) {
                if (null != model) {
                    if (isFresh) {
                        mEasyRefreshLayout.refreshComplete();
                        mAdapter.swapData(model.getData());
                    } else {
                        mEasyRefreshLayout.loadMoreComplete();
                        mAdapter.swapMoreData(model.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserTransationResult> call, Throwable t) {
                super.onFailure(call, t);
                if (isFresh) {
                    mEasyRefreshLayout.refreshComplete();
                } else {
                    mEasyRefreshLayout.loadMoreComplete();
                }
            }
        };
        requestApi(resultCall, easyCallBack);
    }

    private void queryWater(String yyyyDD) {
        String flowType = "2";
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        LogUtils.d(userId + "---");
        Call<QueryWater> call = mHttpApi.queryWater(userId, yyyyDD, flowType);
        Callback<QueryWater> callback = new EasyCallBack<QueryWater>() {
            @Override
            public void onSuccess(Call<QueryWater> call, QueryWater model) {
                if (null != model && model.getMsg().isSuccess()) {
                    Log.i("123",""+model.getData().toString());
                    mShieldTvOut.setText(String.valueOf(model.getData().getTansactionNum1()));
                    mShieldTvIncome.setText(String.valueOf(model.getData().getTansactionNum2()));
                } else {
                    ToastUtils.showToast(ShieldDetailsActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }
}
