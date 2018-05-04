package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.king.base.util.SharedPreferencesUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/23.
 * 账户设置界面
 */
public class AccountSettingActivity extends AbstractPermissionActivity {

    public static final int REQUEST_PERSON_INFO = 100;
    public static final int REQUEST_USER_AUTH = 200;

    private static final String CARD_ID_FORMAT = "身份证:%s";

    @BindView(R.id.setting_header)
    ImageView mSettingHeader;
    @BindView(R.id.setting_nick)
    TextView mSettingNick;
    @BindView(R.id.setting_username)
    TextView mSettingUsername;
    @BindView(R.id.setting_person_info)
    RelativeLayout mSettingPersonInfo;
    @BindView(R.id.person_birthday_jiantou)
    ImageView mPersonBirthdayJiantou;
    @BindView(R.id.setting_infos_container)
    RelativeLayout mSettingInfosContainer;
    @BindView(R.id.setting_auth_container)
    RelativeLayout mSettingAuthContainer;
    @BindView(R.id.setting_places_container)
    RelativeLayout mSettingPlacesContainer;
    @BindView(R.id.setting_bank_container)
    RelativeLayout mSettingBankContainer;
    @BindView(R.id.setting_safe_container)
    RelativeLayout mSettingSafeContainer;
    @BindView(R.id.setting_set_container)
    RelativeLayout mSettingSetContainer;
    @BindView(R.id.setting_exit)
    Button mSettingExit;
    @BindView(R.id.setting_pay_container)
    RelativeLayout mSettingPayContainer;
    @BindView(R.id.setting_cash_container)
    RelativeLayout mSettingCashContainer;
    @BindView(R.id.setting_shield_container)
    RelativeLayout mSettingShieldContainer;

    @Override
    public void initUIWithPermission() {
        UserInfo info = ShoppingMallApp.getInstance().getUserInfo();
        if (null != info) {
            refreshUI(info);
        }
    }

    private void refreshUI(UserInfo info) {
        loadAvatar(NetConstant.IMGAE_PATH + info.getData().getAvatarUrl(), mSettingHeader);
        mSettingNick.setText(info.getData().getNickName());
        mSettingUsername.setText(info.getData().getUserName());
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_account_setting;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.setting_person_info, R.id.setting_infos_container, R.id
            .setting_auth_container, R.id.setting_places_container, R.id.setting_bank_container,
            R.id.setting_safe_container, R.id.setting_set_container, R.id.setting_pay_container, R
            .id.setting_cash_container, R.id.setting_shield_container})
    public void onViewClicked(View view) {
        UserInfo info = ShoppingMallApp.getInstance().getUserInfo();
        if (null == info) {
            getPersonInfo();
            return;
        }
        switch (view.getId()) {
            case R.id.setting_person_info:
                Intent personInfos = new Intent(this, PersonInfoActivity.class);
                personInfos.putExtra(PersonInfoActivity.PERSON_INFO, info);
                startActivityForResult(personInfos, REQUEST_PERSON_INFO);
                break;
            case R.id.setting_infos_container:
                Intent infos = new Intent(this, InfosActivity.class);
                startActivity(infos);
                break;
            case R.id.setting_auth_container:
                Intent auth = new Intent(this, AuthNameActivity.class);
                startActivityForResult(auth, REQUEST_USER_AUTH);
                break;
            case R.id.setting_places_container:
                Intent manageAddress = new Intent(this, ManageAddressActivity.class);
                startActivity(manageAddress);
                break;
            case R.id.setting_bank_container:
                Intent bankCard = new Intent(this, ManageBankActivity.class);
                startActivity(bankCard);
                break;
            case R.id.setting_safe_container:
                Intent safe = new Intent(this, SysSafeActivity.class);
                startActivity(safe);
                break;
            case R.id.setting_set_container:
                Intent about = new Intent(this, SettingActivity.class);
                startActivity(about);
                break;
            case R.id.setting_pay_container:
                Intent payHistroy = new Intent(this, PayHistroyActivity.class);
                startActivity(payHistroy);
                break;
            case R.id.setting_cash_container:
                Intent cashHistroy = new Intent(this, PointsCashHistroyActivity.class);
                startActivity(cashHistroy);
                break;
            case R.id.setting_shield_container:
                Intent shieldHistroy = new Intent(this, ShieldHistroyActivity.class);
                startActivity(shieldHistroy);
                break;
        }
    }

    public void exitApp(View exitBtn) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this).autoDismiss(false)
                .canceledOnTouchOutside(false).positiveText("确定").negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                            DialogAction which) {
                        SharedPreferencesUtils.put(AccountSettingActivity.this,
                                ShoppingMallApp.USER_SP_KEY, "");
                        ShoppingMallApp.getInstance().setUser(null);
                        ShoppingMallApp.getInstance().setUserInfo(null);
                        dialog.dismiss();
                        ActivityUtils.finishAllActivities();
                        ActivityUtils.startActivity(new Intent(AccountSettingActivity
                                .this, LoginActivity.class));
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                            DialogAction which) {
                        dialog.dismiss();
                    }
                }).title("退出登录").content("您确定要退出登录吗？");
        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
                @Override
                public void onSuccess(Call<UserInfo> call, UserInfo model) {
                    if (null != model && model.getMsg().isSuccess()) {
                        LoginResult loginBean = LoginResult.copy(model);
                        ShoppingMallApp.getInstance().setUserInfo(model);
                        ShoppingMallApp.getInstance().setUser(loginBean);
                        refreshUI(model);
                    }
                }
            };
            getPersonInfo(callback);
        }
    }

}
