package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
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
 * 积分明细界面
 */
public class PointsDetailsActivity extends AbstractPermissionActivity {

    public static final String LIMIT = "10";
    private static final String YEAR_FORMAT = "%s年";
    private static final String MONTH_FROMAT = "%s月";

    @BindView(R.id.points_date_container)
    RelativeLayout mPointsDateContainer;
    @BindView(R.id.points_tv_out)
    TextView mPointsTvOut;
    @BindView(R.id.points_out_container)
    RelativeLayout mPointsOutContainer;
    @BindView(R.id.points_tv_income)
    TextView mPointsTvIncome;
    @BindView(R.id.points_income_container)
    RelativeLayout mPointsIncomeContainer;
    @BindView(R.id.details_list)
    RecyclerView mDetailsList;
    @BindView(R.id.points_details_year)
    TextView mPointsDetailsYear;
    @BindView(R.id.points_details_month)
    TextView mPointsDetailsMonth;
    @BindView(R.id.easylayout)
    EasyRefreshLayout mEasyRefreshLayout;
    @BindView(R.id.points_title)
    CommonTitle mPointsTitle;

    private UserTransationAdapter mAdapter;
//    private Api mPointApi;//网络请求
    private int mPage = 1;//页数，用于获取数据

    private String yyyyDD;

    @Override
    public void initUIWithPermission() {
        mPointsDetailsYear.setText(String.format(YEAR_FORMAT, TimeUtils.getThisYear()));
        mPointsDetailsMonth.setText(String.format(MONTH_FROMAT, TimeUtils.getMonth()));
        mDetailsList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new UserTransationAdapter(this, 1);
        mDetailsList.setAdapter(mAdapter);
        mPointsTitle.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help = new Intent(PointsDetailsActivity.this, JifenhelpActivity.class);
                /*help.putExtra(CommonWebBrowser.SHOW_TITLE, true);
                help.putExtra(CommonWebBrowser.BROWSER_URL, NetConstant.HELP_URL_POINTS);
                help.putExtra(CommonWebBrowser.BROWSER_TITLE, getString(R.string.help_title_hint));*/
                startActivity(help);
            }
        });
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_points_details;
    }

    @Override
    public void initData() {
        mPage = 1;
        yyyyDD = TimeUtils.getThisMonth();
        loadData(true, mPage + "", yyyyDD);
        queryWater(yyyyDD, "1");
    }

    private void queryWater(String yyyyDD, String flowType) {
        flowType = "1";
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Call<QueryWater> call = mHttpApi.queryWater(userId, yyyyDD, flowType);
        Callback<QueryWater> callback = new EasyCallBack<QueryWater>() {
            @Override
            public void onSuccess(Call<QueryWater> call, QueryWater model) {
                if (null != model && model.getMsg().isSuccess()) {
                    mPointsTvOut.setText(String.valueOf(model.getData().getTansactionNum1()));
                    mPointsTvIncome.setText(String.valueOf(model.getData().getTansactionNum2()));
                } else {
                    ToastUtils.showToast(PointsDetailsActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }

    private void loadData(final boolean isFresh, String page, String yyyyDD) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
//        mPointApi = ApiServiceImp.getRetrofit(Api.class);
        Call<UserTransationResult> resultCall = mHttpApi.getUserTransationResult("1",
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

    @OnClick({R.id.points_date_container, R.id.points_out_container, R.id.points_income_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.points_date_container:
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
                        mPointsDetailsYear.setText(String.format(YEAR_FORMAT, year));
                        mPointsDetailsMonth.setText(String.format(MONTH_FROMAT, month));
                        mPage = 1;
                        yyyyDD = year + month;
                        loadData(true, mPage + "", yyyyDD);
                        queryWater(yyyyDD, "1");
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
                break;
            case R.id.points_out_container:
                break;
            case R.id.points_income_container:
                break;
        }
    }
}
