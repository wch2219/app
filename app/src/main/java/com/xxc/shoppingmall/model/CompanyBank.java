package com.xxc.shoppingmall.model;

import com.xxc.shoppingmall.utils.StringReplaceUtil;

import java.util.List;

/**
 * Created by xuxingchen on 2017/12/4.
 */

public class CompanyBank {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"id":1,"bankName":"11","bankNum":"11"}]
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
         * id : 1
         * bankName : 11
         * bankNum : 11
         */

        private int id;
        private String bankName;
        private String bankNum;
        private String owner="唐敬原";

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        @Override
        public String toString() {
            String myString = bankName + "(" + StringReplaceUtil.bankCardReplaceWithStar(bankNum) +
                    ")      " + owner;
            return myString;
        }
    }
}
