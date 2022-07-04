package com.example.assesttrackingsystem.mappingassest;

public class MappingModel {
    private String barcode;
    private  String rfid;
    private String username;
    private String usercode;

    public MappingModel(String barcode, String rfid, String username, String usercode) {
        this.barcode = barcode;
        this.rfid = rfid;
        this.username = username;
        this.usercode = usercode;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    @Override
    public String toString() {
        return "{" +
                "barcode='" + barcode + '\'' +
                ", rfid='" + rfid + '\'' +
                ", username='" + username + '\'' +
                ", usercode='" + usercode + '\'' +
                '}';
    }
}
