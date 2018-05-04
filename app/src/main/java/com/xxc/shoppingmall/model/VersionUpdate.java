package com.xxc.shoppingmall.model;

/**
 * Created by xuxingchen on 2017/12/4.
 */

public class VersionUpdate {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : {"id":"0101fe50e5-85ee-44b6-8828-bac79king","code":3,"name":"1.1.7",
     * "storagefile":"智能管控.apk","updatetime":null,"storagepath":"appVersions","createuser":"1",
     * "memo":"更新内容","imageurl":null}
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
         * id : 0101fe50e5-85ee-44b6-8828-bac79king
         * code : 3
         * name : 1.1.7
         * storagefile : 智能管控.apk
         * updatetime : null
         * storagepath : appVersions
         * createuser : 1
         * memo : 更新内容
         * imageurl : null
         */

        private String id;
        private int code;
        private String name;
        private String storagefile;
        private Object updatetime;
        private String storagepath;
        private String createuser;
        private String memo;
        private String imageurl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStoragefile() {
            return storagefile;
        }

        public void setStoragefile(String storagefile) {
            this.storagefile = storagefile;
        }

        public Object getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(Object updatetime) {
            this.updatetime = updatetime;
        }

        public String getStoragepath() {
            return storagepath;
        }

        public void setStoragepath(String storagepath) {
            this.storagepath = storagepath;
        }

        public String getCreateuser() {
            return createuser;
        }

        public void setCreateuser(String createuser) {
            this.createuser = createuser;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public String getImageurl() {
            return imageurl;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }
    }
}
