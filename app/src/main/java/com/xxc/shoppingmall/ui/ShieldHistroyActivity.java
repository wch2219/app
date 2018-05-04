package com.xxc.shoppingmall.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ajguan.library.EasyRefreshLayout;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.HistroyAdapter;
import com.xxc.shoppingmall.model.ShieldHistroyModel;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2018/1/22.
 * 卖盾记录页面
 */
public class ShieldHistroyActivity extends AbstractPermissionActivity {

    @BindView(R.id.shield_histroy_list)
    RecyclerView mShieldHistroyList;
    @BindView(R.id.easylayout)
    EasyRefreshLayout mEasylayout;
    @BindView(R.id.root_view)
    LinearLayout mRootView;

    private int page = 0;
    private HistroyAdapter mHistroyAdapter;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_shield_histroy;
    }

    @Override
    public void initData() {
        queryData(true);
    }

    @Override
    public void addListeners() {
        mEasylayout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {
                page++;
                queryData(false);
            }

            @Override
            public void onRefreshing() {
                page = 0;
                queryData(true);
            }
        });
    }

    private void queryData(final boolean isRefresh) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.LIMIT, 15);
        params.put(ParamKey.PAGE, page);
        Call<ShieldHistroyModel> call = mHttpApi.getShieldHistroy(params);
        Callback<ShieldHistroyModel> callback = new EasyCallBack<ShieldHistroyModel>() {
            @Override
            public void onSuccess(Call<ShieldHistroyModel> call, ShieldHistroyModel model) {
                if (null != model && model.getMsg().isSuccess()) {
                    if (mHistroyAdapter != null) {
                        if (isRefresh) {
                            mHistroyAdapter.setNewData(model.getData());
                            mHistroyAdapter.notifyDataSetChanged();
                            mEasylayout.refreshComplete();
                        } else {
                            mHistroyAdapter.addData(model.getData());
                            mHistroyAdapter.notifyDataSetChanged();
                            mEasylayout.loadMoreComplete();
                        }
                    } else {
                        mHistroyAdapter = new HistroyAdapter(HistroyAdapter.TYPE_SHIELD_HISTORY,
                                model.getData());
                        View emptyView = LayoutInflater.from(ShieldHistroyActivity.this)
                                .inflate(R.layout.layout_empty, mRootView, false);
                        mHistroyAdapter.setEmptyView(emptyView);
                        mShieldHistroyList.addItemDecoration(new HorizontalDividerItemDecoration
                                .Builder(ShieldHistroyActivity.this).colorResId(R.color
                                .color_f4f4f4).sizeResId(R.dimen.normal_1dp).build());
                        mShieldHistroyList.setLayoutManager(new LinearLayoutManager
                                (ShieldHistroyActivity.this));
                        mShieldHistroyList.setAdapter(mHistroyAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<ShieldHistroyModel> call, Throwable t) {
                super.onFailure(call, t);
                if (isRefresh) {
                    mEasylayout.refreshComplete();
                } else {
                    mEasylayout.loadMoreComplete();
                }
            }
        };
        requestApi(call, callback);
    }
}
