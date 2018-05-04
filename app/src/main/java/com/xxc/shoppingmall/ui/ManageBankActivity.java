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
import com.xxc.shoppingmall.adapter.BankCardAdapter;
import com.xxc.shoppingmall.model.BankCard;
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
 * 银行卡管理界面
 */
public class ManageBankActivity extends AbstractPermissionActivity {

    public static final int REQUEST_ADD_BANKCARD = 100;
    public static final int REQUEST_UPDATE_BANKCARD = 101;

    @BindView(R.id.bank_title)
    CommonTitle mBankTitle;
    @BindView(R.id.manage_bank_list)
    RecyclerView mManageBankList;
    @BindView(R.id.manage_bank_bottom)
    RelativeLayout mManageBankBottom;

    private BankCardAdapter mBankCardAdapter;
    private UserInfo mUserInfo;
    private BankCard mBankCard;
    private BaseQuickAdapter.OnItemChildClickListener mOnItemChildClickListener;

    @Override
    public void initUIWithPermission() {
        mUserInfo = ShoppingMallApp.getInstance().getUserInfo();
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_manage_bank;
    }

    @Override
    public void initData() {
        if (null != mUserInfo) {
            initBankList();
        } else {
            Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
                @Override
                public void onSuccess(Call<UserInfo> call, UserInfo model) {
                    if (model != null) {
                        if (model.getMsg().isSuccess()) {
                            mUserInfo = model;
                            initBankList();
                            ShoppingMallApp.getInstance().setUserInfo(model);
                        }
                    } else {
                        ToastUtils.showToast(ManageBankActivity.this, null == model ? "加载失败" :
                                model.getMsg().getInfo());
                    }
                }
            };
            getPersonInfo(callback);
        }
    }

    private void initBankList() {
        mOnItemChildClickListener = new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final BankCard.DataBean bean = mBankCard.getData().get(position);
                switch (view.getId()) {
                    case R.id.bank_default_container:
                        if (bean.getIsDefault() != 1) {
                            updateBankCardInfo(bean.getBankName(), bean.getBankNum(), 1, bean);
                        }
                        break;
                    case R.id.bank_edit_container:
                        Intent editAddress = new Intent(ManageBankActivity.this,
                                AddBankCardActivity.class);
                        editAddress.putExtra(AddBankCardActivity.BANK_DATA_KEY, mBankCard.getData
                                ().get(position));
                        startActivityForResult(editAddress, REQUEST_UPDATE_BANKCARD);
                        break;
                    case R.id.bank_delete_container:
                        MaterialDialog.Builder builder = new MaterialDialog.Builder
                                (ManageBankActivity.this)
                                .autoDismiss(false)
                                .canceledOnTouchOutside(false).positiveText("确定").negativeText
                                        ("取消").onPositive
                                        (new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog,
                                                                @NonNull

                                                    DialogAction which) {
                                                dialog.dismiss();
                                                deleteAddress(mUserInfo.getData().getUserId(),
                                                        bean.getId());
                                            }
                                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull
                                            DialogAction
                                            which) {
                                        dialog.dismiss();
                                    }
                                }).title("删除银行卡").content("您确定要删除该银行卡吗？");
                        MaterialDialog dialog = builder.build();
                        dialog.show();
                        break;
                }
            }
        };
        showLoadingDialog("获取银行卡列表中。。。");
        getBankCardList(mUserInfo.getData().getUserId());
    }

    private void getBankCardList(String userId) {
        Call<BankCard> call = mHttpApi.getBankCardList(userId);
        Callback<BankCard> cardCallback = new EasyCallBack<BankCard>() {
            @Override
            public void onSuccess(Call<BankCard> call, BankCard model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess()) {
                    mBankCard = model;
                    mManageBankList.setLayoutManager(new LinearLayoutManager(ManageBankActivity
                            .this));
                    if (null == mBankCardAdapter) {
                        mBankCardAdapter = new BankCardAdapter(model.getData(),
                                ManageBankActivity.this);
                        mBankCardAdapter.setOnItemChildClickListener(mOnItemChildClickListener);
                        mManageBankList.addItemDecoration(new HorizontalDividerItemDecoration
                                .Builder
                                (ManageBankActivity.this).colorResId(R.color.color_f4f4f4)
                                .sizeResId(R.dimen
                                .normal_6dp).build());
                        mManageBankList.setAdapter(mBankCardAdapter);
                    } else {
                        mBankCardAdapter.setNewData(model.getData());
                        mBankCardAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showToast(ManageBankActivity.this, null == model ? "请求失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        requestApi(call, cardCallback);
    }

    @Override
    public void addListeners() {

    }

    @OnClick(R.id.manage_bank_bottom)
    public void onViewClicked() {
        Intent addbankCard = new Intent(this, AddBankCardActivity.class);
        startActivityForResult(addbankCard, REQUEST_ADD_BANKCARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            showLoadingDialog("正在同步银行卡信息。。。");
            getBankCardList(mUserInfo.getData().getUserId());
        }
    }

    private void updateBankCardInfo(String bankName, String bankNum, int isDefault, BankCard
            .DataBean dataBean) {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        Map<String, Object> params = new HashMap<>();
        params.put(ParamKey.USERID, userId);
        params.put(ParamKey.BANK_NUM, bankNum);
        params.put(ParamKey.BANK_NAME, bankName);
        params.put(ParamKey.IS_DEFAULT, 1 == isDefault ? 1 : 0);
        if (null != dataBean) {
            params.put("id", dataBean.getId());
        }
        Call<SubmitOrder> call = mHttpApi.updateBankCard(params);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    getBankCardList(mUserInfo.getData().getUserId());
                } else {
                    dismissLoading();
                    ToastUtils.showToast(ManageBankActivity.this, null == model ? "请求失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("正在更新银行卡信息");
        requestApi(call, callback);
    }

    private void deleteAddress(String userId, int id) {
        Call<SubmitOrder> call = mHttpApi.deleteBankcard(userId, id);
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                if (null != model && model.getMsg().isSuccess()) {
                    getBankCardList(mUserInfo.getData().getUserId());
                } else {
                    dismissLoading();
                    ToastUtils.showToast(ManageBankActivity.this, null == model ? "请求失败" : model
                            .getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("更新银行卡信息中...");
        requestApi(call, callback);
    }
}
