package com.smartwebarts.developer.naujyu.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StateModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName(value="name", alternate={"city"})
    @Expose
    private String name;
    @SerializedName("state")
    @Expose
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
