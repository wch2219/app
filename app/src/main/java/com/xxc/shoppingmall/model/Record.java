package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class Record {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : ["贾小强2018年03月08日充值720.00元","贾小强2018年03月08日充值360.00元","陈勇奇2017年12月22日充值720.00元"]
     */

    private MsgBean msg;
    private List<String> data;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public static class MsgBean {
        /**
         * info :
         * code : 0
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
}
