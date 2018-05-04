package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class TodaySignBean {


    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"zongjifei":"0.6","jifei":"0.1","day":1,"status":1}]
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
         * zongjifei : 0.6
         * jifei : 0.1
         * day : 1
         * status : 1
         */

        private String zongjifei;
        private String jifei;
        private int day;
        private int status;

        public String getZongjifei() {
            return zongjifei;
        }

        public void setZongjifei(String zongjifei) {
            this.zongjifei = zongjifei;
        }

        public String getJifei() {
            return jifei;
        }

        public void setJifei(String jifei) {
            this.jifei = jifei;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
