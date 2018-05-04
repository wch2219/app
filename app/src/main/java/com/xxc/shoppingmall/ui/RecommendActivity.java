package com.xxc.shoppingmall.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.king.base.util.LogUtils;
import com.king.base.util.ToastUtils;
import com.xxc.shoppingmall.R;
import com.xxc.shoppingmall.ShoppingMallApp;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.model.VersionUpdate;
import com.xxc.shoppingmall.network.EasyCallBack;
import com.xxc.shoppingmall.network.NetConstant;
import com.xxc.shoppingmall.ui.base.AbstractPermissionActivity;
import com.xxc.shoppingmall.utils.AndroidShare;
import com.xxc.shoppingmall.utils.GlideExtra;
import com.xxc.shoppingmall.utils.ImageLoader;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by xuxingchen on 2017/11/20.
 * 推荐界面
 */
public class RecommendActivity extends AbstractPermissionActivity {

    private static final String CLICK_COPY = "点击复制邀请码:\n%s";

    @BindView(R.id.recommend_qr_code)
    ImageView mRecommendQrCode;
    @BindView(R.id.recommend_download_link_url)
    TextView mRecommendDownloadLinkUrl;
    @BindView(R.id.recommend_wechat_container)
    LinearLayout mRecommendWechatContainer;
    @BindView(R.id.recommend_qq_container)
    LinearLayout mRecommendQqContainer;
    @BindView(R.id.recommend_weibo_container)
    LinearLayout mRecommendWeiboContainer;
    @BindView(R.id.recommend_moments_container)
    LinearLayout mRecommendMomentsContainer;
    @BindView(R.id.recommend_btn_completed)
    Button mRecommendBtnCompleted;

    private AndroidShare mShare;
//    private Bitmap mBitmap;

    @Override
    public void initUIWithPermission() {
        UserInfo info = ShoppingMallApp.getInstance().getUserInfo();
        if (null != info) {
            String inviteCode = info.getData().getInvitecode();
            if (TextUtils.isEmpty(inviteCode)) {
                ToastUtils.showToast(RecommendActivity.this, "暂无您的邀请码信息");
            } else {
                mRecommendDownloadLinkUrl.setText(String.format(CLICK_COPY, inviteCode));
            }
        } else {
            Callback<UserInfo> callback = new EasyCallBack<UserInfo>() {
                @Override
                public void onSuccess(Call<UserInfo> call, UserInfo model) {
                    if (null != model && model.getMsg().isSuccess()) {
                        ShoppingMallApp.getInstance().setUserInfo(model);
                        String inviteNum = model.getData().getInvitecode();
                        if (!TextUtils.isEmpty(inviteNum)) {
                            mRecommendDownloadLinkUrl.setText(String.format(CLICK_COPY, inviteNum));
                        } else {
                            ToastUtils.showToast(RecommendActivity.this, "暂无您的邀请码信息");
                        }
                    } else {
                        ToastUtils.showToast(RecommendActivity.this, null == model ? "加载失败"
                                : model.getMsg().getInfo());
                    }
                }
            };
            getPersonInfo(callback);
        }

    }

    @Override
    public int layoutRes() {
        return R.layout.layout_recommend;
    }

    @Override
    public void initData() {
        mShare = new AndroidShare(this);
//        mBitmap = BitmapFactory.decodeFile
//                ("/storage/emulated/0/Download/f1d5b996ecd440365551ab1ab58d9a94.jpeg");

        Map<String, Object> params = new HashMap<>();
        params.put("tuijian", "Y");
        getVersionInfos(params);
    }

    private void getVersionInfos(Map<String, Object> params) {
        Call<VersionUpdate> call = mHttpApi.getVersionInfo(params);
        Callback<VersionUpdate> callback = new EasyCallBack<VersionUpdate>() {
            @Override
            public void onSuccess(Call<VersionUpdate> call, VersionUpdate model) {
                dismissLoading();
                if (null != model && model.getMsg().isSuccess()) {
                    GlideExtra extra = new GlideExtra();
                    extra.placeHolderRes = R.drawable.img_morenshangpin;
                    extra.scaleType = GlideExtra.CENTER_CROP;
                    ImageLoader.loadImage(RecommendActivity.this, mRecommendQrCode, NetConstant
                            .IMGAE_PATH + model.getData().getImageurl(), extra);
                } else {
                    ToastUtils.showToast(RecommendActivity.this, null == model ? "加载失败"
                            : model.getMsg().getInfo());
                }
            }
        };
        showLoadingDialog("加载中。。。");
        requestApi(call, callback);
    }

    @Override
    public void addListeners() {

    }

