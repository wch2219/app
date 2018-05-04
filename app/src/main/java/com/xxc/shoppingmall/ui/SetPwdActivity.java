package com.xxc.shoppingmall.ui;

import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.RegistResult;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/16.
 * 设置密码界面
 */
public class SetPwdActivity extends AbstractPermissionActivity implements View.OnClickListener {

    public static final String VER_KEY = "ver_key";
    public static final String VISIT_NUM_KEY = "visitNum";
    public static final String PHONE_NUM = "phone";


    @BindView(R.id.title)
    CommonTitle mTitle;
    @BindView(R.id.set_pwd_password)
    EditText mSetPwdPassword;
    @BindView(R.id.set_pwd_showpwd)
    CheckBox mSetPwdShowpwd;
    @BindView(R.id.set_pwd_complete)
    Button mSetPwdComplete;

    private String mVerNum;
    private String mVisit;
    private String mPhone;

    @Override
    public void initUIWithPermission() {
        mSetPwdComplete.setOnClickListener(this);
        CompoundButton.OnCheckedChangeListener changeListener = new CompoundButton
                .OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSetPwdPassword.setInputType(EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    mSetPwdPassword.setInputType((EditorInfo.TYPE_CLASS_TEXT | EditorInfo
                            .TYPE_TEXT_VARIATION_PASSWORD));
                }
            }
        };
        mSetPwdShowpwd.setOnCheckedChangeListener(changeListener);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_set_pwd;
    }

    @Override
    public void initData() {
        mVerNum = getIntent().getStringExtra(VER_KEY);
        mVisit = getIntent().getStringExtra(VISIT_NUM_KEY);
        mPhone = getIntent().getStringExtra(PHONE_NUM);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_pwd_complete:
                String pwd = mSetPwdPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd) && pwd.length() >= 6) {
                    if (isNumeric(pwd) && pwd.length() < 10) {
                        ToastUtils.showToast(this, "密码不能为10位以下纯数字");
                        return;
                    }
                    if (isAllEnglishChar(pwd) && pwd.length() < 10) {
                        ToastUtils.showToast(this, "密码不能为10位以下纯英文");
                        return;
                    }
                    Map<String, String> params = new HashMap<>();
                    params.put(ParamKey.PASSWORD, pwd);
                    params.put(ParamKey.MOBILE, mPhone);
                    params.put(ParamKey.VERIFICATIONCODE, mVerNum);
                    params.put(ParamKey.INVITATIONCODE, mVisit);
                    Call<RegistResult> call = mHttpApi.registUser(params);
                    Callback<RegistResult> callback = new EasyCallBack<RegistResult>() {
                        @Override
                        public void onSuccess(Call<RegistResult> call, RegistResult model) {
                            LogUtils.d(model + "");
                            if (model.getMsg().isSuccess()) {
                                ToastUtils.showToast(SetPwdActivity.this, "注册成功");
                                finish();
                            } else {
                                ToastUtils.showToast(SetPwdActivity.this, model.getMsg().getInfo());
                            }
                        }
                    };
                    requestApi(call, callback);
                } else {
                    ToastUtils.showToast(this, "请输入正确的密码");
                }
                break;
        }
    }

    /**
     * 是否为纯数字
     *
     * @param str 校验目标
     * @return 校验结果®
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 是否为都是纯英文
     *
     * @param target 校验目标
     * @return 校验结果
     */
    public static boolean isAllEnglishChar(String target) {
        boolean result = target.matches("[a-zA-Z]+");//true:全文英文
        return result;
    }
}
