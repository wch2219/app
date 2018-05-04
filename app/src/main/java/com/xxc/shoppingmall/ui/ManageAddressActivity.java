package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.adapter.AddressAdapter;
import com.xxc.shoppingmall.model.Address;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.CommonTitle;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/24.
 * 地址管理界面
 */
public class ManageAddressActivity extends AbstractPermissionActivity {

    public static final int REQUEST_UPDATE_ADDRESS = 100;
    public static final int REQUEST_ADD_ADDRESS = 101;

    @BindView(R.id.manage_address_title)
    CommonTitle mManageAddressTitle;
    @BindView(R.id.manage_address_list)
    RecyclerView mManageAddressList;
    @BindView(R.id.manage_address_bottom)
    RelativeLayout mManageAddressBottom;

    private AddressAdapter mAddressAdapter;
    private UserInfo mUserInfo;
    private Address mAddress;

    private BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener;

    @Override
    public void initUIWithPermission() {
        mUserInfo = ShoppingMallApp.getInstance().getUserInfo();
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_manage_address;
    }

    @Override
    public void initData() {
        if (null != mUserInfo) {
            initAddressList();
        } else {
            Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
                @Override
                public void onSuccess(Call<UserInfo> call, UserInfo model) {
                    if (model != null) {
                        if (model.getMsg().isSuccess()) {
                            mUserInfo = model;
                            initAddressList();
                            ShoppingMallApp.getInstance().setUserInfo(model);
                        }
                    } else {
                        ToastUtils.showToast(ManageAddressActivity.this, null == model ? "加载失败" :
                                model.getMsg().getInfo());
                    }
                }
            };
            getPersonInfo(callback);
        }
    }

    private void initAddressList() {
        mOnItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final Address.DataBean bean = mAddress.getData().get(position);
                switch (view.getId()) {
                    case R.id.address_edit_container:
                        Intent editAddress = new Intent(ManageAddressActivity.this,
                                AddAddressActivity.class);
                        editAddress.putExtra(AddAddressActivity.ADDRESS_KEY, mAddress.getData
                                ().get(position));
                        startActivityForResult(editAddress, REQUEST_UPDATE_ADDRESS);
                        break;
                    case R.id.address_delete_container:
                        MaterialDialog.Builder builder = new MaterialDialog.Builder
                                (ManageAddressActivity.this)
                                .autoDismiss(false)
                                .canceledOnTouchOutside(false).positiveText("确定")
                                .negativeText("取消").onPositive
                                        (new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog
                                                                        dialog, @NonNull
                                                                        DialogAction which) {
                                                dialog.dismiss();
                                                deleteAddress(mUserInfo.getData().getUserId()
                                                        , bean.getId());
                                            }
                                        }).onNegative(new MaterialDialog.SingleButtonCallback
                                        () {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog,
                                                        @NonNull DialogAction
                                                                which) {
                                        dialog.dismiss();
                                    }
                                }).title("删除地址").content("您确定要删除该地址吗？");
                        MaterialDialog dialog = builder.build();
                        dialog.show();
                        break;
                    case R.id.address_default_container:
                        if (bean.getIsDefault() != 1) {
                            updateAddress(bean.getName(), bean.getMobile(), bean
                                    .getUserAddress(), true, bean);
                        }
                        break;
                }
            }
        };
        showLoadingDialog("正在同步地址。。。");
        getAddressList(mUserInfo.getData().getUserId());
    }

    private void getAddressList(String userId) {
        Call<Address> call = mHttpApi.getAddressList(userId);
        Callback<Address> callback = new EasyCallBack<Address>() {
            @Override
            public void onSuccess(Call<Address> call, Address model) {
                dismissLoading();
                if (null != model) {
                    mAddress = model;
                    mManageAddressList.setLayoutManager(new LinearLayoutManager
                            (ManageAddressActivity.this));
                    if (null == mAddressAdapter) {
                        mAddressAdapter = new AddressAdapter(model.getData(),
                                ManageAddressActivity.this);
                        mAddressAdapter.setOnItemChildClickListener(mOnItemChildClickListener);
                        mManageAddressList.addItemDecoration(new HorizontalDividerItemDecoration
                                .Builder
                                (ManageAddressActivity.this)
                                .colorResId(R.color.color_f4f4f4).sizeResId(R.dimen.normal_6dp)
                                .build());
                        mManageAddressList.setAdapter(mAddressAdapter);
                    } else {
                        mAddressAdapter.setNewData(model.getData());
                        mAddressAdapter.notifyDataSetChanged();
                    }
                }
            }
        };
        requestApi(call, callback);
    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.manage_address_bottom)
    public void onViewClicked() {
        Intent addAddress = new Intent(ManageAddressActivity.this, AddAddressActivity.class);
        startActivityForResult(addAddress, REQUEST_ADD_ADDRESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            showLoadingDialog("正在同步地址。。。");
            getAddressList(mUserInfo.getData().getUserId());
        }
    }

    public void updateAddress(String receiver, String phone, String address, boolean isChecked,
                              Address.DataBean
                                      dataBean) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.USER_ADDRESS, address);
        params.put(ParamKey.IS_DEFAULT, isChecked ? 1 : 0);
        params.put(ParamKey.MOBILE, phone);
        params.put(ParamKey.NAME, receiver);
        params.put("id", dataBean.getId());
        Call<SubmitOrder> call = mHttpApi.updateAddress(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    getAddressList(mUserInfo.getData().getUserId());
                } else {
                    dismissLoading();
                    ToastUtils.showToast(ManageAddressActivity.this, null == model ? "请求失败" :
                            model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("更新地址中。。。");
        requestApi(call, callback);
    }


    private void deleteAddress(String userId, int id) {
        Call<SubmitOrder> call = mHttpApi.deleteAddress(userId, id);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    getAddressList(mUserInfo.getData().getUserId());
                } else {
                    dismissLoading();
                    ToastUtils.showToast(ManageAddressActivity.this, null == model ? "请求失败" :
                            model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("更新地址中");
        requestApi(call, callback);
    }
}
