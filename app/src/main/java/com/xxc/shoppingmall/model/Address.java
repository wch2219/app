package com.xxc.shoppingmall.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuxingchen on 2017/12/2.
 */

public class Address {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"id":86,"userAddress":"恶魔呢呃呃呃额额的","userId":"admin","isDefault":1,"createTime":1512047131000,
     * "updateTime":null,"mobile":"13646646446","name":"恶魔"}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 86
         * userAddress : 恶魔呢呃呃呃额额的
         * userId : admin
         * isDefault : 1
         * createTime : 1512047131000
         * updateTime : null
         * mobile : 13646646446
         * name : 恶魔
         */

        private int id;
        private String userAddress;
        private String userId;
        private int isDefault;
        private long createTime;
        private Object updateTime;
        private String mobile;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
