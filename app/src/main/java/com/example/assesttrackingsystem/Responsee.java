package com.example.assesttrackingsystem;

import com.example.assesttrackingsystem.assignassest.pojoclasses.EmployeeName;
import com.example.assesttrackingsystem.assignassest.pojoclasses.LocationDetail;
import com.example.assesttrackingsystem.assignassest.pojoclasses.SubLocationDetail;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Responsee {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("Employee Name")
    @Expose
    private List<EmployeeName> employeeName = null;
    @SerializedName("Location Details")
    @Expose
    private List<LocationDetail> locationDetails = null;
    @SerializedName("Sub Location Details")
    @Expose
    private List<SubLocationDetail> subLocationDetails = null;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<EmployeeName> getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(List<EmployeeName> employeeName) {
        this.employeeName = employeeName;
    }

    public List<LocationDetail> getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(List<LocationDetail> locationDetails) {
        this.locationDetails = locationDetails;
    }

    public List<SubLocationDetail> getSubLocationDetails() {
        return subLocationDetails;
    }

    public void setSubLocationDetails(List<SubLocationDetail> subLocationDetails) {
        this.subLocationDetails = subLocationDetails;
    }


}
