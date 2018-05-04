package com.xxc.shoppingmall.model;

import java.util.List;

/**
 * Created by guo on 2018/3/26 0026.
 */

public class MyTeam {

    /**
     * msg : {"info":"","code":0,"success":true}
     * data : {"teamList":[{"userId":"3fe8e8b8e8654d22b07168c56e16fde2","userName":"乐视员工","avatarUrl":"d546e07bdcb5409685d8c7b3348bb19c.jpeg","childrenList":[{"userId":"cb7fd79825e84cc3a74da14a25d3c143","userName":"陈勇奇","avatarUrl":null,"childrenList":[]},{"userId":"584ad68acd8c43018c2c947bdbc2fc22","userName":null,"avatarUrl":null,"childrenList":[]},{"userId":"876e87ab40c742699c8d0b5cca2f855b","userName":null,"avatarUrl":null,"childrenList":[]},{"userId":"e6a18e10e5404be5b733deb5d43af7ad","userName":"贾小强","avatarUrl":null,"childrenList":[]},{"userId":"7b5b414c0d54471b895dd195f1b0a7ba","userName":"唐敬原","avatarUrl":null,"childrenList":[]},{"userId":"c474cdaa3988440fbba876057d073381","userName":"唐敬原0","avatarUrl":null,"childrenList":[]}]}],"number":6}
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
         * teamList : [{"userId":"3fe8e8b8e8654d22b07168c56e16fde2","userName":"乐视员工","avatarUrl":"d546e07bdcb5409685d8c7b3348bb19c.jpeg","childrenList":[{"userId":"cb7fd79825e84cc3a74da14a25d3c143","userName":"陈勇奇","avatarUrl":null,"childrenList":[]},{"userId":"584ad68acd8c43018c2c947bdbc2fc22","userName":null,"avatarUrl":null,"childrenList":[]},{"userId":"876e87ab40c742699c8d0b5cca2f855b","userName":null,"avatarUrl":null,"childrenList":[]},{"userId":"e6a18e10e5404be5b733deb5d43af7ad","userName":"贾小强","avatarUrl":null,"childrenList":[]},{"userId":"7b5b414c0d54471b895dd195f1b0a7ba","userName":"唐敬原","avatarUrl":null,"childrenList":[]},{"userId":"c474cdaa3988440fbba876057d073381","userName":"唐敬原0","avatarUrl":null,"childrenList":[]}]}]
         * number : 6
         */

        private int number;
        private List<TeamListBean> teamList;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public List<TeamListBean> getTeamList() {
            return teamList;
        }

        public void setTeamList(List<TeamListBean> teamList) {
            this.teamList = teamList;
        }

        public static class TeamListBean {
            /**
             * userId : 3fe8e8b8e8654d22b07168c56e16fde2
             * userName : 乐视员工
             * avatarUrl : d546e07bdcb5409685d8c7b3348bb19c.jpeg
             * childrenList : [{"userId":"cb7fd79825e84cc3a74da14a25d3c143","userName":"陈勇奇","avatarUrl":null,"childrenList":[]},{"userId":"584ad68acd8c43018c2c947bdbc2fc22","userName":null,"avatarUrl":null,"childrenList":[]},{"userId":"876e87ab40c742699c8d0b5cca2f855b","userName":null,"avatarUrl":null,"childrenList":[]},{"userId":"e6a18e10e5404be5b733deb5d43af7ad","userName":"贾小强","avatarUrl":null,"childrenList":[]},{"userId":"7b5b414c0d54471b895dd195f1b0a7ba","userName":"唐敬原","avatarUrl":null,"childrenList":[]},{"userId":"c474cdaa3988440fbba876057d073381","userName":"唐敬原0","avatarUrl":null,"childrenList":[]}]
             */

            private String userId;
            private String userName;
            private String avatarUrl;
           // private List<ChildrenListBean> childrenList;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

           /* public List<ChildrenListBean> getChildrenList() {
                return childrenList;
            }

            public void setChildrenList(List<ChildrenListBean> childrenList) {
                this.childrenList = childrenList;
            }*/

            public static class ChildrenListBean {
                /**
                 * userId : cb7fd79825e84cc3a74da14a25d3c143
                 * userName : 陈勇奇
                 * avatarUrl : null
                 * childrenList : []
                 */

                private String userId;
                private String userName;
                private Object avatarUrl;
                private List<?> childrenList;

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public String getUserName() {
                    return userName;
                }

                public void setUserName(String userName) {
                    this.userName = userName;
                }

                public Object getAvatarUrl() {
                    return avatarUrl;
                }

                public void setAvatarUrl(Object avatarUrl) {
                    this.avatarUrl = avatarUrl;
                }

                public List<?> getChildrenList() {
                    return childrenList;
                }

                public void setChildrenList(List<?> childrenList) {
                    this.childrenList = childrenList;
                }
            }
        }
    }
}
