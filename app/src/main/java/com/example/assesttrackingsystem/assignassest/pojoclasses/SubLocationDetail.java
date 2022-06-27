package com.example.assesttrackingsystem.assignassest.pojoclasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubLocationDetail {

    @SerializedName("sublocation")
    @Expose
    private String sublocation;

    public String getSublocation() {
        return sublocation;
    }

    public void setSublocation(String sublocation) {
        this.sublocation = sublocation;
    }

}
