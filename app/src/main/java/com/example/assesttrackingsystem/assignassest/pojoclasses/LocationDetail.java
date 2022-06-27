package com.example.assesttrackingsystem.assignassest.pojoclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationDetail {

    @SerializedName("locationname")
    @Expose
    private String locationname;

    public String getLocationname() {
        return locationname;
    }

    public void setLocationname(String locationname) {
        this.locationname = locationname;
    }

}
