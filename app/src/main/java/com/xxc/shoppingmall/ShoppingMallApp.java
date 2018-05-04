package com.xxc.shoppingmall;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.king.base.util.LogUtils;
import com.king.base.util.SharedPreferencesUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.xxc.shoppingmall.db.MyDao;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.UserInfo;

import java.io.File;



/**
 * Created by xuxingchen on 2017/11/7.
 * 全局application
 */
public class ShoppingMallApp extends Application {

    public static final String USER_SP_KEY = "user_json";

    private static ShoppingMallApp instance;

    public static final String OUT_PATH = Environment.getExternalStorageDirectory() + "/HJImages/";

    static {
        File dir = new File(OUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static ShoppingMallApp getInstance() {
        return instance;
    }

    private LoginResult mUser;
    private UserInfo mUserInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Utils.init(this);
        MyDao.getInstence(this);
//        CrashReport.initCrashReport(getApplicationContext(), "c5893d23d7", false);
        Bugly.init(getApplicationContext(), "c5893d23d7", false);

    }

    public LoginResult getUser() {
        if (null == mUser) {
            String userJson = SharedPreferencesUtils.getString(this, ShoppingMallApp.USER_SP_KEY);
            if (!TextUtils.isEmpty(userJson)) {
                mUser = new Gson().fromJson(userJson, LoginResult.class);
                return mUser;
            }
        }
        return mUser;
    }

    public void setUser(LoginResult user) {
        Gson gson = new Gson();
        SharedPreferencesUtils.put(this, ShoppingMallApp.USER_SP_KEY, null == user ? "" : gson
                .toJson(user));
        mUser = user;
    }

    public UserInfo getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
    }
}
