package com.xxc.shoppingmall.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hss01248.dialog.config.BottomSheetStyle;
import com.hss01248.dialog.config.ConfigBean;
import com.hss01248.dialog.config.DefaultConfig;
import com.hss01248.dialog.interfaces.MyItemDialogListener;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.network.ParamKey;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.ui.base.IOnCompressCompleted;
import com.xxc.shoppingmall.utils.MyConfigBean;
import com.xxc.shoppingmall.utils.TimeUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.qqtheme.framework.picker.DatePicker;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/23.
 * 个人信息界面
 */
public class PersonInfoActivity extends AbstractPermissionActivity {

    public static final String PERSON_INFO = "personInfo";

    private static final int PRC_PHOTO_PICKER = 1;

    private static final int RC_CHOOSE_PHOTO = 1;

    public static final String IMAGE_PATH = "image_selected";

    public static final int REQUEST_CHANGE_NICK = 10;

    public static final int REQUEST_CHANGE_CARD_ID = 11;

    @BindView(R.id.person_info_header)
    ImageView mPersonInfoHeader;
    @BindView(R.id.person_info_header_container)
    RelativeLayout mPersonInfoHeaderContainer;
    @BindView(R.id.person_user_name)
    TextView mPersonUserName;
    @BindView(R.id.person_info_username_container)
    RelativeLayout mPersonInfoUsernameContainer;
    @BindView(R.id.person_nick_name)
    TextView mPersonNickName;
    @BindView(R.id.person_nick_name_container)
    RelativeLayout mPersonNickNameContainer;
    @BindView(R.id.person_sax)
    TextView mPersonSax;
    @BindView(R.id.person_sax_container)
    RelativeLayout mPersonSaxContainer;
    @BindView(R.id.person_tv_birthday)
    TextView mPersonTvBirthday;
    @BindView(R.id.person_birthday_container)
    RelativeLayout mPersonBirthdayContainer;
    @BindView(R.id.person_submit)
    Button mPersonSubmit;
    @BindView(R.id.person_card_id_name)
    TextView mPersonCardIdName;
    @BindView(R.id.person_card_id_container)
    RelativeLayout mPersonCardIdContainer;


    private UserInfo mUserInfo;
    private String mImgPath;
    private File mOutFileHeader;

    @Override
    public void initUIWithPermission() {
        mUserInfo = (UserInfo) getIntent().getSerializableExtra(PERSON_INFO);
        if (mUserInfo != null) {
            loadAvatar(NetConstant.IMGAE_PATH + mUserInfo.getData().getAvatarUrl(),
                    mPersonInfoHeader);
            mPersonUserName.setText(mUserInfo.getData().getUserName());
            mPersonNickName.setText(mUserInfo.getData().getNickName());
            String sex = mUserInfo.getData().getSex() == UserInfo.SECRET ? "保密" : mUserInfo
                    .getData().getSex() == UserInfo.MAN ? "男" : "女";
            mPersonSax.setText(sex);
            if (TextUtils.isEmpty(mUserInfo.getData().getIdCard())) {
                mPersonCardIdName.setText(String.valueOf(""));
            }else
            mPersonCardIdName.setText(String.valueOf(mUserInfo.getData().getIdCard()));
            String birthday = mUserInfo.getData().getBirthday();
            if (!TextUtils.isEmpty(birthday)) {
                mPersonTvBirthday.setText(TimeUtils.getFormatDay(Long.valueOf(birthday)));
            } else {
                mPersonTvBirthday.setText("暂无");
            }
        }
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_person_info;
    }

