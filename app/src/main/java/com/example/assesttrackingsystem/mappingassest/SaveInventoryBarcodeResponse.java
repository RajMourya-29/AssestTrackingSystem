package com.example.assesttrackingsystem.mappingassest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Response;

public class SaveInventoryBarcodeResponse {

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

        @SerializedName("Barcode")
        @Expose
        private String barcode;
        @SerializedName("RFID")
        @Expose
        private String rfid;
        @SerializedName("ScanBy")
        @Expose
        private String scanBy;
        @SerializedName("usercode")
        @Expose
        private String usercode;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getRfid() {
            return rfid;
        }

        public void setRfid(String rfid) {
            this.rfid = rfid;
        }

        public String getScanBy() {
            return scanBy;
        }

        public void setScanBy(String scanBy) {
            this.scanBy = scanBy;
        }

        public String getUsercode() {
            return usercode;
        }

        public void setUsercode(String usercode) {
            this.usercode = usercode;
        }

    }
    }

