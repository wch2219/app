package com.xxc.shoppingmall.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ActivityUtils;
import com.king.base.util.LogUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.utils.GlideExtra;
import com.xxc.shoppingmall.utils.ImageLoader;
import com.xxc.shoppingmall.widget.CommonTitle;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by xuxingchen on 2017/11/21.
 * 上传汇款凭证的界面
 */
public class UploadImagesActivity extends AbstractPermissionActivity {

    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 2;

    public static final String IMAGE_PATH = "image_selected";

    @BindView(R.id.upload_title)
    CommonTitle mUploadTitle;
    @BindView(R.id.click_upload)
    RelativeLayout mClickUpload;
    @BindView(R.id.upload_submit)
    Button mUploadSubmit;
    @BindView(R.id.upload_preview)
    ImageView mUploadPreview;

    private String mImgPath;

    @Override
    public void initUIWithPermission() {

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_upload_images;
    }

    @Override
    public void initData() {

    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.click_upload, R.id.upload_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.click_upload:
                LogUtils.d("11111");
                choicePhotoWrapper();
                break;
            case R.id.upload_submit:
                if (null != mImgPath) {
                    Intent intent = new Intent();
                    intent.putExtra(IMAGE_PATH, mImgPath);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        LogUtils.d("22222");
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (!ActivityUtils.isActivityExistsInStack(BGAPhotoPickerActivity.class)) {
                // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
                File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "HJPhotos");

                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                        .cameraFileDir(takePhotoDir) //
                        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                        .maxChooseCount(1) // 图片选择张数的最大值
                        .selectedPhotos(null) // 当前已选中的图片路径集合
                        .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                        .build();
                LogUtils.d("333333");
                startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
            }
        } else {
            LogUtils.d("444444");
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照",
                    PRC_PHOTO_PICKER, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            List<String> imagePaths = BGAPhotoPickerActivity.getSelectedPhotos(data);
            LogUtils.d(imagePaths.get(0));
            GlideExtra extra = new GlideExtra();
            extra.scaleType = GlideExtra.CENTER_CROP;
            ImageLoader.loadImage(this, mUploadPreview, "file://" + imagePaths.get(0), extra);
            mImgPath = imagePaths.get(0);
        }
    }
}
