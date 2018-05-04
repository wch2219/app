package com.xxc.shoppingmall.model;

/**
 * Created by xuxingchen on 2017/12/3.
 */

public class QueryWater {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : {"tansactionNum2":12,"createtime":"201712","tansactionNum1":-4240,"userId":"admin",
     * "flowType":2}
     */

    private MsgBean msg;
    private DataBean data;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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

        private int tansactionNum2;
        private String createtime;
        private int tansactionNum1;
        private String userId;
        private int flowType;

        public int getTansactionNum2() {
            return tansactionNum2;
        }

        public void setTansactionNum2(int tansactionNum2) {
            this.tansactionNum2 = tansactionNum2;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getTansactionNum1() {
            return tansactionNum1;
        }

        public void setTansactionNum1(int tansactionNum1) {
            this.tansactionNum1 = tansactionNum1;
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
}
