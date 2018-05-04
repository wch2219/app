package com.xxc.shoppingmall.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.king.base.BaseActivity;
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

import com.xxc.shoppingmall.utils.GlideApp;
import com.xxc.shoppingmall.utils.GlideExtra;
import com.xxc.shoppingmall.utils.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.ButterKnife;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/11.
 * 带权限申请的基类
 */
public abstract class AbstractPermissionActivity extends BaseActivity {

    protected Api mHttpApi;
    public static final int REQUEST_CODE = 100;

    InputMethodManager manager;
    protected List<Call> mCalls = new ArrayList<>();
    protected Handler mHandler;
    private MaterialDialog mMaterialDialog;
    private long mLastTime;

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private void initPermission() {
        if (EasyPermissions.hasPermissions(this, getPermission())) {
            initUIWithPermission();
        } else {
            EasyPermissions.requestPermissions(this, "This app need many permissions",
                    REQUEST_CODE, getPermission());
        }
    }

    public abstract void initUIWithPermission();

    public abstract int layoutRes();

    protected String[] getPermission() {
        return null;
    }

    @Override
    public void initUI() {
        mHandler = new Handler();
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mHttpApi = ApiServiceImp.getRetrofit(Api.class);
        setContentView(layoutRes());
        ButterKnife.bind(this);
        if (null != getPermission()) {
            initPermission();
        } else {
            initUIWithPermission();
        }
    }

    public void postDelayed(long time, Runnable task) {
        mHandler.postDelayed(task, time);
    }

    public void loadAvatar(String url, ImageView target) {
        GlideExtra extras = new GlideExtra();
        extras.scaleType = GlideExtra.CENTER_CROP;
        extras.showCircle = GlideExtra.SHOW_CIRCLE;
        extras.placeHolderRes = R.drawable.img_morentouxiang;
        ImageLoader.loadImage(this, target, url, extras);
    }

    public void requestApi(Call call, Callback callback) {
        mCalls.add(call);
        if (callback instanceof EasyCallBack) {
            ((EasyCallBack) callback).setActivity(this);
        }
        call.enqueue(callback);
    }

    public MaterialDialog showLoadingDialog(String content) {
        return showLoadingDialog(null, content, false);
    }

    public MaterialDialog showLoadingDialog(String content, boolean cancelable) {
        return showLoadingDialog(null, content, cancelable);
    }

    public MaterialDialog showLoadingDialog(boolean cancelable) {
        return showLoadingDialog(null, null, cancelable);
    }

    public MaterialDialog showLoadingDialog() {
        return showLoadingDialog(null);
    }

    public MaterialDialog showLoadingDialog(String title, String content, boolean cancelable) {
        mLastTime = System.currentTimeMillis();
        return mMaterialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .canceledOnTouchOutside(false)
                .cancelable(cancelable)
                .progress(true, 0)
                .show();
    }

    public Dialog showSingleChoiceDialog(String title, List<String> items, int selectedItem,
                                         MaterialDialog.ListCallbackSingleChoice
                                                 callbackSingleChoice) {
        return showSingleChoiceDialog(title, items, selectedItem, "确定", callbackSingleChoice);
    }

