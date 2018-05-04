package com.xxc.shoppingmall.model;

import java.io.Serializable;

/**
 * Created by xuxingchen on 2017/11/26.
 */

public class UserInfo implements Serializable {

    public static final int MAN = 1;
    public static final int WOMAN = 2;
    public static final int SECRET = 0;

    //    0会员  1 初级微股东 2中级微股东  3高级微股东
    public static final int V_TYPE_0 = 0;
    public static final int V_TYPE_1 = 1;
    public static final int V_TYPE_2 = 2;
    public static final int V_TYPE_3 = 3;

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : {"birthday":null,"ticket":99,"avatarUrl":null,"nickName":null,"idCard":null,
     * "sex":false,
     * "mobile":"15811174911","dong":374,"userName":"15811174911","userId":"admin",
     * "password":"e10adc3949ba59abbe56e057f20f883e","transactionPassword":null,
     * "integration":2000,"email":null}
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

    public static class MsgBean implements Serializable {
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

        @Override
        public String toString() {
            return "MsgBean{" +
                    "info='" + info + '\'' +
                    ", code=" + code +
                    ", success=" + success +
                    '}';
        }
    }

    public static class DataBean implements Serializable {
        /**
         * birthday : null
         * ticket : 99
         * avatarUrl : null
         * nickName : null
         * idCard : null
         * sex : false
         * mobile : 15811174911
         * dong : 374
         * userName : 15811174911
         * userId : admin
         * password : e10adc3949ba59abbe56e057f20f883e
         * transactionPassword : null
         * integration : 2000
         * email : null
         */

        private String birthday;
        private float ticket;
        private String avatarUrl;
        private String nickName;
        private String idCard;
        private int sex;
        private String mobile;
        private float dong;
        private String userName;
        private String userId;
        private String password;
        private String transactionPassword;
        private float integration;
        private String email;
        private float holdDongNum;
        private String dongPrice;
        private String invitecode;
        private float shopcoin;
        private float hold_gsc_num;
        private float gsc_num;
        private int roleType;
        private String depIntNum;
        private float holdIntNum;

        public float getHoldIntNum() {
            return holdIntNum;
        }

        public void setHoldIntNum(float holdIntNum) {
            this.holdIntNum = holdIntNum;
        }

        public String getDepIntNum() {
            return depIntNum;
        }

        public void setDepIntNum(String depIntNum) {
            this.depIntNum = depIntNum;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }


        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTransactionPassword() {
            return transactionPassword;
        }

        public void setTransactionPassword(String transactionPassword) {
            this.transactionPassword = transactionPassword;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }


        public String getDongPrice() {
            return dongPrice;
        }

        public void setDongPrice(String dongPrice) {
            this.dongPrice = dongPrice;
        }

        public String getInvitecode() {
            return invitecode;
        }

        public void setInvitecode(String invitecode) {
            this.invitecode = invitecode;
        }

        public float getShopcoin() {
            return shopcoin;
        }

        public void setShopcoin(float shopcoin) {
            this.shopcoin = shopcoin;
        }

        public float getHold_gsc_num() {
            return hold_gsc_num;
        }

        public void setHold_gsc_num(float hold_gsc_num) {
            this.hold_gsc_num = hold_gsc_num;
        }

        public float getGsc_num() {
            return gsc_num;
        }

        public void setGsc_num(float gsc_num) {
            this.gsc_num = gsc_num;
        }

        public float getTicket() {
            return ticket;
        }

        public void setTicket(float ticket) {
            this.ticket = ticket;
        }

        public float getDong() {
            return dong;
        }

        public void setDong(float dong) {
            this.dong = dong;
        }

        public float getIntegration() {
            return integration;
        }

        public void setIntegration(float integration) {
            this.integration = integration;
        }

        public float getHoldDongNum() {
            return holdDongNum;
        }

        public void setHoldDongNum(float holdDongNum) {
            this.holdDongNum = holdDongNum;
        }

        public int getRoleType() {
            return roleType;
        }

        public void setRoleType(int roleType) {
            this.roleType = roleType;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "birthday='" + birthday + '\'' +
                    ", ticket=" + ticket +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", idCard='" + idCard + '\'' +
                    ", sex=" + sex +
                    ", mobile='" + mobile + '\'' +
                    ", dong=" + dong +
                    ", userName='" + userName + '\'' +
                    ", userId='" + userId + '\'' +
                    ", password='" + password + '\'' +
                    ", transactionPassword='" + transactionPassword + '\'' +
                    ", integration=" + integration +
                    ", email='" + email + '\'' +
                    ", holdDongNum=" + holdDongNum +
                    ", dongPrice='" + dongPrice + '\'' +
                    ", invitecode='" + invitecode + '\'' +
                    ", shopcoin=" + shopcoin +
                    ", hold_gsc_num=" + hold_gsc_num +
                    ", gsc_num=" + gsc_num +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "msg=" + msg +
                ", data=" + data +
                '}';
    }
}
