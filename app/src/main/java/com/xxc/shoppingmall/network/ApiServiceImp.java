package com.xxc.shoppingmall.network;

import com.king.base.util.LogUtils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by xuxingchen on 2017/11/15.
 * retrofit功能类
 */
public class ApiServiceImp {

    private static Retrofit mServiceProvider;

    static {
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new
                RequesterInterceptor()).build();
            LogUtils.d("接口地址："+NetConstant.BASE_URL);
        mServiceProvider = new Retrofit.Builder().baseUrl(NetConstant.BASE_URL).addConverterFactory
                (ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory
                .create()).client(client).build();
    }

    /**
     * 创建retrofit代理对象
     * @param tClass 代理类类型
     * @param <T> 范型
     * @return 代理对象
     */
    public static <T> T getRetrofit(Class<T> tClass) {
        return mServiceProvider.create(tClass);
    }

}
