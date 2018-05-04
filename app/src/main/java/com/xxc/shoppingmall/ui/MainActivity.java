package com.xxc.shoppingmall.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import com.gyf.barlibrary.ImmersionBar;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.adapter.MainViewPagerAdapter;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.fragments.HomeFragment;
import com.xxc.shoppingmall.ui.fragments.MineFragment;
import com.xxc.shoppingmall.ui.fragments.SMallFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * app主界面
 */
public class MainActivity extends AbstractPermissionActivity {


    @BindView(R.id.main_viewpager)
    ViewPager mMainViewpager;
    @BindView(R.id.main_tablayout)
    TabLayout mMainTablayout;

    List<Fragment> mFragments = new ArrayList<>();
    MainViewPagerAdapter mMainViewPagerAdapter;

    private ImmersionBar mImmersionBar;
    private long mLatestClick;

    @Override
    public void initUIWithPermission() {
        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.init();   //所有子类都将继承这些相同的属性
        mFragments.add(new HomeFragment());
        mFragments.add(new SMallFragment());
        mFragments.add(new MineFragment());
        mMainViewPagerAdapter = new MainViewPagerAdapter(mFragments, getSupportFragmentManager());
        mMainViewpager.setAdapter(mMainViewPagerAdapter);
        mMainTablayout.setupWithViewPager(mMainViewpager);

        mMainTablayout.getTabAt(0).setIcon(R.drawable.main_home_selector);
        mMainTablayout.getTabAt(1).setIcon(R.drawable.main_shopping_mall_selector);
        mMainTablayout.getTabAt(2).setIcon(R.drawable.main_mine_selector);

    }

    @Override
    public int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CONTACTS,Manifest.permission.CAMERA
            }, 1002);
        }
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLatestClick > 2000) {
            mLatestClick = System.currentTimeMillis();
            ToastUtils.showToast(this, "再按一次退出");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }
}
