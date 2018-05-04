package com.xxc.shoppingmall.network;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xuxingchen on 2017/11/15.
 */

public abstract class EasyCallBack<T> implements Callback<T> {

    private static final String CUSTOM_ERROR_MSG = "HttpResponse code is %d form %s";

    private AbstractPermissionActivity mActivity;

    public void setActivity(AbstractPermissionActivity activity) {
        mActivity = activity;
    }
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        LogUtils.d(response.toString());
        if (null != response.body()) {
            onSuccess(call, response.body());
        } else {
            onFailure(call, new Throwable(String.format(CUSTOM_ERROR_MSG, response.code(), call
                    .request().url())));
        }
        mActivity = null;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mActivity != null) {
            mActivity.dismissLoading();
            ToastUtils.showToast(mActivity, "网络加载异常");
        }
        t.printStackTrace();
        mActivity = null;
    }
    public abstract void onSuccess(Call<T> call, T model);
}
