package com.smartwebarts.developer.naujyu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginData {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName(value = "id", alternate = {"userid"})
    @Expose
    private String id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}