package com.cumt.chiduoduo.model;

import java.util.Date;

public class UserPassword {
    private Integer id;

    private String password;

    private Date lastResetPasswordTime;

    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getLastResetPasswordTime() {
        return lastResetPasswordTime;
    }

    public void setLastResetPasswordTime(Date lastResetPasswordTime) {
        this.lastResetPasswordTime = lastResetPasswordTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}