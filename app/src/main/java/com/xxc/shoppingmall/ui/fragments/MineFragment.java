package com.xxc.shoppingmall.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.AccountSettingActivity;
import com.xxc.shoppingmall.ui.ExtrDetailsActivity;
import com.xxc.shoppingmall.ui.OrderListActivity;
import com.xxc.shoppingmall.ui.PayHistroyActivity;
import com.xxc.shoppingmall.ui.PointsDetailsActivity;
import com.xxc.shoppingmall.ui.RecommendActivity;
import com.xxc.shoppingmall.ui.ShieldDetailsActivity;
import com.xxc.shoppingmall.ui.SignInActivity;
import com.xxc.shoppingmall.ui.VoucherDetailsActivity;
import com.xxc.shoppingmall.ui.base.BaseLazyFragment;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/17.
 * 主界面---我的
 */
public class MineFragment extends BaseLazyFragment {
    @BindView(R.id.mine_header)
    ImageView mMineHeader;
    @BindView(R.id.mine_username)
    TextView mMineUsername;
    @BindView(R.id.mine_ver)
    TextView mMineVer;
    @BindView(R.id.mine_ver_container)
    RelativeLayout mMineVerContainer;
    @BindView(R.id.mine_points)
    TextView mMinePoints;
    @BindView(R.id.mine_points1_container)
    RelativeLayout mMinePointsContainer;
    @BindView(R.id.mine_dun)
    TextView mMineDun;
    @BindView(R.id.mine_dun1_container)
    RelativeLayout mMineDunContainer;
    @BindView(R.id.mine_voucher)
    TextView mMineVoucher;
    @BindView(R.id.mine_voucher1_container)
    RelativeLayout mMineVoucherContainer;
    @BindView(R.id.mine_invite_container)
    RelativeLayout mMineInviteContainer;
    @BindView(R.id.mine_support_tel)
    TextView mMineSupportTel;
    @BindView(R.id.mine_daifukuan_container)
    RelativeLayout mMineDaifukuanContainer;
    @BindView(R.id.mine_daishouhuo_container)
    RelativeLayout mMineDaishouhuoContainer;
    @BindView(R.id.mine_daipingjia_container)
    RelativeLayout mMineDaipingjiaContainer;
    @BindView(R.id.mine_tuihuan_container)
    RelativeLayout mMineTuihuanContainer;
    @BindView(R.id.mine_order_container)
    RelativeLayout mMineOrderContainer;
    @BindView(R.id.mine_pay_container)
    RelativeLayout mMinePayContainer;
   @BindView(R.id.mine_charge)
    RelativeLayout mMinechargeContainer;
   @BindView(R.id.mine_sign)
    RelativeLayout mMineSignContainer;

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initUI() {
        UserInfo info = ShoppingMallApp.getInstance().getUserInfo();
        if (null != info) {
            refreshUI(info);
        } else {
            Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
                @Override
                public void onSuccess(Call<UserInfo> call, UserInfo model) {
                    if (null != model && model.getMsg().isSuccess()) {
                        ShoppingMallApp.getInstance().setUserInfo(model);
                        refreshUI(model);
                    } else {
                        ToastUtils.showToast(getContext(), null == model ? "加载失败" : model.getMsg
                                ().getInfo());
                    }
                }
            };
            getPersonInfo(callback, false);
        }
    }

    private void refreshUI(UserInfo info) {
        loadAvatar(NetConstant.IMGAE_PATH + info.getData().getAvatarUrl(), mMineHeader);
        mMineUsername.setText(info.getData().getUserName());
        if (!TextUtils.isEmpty(info.getData().getIdCard())) {
            mMineVer.setText("已认证");
        } else {
            mMineVer.setText("未认证");
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.mine_header, R.id.mine_ver_container, R.id.mine_points1_container, R.id
            .mine_dun1_container, R.id.mine_voucher1_container, R.id.mine_invite_container, R.id
            .mine_support_tel, R.id.mine_daifukuan_container, R.id.mine_daishouhuo_container, R.id
            .mine_daipingjia_container, R.id.mine_tuihuan_container, R.id.mine_order_container, R
            .id.mine_pay_container,R.id.mine_charge,R.id.mine_sign})
    public void onViewClicked(View view) {
        LoginResult user = checkLogin();
        switch (view.getId()) {
            case R.id.mine_header:
                if (null != user) {
                    Intent accountSetting = new Intent(getContext(), AccountSettingActivity.class);
                    startActivity(accountSetting);
                }
                break;
            case R.id.mine_ver_container:
                if (null != user) {
                    Intent accountSetting = new Intent(getContext(), AccountSettingActivity.class);
                    startActivity(accountSetting);
                }
                break;
            case R.id.mine_points1_container:
                if (null != user) {
                    Intent pointsDetails = new Intent(getContext(), PointsDetailsActivity.class);
                    startActivity(pointsDetails);
                }
                break;
            case R.id.mine_dun1_container:
                if (null != user) {
                    Intent shieldDetails = new Intent(getContext(), ShieldDetailsActivity.class);
                    startActivity(shieldDetails);
                }
                break;
            case R.id.mine_voucher1_container:
                if (null != user) {
                    Intent voucherDetails = new Intent(getContext(), VoucherDetailsActivity.class);
                    startActivity(voucherDetails);
                }
                break;
            case R.id.mine_invite_container:
                if (null != user) {
                    Intent recommond = new Intent(getContext(), RecommendActivity.class);
                    startActivity(recommond);
                }
                break;
            case R.id.mine_support_tel:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(getString(R.string
                        .helper_tel)));
                //跳转到拨号界面，同时传递电话号码
                startActivity(dialIntent);
                break;
            case R.id.mine_daifukuan_container:
                ToastUtils.showToast(getContext(), "待付款");
                break;
            case R.id.mine_daishouhuo_container:
                if (null != user) {
                    Intent orderWait = new Intent(getContext(), OrderListActivity.class);
                    orderWait.putExtra(OrderListActivity.ORDER_KEY, OrderListActivity
                            .ORDER_WAIT_GOODS);
                    startActivity(orderWait);
                }
                break;
            case R.id.mine_daipingjia_container:
                ToastUtils.showToast(getContext(), "待评价");
                break;
            case R.id.mine_tuihuan_container:
                ToastUtils.showToast(getContext(), "退换货");
                break;
            case R.id.mine_order_container:
                if (null != user) {
                    Intent myOrderList = new Intent(getContext(), OrderListActivity.class);
                    myOrderList.putExtra(OrderListActivity.ORDER_KEY, OrderListActivity
                            .ORDER_COMPLETED);
                    startActivity(myOrderList);
                }
                break;
            case R.id.mine_pay_container:
                if (null != user) {
                    Intent payHistroy = new Intent(getContext(), PayHistroyActivity.class);
                    payHistroy.putExtra(OrderListActivity.ORDER_KEY, OrderListActivity
                            .ORDER_COMPLETED);
                    startActivity(payHistroy);
                }
                break;
            case R.id.mine_charge://提币流水
                if (null != user) {
                   Intent payHistroy = new Intent(getContext(), ExtrDetailsActivity.class);
                    payHistroy.putExtra(OrderListActivity.ORDER_KEY, OrderListActivity
                            .ORDER_COMPLETED);
                    startActivity(payHistroy);
                }
                break;
            case R.id.mine_sign://签到
                if (null != user) {
                   Intent payHistroy = new Intent(getContext(), SignInActivity.class);
                    payHistroy.putExtra(OrderListActivity.ORDER_KEY, OrderListActivity
                            .ORDER_COMPLETED);
                    startActivity(payHistroy);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        UserInfo info = ShoppingMallApp.getInstance().getUserInfo();
        if (info == null) {
            loadAvatar("", mMineHeader);
            mMineUsername.setText("");
            mMineVer.setText("未认证");
        }
    }
}
