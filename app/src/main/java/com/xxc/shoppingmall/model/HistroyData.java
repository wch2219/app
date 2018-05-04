package com.xxc.shoppingmall.model;

/**
 * Created by xuxingchen on 2018/1/23.
 */

public class HistroyData {

    /**
     * 被驳回
     */
    public static final int REJECT_CODE = -1;

    /**
     * 审核通过
     */
    public static final int PASS_CODE = 1;

    /**
     * 已奖励
     */
    public static final int AWARD_CODE=2;

    /**
     * 未审核
     */
    public static final int UNCHECKED_CODE = 0;


    public String getComments() {
        return null;
    }


    public double getMoney() {
        return 0;
    }


    public long getCreateTime() {
        return 0;
    }


    public int getStatus() {
        return 0;
    }

}
