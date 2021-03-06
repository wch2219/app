package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class SignBottomBean {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"state":1,"time":"2018-03-20","jifei":"0.1"}]
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
         * state : 1
         * time : 2018-03-20
         * jifei : 0.1
         */

        private int state;
        private String time;
        private String jifei;

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getJifei() {
            return jifei;
        }

        public void setJifei(String jifei) {
            this.jifei = jifei;
        }
    }
}
