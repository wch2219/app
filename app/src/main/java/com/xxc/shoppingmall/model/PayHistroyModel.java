package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by xuxingchen on 2018/1/23.
 */

public class PayHistroyModel {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"comments":"","money":7200,"createTime":1516675856000,"review":2},{"comments":"",
     * "money":360,"createTime":1516594253000,"review":0},{"comments":"","money":360,
     * "createTime":1516594212000,"review":0},{"comments":"","money":360,
     * "createTime":1516593955000,"review":0},{"comments":"老板是渣渣","money":1800,
     * "createTime":1514400954000,"review":-1}]
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

    public static class DataBean extends HistroyData {
        /**
         * comments :
         * money : 7200
         * createTime : 1516675856000
         * review : 2
         */

        private String comments;
        private double money;
        private long createTime;
        private int review;

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getReview() {
            return review;
        }

        public void setReview(int review) {
            this.review = review;
        }

        @Override
        public int getStatus() {
            return getReview();
        }

    }


}
