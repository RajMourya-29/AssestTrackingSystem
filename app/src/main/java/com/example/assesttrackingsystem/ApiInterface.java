package com.example.assesttrackingsystem;

import com.example.assesttrackingsystem.auditpackage.GetDeptResponse;
import com.example.assesttrackingsystem.auditpackage.LocSublocResponse;
import com.example.assesttrackingsystem.mappingassest.SaveInventoryBarcodeResponse;
import com.example.assesttrackingsystem.transferassest.pojoclass.AssetDetail;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("GetEmployeeName")
    Call<Responsee> getempdata();

    @GET("GetLocations")
    Call<Responsee> getlocationdata();

    @GET("GetSubLocationDetails")
    Call<Responsee> getsublocationdata(
            @Query("location") String location);

    @FormUrlEncoded
    @POST("SaveData")
    Call<Responsee> savedata(
            @Field("barcode") String barcode,
            @Field("empname") String empname,
            @Field("location") String location,
            @Field("sublocation") String sublocation,
            @Field("ondate") String ondate);

    @FormUrlEncoded
    @POST("SaveMappingAssest")
    Call<Responsee> savemappingassestdata(
            @Field("json") String GRNSticker);

    @FormUrlEncoded
    @POST("CheckLogin")
    Call<Responsee> login(@Field("userid") String userid,
                          @Field("password") String password);

    @GET("BasicAssetDetails")
    Call<AssetDetail> getassetdetail(
            @Query("assetbarcode") String assetbarcode);

    @GET("IssueAssetDetails")
    Call<AssetDetail> getissueassetdetail(
            @Query("assetbarcode") String assetbarcode);

    @POST("SaveInventoryBarcodeDetail")
    @FormUrlEncoded
    Call<SaveInventoryBarcodeResponse> saveAllBarcode(
            @Field("json") String barcodeData);


    @GET("Getloc_subloc")
    Call<LocSublocResponse> GetLocandsubloc();

    @GET("Getdepartment")
    Call<GetDeptResponse> Getdepartment();

}