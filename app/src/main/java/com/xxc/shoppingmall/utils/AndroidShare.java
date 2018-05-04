package com.xxc.shoppingmall.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.king.base.util.LogUtils;
import com.xxc.shoppingmall.R;

import java.io.File;
import java.util.List;

/**
 * Created by xuxingchen on 2018/1/24.
 */

public class AndroidShare {

    /**
     * 上下文
     */
    private Context context;

    /**
     * 文本类型
     */
    public static int TEXT = 0;

    /**
     * 图片类型
     */
    public static int DRAWABLE = 1;

    public AndroidShare(Context context) {
        this.context = context;
    }

    /**
     * 分享到QQ好友
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareQQFriend(String msgTitle, String msgText, int type, Bitmap drawable) {
        shareMsg("com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.JumpActivity", "QQ", msgTitle,
                msgText, type, drawable);
    }


    /**
     * 分享到微信好友
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param drawable (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareWeChatFriend(String msgTitle, String msgText, int type, Bitmap drawable) {
        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", "微信",
                msgTitle, msgText, type, drawable);
    }

    /**
     * 分享到微信朋友圈(分享朋友圈一定需要图片)
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param drawable (分享图片)
     */
    public void shareWeChatFriendCircle(String msgTitle, String msgText, Bitmap drawable) {
        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI",
                "微信", msgTitle, msgText, AndroidShare.DRAWABLE, drawable);
    }

    public void shareWeiBo(String msgTitle, String msgText, int type, Bitmap drawable) {
        String actName = "com.sina.weibo.composerinde.ComposerDispatchActivity";
        shareMsg("com.sina.weibo", actName, "微博", msgTitle, msgText, type, drawable);
    }

    /**
     * 点击分享的代码
     *
     * @param packageName  (包名,跳转的应用的包名)
     * @param activityName (类名,跳转的页面名称)
     * @param appname      (应用名,跳转到的应用名称)
     * @param msgTitle     (标题)
     * @param msgText      (内容)
     * @param type         (发送类型：text or pic 微信朋友圈只支持pic)
     */
    @SuppressLint("NewApi")
    private void shareMsg(String packageName, String activityName,
                          String appname, String msgTitle, String msgText, int type,
                          final Bitmap drawable) {
        if (!packageName.isEmpty() && !isAvilible(context, packageName)) {// 判断APP是否存在
            Toast.makeText(context, "请先安装" + appname, Toast.LENGTH_SHORT).show();
            return;
        }

        final Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
//        if (type == AndroidShare.TEXT) {
//        }
//        else if (type == AndroidShare.DRAWABLE) {
//            intent.setType("image/*");
////          BitmapDrawable bd = (BitmapDrawable) drawable;
////          Bitmap bt = bd.getBitmap();
//            Glide.with(context).asFile().load(drawable).into(new SimpleTarget<File>() {
//                @Override
//                public void onResourceReady(File resource, Transition<? super File> transition) {
//                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
//                            context.getContentResolver(), drawable, null, null));
////                Uri uri = resourceIdToUri(context,R.drawable.friends);
////            LogUtils.d(uri + "---------------------");
//                    intent.putExtra(Intent.EXTRA_STREAM, uri);
//                }
//            });
//        }
//        IWXAPI api=null;
//        WXMediaMessage mediaMessage=null;
//        WXImageObject imageObject = null;
//        WXMusicObject musicObject = null;
//        WXVideoObject videoObject = null;


//        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.putExtra("Kdescription", msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!packageName.isEmpty()) {
            intent.setComponent(new ComponentName(packageName, activityName));
            context.startActivity(intent);
        } else {
            context.startActivity(Intent.createChooser(intent, msgTitle));
        }
    }


    /**
     * 分享到QQ好友-------兼容传bitmap
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param path     (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareQQFriend(String msgTitle, String msgText, int type, String path) {

        shareMsg("com.tencent.mobileqq",
                "com.tencent.mobileqq.activity.JumpActivity", "QQ", msgTitle,
                msgText, type, path);
    }


    /**
     * 分享到微信好友
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param type     (分享类型)
     * @param path     (分享图片，若分享类型为AndroidShare.TEXT，则可以为null)
     */
    public void shareWeChatFriend(String msgTitle, String msgText, int type, String path) {

        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI", "微信",
                msgTitle, msgText, type, path);
    }

    /**
     * 分享到微信朋友圈(分享朋友圈一定需要图片)----兼容传bitmap
     *
     * @param msgTitle (分享标题)
     * @param msgText  (分享内容)
     * @param path     (分享图片)
     */
    public void shareWeChatFriendCircle(String msgTitle, String msgText, String path) {

        shareMsg("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI",
                "微信", msgTitle, msgText, AndroidShare.DRAWABLE, path);
    }

    public void shareWeiBo(String msgTitle, String msgText, int type, String path) {
//        com.sina.weibo.composerinde.ComposerDispatchActivity
//        com.sina.weibo.composerinde.OriginalComposerActivity
        String actName = "com.sina.weibo.composerinde.ComposerDispatchActivity";
        shareMsg("com.sina.weibo", actName, "微博", msgTitle, msgText, AndroidShare.DRAWABLE, path);
    }


    /**
     * 点击分享的代码
     *
     * @param packageName  (包名,跳转的应用的包名)
     * @param activityName (类名,跳转的页面名称)
     * @param appname      (应用名,跳转到的应用名称)
     * @param msgTitle     (标题)
     * @param msgText      (内容)
     * @param type         (发送类型：text or pic 微信朋友圈只支持pic)
     */
    @SuppressLint("NewApi")
    private void shareMsg(String packageName, String activityName,
                          String appname, String msgTitle, String msgText, int type,
                          String path) {
        if (!packageName.isEmpty() && !isAvilible(context, packageName)) {// 判断APP是否存在
            Toast.makeText(context, "请先安装" + appname, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent("android.intent.action.SEND");
        if (type == AndroidShare.TEXT) {
            intent.setType("text/plain");
        } else if (type == AndroidShare.DRAWABLE) {
            intent.setType("image/*");
//          BitmapDrawable bd = (BitmapDrawable) drawable;
//          Bitmap bt = bd.getBitmap();

            final Uri uri = Uri.fromFile(new File(path));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }

        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.putExtra("Kdescription", msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!packageName.isEmpty()) {
            intent.setComponent(new ComponentName(packageName, activityName));
            context.startActivity(intent);
        } else {
            context.startActivity(Intent.createChooser(intent, msgTitle));
        }
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (((PackageInfo) pinfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 指定分享到qq
     *
     * @param context
     * @param bitmap
     */
    public void sharedQQ(Activity context, Bitmap bitmap) {
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
                context.getContentResolver(), BitmapFactory.decodeResource(context.getResources()
                        , R.drawable.img_logo), null, null));
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setPackage("com.tencent.mobileqq");
        imageIntent.setType("image/*");
        imageIntent.putExtra(Intent.EXTRA_STREAM, uri);
        imageIntent.putExtra(Intent.EXTRA_TEXT, "您的好友邀请您进入天好圈");
        imageIntent.putExtra(Intent.EXTRA_TITLE, "天好圈");
        context.startActivity(imageIntent);
    }


    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }
}
