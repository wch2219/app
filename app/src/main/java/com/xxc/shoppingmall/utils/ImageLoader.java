package com.xxc.shoppingmall.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by admin on 2017/8/25.
 */

public class ImageLoader {

    /**
     * 额外参数类
     */
    public static class Extras {

    }

    /**
     * 加载图片
     *
     * @param container 容器对象
     * @param target    目标imageview
     * @param uri       图片路径
     * @param extras    额外参数
     */
    public static void loadImage(View container, ImageView target, String uri, Extras extras) {
//        Glide.with(container).load(uri).into(target);
        GlideApp.with(container).load(uri).into(target);
    }

    /**
     * 加载图片
     *
     * @param activity 活动对象
     * @param target   目标imageview
     * @param uri      图片路径
     * @param extras   额外参数
     */
    public static void loadImage(Activity activity, ImageView target, String uri, Extras extras) {
        RequestOptions options = new RequestOptions();
        if (null != extras && extras instanceof GlideExtra) {
            GlideExtra glideExtra = (GlideExtra) extras;
            if (glideExtra.scaleType == GlideExtra.CENTER_CROP) {
                options.centerCrop();
            }
            if (glideExtra.showCircle == GlideExtra.SHOW_CIRCLE) {
                options = RequestOptions.bitmapTransform(new CircleCrop());
                options.error(((GlideExtra) extras).placeHolderRes).placeholder(((GlideExtra) extras).placeHolderRes);
            } else {
                options.error(((GlideExtra) extras).placeHolderRes).placeholder(((GlideExtra) extras).placeHolderRes);
            }
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        GlideApp.with(activity).load(uri).apply(options).into(target);
    }

    /**
     * 加载图片
     *
     * @param fragment 碎片对象
     * @param target   目标图片
     * @param uri      图片路径
     * @param extras   额外参数
     */
    public static void loadImage(Fragment fragment, ImageView target, String uri, Extras extras) {
        RequestOptions options = new RequestOptions();
        if (null != extras && extras instanceof GlideExtra) {
            GlideExtra glideExtra = (GlideExtra) extras;
            if (glideExtra.scaleType == GlideExtra.CENTER_CROP) {
                options.centerCrop();
            }
            if (glideExtra.showCircle == GlideExtra.SHOW_CIRCLE) {
                options = RequestOptions.bitmapTransform(new CircleCrop());
                options.error(((GlideExtra) extras).placeHolderRes).placeholder(((GlideExtra) extras).placeHolderRes);
            } else {
                options.error(((GlideExtra) extras).placeHolderRes).placeholder(((GlideExtra) extras).placeHolderRes);
            }
            options.diskCacheStrategy(DiskCacheStrategy.ALL);
        }
        GlideApp.with(fragment).load(uri).apply(options).into(target);
    }

    /**
     * 圆形图片
     *
     * @param activity 活动对象
     * @param target   目标imageview
     * @param uri      图片路径
     * @param extras   额外参数
     */
    public static void loadImageAsCircle(Activity activity, ImageView target, String uri, Extras extras) {
        RequestOptions options =RequestOptions.bitmapTransform(new CircleCrop());
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideApp.with(activity).load(uri).apply(options).into(target);
    }

    /**
     * 圆角矩形
     *
     * @param activity 活动对象
     * @param target   目标imageview
     * @param uri      图片路径
     * @param extras   额外参数
     */
    public static void loadImageAsCircleDegree(Activity activity, ImageView target, String uri, Extras extras) {
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(ConvertUtils.dp2px(3)));
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        GlideApp.with(activity).load(uri).apply(options).into(target);
    }

}
