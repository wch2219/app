package com.xxc.shoppingmall.ui;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.MyTeam;
import com.xxc.shoppingmall.model.QueryWater;
import com.xxc.shoppingmall.model.Record;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.HJBridgeHandler;
import com.xxc.shoppingmall.network.HJWebViewClient;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.widget.NoticeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/21.
 * 我的团队界面
 */
public class MyTeamActivity extends AbstractPermissionActivity {
    @BindView(R.id.team_browser)
    BridgeWebView mTeamBrowser;
    @BindView(R.id.browser_error)
    ImageView mBrowserError;
    @BindView(R.id.head_my)
    ImageView head_my;

    @BindView(R.id.team_numb)
    TextView team_numb;
    @Override
    public void initUIWithPermission() {
        configWebSetting(mTeamBrowser, this);
        mTeamBrowser.setWebViewClient(new HJWebViewClient(mTeamBrowser) {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String
                    failingUrl) {
                mTeamBrowser.setVisibility(View.INVISIBLE);
                mBrowserError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                mTeamBrowser.setVisibility(View.INVISIBLE);
                mBrowserError.setVisibility(View.VISIBLE);
            }
        });
        mTeamBrowser.loadUrl(NetConstant.TEAM_URL);
//        mTeamBrowser.loadUrl("http://www.sina.com");
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_team;
    }

    @Override
    public void initData() {
        mTeamBrowser.setDefaultHandler(new DefaultHandler());
        mTeamBrowser.registerHandler(NetConstant.JS_HANDLER_NAME, new HJBridgeHandler() {
            @Override
            public Activity getActivityContext() {
                return MyTeamActivity.this;
            }
        });
        getTeam();
        getChildRechargeRecord();


        head_my.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(MyTeamActivity.this,PersonActivity.class);
//                startActivity(intent);
            }
        });
    }
    private void getChildRechargeRecord() {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        LogUtils.d(userId + "---");
        Call<Record> call = mHttpApi.GetChildRecord(userId);
        Callback<Record> callback = new EasyCallBack<Record>() {
            @Override
            public void onSuccess(Call<Record> call, Record model) {
                if (null != model && model.getMsg().isSuccess()) {
                    Log.i("123",""+model.getData().toString());
                    NoticeView noticeView = (NoticeView) findViewById(R.id.home_public_notice);
                    noticeView.addNotice(model.getData());
                    noticeView.startFlipping();
                } else {
                    ToastUtils.showToast(MyTeamActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }
    private void getTeam() {
        String userId = ShoppingMallApp.getInstance().getUser().getData().getUserId();
        LogUtils.d(userId + "---");
        Call<MyTeam> call = mHttpApi.GetTeam(userId);
        Callback<MyTeam> callback = new EasyCallBack<MyTeam>() {
            @Override
            public void onSuccess(Call<MyTeam> call, MyTeam model) {
                if (null != model && model.getMsg().isSuccess()) {
                    Log.i("123",""+model.getData().toString());
                    team_numb.setText(String.valueOf(model.getData().getNumber()));
                    if(null!=model.getData().getTeamList().get(0).getAvatarUrl()){
                        Glide.with(MyTeamActivity.this)
                                .load(NetConstant.IMGAE_PATH+model.getData().getTeamList().get(0).getAvatarUrl())
                                .into(head_my);
                    }

                } else {
                    ToastUtils.showToast(MyTeamActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        requestApi(call, callback);
    }
    @Override
    public void addListeners() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mTeamBrowser) {
            mTeamBrowser.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mTeamBrowser) {
            mTeamBrowser.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mTeamBrowser) {
            mTeamBrowser.destroy();
        }
    }
}