    @Override
    public void initData() {
        mOutFileHeader = new File(ShoppingMallApp.OUT_PATH, "HJHeader.jpg");
    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.person_info_header_container, R.id.person_info_username_container, R.id
            .person_nick_name_container, R.id.person_sax_container, R.id
            .person_birthday_container, R.id.person_submit, R.id.person_card_id_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.person_info_header_container:
                mImgPath = null;
                choicePhotoWrapper();
                break;
            case R.id.person_info_username_container:
                ToastUtils.showToast(this, "用户名不可修改");
                break;
            case R.id.person_nick_name_container:
//                Intent changeNick = new Intent(this, NickActivity.class);
//                startActivityForResult(changeNick, REQUEST_CHANGE_NICK);
                break;
            case R.id.person_sax_container:
                if (!mPersonSax.getText().toString().trim().equals("保密")) {
                    ToastUtils.showToast(PersonInfoActivity.this,"性别不可重复修改");
                    return;
                }
                List<String> saxs = new ArrayList<>();
                saxs.add("保密");
                saxs.add("男");
                saxs.add("女");
                MyItemDialogListener listener = new MyItemDialogListener() {
                    @Override
                    public void onItemClick(CharSequence charSequence, int i) {
                        mPersonSax.setText(charSequence);
                    }
                };
                DefaultConfig.bottomTxt = "取消";
                ConfigBean bean = assignBottomItemDialog(this, saxs, listener).setActivity(this)
                        .setBottomSheetStyle(BottomSheetStyle.newBuilder().bottomTxtColor
                                (getResources().getColor(R.color.color_ff4040)).build());
                bean.show();
                break;
            case R.id.person_birthday_container:
                if (!mPersonTvBirthday.getText().toString().trim().equals("暂无")) {
                    ToastUtils.showToast(PersonInfoActivity.this,"生日不可重复修改");
                    return;
                }
                DatePicker picker = new DatePicker(this);
                picker.setDividerColor(getResources().getColor(R.color.color_ff4040));
                picker.setTextColor(getResources().getColor(R.color.color_ff4040));
                picker.setCancelTextColor(getResources().getColor(R.color.color_ff4040));
                picker.setSubmitTextColor(getResources().getColor(R.color.color_ff4040));
                picker.setTopLineColor(getResources().getColor(R.color.color_ff4040));
                picker.setLabelTextColor(getResources().getColor(R.color.color_ff4040));
                picker.setPressedTextColor(getResources().getColor(R.color.color_ff4040));
                DatePicker.OnYearMonthDayPickListener onDatePickListener = new DatePicker
                        .OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        mPersonTvBirthday.setText(year + "-" + month + "-" + day);
                    }
                };
                picker.setOnDatePickListener(onDatePickListener);
                picker.setCanceledOnTouchOutside(false);
                picker.setRangeStart(1950, 1, 1);
                Calendar cal = Calendar.getInstance();
                //当前年
                int year = cal.get(Calendar.YEAR);
                //当前月
                int month = (cal.get(Calendar.MONTH)) + 1;
                //当前月的第几天：即当前日
                int day = cal.get(Calendar.DAY_OF_MONTH);
                picker.setSelectedItem(year, month, day);
                picker.show();
                break;
            case R.id.person_submit:
                String nickName = mPersonNickName.getText().toString().trim();
                String sax = mPersonSax.getText().toString().trim();
                String birthday = mPersonTvBirthday.getText().toString().trim();
                String personCard = mPersonCardIdName.getText().toString().trim();
                if (!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(sax) && !TextUtils.isEmpty
                        (birthday) && !TextUtils.isEmpty(personCard)) {
                    final Map<String, Object> params = new HashMap<>();
                    params.put(ParamKey.NICK_NAME, nickName);
                    params.put(ParamKey.ID_CARD, personCard);
                    params.put(ParamKey.SEX, "保密".equals(sax) ? 0 : "男".equals(sax) ? 1 : 2);
                    if (!"暂无".equals(birthday)) {
                        params.put(ParamKey.BIRTHDAY, birthday);
                    }
                    params.put(ParamKey.USERID, ShoppingMallApp.getInstance().getUser().getData()
                            .getUserId());
                    showLoadingDialog("提交中。。。", false);
                    if (null == mImgPath) {
                        submitPersonInfos(params, null);
                    } else {
                        compressImage(mImgPath, mOutFileHeader.getAbsolutePath(), new
                                IOnCompressCompleted() {

                                    @Override
                                    public void completed(String outPath, File outFile) {
                                        submitPersonInfos(params, outFile);
                                    }
                                });
                    }
                } else {
                    ToastUtils.showToast(this, "请完善您的姓名，性别，生日等信息~");
                }
                break;
            case R.id.person_card_id_container:
//                Intent chagnePersonId = new Intent(this, ChangePersonIdCardActivity.class);
//                startActivityForResult(chagnePersonId, REQUEST_CHANGE_CARD_ID);
                break;
        }
    }

    private void submitPersonInfos(Map<String, Object> params, File file) {
        Call<SubmitOrder> call;
        if (null == file) {
            call = mHttpApi.submitPersonInfo(params);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    file);
            call = mHttpApi.submitPersonInfo(params, requestBody);
        }
        Callback<SubmitOrder> callback = new EasyCallBack<SubmitOrder>() {
            @Override
            public void onSuccess(Call<SubmitOrder> call, SubmitOrder model) {
                dismissLoading();
                if (null != model) {
                    if (model.getMsg().isSuccess()) {
                        mImgPath = null;
                        ToastUtils.showToast(PersonInfoActivity.this, "提交成功");
                        postDelayed(1200, new Runnable() {
                            @Override
                            public void run() {
                                Intent preIntent = getIntent();
                                setResult(Activity.RESULT_OK, preIntent);
                                finish();
                            }
                        });
                    } else {
                        ToastUtils.showToast(PersonInfoActivity.this, model.getMsg().getInfo());
                    }
                }
            }
        };
        requestApi(call, callback);
    }

    @AfterPermissionGranted(PRC_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "HJPhotos");

            Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                    .cameraFileDir(takePhotoDir) //
                    // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                    .maxChooseCount(1) // 图片选择张数的最大值
                    .selectedPhotos(null) // 当前已选中的图片路径集合
                    .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                    .build();
            startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
        } else {
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
            loadAvatar("file://" + imagePaths.get(0), mPersonInfoHeader);
            mImgPath = imagePaths.get(0);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHANGE_NICK) {
            String newNick = data.getStringExtra(NickActivity.NEW_NICK);
            mPersonNickName.setText(newNick);
        } else if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CHANGE_CARD_ID) {
            String personCardId = data.getStringExtra(ChangePersonIdCardActivity.PERSON_CARD_ID);
            mPersonCardIdName.setText(personCardId);
        }
    }

    public ConfigBean assignBottomItemDialog(Context context, List<? extends CharSequence> words,
                                             MyItemDialogListener listener) {
        ConfigBean bean = new MyConfigBean();
        bean.context = context;
        bean.itemListener = listener;
        bean.wordsIos = words;
        bean.type = 7;
        bean.gravity = 80;
        return bean;
    }

}
