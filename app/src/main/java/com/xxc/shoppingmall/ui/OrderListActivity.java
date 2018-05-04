package com.xxc.shoppingmall.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ajguan.library.EasyRefreshLayout;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.OrderListAdapter;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.OrderBean;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/25.
 * 各种订单界面
 */
public class OrderListActivity extends AbstractPermissionActivity {

    public static final String ORDER_KEY = "order_type";

    public static final int ORDER_WAIT_GOODS = 1;
    public static final int ORDER_COMPLETED = 2;

    private static final int LIMIT = 10;

    @BindView(R.id.order_title)
    CommonTitle mOrderTitle;
    @BindView(R.id.order_list)
    RecyclerView mOrderList;
    @BindView(R.id.easylayout)
    EasyRefreshLayout mEasyRefreshLayout;
    @BindView(R.id.order_root)
    RelativeLayout mOrderRoot;

    private OrderListAdapter mOrderListAdapter;
    private int mOrderType = ORDER_COMPLETED;
    private int PAGE = 1;
    private OrderBean mOrderBean;
    private BaseQuickAdapter.OnItemChildClickListener mOnItemClickListener;

    @Override
    public void initUIWithPermission() {
        mOrderType = getIntent().getIntExtra(ORDER_KEY, ORDER_COMPLETED);
        if (mOrderType == ORDER_WAIT_GOODS) {
            mOrderTitle.setTitle("待收货订单");
        } else if (mOrderType == ORDER_COMPLETED) {
            mOrderTitle.setTitle("我的订单");
        }
        mOrderList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_order_list;
    }

    @Override
    public void initData() {
        Map<String, Object> params = getParams(mOrderType, PAGE);
        queryOrderList(true, params);
    }

    @Override
    public void addListeners() {
        final LoginResult user = ShoppingMallApp.getInstance().getUser();
        if (null != user) {
            mOnItemClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, final int
                        position) {
                    TextView textView = (TextView) view;
                    String content = textView.getText().toString().trim();
                    if ("确认收货".equalsIgnoreCase(content)) {
                        MaterialDialog.Builder builder = new MaterialDialog.Builder
                                (OrderListActivity.this)
                                .autoDismiss(false)
                                .canceledOnTouchOutside(false).positiveText("确定").negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                            DialogAction which) {
                                        Map<String, Object> params = new HashMap<>();
                                        params.put(ParamKey.USERID, user.getData().getUserId());
                                        params.put("orderId", mOrderBean.getData().get(position)
                                                .getId());
                                        dialog.dismiss();
                                        updateOrder(params);
                                    }
                                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                            DialogAction which) {
                                        dialog.dismiss();
                                    }
                                }).title("确认收货").content("您确定已经收到所购物品吗？确认后无法撤销！");
                        MaterialDialog dialog = builder.build();
                        dialog.show();
                    }
                }
            };
            mEasyRefreshLayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
                @Override
                public void onLoadMore() {
                    PAGE++;
                    Map<String, Object> params = getParams(mOrderType, PAGE);
                    queryOrderList(false, params);
                }

                @Override
                public void onRefreshing() {
                    PAGE = 1;
                    Map<String, Object> params = getParams(mOrderType, PAGE);
                    queryOrderList(true, params);
                }
            });
        }
    }

    private void queryOrderList(final boolean isRefresh, Map<String, Object> params) {
        Call<OrderBean> call = mHttpApi.queryOrderList(params);
        Callback<OrderBean> callback = new EasyCallBack<OrderBean>() {
            @Override
            public void onSuccess(Call<OrderBean> call, OrderBean model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess()) {
                    if (null == mOrderListAdapter) {
                        mOrderBean = model;
                        mOrderListAdapter = new OrderListAdapter(model.getData(),
                                OrderListActivity.this);
                        View emptyView = LayoutInflater.from(OrderListActivity.this).inflate(R
                                .layout.layout_empty, mOrderRoot, false);
                        mOrderListAdapter.setEmptyView(emptyView);
                        mOrderListAdapter.setOnItemChildClickListener(mOnItemClickListener);
                        mOrderList.addItemDecoration(new HorizontalDividerItemDecoration.Builder
                                (OrderListActivity.this).colorResId(R.color.color_f4f4f4)
                                .sizeResId(R.dimen.normal_6dp).build());
                        mOrderList.setAdapter(mOrderListAdapter);
                    } else {
                        if (isRefresh) {
                            mOrderListAdapter.setNewData(model.getData());
                            mOrderListAdapter.notifyDataSetChanged();
                            mEasyRefreshLayout.refreshComplete();
                        } else {
                            mOrderListAdapter.addData(model.getData());
                            mOrderListAdapter.notifyDataSetChanged();
                            mEasyRefreshLayout.loadMoreComplete();
                        }
                    }
                } else {
                    ToastUtils.showToast(OrderListActivity.this, null == model ? "加载失败" : model
                            .getMsg().getInfo());
                }
            }

            @Override
            public void onFailure(Call<OrderBean> call, Throwable t) {
                super.onFailure(call, t);
                if (isRefresh) {
                    mEasyRefreshLayout.refreshComplete();
                } else {
                    mEasyRefreshLayout.loadMoreComplete();
                }
            }
        };
        requestApi(call, callback);
    }

    public Map<String, Object> getParams(int type, int page) {
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, user.getData().getUserId());
        if (type != 2) {
            params.put(ParamKey.STATUS, type);
        }
        params.put(ParamKey.LIMIT, LIMIT);
        params.put(ParamKey.PAGE, page);
        return params;
    }

    private void updateOrder(Map<String, Object> params) {
        Call<SubmitOrder> call = mHttpApi.updateOrder(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    PAGE = 1;
                    Map<String, Object> params = getParams(mOrderType, PAGE);
                    queryOrderList(true, params);
                } else {
                    dismissLoading();
                    ToastUtils.showToast(OrderListActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("订单更新中。。。");
        requestApi(call, callback);
    }
}
