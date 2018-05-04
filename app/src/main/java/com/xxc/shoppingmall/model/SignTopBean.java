package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class SignTopBean {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"integrationNum":"1","nickname":"刘笑俨"},{"integrationNum":"0.1","nickname":"乔俊颖"}]
     */

    private MsgBean msg;
    private List<DataBean> data;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
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

    public static class DataBean {
        /**
         * integrationNum : 1
         * nickname : 刘笑俨
         */

        private String integrationNum;
        private String nickname;

        public String getIntegrationNum() {
            return integrationNum;
        }

        public void setIntegrationNum(String integrationNum) {
            this.integrationNum = integrationNum;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
