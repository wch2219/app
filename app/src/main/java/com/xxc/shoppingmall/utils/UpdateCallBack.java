package com.xxc.shoppingmall.utils;

import android.os.Handler;
import android.os.Looper;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.ShoppingMallApp;

import org.lzh.framework.updatepluginlib.callback.UpdateCheckCB;
import org.lzh.framework.updatepluginlib.callback.UpdateDownloadCB;
import org.lzh.framework.updatepluginlib.model.Update;

import java.io.File;

/**
 * Created by xuxingchen on 2017/12/4.
 */

public class UpdateCallBack implements UpdateCheckCB, UpdateDownloadCB {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCheckStart() {

    }

    @Override
    public void hasUpdate(Update update) {

    }

    @Override
    public void noUpdate() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(ShoppingMallApp.getInstance(), "无版本更新");
            }
        });
    }

    @Override
    public void onCheckError(Throwable t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(ShoppingMallApp.getInstance(), "检测异常");
            }
        });
    }

    @Override
    public void onUserCancel() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(ShoppingMallApp.getInstance(), "已取消更新");
            }
        });
    }

    @Override
    public void onCheckIgnore(Update update) {

    }

    @Override
    public void onDownloadStart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(ShoppingMallApp.getInstance(), "开始下载");
            }
        });
    }

    @Override
    public void onDownloadComplete(File file) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(ShoppingMallApp.getInstance(), "下载完成");
            }
        });
    }

    @Override
    public void onDownloadProgress(long current, long total) {

    }

    @Override
    public void onDownloadError(Throwable t) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showToast(ShoppingMallApp.getInstance(), "下载出错");
            }
        });
    }
}
