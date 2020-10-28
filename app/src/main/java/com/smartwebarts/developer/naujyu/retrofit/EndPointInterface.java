package com.smartwebarts.developer.naujyu.retrofit;


import com.smartwebarts.developer.naujyu.models.BrandModel;
import com.smartwebarts.developer.naujyu.models.LoginData;
import com.smartwebarts.developer.naujyu.models.MessageModel;
import com.smartwebarts.developer.naujyu.models.SignUpModel;
import com.smartwebarts.developer.naujyu.models.StateModel;
import com.smartwebarts.developer.naujyu.models.VehicleModel;
import com.smartwebarts.developer.naujyu.models.VendorDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EndPointInterface {

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<LoginData> login(@Field("login") String login,
                          @Field("mobile") String mobile,
                          @Field("password") String password);

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<SignUpModel> signup(@Field("register") String register,
                             @Field("name") String fullname,
                             @Field("mobile") String mobile,
                             @Field("password") String password,
                             @Field("stateid") String state,
                             @Field("cityid") String city,
                             @Field("zipcode") String zipcode);

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<List<VehicleModel>> vehiclemodel(@Field("vehicletype") String vehicletype);

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<List<BrandModel>> model(@Field("brand") String brand,
                                 @Field("models") String models);

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<List<BrandModel>> brand(@Field("brands") String brand,
                                 @Field("vehilcetypeid") String vehicletype);

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<BookingResponse> booking(@Field("customerid") String customerid,
                                   @Field("vehiclenumber") String vehiclenumber,
                                   @Field("vehicletyp") String vehicletype,
                                   @Field("vehiclemodel") String vehiclemodel,
                                   @Field("fueltype") String fueltype,
                                   @Field("request") String request,
                                   @Field("issue") String issue,
                                   @Field("location") String location,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String longitude,
                                   @Field("pincode") String pincode,
                                   @Field("brandid") String brandid
    );

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<List<StateModel>> state(@Field("state") String state);

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<List<StateModel>> city(@Field("city") String city,
                                @Field("statename") String statename);

    @POST("autopilot.php")
    @FormUrlEncoded
    Call<VendorDetails> loginDetails(@Field("vendordetails") String vendordetails,
                                     @Field("id") String id);


    @POST("autopilot.php")
    @FormUrlEncoded
    Call<MessageModel> updateProfile(@Field("profile") String profile,
                                     @Field("name") String name,
                                     @Field("mobile") String mobile,
                                     @Field("password") String password,
                                     @Field("shopname") String shopname,
                                     @Field("location") String location,
                                     @Field("stateid") String stateid,
                                     @Field("cityid") String cityid,
                                     @Field("zipcode") String zipcode,
                                     @Field("gstin") String gstin,
                                     @Field("id") String id);
}
