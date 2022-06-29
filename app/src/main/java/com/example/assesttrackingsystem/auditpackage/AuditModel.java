package com.example.assesttrackingsystem.auditpackage;

public class AuditModel
{
    private String barcode;
    private String rfid;
    private  String loc;
    private String subloc;
    private String dept;

    public AuditModel(String barcode, String rfid, String loc, String subloc, String dept) {
        this.barcode = barcode;
        this.rfid = rfid;
        this.loc = loc;
        this.subloc = subloc;
        this.dept = dept;
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

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getSubloc() {
        return subloc;
    }

    public void setSubloc(String subloc) {
        this.subloc = subloc;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    @Override
    public String toString() {
        return "{" +
                "barcode='" + barcode + '\'' +
                ", rfid='" + rfid + '\'' +
                ", loc='" + loc + '\'' +
                ", subloc='" + subloc + '\'' +
                ", dept='" + dept + '\'' +
                '}';
    }
}
