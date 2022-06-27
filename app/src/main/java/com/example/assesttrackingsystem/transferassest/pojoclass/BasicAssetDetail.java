package com.example.assesttrackingsystem.transferassest.pojoclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicAssetDetail {

    @SerializedName("Column1")
    @Expose
    private String column1;
    @SerializedName("Column2")
    @Expose
    private String column2;
    @SerializedName("Column3")
    @Expose
    private String column3;
    @SerializedName("Column4")
    @Expose
    private String column4;
    @SerializedName("Column5")
    @Expose
    private String column5;
    @SerializedName("Column6")
    @Expose
    private String column6;
    @SerializedName("Column7")
    @Expose
    private String column7;

    public String getColumn1() {
        return column1;
    }

    public void setColumn1(String column1) {
        this.column1 = column1;
    }

    public String getColumn2() {
        return column2;
    }

    public void setColumn2(String column2) {
        this.column2 = column2;
    }

    public String getColumn3() {
        return column3;
    }

    public void setColumn3(String column3) {
        this.column3 = column3;
    }

    public String getColumn4() {
        return column4;
    }

    public void setColumn4(String column4) {
        this.column4 = column4;
    }

    public String getColumn5() {
        return column5;
    }

    public void setColumn5(String column5) {
        this.column5 = column5;
    }

    public String getColumn6() {
        return column6;
    }

    public void setColumn6(String column6) {
        this.column6 = column6;
    }

    public String getColumn7() {
        return column7;
    }

    public void setColumn7(String column7) {
        this.column7 = column7;
    }
}
