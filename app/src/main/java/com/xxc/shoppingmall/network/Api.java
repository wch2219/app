package com.xxc.shoppingmall.network;

import com.xxc.shoppingmall.model.AccountBank;
import com.xxc.shoppingmall.model.Address;
import com.xxc.shoppingmall.model.BankCard;
import com.xxc.shoppingmall.model.BannerModel;
import com.xxc.shoppingmall.model.ClickSignBean;
import com.xxc.shoppingmall.model.CompanyBank;
import com.xxc.shoppingmall.model.ConvertPoints;
import com.xxc.shoppingmall.model.ConvertShield;
import com.xxc.shoppingmall.model.DigCashBean;
import com.xxc.shoppingmall.model.ExtrEntity;
import com.xxc.shoppingmall.model.ExtrResult;
import com.xxc.shoppingmall.model.GscPriceBen;
import com.xxc.shoppingmall.model.Invite;
import com.xxc.shoppingmall.model.LoginResult;
import com.xxc.shoppingmall.model.MerchAccBean;
import com.xxc.shoppingmall.model.MyTeam;
import com.xxc.shoppingmall.model.NoticeModel;
import com.xxc.shoppingmall.model.OrderBean;
import com.xxc.shoppingmall.model.PayHistroyModel;
import com.xxc.shoppingmall.model.PayMoney;
import com.xxc.shoppingmall.model.PointsCashHistroyModel;
import com.xxc.shoppingmall.model.Position;
import com.xxc.shoppingmall.model.ProductBannerBean;
import com.xxc.shoppingmall.model.ProductListBean;
import com.xxc.shoppingmall.model.QueryWater;
import com.xxc.shoppingmall.model.QueryWaterGsc;
import com.xxc.shoppingmall.model.Record;
import com.xxc.shoppingmall.model.RegistResult;
import com.xxc.shoppingmall.model.ResultBean;
import com.xxc.shoppingmall.model.ShieldHistroyModel;
import com.xxc.shoppingmall.model.SignBottomBean;
import com.xxc.shoppingmall.model.SignTopBean;
import com.xxc.shoppingmall.model.SubmitOrder;
import com.xxc.shoppingmall.model.TicketsResult;
import com.xxc.shoppingmall.model.TodaySignBean;
import com.xxc.shoppingmall.model.TopUpMoneyListBean;
import com.xxc.shoppingmall.model.UserDetail;
import com.xxc.shoppingmall.model.UserInfo;
import com.xxc.shoppingmall.model.UserTransationResult;
import com.xxc.shoppingmall.model.VerResult;
import com.xxc.shoppingmall.model.VersionUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by xuxingchen on 2017/11/15.
 * 所有api接口
 */
public interface Api {

    /**
     * 用户登录的接口
     *
     * @param username 用户名
     * @param pwd      密码
     * @return 请求对象
     */
    @GET("/api/sys/login")
    Call<LoginResult> userLogin(@Query("username") String username, @Query("password") String pwd);


    /**
     * 获取短信验证码
     *
     * @return 请求对象
     */
    @GET("/api/sms/getRegistVerificationCode")
    Call<VerResult> getVerNum(@Query("mobile") String phone);

    /**
     * @param flowType  1 积分 2 盾 3 服务券
     * @param userId    用户ID
     * @param queryDate yyyyMM
     * @param limit     每页的条数
     * @param page      页数
     * @return 请求对象
     */
    @GET("/api/transactionFlow/list")
    Call<UserTransationResult> getUserTransationResult(@Query("flowType") String flowType, @Query
            ("userId") String userId, @Query("queryDate") String queryDate, @Query("limit")
                                                               String limit, @Query
                                                               ("page") String page);

    /**
     *
     * @param userId    用户ID
     * @param limit     每页的条数
     * @param page      页数
     * @return 请求对象
     */
    @GET("api/gscFlow/gscFlowList")
   Call<ExtrResult> getExtrResult(@Query("flowType") String flowType, @Query
            ("userId") String userId, @Query("createTime") String createtime, @Query("limit")
                                                               String limit, @Query
                                                               ("page") String page);
    /**
     * 注册用户的接口
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/user/save")
    Call<RegistResult> registUser(@FieldMap Map<String, String> params);


    /**
     * 获取个人信息
     *
     * @param userId 用户ID
     * @return 请求对象
     */
    @GET("/api/user/get")
    Call<UserInfo> getUserInfo(@Query("userId") String userId);

    /**
     * 积分兑换
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/integration/cash")
    Call<ConvertPoints> exchangePoints(@FieldMap Map<String, String> params);

    /**
     * 获取用户银行卡列表
     *
     * @param userId 用户ID
     * @return 请求对象
     */
    @GET("/api/userBank/list")
    Call<BankCard> getBankCardList(@Query("userId") String userId);

