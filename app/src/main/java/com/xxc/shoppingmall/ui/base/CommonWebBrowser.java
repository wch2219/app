package com.xxc.shoppingmall.ui.base;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.DefaultHandler;
import com.gyf.barlibrary.ImmersionBar;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.network.HJBridgeHandler;
import com.xxc.shoppingmall.network.HJWebViewClient;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.widget.CommonTitle;

import butterknife.BindView;

/**
 * Created by xuxingchen on 2017/11/21.
 * 本地浏览器
 */
public class CommonWebBrowser extends AbstractPermissionActivity {

    public static final String BROWSER_TITLE = "BrowserTitle";
    public static final String BROWSER_URL = "BrowserUrl";
    public static final String SHOW_TITLE = "showTitle";
    public static final String SHOW_STATE_BAR = "ShowStateBar";

    @BindView(R.id.browser_title)
    CommonTitle mCommonTitle;
    @BindView(R.id.common_browser)
    BridgeWebView mCommonBrowser;
    @BindView(R.id.browser_error)
    ImageView mBrowserError;

    protected String mUrl;
    private ImmersionBar mImmersionBar;
    protected String mTitle;

    @Override
    public void initUIWithPermission() {
        Intent from = getIntent();
        mTitle = from.getStringExtra(BROWSER_TITLE);
        boolean showStateBar = from.getBooleanExtra(SHOW_STATE_BAR, false);
        boolean showTitle = from.getBooleanExtra(SHOW_TITLE, true);
        mUrl = from.getStringExtra(BROWSER_URL);
        if (!TextUtils.isEmpty(mTitle)) {
            mCommonTitle.setTitle(mTitle);
        }

        if (showStateBar) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.init();   //所有子类都将继承这些相同的属性
        }

        if (!showTitle) {
            mCommonTitle.setVisibility(View.GONE);
        }

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_browser;
    }

    @Override
    public void initData() {
        configWebSetting(mCommonBrowser, this);

        mCommonBrowser.setDefaultHandler(new DefaultHandler());
        mCommonBrowser.registerHandler(NetConstant.JS_HANDLER_NAME, new HJBridgeHandler() {
            @Override
            public Activity getActivityContext() {
                return CommonWebBrowser.this;
            }
        });
        mCommonBrowser.setWebViewClient(new HJWebViewClient(mCommonBrowser) {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String
                    failingUrl) {
                mCommonBrowser.setVisibility(View.INVISIBLE);
                mBrowserError.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                mCommonBrowser.setVisibility(View.INVISIBLE);
                mBrowserError.setVisibility(View.VISIBLE);
            }
        });
        mCommonBrowser.loadUrl(mUrl);
    }

    @Override
    public void addListeners() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != mCommonBrowser) {
            mCommonBrowser.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != mCommonBrowser) {
            mCommonBrowser.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
        if (null != mCommonBrowser) {
            mCommonBrowser.destroy();
        }
    }
}
