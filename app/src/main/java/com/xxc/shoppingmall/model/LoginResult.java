package com.xxc.shoppingmall.model;

import com.xxc.shoppingmall.ShoppingMallApp;

/**
 * Created by xuxingchen on 2017/11/15.
 */

public class LoginResult {


    /**
     * msg : {"info":"成功!","code":200,"success":true}
     * data : {"birthday":"","avatarUrl":"","nickName":"admin","sex":false,"mobile":"","id":1,
     * "userName":"admin",
     * "userId":"admin"}
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
         * info : 成功!
         * code : 200
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

    public static class DataBean {
        /**
         * birthday :
         * avatarUrl :
         * nickName : admin
         * sex : false
         * mobile :
         * id : 1
         * userName : admin
         * userId : admin
         */

        private String birthday;
        private String avatarUrl;
        private String nickName;
        private int sex;
        private String mobile;
        private int id;
        private String userName;
        private String userId;

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        @Override
        public String toString() {
            return "DataBean{" +
                    "birthday='" + birthday + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", sex=" + sex +
                    ", mobile='" + mobile + '\'' +
                    ", id=" + id +
                    ", userName='" + userName + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "msg=" + msg +
                ", data=" + data +
                '}';
    }

    public LoginResult copy() {
        LoginResult result = new LoginResult();
        MsgBean msgBean = new MsgBean();
        msgBean.setCode(msg.getCode());
        msgBean.setInfo(msg.getInfo());
        msgBean.setSuccess(msg.isSuccess());
        result.setMsg(msgBean);
        DataBean dataBean = new DataBean();
        dataBean.setAvatarUrl(data.getAvatarUrl());
        dataBean.setBirthday(data.getBirthday());
        dataBean.setId(data.getId());
        dataBean.setMobile(data.getMobile());
        dataBean.setNickName(data.getNickName());
        dataBean.setSex(data.getSex());
        dataBean.setUserName(data.getUserName());
        dataBean.setUserId(data.getUserId());
        result.setData(dataBean);
        return result;
    }

    public static LoginResult copy(UserInfo info) {
        LoginResult instanceBean = ShoppingMallApp.getInstance().getUser();
        LoginResult result = new LoginResult();
        MsgBean msgBean = new MsgBean();
        UserInfo.MsgBean msg = info.getMsg();
        msgBean.setCode(msg.getCode());
        msgBean.setInfo(msg.getInfo());
        msgBean.setSuccess(msg.isSuccess());
        result.setMsg(msgBean);
        DataBean dataBean = new DataBean();
        UserInfo.DataBean data = info.getData();
        dataBean.setAvatarUrl(data.getAvatarUrl());
        dataBean.setBirthday(data.getBirthday());
        if (null != instanceBean) {
            dataBean.setId(instanceBean.getData().getId());
        } else {
            dataBean.setId(0);
        }
        dataBean.setMobile(data.getMobile());
        dataBean.setNickName(data.getNickName());
        dataBean.setSex(data.getSex());
        dataBean.setUserName(data.getUserName());
        dataBean.setUserId(data.getUserId());
        result.setData(dataBean);
        return result;
    }
}
