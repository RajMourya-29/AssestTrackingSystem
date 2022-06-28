package com.example.assesttrackingsystem.auditpackage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Response;

public class LocSublocResponse {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }
    public class Response {

        @SerializedName("locationname")
        @Expose
        private String locationname;
        @SerializedName("sublocation")
        @Expose
        private String sublocation;

        public String getLocationname() {
            return locationname;
        }

        public void setLocationname(String locationname) {
            this.locationname = locationname;
        }

        public String getSublocation() {
            return sublocation;
        }

        public void setSublocation(String sublocation) {
            this.sublocation = sublocation;
        }
    }
}
