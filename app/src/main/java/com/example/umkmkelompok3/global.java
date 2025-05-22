package com.example.umkmkelompok3;

import android.app.Application;

public class global extends Application {
    private Boolean isLogin = false;
    private String email = "";
    private String username = "";

    protected Boolean getIsLogin() {
        return this.isLogin;
    }

    protected void setIsLogin(Boolean value) {
        this.isLogin = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
