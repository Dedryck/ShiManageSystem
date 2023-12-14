package com.zqu;

/**
 * @aurhor Dedryck
 * @create 2023-11-19-19:38
 * @description:
 */
public class UserInfo {

        private String userID;
        private String name;
        private String role;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        // 构造函数
        public UserInfo(String userID, String name, String role) {
            this.userID = userID;
            this.name = name;
            this.role = role;

        }

//      逻辑判断用户是否为管理员
    public boolean isAdmin() {

        return this.role.equals("管理员"); // 假设你有一个字段来存储用户角色
    }

}
