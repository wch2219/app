package com.xxc.shoppingmall.model;

/**
 * Created by XXC on 2017/11/26.
 * 用户积分、盾、服务券流
 */
public class UserTransationEntity {
    /**
     * createTime : 1511015324000
     * tranNum : 1200
     * flowType : 2
     */

    private String createTime;
    private String tranNum;
    private String flowType;
    private int incomeType;
    private int soldStatus;
    private String comments;


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTranNum() {
        return tranNum;
    }

    public void setTranNum(String tranNum) {
        this.tranNum = tranNum;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }


    public int getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(int incomeType) {
        this.incomeType = incomeType;
    }

    public int getSoldStatus() {
        return soldStatus;
    }

    public void setSoldStatus(int soldStatus) {
        this.soldStatus = soldStatus;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
