package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by xuxingchen on 2018/1/12.
 */

public class BannerModel {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"id":1,"imgUrl":"banner/img_banner.png","orderId":1,"status":1},{"id":2,
     * "imgUrl":"banner/img_banner.png","orderId":2,"status":1},{"id":3,
     * "imgUrl":"banner/img_banner.png","orderId":3,"status":1},{"id":4,
     * "imgUrl":"banner/img_banner.png","orderId":4,"status":1}]
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
         * imgUrl : banner/img_banner.png
         * orderId : 1
         * status : 1
         */

        private int id;
        private String imgUrl;
        private int orderId;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
