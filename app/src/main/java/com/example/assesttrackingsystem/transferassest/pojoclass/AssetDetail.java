package com.example.assesttrackingsystem.transferassest.pojoclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssetDetail {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("Basic Asset Details")
    @Expose
    private List<BasicAssetDetail> basicAssetDetails = null;
    @SerializedName("Issue Asset Details")
    @Expose
    IssueAssetDetail[] issueAssetDetails = null;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<BasicAssetDetail> getBasicAssetDetails() {
        return basicAssetDetails;
    }

    public void setBasicAssetDetails(List<BasicAssetDetail> basicAssetDetails) {
        this.basicAssetDetails = basicAssetDetails;
    }

    public IssueAssetDetail[] getIssueAssetDetails() {
        return issueAssetDetails;
    }

    public void setIssueAssetDetails(IssueAssetDetail[] issueAssetDetails) {
        this.issueAssetDetails = issueAssetDetails;
    }

}
