package com.xxc.shoppingmall.ui;

import android.text.TextUtils;
import android.widget.TextView;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.UserDetail;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/23.
 * 信息一览界面
 */
public class InfosActivity extends AbstractPermissionActivity {
    @BindView(R.id.infos_name)
    TextView mInfosName;
    @BindView(R.id.infos_person_id)
    TextView mInfosPersonId;
    @BindView(R.id.infos_phone)
    TextView mInfosPhone;
    @BindView(R.id.infos_create_bank)
    TextView mInfosCreateBank;
    @BindView(R.id.infos_card_num)
    TextView mInfosCardNum;
    @BindView(R.id.infos_v_account)
    TextView mInfosVAccount;
    @BindView(R.id.infos_v_name)
    TextView mInfosVName;
    @BindView(R.id.infos_v_wechat)
    TextView mInfosVWechat;
    @BindView(R.id.infos_receiver_address)
    TextView mInfosReceiverAddress;
    private String mUserId;

    @Override
    public void initUIWithPermission() {
        LoginResult info = ShoppingMallApp.getInstance().getUser();
        mUserId = info.getData().getUserId();

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_infos;
    }

    @Override
    public void initData() {
        getUserDetail(mUserId);
    }

    private void getUserDetail(String userId) {
        Call<UserDetail> call = mHttpApi.getUserDetail(userId);
        Callback<UserDetail> callback = new EasyCallBack<UserDetail>() {
            @Override
            public void onSuccess(Call<UserDetail> call, UserDetail model) {
                if (null != model && model.getMsg().isSuccess()) {
                    refreshUi(model);
                } else {
                    ToastUtils.showToast(InfosActivity.this, null == model ? "加载失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }

    @Override
    public void addListeners() {

    }

    private void refreshUi(UserDetail detail) {
        mInfosName.setText(detail.getData().getNickName());
        mInfosPersonId.setText(detail.getData().getIdCard());
        mInfosPhone.setText(detail.getData().getUserName());
        mInfosCreateBank.setText(detail.getData().getBankName());
        mInfosCardNum.setText(detail.getData().getBankNum());
        String vaccountName = detail.getData().getParentId();
        mInfosVAccount.setText(TextUtils.isEmpty(vaccountName) ? "暂无" : vaccountName);
        String vname = detail.getData().getParentname();
        mInfosVName.setText(TextUtils.isEmpty(vname)?"暂无":vname);
        String address = detail.getData().getUserAddress();
        mInfosReceiverAddress.setText(TextUtils.isEmpty(address)?"暂无":address);
    }

}
