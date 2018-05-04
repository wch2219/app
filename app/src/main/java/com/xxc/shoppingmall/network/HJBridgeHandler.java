package com.xxc.shoppingmall.network;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.king.base.util.LogUtils;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.ui.MainActivity;
import com.xxc.shoppingmall.ui.base.CommonWebBrowser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xuxingchen on 2017/11/27.
 */
public abstract class HJBridgeHandler implements BridgeHandler {

    private static final String ACTION_KEY = "action";
    private static final String BACK_ACTION_KEY = "back";
    private static final String OPEN_ACTION_KEY = "open";
    private static final String TITLE_KEY = "title";
    private static final String ACTION_GET_USER_INFO = "getUserInfo";
    private static final String ACTION_GOTO_HOME = "gotoHome";

    public abstract Activity getActivityContext();

    @Override
    public void handler(String data, CallBackFunction function) {
        LogUtils.d(data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String action = jsonObject.optString(ACTION_KEY);
            String title = jsonObject.optString(TITLE_KEY);
            if (action.equalsIgnoreCase(BACK_ACTION_KEY)) {
                getActivityContext().finish();
            } else if (action.equalsIgnoreCase(OPEN_ACTION_KEY)) {
                String url = jsonObject.optString("url");
                LogUtils.d(url + "----");
                Intent intent = new Intent(getActivityContext(), CommonWebBrowser.class);
                intent.putExtra(CommonWebBrowser.BROWSER_TITLE, title);
                intent.putExtra(CommonWebBrowser.BROWSER_URL, url);
                intent.putExtra(CommonWebBrowser.SHOW_TITLE, !TextUtils.isEmpty(title));
                getActivityContext().startActivity(intent);
            } else if (action.equalsIgnoreCase(ACTION_GET_USER_INFO)) {
                UserInfo userInfo = ShoppingMallApp.getInstance().getUserInfo();
                if (null == userInfo) {
                    function.onCallBack("UserInfo is null");
                } else {
                    Gson gson = new Gson();
                    String userInfoJson = gson.toJson(userInfo.getData());
                    function.onCallBack(userInfoJson);
                }
            } else if (action.equalsIgnoreCase(ACTION_GOTO_HOME)) {
                ActivityUtils.finishToActivity(MainActivity.class, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        function.onCallBack("hello js");
    }
}
