package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by XXC on 2017/11/26.
 * 用户积分、盾、服务券流
 */
public class UserTransationResult {
    /**
     * {"msg":{"info":"","code":0,"success":true},"data":[{"createTime":1511015324000,"tranNum":1200,"flowType":2}]}
     */
    private MsgBean msg;
    private List<UserTransationEntity> data;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public List<UserTransationEntity> getData() {
        return data;
    }

    public void setData(List<UserTransationEntity> data) {
        this.data = data;
    }
}
