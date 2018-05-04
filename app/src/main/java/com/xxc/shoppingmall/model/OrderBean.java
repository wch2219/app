package com.xxc.shoppingmall.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by xuxingchen on 2017/12/3.
 */

public class OrderBean {

    public static final int ORDER_ONE = 1;
    public static final int ORDER_TWO = 2;

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"userAddress":"qegfgsfd","createTime":1511845990000,"integrationNum":120,
     * "status":1,"products":[{"imgUrl":"11","productCode":"12","num":"6","name":"浴疗包",
     * "description":"泓济浴疗包","status":true},{"imgUrl":"11","productCode":"13","num":"1",
     * "name":"果维密码","description":"泓济果维密码","status":true}]},{"userAddress":"qegfgsfd",
     * "createTime":1511846856000,"integrationNum":120,"status":1,"products":[]},
     * {"userAddress":null,"createTime":1511936151000,"integrationNum":1,"status":1,
     * "products":[]},{"userAddress":null,"createTime":1511936353000,"integrationNum":1,
     * "status":1,"products":[{"imgUrl":"11","productCode":"12","num":"6","name":"浴疗包",
     * "description":"泓济浴疗包","status":true},{"imgUrl":"11","productCode":"13","num":"1",
     * "name":"果维密码","description":"泓济果维密码","status":true}]},{"userAddress":null,
     * "createTime":1511936869000,"integrationNum":1,"status":1,"products":[{"imgUrl":"11",
     * "productCode":"12","num":"6","name":"浴疗包","description":"泓济浴疗包","status":true}]},
     * {"userAddress":null,"createTime":1511937064000,"integrationNum":1,"status":1,
     * "products":[{"imgUrl":"11","productCode":"12","num":"6","name":"浴疗包",
     * "description":"泓济浴疗包","status":true},{"imgUrl":"11","productCode":"13","num":"1",
     * "name":"果维密码","description":"泓济果维密码","status":true}]},{"userAddress":null,
     * "createTime":1511938558000,"integrationNum":200,"status":1,"products":[{"imgUrl":"11",
     * "productCode":"13","num":"1","name":"果维密码","description":"泓济果维密码","status":true}]},
     * {"userAddress":null,"createTime":1511938581000,"integrationNum":200,"status":1,
     * "products":[{"imgUrl":"11","productCode":"13","num":"1","name":"果维密码",
     * "description":"泓济果维密码","status":true}]},{"userAddress":null,"createTime":1511938631000,
     * "integrationNum":200,"status":1,"products":[{"imgUrl":"11","productCode":"13","num":"1",
     * "name":"果维密码","description":"泓济果维密码","status":true}]},{"userAddress":null,
     * "createTime":1511938650000,"integrationNum":200,"status":1,"products":[{"imgUrl":"11",
     * "productCode":"13","num":"1","name":"果维密码","description":"泓济果维密码","status":true}]}]
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

    public static class DataBean implements MultiItemEntity {
        /**
         * userAddress : qegfgsfd
         * createTime : 1511845990000
         * integrationNum : 120
         * status : 1
         * products : [{"imgUrl":"11","productCode":"12","num":"6","name":"浴疗包",
         * "description":"泓济浴疗包","status":true},{"imgUrl":"11","productCode":"13","num":"1",
         * "name":"果维密码","description":"泓济果维密码","status":true}]
         */

        private int id;
        private String userAddress;
        private long createTime;
        private int integrationNum;
        private int status;
        private List<ProductsBean> products;

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

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getIntegrationNum() {
            return integrationNum;
        }

        public void setIntegrationNum(int integrationNum) {
            this.integrationNum = integrationNum;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<ProductsBean> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsBean> products) {
            this.products = products;
        }

        @Override
        public int getItemType() {
            if (products.size() == 1) {
                return ORDER_ONE;
            } else {
                return ORDER_TWO;
            }
        }

        public static class ProductsBean {
            /**
             * imgUrl : 11
             * productCode : 12
             * num : 6
             * name : 浴疗包
             * description : 泓济浴疗包
             * status : true
             */

            private String imgUrl;
            private String productCode;
            private String num;
            private String name;
            private String description;
            private boolean status;

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getProductCode() {
                return productCode;
            }

            public void setProductCode(String productCode) {
                this.productCode = productCode;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }
        }
    }
}
