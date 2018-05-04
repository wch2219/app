package com.xxc.shoppingmall.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xxc.shoppingmall.R;
import com.youth.banner.loader.ImageLoaderInterface;

/**
 * Created by xuxingchen on 2018/1/12.
 */

public class GlideImageLoader implements ImageLoaderInterface {
    @Override
    public void displayImage(Context context, Object path, View imageView) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.error(R.drawable.home_title_bg).placeholder(R.drawable.home_title_bg);
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.skipMemoryCache(true);
        GlideApp.with(context).load(path).apply(options).into((ImageView) imageView);
    }

    @Override
    public View createImageView(Context context) {
        return new ImageView(context);
    }
}
