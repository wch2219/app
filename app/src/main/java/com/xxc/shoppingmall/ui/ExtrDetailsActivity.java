package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajguan.library.EasyRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.ExtrAdapter;
import com.xxc.shoppingmall.model.ExtrEntity;
import com.xxc.shoppingmall.model.ExtrResult;
import com.xxc.shoppingmall.model.QueryWaterGsc;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.base.CommonWebBrowser;
import com.xxc.shoppingmall.utils.TimeUtils;
import com.xxc.shoppingmall.widget.CommonTitle;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by  on guo 2018.3.8
 * 币收支账本
 */
public class ExtrDetailsActivity extends AbstractPermissionActivity {

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

    private ExtrAdapter mAdapter;
    private int mPage = 1;//页数，用于获取数据

    private String yyyyDD;

    @Override
    public void initUIWithPermission() {
        mShieldDetailsYear.setText(String.format(YEAR_FORMAT, TimeUtils.getThisYear()));
        mShieldDetailsMonth.setText(String.format(MONTH_FROMAT, TimeUtils.getMonth()));
        mDetailsList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExtrAdapter(this);
        mDetailsList.setAdapter(mAdapter);
        mShieldTitle.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent help = new Intent(ExtrDetailsActivity.this, MyhelpActivity.class);

                startActivity(help);
            }
        });
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_extr_details;
    }

    @Override
    public void initData() {
        mPage = 1;
        yyyyDD = TimeUtils.getThisMonth();

     loadData(true, mPage + "", yyyyDD);
       // getDataAsync();
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
                LogUtils.d(yyyyDD);
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
    private void getDataAsync() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://122.114.161.3:8080/qwe/")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                android.util. Log.d("kwwl","获取数据失败了");

                runOnUiThread(new Runnable()

                {

                    public void run()

                    {
                        String str="[{ \"amount\": 11.0, \"comments\": \"1\", \"createTime\": \"2018-01-22 00:00:00.0\", \"flowType\": 1, \"holdgscNum\": 1.0, \"id\": 1, \"monyAddren\": \"yhhjjj???\", \"operateUser\": \"1\", \"review\": 1, \"sellingTime\": \"2018-01-22 00:00:00.0\", \"updateTime\": \"2018-03-06 00:00:00.0\", \"userId\": \"11\" }, { \"amount\": 11.0, \"createTime\": \"2018-01-22 00:00:00.0\", \"flowType\": 1, \"id\": 2, \"monyAddren\": \"yhhjjj???\", \"sellingTime\": \"2018-01-26 00:00:00.0\", \"userId\": \"11\" }, { \"amount\": 29.0, \"comments\": \"2\", \"createTime\": \"2018-01-23 00:00:00.0\", \"flowType\": 2, \"id\": 4, \"monyAddren\": \"????????\", \"operateUser\": \"3\", \"review\": 2, \"sellingTime\": \"2018-01-23 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 111.0, \"comments\": \"1\", \"createTime\": \"2018-02-01 00:00:00.0\", \"flowType\": 1, \"id\": 5, \"monyAddren\": \"abcdefgh\", \"operateUser\": \"2\", \"review\": 3, \"sellingTime\": \"2018-02-01 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1342.0, \"comments\": \"2\", \"createTime\": \"2018-02-04 00:00:00.0\", \"flowType\": 2, \"id\": 6, \"monyAddren\": \"ddddffff\", \"operateUser\": \"2\", \"review\": -1, \"sellingTime\": \"2018-02-04 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-05 00:00:00.0\", \"flowType\": 1, \"id\": 8, \"monyAddren\": \"67949469464\", \"sellingTime\": \"2018-02-05 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-05 00:00:00.0\", \"flowType\": 2, \"id\": 9, \"monyAddren\": \"123123123123\", \"sellingTime\": \"2018-02-05 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 66.0, \"createTime\": \"2018-02-06 00:00:00.0\", \"flowType\": 1, \"id\": 10, \"monyAddren\": \"56698756\", \"sellingTime\": \"2018-02-06 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-07 00:00:00.0\", \"flowType\": 1, \"id\": 11, \"monyAddren\": \"666875682568\", \"sellingTime\": \"2018-02-07 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-07 00:00:00.0\", \"flowType\": 2, \"id\": 12, \"monyAddren\": \"ddddddd\", \"sellingTime\": \"2018-02-07 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 22.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 14, \"monyAddren\": \"222222222222\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 3.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 15, \"monyAddren\": \"3333333333333333\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 2, \"id\": 16, \"monyAddren\": \"556563563465346534\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 17, \"monyAddren\": \"556563563465346534\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 18, \"monyAddren\": \"345234523452345\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 19, \"monyAddren\": \"1\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 20, \"monyAddren\": \"1\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 2, \"id\": 21, \"monyAddren\": \"1\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 22, \"monyAddren\": \"qwerqwerqwer\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 23, \"monyAddren\": \"43343434534534\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 24, \"monyAddren\": \"23452345234523452\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 25, \"monyAddren\": \"2345234523452345\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 3.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 26, \"monyAddren\": \"qwerqwerqwer\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 27, \"monyAddren\": \"3646536534653\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 9.0, \"createTime\": \"2018-02-27 00:00:00.0\", \"flowType\": 1, \"id\": 28, \"monyAddren\": \"4523452234523452345\", \"sellingTime\": \"2018-02-27 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 30.0, \"createTime\": \"2018-02-27 00:00:00.0\", \"flowType\": 1, \"id\": 29, \"monyAddren\": \"24523452345234243524\", \"sellingTime\": \"2018-02-27 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 22.0, \"createTime\": \"2018-02-27 00:00:00.0\", \"flowType\": 1, \"id\": 30, \"monyAddren\": \"wrgregwtwertwert\", \"sellingTime\": \"2018-02-27 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 1.0, \"createTime\": \"2018-03-08 10:21:54.0\", \"flowType\": 1, \"id\": 31, \"monyAddren\": \"1\", \"sellingTime\": \"2018-03-08 10:21:54.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-03-08 16:21:49.0\", \"flowType\": 1, \"id\": 32, \"monyAddren\": \"123\", \"sellingTime\": \"2018-03-08 16:21:49.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 5.0, \"createTime\": \"2018-03-08 16:22:10.0\", \"flowType\": 1, \"id\": 33, \"monyAddren\": \"qwe\", \"sellingTime\": \"2018-03-08 16:22:10.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }] \n";
                        Gson gson = new Gson();
                        List<ExtrEntity> students = gson.fromJson(str, new TypeToken<List<ExtrEntity>>(){}.getType());
                        mAdapter.swapData(students);
                    }



                });

            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    android.util. Log.d("kwwl","获取数据成功了");
                    android.util.Log.d("kwwl","response.code()=="+response.code());
                    Log.d("kwwl","response.body().string()=="+response.body().string());
                    String str="[{ \"amount\": 11.0, \"comments\": \"1\", \"createTime\": \"2018-01-22 00:00:00.0\", \"flowType\": 1, \"holdgscNum\": 1.0, \"id\": 1, \"monyAddren\": \"yhhjjj???\", \"operateUser\": \"1\", \"review\": 1, \"sellingTime\": \"2018-01-22 00:00:00.0\", \"updateTime\": \"2018-03-06 00:00:00.0\", \"userId\": \"11\" }, { \"amount\": 11.0, \"createTime\": \"2018-01-22 00:00:00.0\", \"flowType\": 1, \"id\": 2, \"monyAddren\": \"yhhjjj???\", \"sellingTime\": \"2018-01-26 00:00:00.0\", \"userId\": \"11\" }, { \"amount\": 29.0, \"comments\": \"2\", \"createTime\": \"2018-01-23 00:00:00.0\", \"flowType\": 2, \"id\": 4, \"monyAddren\": \"????????\", \"operateUser\": \"3\", \"review\": 2, \"sellingTime\": \"2018-01-23 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 111.0, \"comments\": \"1\", \"createTime\": \"2018-02-01 00:00:00.0\", \"flowType\": 1, \"id\": 5, \"monyAddren\": \"abcdefgh\", \"operateUser\": \"2\", \"review\": 3, \"sellingTime\": \"2018-02-01 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1342.0, \"comments\": \"2\", \"createTime\": \"2018-02-04 00:00:00.0\", \"flowType\": 2, \"id\": 6, \"monyAddren\": \"ddddffff\", \"operateUser\": \"2\", \"review\": -1, \"sellingTime\": \"2018-02-04 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-05 00:00:00.0\", \"flowType\": 1, \"id\": 8, \"monyAddren\": \"67949469464\", \"sellingTime\": \"2018-02-05 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-05 00:00:00.0\", \"flowType\": 2, \"id\": 9, \"monyAddren\": \"123123123123\", \"sellingTime\": \"2018-02-05 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 66.0, \"createTime\": \"2018-02-06 00:00:00.0\", \"flowType\": 1, \"id\": 10, \"monyAddren\": \"56698756\", \"sellingTime\": \"2018-02-06 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-07 00:00:00.0\", \"flowType\": 1, \"id\": 11, \"monyAddren\": \"666875682568\", \"sellingTime\": \"2018-02-07 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-07 00:00:00.0\", \"flowType\": 2, \"id\": 12, \"monyAddren\": \"ddddddd\", \"sellingTime\": \"2018-02-07 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 22.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 14, \"monyAddren\": \"222222222222\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 3.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 15, \"monyAddren\": \"3333333333333333\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 2, \"id\": 16, \"monyAddren\": \"556563563465346534\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 17, \"monyAddren\": \"556563563465346534\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 18, \"monyAddren\": \"345234523452345\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 19, \"monyAddren\": \"1\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 1, \"id\": 20, \"monyAddren\": \"1\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-02-25 00:00:00.0\", \"flowType\": 2, \"id\": 21, \"monyAddren\": \"1\", \"sellingTime\": \"2018-02-25 00:00:00.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 22, \"monyAddren\": \"qwerqwerqwer\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 23, \"monyAddren\": \"43343434534534\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 4.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 24, \"monyAddren\": \"23452345234523452\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 25, \"monyAddren\": \"2345234523452345\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 3.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 26, \"monyAddren\": \"qwerqwerqwer\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 5.0, \"createTime\": \"2018-02-26 00:00:00.0\", \"flowType\": 1, \"id\": 27, \"monyAddren\": \"3646536534653\", \"sellingTime\": \"2018-02-26 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 9.0, \"createTime\": \"2018-02-27 00:00:00.0\", \"flowType\": 1, \"id\": 28, \"monyAddren\": \"4523452234523452345\", \"sellingTime\": \"2018-02-27 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 30.0, \"createTime\": \"2018-02-27 00:00:00.0\", \"flowType\": 1, \"id\": 29, \"monyAddren\": \"24523452345234243524\", \"sellingTime\": \"2018-02-27 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 22.0, \"createTime\": \"2018-02-27 00:00:00.0\", \"flowType\": 1, \"id\": 30, \"monyAddren\": \"wrgregwtwertwert\", \"sellingTime\": \"2018-02-27 00:00:00.0\", \"userId\": \"16d11ec8a2de4ae48dd3622e289a53cf\" }, { \"amount\": 1.0, \"createTime\": \"2018-03-08 10:21:54.0\", \"flowType\": 1, \"id\": 31, \"monyAddren\": \"1\", \"sellingTime\": \"2018-03-08 10:21:54.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 1.0, \"createTime\": \"2018-03-08 16:21:49.0\", \"flowType\": 1, \"id\": 32, \"monyAddren\": \"123\", \"sellingTime\": \"2018-03-08 16:21:49.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }, { \"amount\": 5.0, \"createTime\": \"2018-03-08 16:22:10.0\", \"flowType\": 1, \"id\": 33, \"monyAddren\": \"qwe\", \"sellingTime\": \"2018-03-08 16:22:10.0\", \"userId\": \"3fe8e8b8e8654d22b07168c56e16fde2\" }] \n";
                    Gson gson = new Gson();
                    List<ExtrEntity> students = gson.fromJson(str, new TypeToken<List<ExtrEntity>>(){}.getType());
                    mAdapter.swapData(students);
                }
            }
        });
    }
    private void loadData(final boolean isFresh, String page, String yyyyDD) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        android.util. Log.d("userId","userId"+userId);
        Call<ExtrResult> resultCall = mHttpApi.getExtrResult("1", userId, yyyyDD, LIMIT, page);
        EasyCallBack<ExtrResult> easyCallBack = new EasyCallBack<ExtrResult>() {
            @Override
            public void onSuccess(Call<ExtrResult> call,ExtrResult model) {
                if (null != model) {
                    android.util. Log.d("ExtrResult","获取数据成功了"+model.toString());
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
            public void onFailure(Call<ExtrResult> call, Throwable t) {
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
        LogUtils.d("传参"+userId + "---"+yyyyDD);
        Call<QueryWaterGsc> call = mHttpApi.queryWaterG(userId,yyyyDD);
        Callback<QueryWaterGsc> callback = new EasyCallBack<QueryWaterGsc>() {
            @Override
            public void onSuccess(Call<QueryWaterGsc> call, QueryWaterGsc model) {
                if (null != model && model.getMsg().isSuccess()&&model.getData().size()>0) {
                    mShieldTvOut.setText(String.valueOf(model.getData().get(0).getZhichu()));
                    mShieldTvIncome.setText(String.valueOf(model.getData().get(0).getShouru()));
                } else {
                    ToastUtils.showToast(ExtrDetailsActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }
}