    /**
     * 兑换服务券
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/ticket/transaction")
    Call<TicketsResult> exchangeTickets(@FieldMap Map<String, String> params);

    /**
     * 兑换盾
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/dong/cash")
    Call<ConvertShield> exchangeShields(@FieldMap Map<String, String> params);

    /**
     * 提交订单
     *
     * @param money        金额
     * @param bankNum      银行卡号
     * @param rechargeCode 汇款码
     * @param usderId      用户ID
     * @param file         截图凭证
     * @return 请求对象
     */
    @Multipart
    @POST("/api/recharge/add")
    Call<SubmitOrder> submitOrder(@Query("rechargeMoney") String money, @Query("companyBank")
            String bankNum, @Query("rechargeCode") String rechargeCode, @Query("userId") String
                                          usderId, @Part("file\";" + "filename=\"orderImage" +
            ".jpeg") RequestBody file);

    /**
     * 提交订单
     *
     * @param params 请求参数
     * @param file   截图凭证
     * @return 请求对象
     */
    @Multipart
    @POST("/api/recharge/add")
    Call<SubmitOrder> submitOrder(@QueryMap Map<String, Object> params, @Part("file\";" +
            "filename=\"orderImage.jpeg") RequestBody file);

    /**
     * 提交个人信息，包括头像
     *
     * @param params 请求参数
     * @param file   头像文件
     * @return 请求对象
     */
    @Multipart
    @POST("/api/user/update")
    Call<SubmitOrder> submitPersonInfo(@QueryMap Map<String, Object> params, @Part("file\";" +
            "filename=\"orderImage.jpeg")
            RequestBody file);

    /**
     * 提交个人信息
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @POST("/api/user/update")
    Call<SubmitOrder> submitPersonInfo(@QueryMap Map<String, Object> params);

    @Multipart
    @POST("/api/user/update")
    Call<SubmitOrder> submitPersonInfo2(@Query("userId") String userId, @Query("nickName") String
            nickname, @Query
                                                ("sex") int sax, @Query("birthday") String
                                                birthday, @Part("file\";" +
            "filename=\"orderImage.jpeg")
                                                RequestBody file);

    /**
     * 根据手机号和银行卡号校验
     *
     * @param phone   手机
     * @param bankNum 银行卡号
     * @return 请求对象
     */
    @GET("/api/user/valiadUserByBank")
    Call<AccountBank> checkAccountBank(@Query("userName") String phone, @Query("bankNum") String
            bankNum);


    @GET("/api/user/getUserExtInfo")
    Call<UserDetail> getUserDetail(@Query("userId") String userId);

    /**
     * 获取用户地址列
     *
     * @param userId 用户ID
     * @return 请求对象
     */
    @GET("/api/userAddress/list")
    Call<Address> getAddressList(@Query("userId") String userId);

    /**
     * 更新或增加收货收到货地址
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/userAddress/saveOrUpdate")
    Call<SubmitOrder> updateAddress(@FieldMap Map<String, Object> params);


    /**
     * 删除收货地址
     *
     * @param userId    用户ID
     * @param addressId 地址ID
     * @return 请求对象
     */
    @GET("/api/userAddress/delete")
    Call<SubmitOrder> deleteAddress(@Query("userId") String userId, @Query("id") int addressId);

    /**
     * 更新银行卡信息
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/userBank/saveOrUpdate")
    Call<SubmitOrder> updateBankCard(@FieldMap Map<String, Object> params);

    /**
     * 删除银行卡
     *
     * @param userId 用户ID
     * @param cardId 银行卡ID
     * @return 请求对象
     */
    @GET("/api/userBank/delete")
    Call<SubmitOrder> deleteBankcard(@Query("userId") String userId, @Query("id") int cardId);

    /**
     * 查询流水
     *
     * @param userId    用户ID
     * @param queryDate 查询日期
     * @param flowType  流水类型
     * @return 请求对象
     */
    @GET("/api/transactionFlow/getTransactionTotal")
    Call<QueryWater> queryWater(@Query("userId") String userId, @Query("queryDate") String
            queryDate, @Query("flowType") String flowType);

 /**
  * 查询流水
  *
  * @param userId    用户ID
  * @param queryDate 查询日期
  * @return 请求对象
  */
 @GET("/api/gscFlow/findGscFlow")
 Call<QueryWaterGsc> queryWaterG(@Query("userId") String userId, @Query("queryDate") String
         queryDate);

    /**
     * 获取订单列表的接口
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("/api/order/list")
    Call<OrderBean> queryOrderList(@QueryMap Map<String, Object> params);

    /**
     * 更新订单状态
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("/api/order/receipt")
    Call<SubmitOrder> updateOrder(@QueryMap Map<String, Object> params);
    /**
     * 获取公司银行账户
     *
     * @return 请求对象
     */
    @GET("/api/comBank/list")
    Call<CompanyBank> queryCompanyBankList();

