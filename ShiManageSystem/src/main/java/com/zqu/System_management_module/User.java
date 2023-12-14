package com.zqu.System_management_module;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @aurhor Dedryck
 * @create 2023-11-21-21:07
 * @description:
 */
public class User {
//    用户ID
    private StringProperty userId = new SimpleStringProperty(this, "userId");
//    员工姓名
    private StringProperty name = new SimpleStringProperty(this, "name");
//    权限
    private StringProperty role = new SimpleStringProperty(this, "role");
//    联系电话
    private StringProperty phone = new SimpleStringProperty(this, "phone");
//    工作岗位
    private StringProperty position = new SimpleStringProperty(this, "position");
//    工作职能
    private StringProperty function = new SimpleStringProperty(this, "function");
    private StringProperty softDeleteMark = new SimpleStringProperty(this, "softDeleteMark");




    public String getUserId() {
        return userId.get();
    }

    public StringProperty userIdProperty() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getPosition() {
        return position.get();
    }

    public StringProperty positionProperty() {
        return position;
    }



    public String getSoftDeleteMark() {
        return softDeleteMark.get();
    }

    public StringProperty softDeleteMarkProperty() {
        return softDeleteMark;
    }

    public void setSoftDeleteMark(String softDeleteMark) {
        this.softDeleteMark.set(softDeleteMark);
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public String getFunction() {
        return function.get();
    }

    public StringProperty functionProperty() {
        return function;
    }

    public void setFunction(String function) {
        this.function.set(function);
    }

    public User(String userId, String name, String role, String position, String function, String phone) {
        this.userId.set(userId);
        this.name.set(name);
        this.role.set(role);
        this.position.set(position);
        this.function.set(function);
        this.phone.set(phone);
    }

    public User(String userId, String name, String role, String position, String function, String phone, String softDeleteMark) {
        this.userId.set(userId);
        this.name.set(name);
        this.role.set(role);
        this.position.set(position);
        this.function.set(function);
        this.phone.set(phone);
        this.softDeleteMark.set(softDeleteMark);
    }

//    用于记录管理员重要操作的细节
    public String getUserDetailsForLogging() {
        return "用户ID: " + getUserId() +
                ", 姓名: " + getName() +
                ", 权限: " + getRole() +
                ", 联系电话: " + getPhone() +
                ", 工作岗位: " + getPosition() +
                ", 工作职能: " + getFunction();
    }
}