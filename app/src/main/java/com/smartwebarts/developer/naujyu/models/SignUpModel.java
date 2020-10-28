package com.smartwebarts.developer.naujyu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignUpModel {

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("userid")
    @Expose
    private Integer userid;

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
