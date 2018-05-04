package com.xxc.shoppingmall.ui;

import android.content.pm.PackageManager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.king.base.util.SystemUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.model.VersionUpdate;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.utils.UpdateCallBack;
import com.xxc.shoppingmall.widget.CommonTitle;

import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by xuxingchen on 2017/11/25.
 * 关于的界面
 */
public class AboutActivity extends AbstractPermissionActivity {

    private static final String CHECK_URL = "/api/appversion/getVersion";

    private static final String VERSION_CODE_FORMAT = "版本号:version %s";

    @BindView(R.id.about_title)
    CommonTitle mAboutTitle;
    @BindView(R.id.about_version)
    TextView mAboutVersion;
    @BindView(R.id.about_check_version)
    TextView mAboutCheckVersion;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_about;
    }

    @Override
    public void initData() {
        String versionName = SystemUtils.getVersionName(this);
        mAboutVersion.setText(String.format(VERSION_CODE_FORMAT, versionName));
    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.about_check_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about_check_version:
                try {
                    String versionName = getPackageManager().getPackageInfo(
                            getPackageName(), 0).versionName;
                    int versionCode = getPackageManager().getPackageInfo(
                            getPackageName(), 0).versionCode;
                    UpdateBuilder.create().url(NetConstant.BASE_URL + CHECK_URL + "?version=" +
                            versionName)
                            .jsonParser(new UpdateParser() {

                                @Override
                                public Update parse(String httpResponse)
                                        throws Exception {
                                    Gson gson = new Gson();
                                    VersionUpdate update = gson.fromJson(httpResponse,
                                            VersionUpdate.class);
                                    Update updateBean = new Update();
                                    updateBean.setForced(false);
                                    updateBean.setIgnore(false);
                                    String updateUrl = update.getData().getStoragepath() + update
                                            .getData().getStoragefile();
                                    updateBean.setUpdateUrl(updateUrl);
                                    updateBean.setVersionName(update.getData().getName());
                                    updateBean.setVersionCode(update.getData().getCode());
                                    updateBean.setUpdateContent(update.getData().getMemo());
                                    return updateBean;
                                }
                            }).checkCB(new UpdateCallBack()).downloadCB(new UpdateCallBack())
                            .check();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
