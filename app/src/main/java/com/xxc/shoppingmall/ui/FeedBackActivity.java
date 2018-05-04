package com.xxc.shoppingmall.ui;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/25.
 * 功能反馈的界面
 */
public class FeedBackActivity extends AbstractPermissionActivity {
    @BindView(R.id.feedback_text_num)
    TextView mFeedbackTextNum;
    @BindView(R.id.feedback_des)
    EditText mFeedbackDes;
    @BindView(R.id.feedback_phone_num)
    EditText mFeedbackPhoneNum;
    @BindView(R.id.feedback_confirm_submit)
    Button mFeedbackConfirmSubmit;

    @Override
    public void initUIWithPermission() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                if (content.length() <= 500) {
                    mFeedbackTextNum.setText(content.length() + "/500");
                }
            }
        };
        mFeedbackDes.addTextChangedListener(watcher);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_feedback;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.feedback_confirm_submit)
    public void onViewClicked() {
        String content = mFeedbackDes.getText().toString().trim();
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        String phone = mFeedbackPhoneNum.getText().toString().trim();
        if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(phone)) {
            if (RegexUtils.isMobileExact(phone)) {
                Map<String, Object> params = new HashMap<>();
                params.put(ParamKey.USERID, userId);
                params.put(ParamKey.MOBILE, phone);
                params.put(ParamKey.CONTENT, content);
                submitFeedBack(params);
            } else {
                ToastUtils.showToast(this, "请填写正确的手机号码");
            }
        } else {
            ToastUtils.showToast(this, "请填写反馈内容及您的联系方式");
        }
    }

    private void submitFeedBack(Map<String, Object> params) {
        Call<SubmitOrder> call = mHttpApi.submitFeedBack(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    ToastUtils.showToast(FeedBackActivity.this, "您的反馈已成功提交！");
                    postDelayed(1000, new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    });
                } else {
                    ToastUtils.showToast(FeedBackActivity.this, null == model ? "请求失败" :
                            model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }
}
