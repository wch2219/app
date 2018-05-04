package com.xxc.shoppingmall.model;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by xuxingchen on 2018/1/22.
 */

public class NoticeModel {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : [{"id":1,"content":"徐小姐出台了","status":true,"platform":true,
     * "startTime":1514796626000,"endTime":1519807831000,"createTime":1516611038000,
     * "updateTime":null},{"id":2,"content":"徐小姐很是浪呀","status":true,"platform":true,
     * "startTime":1514796661000,"endTime":1519807866000,"createTime":1516611071000,
     * "updateTime":null},{"id":3,"content":"徐妈妈很会做生意呀","status":true,"platform":true,
     * "startTime":1514883096000,"endTime":1519807901000,"createTime":1516611107000,
     * "updateTime":null}]
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
         * content : 徐小姐出台了
         * status : true
         * platform : true
         * startTime : 1514796626000
         * endTime : 1519807831000
         * createTime : 1516611038000
         * updateTime : null
         */

        private int id;
        private String content;
        private boolean status;
        private boolean platform;
        private long startTime;
        private long endTime;
        private long createTime;
        private long updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public boolean isPlatform() {
            return platform;
        }

        public void setPlatform(boolean platform) {
            this.platform = platform;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            if (TextUtils.isEmpty(content)) {
                return "暂无公告";
            }
            return content;
        }
    }
}
