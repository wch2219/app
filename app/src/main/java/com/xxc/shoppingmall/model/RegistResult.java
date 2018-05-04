package com.xxc.shoppingmall.model;

/**
 * Created by xuxingchen on 2017/11/26.
 */

public class RegistResult {


    /**
     * msg : {"info":"验证码错误！","code":505,"success":false}
     * data : {}
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
         * info : 验证码错误！
         * code : 505
         * success : false
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

        @Override
        public String toString() {
            return "MsgBean{" +
                    "info='" + info + '\'' +
                    ", code=" + code +
                    ", success=" + success +
                    '}';
        }
    }

    public static class DataBean {
    }

    @Override
    public String toString() {
        return "RegistResult{" +
                "msg=" + msg +
                ", data=" + data +
                '}';
    }
}
