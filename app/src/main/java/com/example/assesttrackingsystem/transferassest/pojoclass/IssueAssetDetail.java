package com.example.assesttrackingsystem.transferassest.pojoclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IssueAssetDetail {

    @SerializedName("employeename")
    @Expose
    private String employeename;
    @SerializedName("locationname")
    @Expose
    private String locationname;
    @SerializedName("SubLocation")
    @Expose
    private String subLocation;
    @SerializedName("isactive")
    @Expose
    private String isactive;
    @SerializedName("date")
    @Expose
    private String date;

    public String getEmployeename() {
        return employeename;
    }

    public void setEmployeename(String employeename) {
        this.employeename = employeename;
    }

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

    public String getSubLocation() {
        return subLocation;
    }

    public void setSubLocation(String subLocation) {
        this.subLocation = subLocation;
    }

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
