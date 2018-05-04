package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by xuxingchen on 2017/12/3.
 */

public class QueryWaterGsc {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : {"tansactionNum2":12,"createtime":"201712","tansactionNum1":-4240,"userId":"admin",
     * "flowType":2}
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
         * tansactionNum2 : 12
         * createtime : 201712
         * tansactionNum1 : -4240
         * userId : admin
         * flowType : 2
         */

        private int zhichu;
        private String userId;
        private int shouru;

        public int getZhichu() {
            return zhichu;
        }

        public void setZhichu(int zhichu) {
            this.zhichu = zhichu;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getShouru() {
            return shouru;
        }

        public void setShouru(int shouru) {
            this.shouru = shouru;
        }
    }
}
