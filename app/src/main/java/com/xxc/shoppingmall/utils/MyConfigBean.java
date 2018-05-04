package com.xxc.shoppingmall.utils;

import android.widget.Button;

import com.hss01248.dialog.config.ConfigBean;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;

/**
 * Created by xuxingchen on 2017/12/1.
 */

public class MyConfigBean extends ConfigBean {

    @Override
    protected ConfigBean buildBottomItemDialog(ConfigBean bean) {
        bean = super.buildBottomItemDialog(bean);
        Button button = bean.dialog.getWindow().findViewById(R.id.btn_bottom);
        button.setTextColor(ShoppingMallApp.getInstance().getResources().getColor(R.color.color_ff4040));
        return bean;
    }
}
