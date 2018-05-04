package com.xxc.shoppingmall.network;

import com.xxc.shoppingmall.BuildConfig;

/**
 * Created by xuxingchen on 2017/11/11.
 * 网络相关的静态常量，静态变量等相关参数
 */
public class NetConstant {

    public static final String JS_HANDLER_NAME = "HJHandler";

    /**
     * 官网地址
     */
    public static final String OFFICIAL_URL = "http://www.zxhjcn.com";

    //测试接口地址
    private static final String BASE_URL_TEST = "http://122.114.161.3:9363/";
//    private static final String BASE_URL_TEST = "http://122.114.5.231:8080/";


    //正式接口地址
    //  private static final String BAES_UEL_REALSE = "http://122.114.161.3:8080/";
    private static final String BAES_UEL_REALSE = "http://122.114.5.231:8180/";

    private static final String BASE_WEB_URL_TEST = "http://122.114.161.3:8088";

    private static final String BASE_WEB_URL_RELEASE = "http://122.114.5.231:8188";

    private static final String BASE_WEB_URL = BuildConfig.DEBUG ? BASE_WEB_URL_TEST :
            BASE_WEB_URL_RELEASE;

    //接口地址
    public static final String BASE_URL = BuildConfig.DEBUG ? BASE_URL_TEST : BAES_UEL_REALSE;
//    public static final String BASE_URL = BuildConfig.DEBUG ? BASE_URL_TEST : BASE_URL_TEST;

    /**
     * 商城首页地址
     */
    public static final String SHOPPING_MALL = BASE_WEB_URL;

    /**
     * 我的团队地址
     */
    public static final String TEAM_URL = BASE_WEB_URL + "/?entry=team";

    /**
     * 帮助说明——积分
     */
    public static final String HELP_URL_POINTS = BASE_WEB_URL + "/index.html?entry=help&subset=jf";

    /**
     * 帮助说明——盾
     */
    public static final String HELP_URL_SHIELD = BASE_WEB_URL + "/index" +
            ".html?entry=help&subset=dong";

    /**
     * 帮助说明——服务券
     */
    public static final String HELP_URL_VOUCHER = BASE_WEB_URL + "/index" +
            ".html?entry=help&subset=juan";

    /**
     * 图片路径
     */
    public static final String IMGAE_PATH = BASE_URL + "/api/showImg/";

}
