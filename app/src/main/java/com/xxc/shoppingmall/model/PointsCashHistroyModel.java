package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by xuxingchen on 2018/1/23.
 */
public class PointsCashHistroyModel {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"comments":"","createTime":1514289162000,"soldStatus":1,"tranNum":-700},
     * {"comments":"","createTime":1514288795000,"soldStatus":0,"tranNum":-700},
     * {"comments":"老板到事件发生丁俊晖发动了是","createTime":1514173960000,"soldStatus":-1,"tranNum":-400}]
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
         * createTime : 1514289162000
         * soldStatus : 1
         * tranNum : -700
         */

        private String comments;
        private long createTime;
        private int soldStatus;
        private double tranNum;

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getSoldStatus() {
            return soldStatus;
        }

        public void setSoldStatus(int soldStatus) {
            this.soldStatus = soldStatus;
        }

        public double getTranNum() {
            return tranNum;
        }

        public void setTranNum(double tranNum) {
            this.tranNum = tranNum;
        }

        @Override
        public int getStatus() {
            return getSoldStatus();
        }

        @Override
        public double getMoney() {
            return getTranNum();
        }
    }
}
