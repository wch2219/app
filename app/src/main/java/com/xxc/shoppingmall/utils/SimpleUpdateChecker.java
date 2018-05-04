package com.xxc.shoppingmall.utils;

import android.text.TextUtils;

import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.ui.SetPwdActivity;

import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateChecker;

/**
 * Created by xuxingchen on 2017/12/8.
 */

public class SimpleUpdateChecker implements UpdateChecker {
    @Override
    public boolean check(Update update) throws Exception {
        String versionName = ShoppingMallApp.getInstance().getPackageManager().getPackageInfo
                (ShoppingMallApp.getInstance().getPackageName(), 0).versionName;
        String serverVN = update.getVersionName();
        if (SetPwdActivity.isNumeric(versionName) && SetPwdActivity.isNumeric(serverVN)) {
            String[] vns = versionName.split(".");
            String[] serverVNs = serverVN.split(".");
            for (int i = 0; i < serverVNs.length; i++) {
                int version = Integer.valueOf(serverVNs[i]);
                String local = null;
                if (i < vns.length) {
                    local = vns[i];
                }
                int versionLocal;
                if (!TextUtils.isEmpty(local)) {
                    versionLocal = Integer.getInteger(local);
                } else {
                    return false;
                }
                return version > versionLocal;
            }
        } else {
            return false;
        }
        return false;
    }
}