    @OnClick({R.id.recommend_qr_code, R.id.recommend_download_link_url, R.id
            .recommend_wechat_container, R.id.recommend_qq_container, R.id
            .recommend_weibo_container, R.id.recommend_moments_container, R.id
            .recommend_btn_completed})
    public void onViewClicked(View view) {
        UserInfo info = ShoppingMallApp.getInstance().getUserInfo();
        final String content = "泓济商城，健康生活，诚邀您的加入，https://www.pgyer.com/63k5，点击链接下载应用，快快加入吧！我的邀请码是：" + info
                .getData().getInvitecode();
//        String path = "/storage/emulated/0/Download" +
//                "/f1d5b996ecd440365551ab1ab58d9a94.jpeg";
        switch (view.getId()) {
            case R.id.recommend_qr_code:
                break;
            case R.id.recommend_download_link_url:
                String invitCode = ShoppingMallApp.getInstance().getUserInfo().getData()
                        .getInvitecode();
                if (!TextUtils.isEmpty(invitCode)) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context
                            .CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("已复制到剪切板", invitCode);
                    cm.setPrimaryClip(clip);
                    ToastUtils.showToast(this, "已复制到您的剪切板");
                } else {
                    ToastUtils.showToast(this, "暂无邀请码");
                }

                break;
            case R.id.recommend_wechat_container:
//                path = "content://media/external/images/media/110577";
//                Intent imageIntent = new Intent(Intent.ACTION_SEND);
//                imageIntent.setType("image/*");
//                Uri uri = Uri.fromFile(new File(path));
////                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
//                imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
//                imageIntent.putExtra("Kdescription", "文字内容");
//                startActivity(Intent.createChooser(imageIntent, "分享"));

                mShare.shareWeChatFriend("分享给好友", content, AndroidShare.TEXT, (Bitmap) null);


//                mShare.shareWeChatFriend("分享给好友", "邀您加入", AndroidShare.DRAWABLE, mBitmap);
                break;
            case R.id.recommend_qq_container:
                mShare.shareQQFriend("分享给好友", content, AndroidShare.TEXT, (Bitmap) null);
                break;
            case R.id.recommend_weibo_container:
                Bitmap bitmapWeiBo = BitmapFactory.decodeResource(getResources(), R.drawable
                        .friend);


                mShare.shareWeiBo("分享给好友", content, AndroidShare.DRAWABLE, bitmapWeiBo);

//                Intent textIntent = new Intent(Intent.ACTION_SEND);
//                textIntent.setType("text/plain");
//                textIntent.putExtra(Intent.EXTRA_TEXT, "这是一段分享的文字");
//                startActivity(Intent.createChooser(textIntent, "分享"));


//                Bitmap bitmap = BitmapFactory.decodeFile
//                        ("/storage/emulated/0/Download/f1d5b996ecd440365551ab1ab58d9a94.jpeg");
//                AndroidShare share = new AndroidShare(this);
//                share.shareWeChatFriend("泓济邀请您", "加入泓济吧", AndroidShare.DRAWABLE, bitmap);


//                String path = "https://timgsa.baidu" +
//                        ".com/timg?image&quality=80&size=b9999_10000&sec=1516794434371&di" +
//                        "=05ff9d4f9c2976f1cbe14e47d13776b2&imgtype=0&src=http%3A%2F%2Fimgsrc" +
//                        ".baidu.com%2Fimage%2Fc0%253Dpixel_huitu%252C0%252C0%252C294%252C40" +
//                        "%2Fsign%3Df44ecf380155b31988f48a352ad1e74a" +
//                        "%2F29381f30e924b899f28cfa0165061d950a7bf653.jpg";
//                String path = getResourcesUri(R.drawable.img_logo);
//                Intent imageIntent = new Intent(Intent.ACTION_SEND);
//                imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                imageIntent.setType("image/*");
//                imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
//                imageIntent.putExtra("Kdescription", "wwwwwwwwwwwwwwwwwwww");
//                imageIntent.putExtra(Intent.EXTRA_TEXT, "扫描这个二维码下载泓济商城");
//                imageIntent.putExtra(Intent.EXTRA_SUBJECT, "标题");
//                startActivity(Intent.createChooser(imageIntent, "分享给朋友"));
                break;
            case R.id.recommend_moments_container:
//                Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.friend)).getBitmap();
                final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.friend);
                Glide.with(this).asBitmap().load(resourceIdToUri(this,R.drawable.friend)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mShare.shareWeChatFriendCircle("分享到朋友圈", content, resource);
                    }
                });

//                String path1 = getResourcesUri(R.drawable.img_logo);
//                ToastUtils.showToast(this, "朋友圈");
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui
// .tools" +
//                        ".ShareToTimeLineUI"));
//                intent.setAction(Intent.ACTION_SEND_MULTIPLE);
//                intent.setType("image/jpeg");
//                Uri uri = Uri.fromFile(new File
//                        ("/storage/emulated/0/Download/f1d5b996ecd440365551ab1ab58d9a94.jpeg"));
//                LogUtils.d(uri + "");
//                ArrayList<Uri> uris = new ArrayList<>();
//                uris.add(uri);
//                intent.putExtra(Intent.EXTRA_STREAM, uris);
//                intent.putExtra("Kdescription", "11111");
//                startActivity(intent);
                break;
            case R.id.recommend_btn_completed:
                finish();
                break;
        }
    }

    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
//        Toast.makeText(this, "Uri:" + uriPath, Toast.LENGTH_SHORT).show();
        return uriPath;
    }


    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

}
