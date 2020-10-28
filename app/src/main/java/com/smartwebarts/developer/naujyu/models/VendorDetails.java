package com.smartwebarts.developer.naujyu.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VendorDetails {

    @SerializedName("statename")
    @Expose
    private String statename;
    @SerializedName("cityname")
    @Expose
    private String cityname;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("shopname")
    @Expose
    private String shopname;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("zipcode")
    @Expose
    private String zipcode;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("shopimage")
    @Expose
    private String shopimage;
    @SerializedName("addressverification")
    @Expose
    private String addressverification;
    @SerializedName("aadharcard")
    @Expose
    private String aadharcard;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("gstin")
    @Expose
    private String gstin;

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopimage() {
        return shopimage;
    }

    public void setShopimage(String shopimage) {
        this.shopimage = shopimage;
    }

    public String getAddressverification() {
        return addressverification;
    }

    public void setAddressverification(String addressverification) {
        this.addressverification = addressverification;
    }

    public String getAadharcard() {
        return aadharcard;
    }

    public void setAadharcard(String aadharcard) {
        this.aadharcard = aadharcard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}

