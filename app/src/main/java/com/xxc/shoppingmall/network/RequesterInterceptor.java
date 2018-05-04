package com.xxc.shoppingmall.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by xuxingchen on 2017/11/15.
 */

public class RequesterInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        return chain.proceed(request);
    }
}