    public Dialog showSingleChoiceDialog(String title, List<?> items, int selecedItem,
                                         String positiveHint, MaterialDialog
                                                 .ListCallbackSingleChoice callBack) {
        return new MaterialDialog.Builder(this)
                .title(title)
                .items(items)
                .itemsCallbackSingleChoice(selecedItem, callBack)
                .positiveText(positiveHint)
                .canceledOnTouchOutside(false)
                .show();
    }
    public void dismissLoading(long during) {
        if (null != mMaterialDialog) {
            long cutdownTime = System.currentTimeMillis() - mLastTime;
            if (cutdownTime < during) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != mMaterialDialog && mMaterialDialog.isShowing()) {
                            mMaterialDialog.dismiss();
                        }
                    }
                }, during - cutdownTime);
            } else {
                if (null != mMaterialDialog && mMaterialDialog.isShowing()) {
                    mMaterialDialog.dismiss();
                }
            }
        }
    }

    public void dismissLoading() {
        dismissLoading(1200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 配置webView的设置项
     *
     * @param webView 浏览器对象
     */
    protected void configWebSetting(WebView webView, Context activity) {
        webView.setInitialScale(100); // 让缩放显示的最小值为起始
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = webView.getSettings();
        webSettings.setSupportMultipleWindows(false);
        //设置webView缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        //设置支持js
        webSettings.setJavaScriptEnabled(true);
        //提高渲染的优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //支持h5网页自适应
        webSettings.setLoadWithOverviewMode(true);
        //配合上一条的设置项，将图片调整到适合webView的大小
        webSettings.setUseWideViewPort(true);
        //不保存表单数据
        webSettings.setSaveFormData(false);

        int i = Build.VERSION.SDK_INT;
        File cacheDir = null;
        if (i >= 8)
            cacheDir = activity.getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = activity.getCacheDir();
        File h5CacheFile = new File(cacheDir, "caimi_web_cache");
        if (!h5CacheFile.exists())
            h5CacheFile.mkdirs();
        String h5CachePath = h5CacheFile.getAbsolutePath();
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        //根据提供的缓存路径，在H5使用缓存过程中生成的缓存文件
        webSettings.setAppCachePath(h5CachePath);
        //设置缓存最大为8m
        webSettings.setAppCacheMaxSize(8 * 1024 * 1024);
        //启用缓存，h5在使用过程中可以使用缓存
        webSettings.setAppCacheEnabled(true);
        //设置webView直接进行图片加载
        webSettings.setLoadsImagesAutomatically(true);
        //设置缓存模式，默认缓存模式为，加载一张网页会检查是否有缓存，如果有，并且没有过期，则使用缓存
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //启用Dom Storage功能
        webSettings.setDomStorageEnabled(true);
        //数据库缓存路径
        File dbCacheFile = new File(cacheDir, "caimi_web_database");
        if (!dbCacheFile.exists())
            dbCacheFile.mkdirs();
        String dbCachePath = dbCacheFile.getAbsolutePath();
        //设置数据库缓存
        webSettings.setDatabaseEnabled(true);
        //设置数据库缓存路径
        webSettings.setDatabasePath(dbCachePath);
        //启动定位功能
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dbCachePath);
        // android 5.0以上默认不支持Mixed Content
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
    }

    public void compressImage(final String imagePath, final String outPath, final
    IOnCompressCompleted listener) {
        RequestBuilder<Bitmap> builder = GlideApp.with(this).asBitmap();
        builder.load("file://" + imagePath).into(new SimpleTarget<Bitmap>(ScreenUtils
                .getScreenWidth(), ScreenUtils.getScreenHeight()) {
            @Override
            public void onResourceReady(final Bitmap resource, Transition<? super Bitmap>
                    transition) {
                asyncThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.d(resource.getWidth() + "---" + resource.getHeight());
                        try {
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            // scale
                            int options = 100;
                            // Store the bitmap into output stream(no compress)
                            resource.compress(Bitmap.CompressFormat.JPEG, options, os);
                            // Compress by loop
                            while (os.toByteArray().length / 1024 > 200) {
                                // Clean up os
                                os.reset();
                                // interval 10
                                options -= 10;
                                if (options > 10) {
                                    resource.compress(Bitmap.CompressFormat.JPEG, options, os);
                                } else {
                                    break;
                                }
                            }
                            if (!TextUtils.isEmpty(outPath)) {
                                File file = new File(outPath);
                                if (!file.exists()) {
                                    file.createNewFile();
                                }
                            }
                            // Generate compressed image file
                            FileOutputStream fos = new FileOutputStream(outPath);
                            fos.write(os.toByteArray());
                            fos.flush();
                            fos.close();
                            if (null != listener) {
                                listener.completed(outPath, new File(outPath));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (null != listener) {
                                listener.completed(null, null);
                            }
                        }
                    }
                });
            }
        });
    }

    protected void getPersonInfo() {
        getPersonInfo(null);
    }

    protected void getPersonInfo(Callback<UserInfo> callback) {
        LoginResult user = ShoppingMallApp.getInstance().getUser();
        if (null != user) {
            Call<UserInfo> call = mHttpApi.getUserInfo(user.getData().getUserId());
            if (null == callback) {
                callback = new EasyCallBack<UserInfo>() {
                    @Override
                    public void onSuccess(Call<UserInfo> call, UserInfo model) {
                        if (model != null) {
                            if (model.getMsg().isSuccess()) {
                                ShoppingMallApp.getInstance().setUserInfo(model);
                            }
                        }
                    }
                };
            }
            ToastUtils.showToast(this, "正在获取您的个人信息");
            requestApi(call, callback);
        } else {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mCalls.size(); i++) {
            mCalls.get(i).cancel();
        }
        if (null != mMaterialDialog && mMaterialDialog.isShowing()) {
            mMaterialDialog.dismiss();
        }
    }

    // 写一个广播的内部类，当收到动作时，结束activity
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(this); // 这句话必须要写要不会报错，不写虽然能关闭，会报一堆错
            ((Activity) context).finish();
        }
    };

    public void register() {
        // 在当前的activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("base_finish_activity");
        registerReceiver(this.broadcastReceiver, filter); // 注册
    }

    public void close() {
        Intent intent = new Intent();
        intent.setAction("base_finish_activity"); // 说明动作
        sendBroadcast(intent);// 该函数用于发送广播
        finish();
    }

}
