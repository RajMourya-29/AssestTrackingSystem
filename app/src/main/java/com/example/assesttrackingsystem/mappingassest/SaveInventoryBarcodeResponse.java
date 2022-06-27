package com.example.assesttrackingsystem.mappingassest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveInventoryBarcodeResponse {

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
