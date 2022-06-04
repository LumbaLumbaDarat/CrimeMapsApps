package com.harifrizki.crimemapsapps.data.remote

import com.google.gson.JsonObject
import com.harifrizki.crimemapsapps.data.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface EndPoint {
    @Headers("Content-Type: application/json")
    @GET(HANDSHAKE)
    fun handshake():
            Call<HandshakeResponse>

    @Headers("Content-Type: application/json")
    @GET(UTILIZATION)
    fun utilization():
            Call<UtilizationResponse>

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    fun login(@Body jsonObject: JsonObject):
            Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(LOGOUT)
    fun logout(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @FormUrlEncoded
    @POST(ADMIN_BY_NAME)
    fun adminByName(@Query("pageNo") pageNo: String,
                    @Field("adminName") adminName: String):
            Call<AdminResponse>

    @FormUrlEncoded
    @POST(ADMIN_BY_ID)
    fun adminById(@Field("adminId") adminId: String):
            Call<AdminResponse>

    @Multipart
    @POST(ADMIN_ADD)
    fun adminAdd(@Part("adminEntity") adminEntity: RequestBody,
                 @Part adminPhotoProfile: MultipartBody.Part):
            Call<AdminResponse>

    @POST(ADMIN_UPDATE)
    fun adminUpdate(@Body jsonObject: JsonObject):
            Call<AdminResponse>

    @Multipart
    @POST(ADMIN_UPDATE_PHOTO_PROFILE)
    fun adminUpdatePhotoProfile(@Part("adminEntity") adminEntity: RequestBody,
                                @Part adminPhotoProfile: MultipartBody.Part):
            Call<AdminResponse>

    @FormUrlEncoded
    @POST(ADMIN_UPDATE_PASSWORD)
    fun adminUpdatePassword(@Field("adminId")          adminId: String,
                            @Field("adminOldPassword") adminOldPassword: String,
                            @Field("adminNewPassword") adminNewPassword: String):
            Call<AdminResponse>

    @POST(ADMIN_UPDATE_ACTIVE)
    fun adminUpdateActive(@Body jsonObject: JsonObject):
            Call<AdminResponse>

    @POST(ADMIN_RESET_PASSWORD)
    fun adminResetPassword(@Body jsonObject: JsonObject):
            Call<AdminResponse>

    @POST(ADMIN_DELETE)
    fun adminDelete(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @FormUrlEncoded
    @POST(PROVINCE_BY_NAME)
    fun provinceByName(@Query("pageNo") pageNo: String,
                       @Field("provinceName") provinceName: String):
            Call<ProvinceResponse>

    @FormUrlEncoded
    @POST(PROVINCE_BY_ID)
    fun provinceById(@Field("provinceId") provinceId: String):
            Call<ProvinceResponse>

    @POST(PROVINCE_ADD)
    fun provinceAdd(@Body jsonObject: JsonObject):
            Call<ProvinceResponse>

    @POST(PROVINCE_UPDATE)
    fun provinceUpdate(@Body jsonObject: JsonObject):
            Call<ProvinceResponse>

    @POST(PROVINCE_DELETE)
    fun provinceDelete(@Body jsonObject: JsonObject):
            Call<ProvinceResponse>

    @FormUrlEncoded
    @POST(CITY_BY_NAME)
    fun cityByName(@Query("pageNo") pageNo: String,
                   @Field("cityName") cityName: String):
            Call<CityResponse>

    @FormUrlEncoded
    @POST(SUB_DISTRICT_BY_NAME)
    fun subDistrictByName(@Query("pageNo") pageNo: String,
                          @Field("subDistrictName") subDistrictName: String):
            Call<SubDistrictResponse>

    @FormUrlEncoded
    @POST(URBAN_VILLAGE_BY_NAME)
    fun urbanVillageByName(@Query("pageNo") pageNo: String,
                           @Field("urbanVillageName") urbanVillageName: String):
            Call<UrbanVillageResponse>
}