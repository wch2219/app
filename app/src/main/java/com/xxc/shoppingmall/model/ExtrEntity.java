package com.xxc.shoppingmall.model;

/**
 * Created by guo on 2017/11/26.
 * gsc
 */
public class ExtrEntity {

    /**
     * amount : 22
     * createTime : 1519488000000
     * holdGscNum : null
     * monyAddren : 222222222222
     * id : 14
     * userId : 16d11ec8a2de4ae48dd3622e289a53cf
     * flowType : 1
     */

    private int amount;
    private long createTime;
    private Object holdGscNum;
    private String monyAddren;
    private int id;
    private String userId;
    private int flowType;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public Object getHoldGscNum() {
        return holdGscNum;
    }

    public void setHoldGscNum(Object holdGscNum) {
        this.holdGscNum = holdGscNum;
    }

    public String getMonyAddren() {
        return monyAddren;
    }

    public void setMonyAddren(String monyAddren) {
        this.monyAddren = monyAddren;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getFlowType() {
        return flowType;
    }

    public void setFlowType(int flowType) {
        this.flowType = flowType;
    }


}
