package com.xxc.shoppingmall.model;

import java.io.Serializable;
import java.util.List;

public class ProductListBean {


    /**
     * data : [{"createTime":1519637510000,"description":"红糖姜茶","id":1,"imgUrl":"product/hongtangjiangcha360.jpg","name":"红糖姜茶","price":360,"productCode":"32","status":1,"updateTime":1519638512000},{"createTime":1520324433000,"description":"酱香私藏（6瓶）","id":2,"imgUrl":"product/jiangxianmaotai2468.jpg","name":"酱香私藏（6瓶）","price":2468,"productCode":"36","status":1,"updateTime":1520349790000},{"description":"东菊香砂片","id":3,"imgUrl":"product/dongjuxiangshapian.jpg","name":"东菊香砂片","price":149,"productCode":"37","status":1},{"createTime":1522909471000,"description":"口腔泡腾片","id":4,"imgUrl":"product/kongqiangpaoteng.jpg","name":"口腔泡腾片","price":160,"productCode":"38","status":1},{"createTime":1522909546000,"description":"孝道阿胶固元糕","id":5,"imgUrl":"product/xiaodaoejiaoguyuan.jpg","name":"孝道阿胶固元糕","price":160,"productCode":"39","status":1},{"createTime":1522909604000,"description":"少林溻渍","id":6,"imgUrl":"product/shaolinze.jpg","name":"少林溻渍","price":59,"productCode":"40","status":1},{"createTime":1522909844000,"description":"欧白牙洁","id":8,"imgUrl":"product/aobaiyajie.jpg","name":"欧白牙洁","price":88,"productCode":"42","status":1},{"createTime":1522909904000,"description":"欧白面膜","id":9,"imgUrl":"product/aobaimianmo.jpg","name":"欧白面膜","price":148,"productCode":"43","status":1},{"createTime":1522910034000,"description":"松针油","id":10,"imgUrl":"product/songzhiyou.jpg","name":"松针油","price":708,"productCode":"44","status":1},{"createTime":1518108229000,"description":"青梅丸","id":17,"imgUrl":"product/qingmei.jpg","name":"青梅丸","price":899,"productCode":"17","status":1}]
     * msg : {"code":0,"info":"","success":true}
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
         * code : 0
         * info :
         * success : true
         */

        private int code;
        private String info;
        private boolean success;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

    public static class DataBean implements Serializable{
        /**
         * createTime : 1519637510000
         * description : 红糖姜茶
         * id : 1
         * imgUrl : product/hongtangjiangcha360.jpg
         * name : 红糖姜茶
         * price : 360.0
         * productCode : 32
         * status : 1
         * updateTime : 1519638512000
         */

        private long createTime;
        private String description;
        private int id;
        private String imgUrl;
        private String name;
        private double price;
        private String productCode;
        private int status;
        private long updateTime;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