    /**
     * 获取充值金额列表
     *
     * @param userId 用户ID
     * @return 请求对象
     */
    @GET("/api/user/getMoneyType")
    Call<PayMoney> queryPayMoneyList(@Query("userId") String userId);

    /**
     * 获取版本更新的信息，主要用在推荐中，版本更新使用了开源库
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("/api/appversion/getVersion")
    Call<VersionUpdate> getVersionInfo(@QueryMap Map<String, Object> params);

    /**
     * 功能反馈
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/feedback/save")
    Call<SubmitOrder> submitFeedBack(@FieldMap Map<String, Object> params);

    /**
     * 验证校验码接口
     *
     * @param code 校验码参数
     * @return 请求对象
     */
    @GET("/api/user/getUseExt")
    Call<Invite> checkInvitNum(@Query("invitationCode") String code);

    /**
     * 区块链查询
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("")
    Call<DigCashBean> queryDigCash(@QueryMap Map<String, Object> params);


    /**
     * 获取首页banner图片
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("/api/banner/list")
    Call<BannerModel> getHomeBanner(@QueryMap Map<String, Object> params);

    /**
     * 获取公告内容
     *
     * @return 请求对象
     */
    @GET("/api/notice/list")
    Call<NoticeModel> getNotices(@Query("platform") int platform);

    /**
     * 获取积分兑现记录
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("/api/integration/soldList")
    Call<PointsCashHistroyModel> getPointsCashHistroy(@QueryMap Map<String, Object> params);

    /**
     * 获取卖盾记录
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("/api/dong/soldList")
    Call<ShieldHistroyModel> getShieldHistroy(@QueryMap Map<String, Object> params);

    /**
     * 获取充值记录
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @GET("/api/recharge/list")
    Call<PayHistroyModel> getPayHistroy(@QueryMap Map<String, Object> params);

    /**
     * 提币接口
     *
     * @param params 请求参数
     * @return 请求对象
     */
    @FormUrlEncoded
    @POST("/api/gscFlow/save")
    Call<SubmitOrder> submitDigMoney(@FieldMap Map<String, Object> params);

    /**
     * 获取坐标接口,一次获取所有的坐标
     *
     * @return 请求对象
     */
    @GET("/api/userMap/list")
    Call<Position> getPositions(@Query("userId")String userId);

    /**
     * 点击签到
     * @param params
     * @return
     */
    @GET("/api/usersign/save")
    Call<ClickSignBean> signClick(@QueryMap Map<String, Object> params);

    /**
     * 签到顶部轮播
     * @param params
     * @return
     */
    @GET("/api/usersign/show")
    Call<SignTopBean> SignTop(@QueryMap Map<String, Object> params);

    /**
     * 底部数据展示
     * @param params
     * @return
     */
    @GET("/api/usersign/list")
    Call<SignBottomBean> SignBottom(@QueryMap Map<String, Object> params);

    /**
     * 今日签到状态
     * @param params
     * @return
     */
    @GET("/api/usersign/today")
    Call<TodaySignBean> TodaySign(@QueryMap Map<String, Object> params);

    /**
     * 充值金额列表
     * @param params
     * @return
     */
    @GET("/api/user/getMoneyType")
    Call<TopUpMoneyListBean> topUpmoneyList(@QueryMap Map<String, Object> params);

    /**
     * 商家账户
     * @return
     */
    @GET("/api/comBank/list")
    Call<MerchAccBean> getMerchAcc();

    /**
     * 我的团队
     * @param userId
     * @return
     */
    @GET("/api/myTeam/list")
    Call<MyTeam> GetTeam(@Query("userId") String userId);

    /**
     * 我的团队
     * @param userId
     * @return
     */
    @GET("/api/myTeam/getChildRechargeRecord")
    Call<Record> GetChildRecord(@Query("userId") String userId);

    /**
     * 商品列表
     * @param params
     * @return
     */
    @GET("/api/product/list")
    Call<ProductListBean> GetProductList(@QueryMap Map<String, Object> params);

    /**
     * 提交订单去结算
     * @param map
     */
    @FormUrlEncoded
    @POST("/api/order/saveOrUpdate")
    Call<ResultBean> SubmitOrder(@FieldMap Map<String, Object> map);

    /**
     * 商品详情轮播
     * @param map
     * @return
     */
    @GET("/api/productPlay/list")
    Call<ProductBannerBean> getProductBanner(@QueryMap Map<String, Object> map);

    /**
     * 商品图文介绍
     * @param map
     * @return
     */
    @GET("/api/productInfo/list")
    Call<ProductBannerBean> getProductInfo(@QueryMap Map<String, Object> map);

    /**
     * 积分兑换币
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("/api/gscFlow/inttogsc")
    Call<ResultBean> converItgetoGsc(@FieldMap Map<String, Object> params);

    /**
     * Gsc价格
     * @param map
     * @return
     */
    @GET("/api/systemConfig/v")
    Call<GscPriceBen> getGscPrice(@QueryMap Map<String, Object> map);
}



