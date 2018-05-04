package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by XXC on 2017/11/26.
 * 用户积分、盾、服务券流
 */
public class ExtrResult {
    /**
     * {"msg":{"info":"","code":0,"success":true},"data":[{"createTime":1511015324000,"tranNum":1200,"flowType":2}]}
     */
   private MsgBean msg;
    private String info;
    private int code;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private List<ExtrEntity> data;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public List<ExtrEntity> getData() {
        return data;
    }

    public void setData(List<ExtrEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {

          return "MsgBean{" +
                "info='" + info + '\'' +
                ", code=" + code +
                ", success=" + success +
                '}'+
                  ", data长度=" + data.size()
                  +
                  '}';
    }
}
