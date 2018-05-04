package com.xxc.shoppingmall.model;

import java.util.List;

public class ProductBannerBean {

    /**
     * data : [{"attachment":"3.jpg","createTime":1524999939000,"datasource":"product/shoulaxiang/lunbo","delFlag":0,"id":"1","ordernum":1,"productCode":"88"},{"attachment":"2.jpg","createTime":1524999945000,"datasource":"product/shoulaxiang/lunbo","delFlag":0,"id":"2","ordernum":1,"productCode":"88"}]
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

    public static class DataBean {
        /**
         * attachment : 3.jpg
         * createTime : 1524999939000
         * datasource : product/shoulaxiang/lunbo
         * delFlag : 0
         * id : 1
         * ordernum : 1
         * productCode : 88
         */

        private String attachment;
        private long createTime;
        private String datasource;
        private int delFlag;
        private String id;
        private int ordernum;
        private String productCode;

        public String getAttachment() {
            return attachment;
        }

        public void setAttachment(String attachment) {
            this.attachment = attachment;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDatasource() {
            return datasource;
        }

        public void setDatasource(String datasource) {
            this.datasource = datasource;
        }

        public int getDelFlag() {
            return delFlag;
        }

        public void setDelFlag(int delFlag) {
            this.delFlag = delFlag;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getOrdernum() {
            return ordernum;
        }

        public void setOrdernum(int ordernum) {
            this.ordernum = ordernum;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
    }
}
