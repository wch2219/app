package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;



/**
 * Created by xuxingchen on 2017/12/6.
 * 启动页
 */
public class LauncherActivity extends AbstractPermissionActivity {

    @Override
    public void initUIWithPermission() {
        new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent mainIntent = new Intent(LauncherActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    };
    @Override
    public int layoutRes() {
        return R.layout.layout_launcher;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }
}
