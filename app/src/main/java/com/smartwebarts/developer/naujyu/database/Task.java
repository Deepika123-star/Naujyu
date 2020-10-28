package com.smartwebarts.developer.naujyu.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Task implements Serializable {


    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "typeid")
    private String typeid;

    @ColumnInfo(name = "brandid")
    private String brandid;

    @ColumnInfo(name = "brandname")
    private String brandname;

    @ColumnInfo(name = "modelid")
    private String modelid;

    @ColumnInfo(name = "modelname")
    private String modelname;

    @ColumnInfo(name = "typename")
    private String typename;

    @ColumnInfo(name = "year")
    private String year;

    @ColumnInfo(name = "number")
    private String number;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "fueltype")
    private String fueltype;

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getBrandid() {
        return brandid;
    }

    public void setBrandid(String brandid) {
        this.brandid = brandid;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getModelname() {
        return modelname;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public Task(String typeid, String brandid, String brandname, String modelid, String modelname, String typename, String year, String number, String image, String fueltype) {
        this.typeid = typeid;
        this.brandid = brandid;
        this.brandname = brandname;
        this.modelid = modelid;
        this.modelname = modelname;
        this.typename = typename;
        this.year = year;
        this.number = number;
        this.image = image;
        this.fueltype = fueltype;
    }


}
