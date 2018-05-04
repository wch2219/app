package com.xxc.shoppingmall.model;

import com.xxc.shoppingmall.utils.StringReplaceUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuxingchen on 2017/11/27.
 */
public class BankCard {

    public static final int IS_DEFAULT = 1;
    public static final int NOT_DEFAULT = 0;

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"id":3,"userId":"admin","bankName":"hhh","bankNum":"1256352","isDefault":0,
     * "createTime":1511601886000,"updateTime":null},{"id":4,"userId":"admin","bankName":"hhh",
     * "bankNum":"1256352",
     * "isDefault":0,"createTime":1511601963000,"updateTime":null}]
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
         * id : 3
         * userId : admin
         * bankName : hhh
         * bankNum : 1256352
         * isDefault : 0
         * createTime : 1511601886000
         * updateTime : null
         */

        private int id;
        private String userId;
        private String bankName;
        private String bankNum;
        private int isDefault;
        private long createTime;
        private String updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getBankNum() {
            return bankNum;
        }

        public void setBankNum(String bankNum) {
            this.bankNum = bankNum;
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

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return bankName + "(" + StringReplaceUtil.bankCardReplaceWithStar(bankNum) + ")";
        }
    }
}
