package com.xxc.shoppingmall.ui;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuxingchen on 2017/11/25.
 * 设置界面
 */
public class SettingActivity extends AbstractPermissionActivity {
    @BindView(R.id.setting_feedback)
    RelativeLayout mSettingFeedback;
    @BindView(R.id.setting_about)
    RelativeLayout mSettingAbout;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_setting;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }


    @OnClick({R.id.setting_feedback, R.id.setting_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_feedback:
                Intent feedback = new Intent(this, FeedBackActivity.class);
                startActivity(feedback);
                break;
            case R.id.setting_about:
                Intent about = new Intent(this, AboutActivity.class);
                startActivity(about);
                break;
        }
    }
}
