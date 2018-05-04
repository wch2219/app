package com.xxc.shoppingmall.model;

/**
 * Created by XXC on 2017/11/26.
 */
public class MsgBean {
    /**
     * info : 成功!
     * code : 200
     * success : true
     */

    private String info;
    private int code;
    private boolean success;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

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
}
