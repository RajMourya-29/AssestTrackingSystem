package com.example.assesttrackingsystem.mappingassest;

public class MappingModel {
    private String barcode;
    private  String rfid;

    public MappingModel(String barcode, String rfid) {
        this.barcode = barcode;
        this.rfid = rfid;
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

    @Override
    public String toString() {
        return "{" +
                "barcode='" + barcode + '\'' +
                ", rfid='" + rfid + '\'' +
                '}';
    }
}
