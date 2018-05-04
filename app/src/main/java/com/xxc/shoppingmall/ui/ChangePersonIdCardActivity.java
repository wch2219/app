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
 * Created by xuxingchen on 2018/1/23.
 */

public class ChangePersonIdCardActivity extends AbstractPermissionActivity {

    public static final String PERSON_CARD_ID = "person_card_id";

    @BindView(R.id.change_person_id_card)
    EditText mChangePersonIdCard;
    @BindView(R.id.change_person_id_submit)
    Button mChangePersonIdSubmit;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_change_person_id;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.change_person_id_submit)
    public void onViewClicked() {
        String personCard = mChangePersonIdCard.getText().toString().trim();
        if (!TextUtils.isEmpty(personCard)) {
            if (personCard.length() < 15 || personCard.length() > 18) {
                ToastUtils.showToast(this, "身份证长度不符合,请重新输入");
            } else {
                Intent result = getIntent();
                result.putExtra(PERSON_CARD_ID, personCard);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        } else {
            ToastUtils.showToast(this, "请输入新的昵称");
        }
    }
}
