package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/23.
 * 实名认证的界面
 */
public class AuthNameActivity extends AbstractPermissionActivity {
    @BindView(R.id.auth_img_head)
    ImageView mAuthImgHead;
    @BindView(R.id.auth_name)
    EditText mAuthName;
    @BindView(R.id.auth_person_id)
    EditText mAuthPersonId;
    @BindView(R.id.auth_points)
    TextView mAuthPoints;
    @BindView(R.id.auth_submit)
    Button mAuthSubmit;

    private UserInfo mUserInfo;

    @Override
    public void initUIWithPermission() {
        mUserInfo = ShoppingMallApp.getInstance().getUserInfo();
        if (null != mUserInfo) {
            if (TextUtils.isEmpty(mUserInfo.getData().getIdCard())) {
                mAuthPersonId.setEnabled(true);
                mAuthName.setEnabled(true);
                mAuthSubmit.setVisibility(View.VISIBLE);
            } else {
                mAuthPersonId.setEnabled(false);
                mAuthPersonId.setGravity(Gravity.RIGHT);
                mAuthName.setEnabled(false);
                mAuthName.setGravity(Gravity.RIGHT);
                mAuthSubmit.setVisibility(View.GONE);
                mAuthPersonId.setBackgroundResource(0);
                mAuthName.setBackgroundResource(0);
                mAuthName.setText(mUserInfo.getData().getNickName());
                mAuthPersonId.setText(mUserInfo.getData().getIdCard());
            }
        } else {
            getPersonInfo();
        }
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_auth_name;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.auth_submit)
    public void onViewClicked() {
        String userId = mUserInfo.getData().getUserId();
        String personId = mAuthPersonId.getText().toString().trim();
        String name = mAuthName.getText().toString().trim();
        if (!TextUtils.isEmpty(personId)) {
            boolean flag=false;
            //正则匹配身份证格式,缺陷是未检验日期的正确性  
            Pattern p= Pattern.compile("(^[1-8][0-7]{2}\\d{3}([12]\\d{3})(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}([0-9Xx])$)");
          
            Matcher m=p.matcher(personId);
            //匹配最后一位检验码是否正确  
            int index[]= {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
            //检验码对应规则，第三位实际上应该是x，这个地方用100但是实际上检验时不会用到  
            int check[]= {1,0,100,9,8,7,6,5,4,3,2};
            if(m.matches()) {
                int sum=0;
                for(int i=0;i<17;i++)    sum+=index[i]*(personId.charAt(i)-'0');
                sum%=11;
                if(sum==2 && ( personId.charAt(17)=='x'||personId.charAt(17)=='X' ) ) flag=true;
                else if(check[sum]==(personId.charAt(17)-'0'))   flag=true;
                authMyself(userId, personId, name);
            }else{
                showLongToast("您输入的身份证号有误");
            }

        }
    }

    private void authMyself(String userId, String personId, String name) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.ID_CARD, personId);
        params.put(ParamKey.NICK_NAME, name);
        Call<SubmitOrder> call = mHttpApi.submitPersonInfo(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    postDelayed(1200, new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showToast(AuthNameActivity.this, "已提交");
                            Intent finishIntent = getIntent();
                            setResult(Activity.RESULT_OK, finishIntent);
                            finish();
                        }
                    });
                } else {
                    ToastUtils.showToast(AuthNameActivity.this, model == null ? "提交失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }


}
