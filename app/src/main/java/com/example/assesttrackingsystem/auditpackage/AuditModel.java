package com.example.assesttrackingsystem.auditpackage;

public class AuditModel
{
    private String barcode;
    private String rfid;
    private  String loc;
    private String subloc;
    private String dept;
    private String username;
    private String usercode;

    public AuditModel(String barcode, String rfid, String loc, String subloc, String dept, String username, String usercode) {
        this.barcode = barcode;
        this.rfid = rfid;
        this.loc = loc;
        this.subloc = subloc;
        this.dept = dept;
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
                ", loc='" + loc + '\'' +
                ", subloc='" + subloc + '\'' +
                ", dept='" + dept + '\'' +
                ", username='" + username + '\'' +
                ", usercode='" + usercode + '\'' +
                '}';
    }
}
