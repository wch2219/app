package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuxingchen on 2017/11/30.
 * 修改昵称的界面
 */
public class NickActivity extends AbstractPermissionActivity {

    public static final String NEW_NICK = "newNick";

    @BindView(R.id.change_nick_new_nick)
    EditText mChangeNickNewNick;
    @BindView(R.id.change_nick_submit)
    Button mChangeNickSubmit;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_change_nick;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.change_nick_submit)
    public void onViewClicked() {
        String newNick = mChangeNickNewNick.getText().toString().trim();
        if (!TextUtils.isEmpty(newNick)) {
            Intent result = getIntent();
            result.putExtra(NEW_NICK, newNick);
            setResult(Activity.RESULT_OK, result);
            finish();
        } else {
            ToastUtils.showToast(this, "请输入新的昵称");
        }
    }
}
