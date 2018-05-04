package com.xxc.shoppingmall.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.Api;
import com.xxc.shoppingmall.network.ApiServiceImp;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.LoginActivity;
import com.xxc.shoppingmall.utils.GlideExtra;
import com.xxc.shoppingmall.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 当使用viewpager加载Fragment，沉浸式的使用，原理懒加载
 * Created by geyifeng on 2017/4/7.
 */
public abstract class BaseLazyFragment extends Fragment {

    protected Api mHttpApi;
    protected List<Call> mCalls = new ArrayList<>();

    protected Handler mHandler = new Handler(Looper.getMainLooper());

    protected Activity mActivity;
    protected View mRootView;

    /**
     * 是否对用户可见
     */
    protected boolean mIsVisible;
    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsPrepare;

    /**
     * 是否加载完成
     * 当执行完onViewCreated方法后即为true
     */
    protected boolean mIsImmersion;

    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        mRootView = inflater.inflate(inflaterRootView(), container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, mRootView);
        mHttpApi = ApiServiceImp.getRetrofit(Api.class);
        if (isLazyLoad()) {
            mIsPrepare = true;
            mIsImmersion = true;
            onLazyLoad();
        } else {
            initData();
            if (isImmersionBarEnabled()) {
                initImmersionBar();
            }
        }
        initUI();
        addListeners();
    }

    public void postDelayed(long time, Runnable task) {
        mHandler.postDelayed(task, time);
    }

    public void requestApi(Call call, Callback callback) {
        mCalls.add(call);
        if (callback instanceof EasyCallBack && (getActivity() instanceof
                AbstractPermissionActivity)) {
            ((EasyCallBack) callback).setActivity((AbstractPermissionActivity) getActivity());
        }
        call.enqueue(callback);
    }

    public void loadAvatar(String url, ImageView target) {
        GlideExtra extras = new GlideExtra();
        extras.scaleType = GlideExtra.CENTER_CROP;
        extras.showCircle = GlideExtra.SHOW_CIRCLE;
        extras.placeHolderRes = R.drawable.img_morentouxiang;
        ImageLoader.loadImage(this, target, url, extras);
    }

    public LoginResult checkLogin() {
        if (null == ShoppingMallApp.getInstance().getUser()) {
            Intent login = new Intent(getContext(), LoginActivity.class);
            startActivity(login);
            return null;
        } else {
            return ShoppingMallApp.getInstance().getUser();
        }
    }

    protected void getPersonInfo() {
        getPersonInfo(null, true);
    }

    protected void getPersonInfo(Callback<UserInfo> callback) {
        getPersonInfo(callback, true);
    }

    protected void getPersonInfo(boolean canLogin) {
        getPersonInfo(canLogin);
    }

    protected void getPersonInfo(Callback<UserInfo> callback, boolean canLogin) {
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        if (null != user) {
            Call<UserInfo> call = mHttpApi.getUserInfo(user.getData().getUserId());
            if (null == callback) {
                callback = new EasyCallBack<UserInfo>() {
                    @Override
                    public void onSuccess(Call<UserInfo> call, UserInfo model) {
                        LogUtils.d(model + "");
                        if (model != null) {
                            if (model.getMsg().isSuccess()) {
                                ShoppingMallApp.getInstance().setUserInfo(model);
                            }
                        }
                    }
                };
            }
            ToastUtils.showToast(getContext(), "正在获取您的个人信息");
            requestApi(call, callback);
        } else {
            if (canLogin) {
                Intent login = new Intent(getContext(), LoginActivity.class);
                startActivity(login);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
        for (int i = 0; i < mCalls.size(); i++) {
            mCalls.get(i).cancel();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return true;
    }

    /**
     * 是否在Fragment使用沉浸式
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    /**
     * 用户可见时执行的操作
     */
    protected void onVisible() {
        onLazyLoad();
    }

    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            initData();
        }
        if (mIsVisible && mIsImmersion && isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }


    /**
     * 初始化沉浸式
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    /**
     * 用户不可见执行
     */
    protected void onInvisible() {

    }

    /**
     * 找到activity的控件
     *
     * @param <T> the type parameter
     * @param id  the id
     * @return the t
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) mActivity.findViewById(id);
    }

    public abstract int inflaterRootView();

    public abstract void initUI();

    public abstract void initData();

    public abstract void addListeners();

}
