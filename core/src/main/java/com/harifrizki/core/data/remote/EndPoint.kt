package com.harifrizki.core.data.remote

import com.google.gson.JsonObject
import com.harifrizki.core.data.remote.response.*
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

    @POST(END_POINT_ADMIN)
    fun admin(
        @Query("searchBy") searchBy: String,
        @Query("pageNo") pageNo: String,
        @Body jsonObject: JsonObject
    ):
            Call<AdminResponse>

    @FormUrlEncoded
    @POST(END_POINT_DETAIL_ADMIN)
    fun adminDetail(@Field("adminId") adminId: String):
            Call<AdminResponse>

    @Multipart
    @POST(END_POINT_ADD_ADMIN)
    fun adminAdd(
        @Part("adminEntity") adminEntity: RequestBody,
        @Part adminPhotoProfile: MultipartBody.Part
    ):
            Call<AdminResponse>

    @POST(END_POINT_UPDATE_ADMIN)
    fun adminUpdate(@Body jsonObject: JsonObject):
            Call<AdminResponse>

    @Multipart
    @POST(END_POINT_UPDATE_PHOTO_PROFILE_ADMIN)
    fun adminUpdatePhotoProfile(
        @Part("adminEntity") adminEntity: RequestBody,
        @Part adminPhotoProfile: MultipartBody.Part
    ):
            Call<AdminResponse>

    @FormUrlEncoded
    @POST(END_POINT_UPDATE_PASSWORD_ADMIN)
    fun adminUpdatePassword(@Field("adminId") adminId: String,
                            @Field("adminOldPassword") adminOldPassword: String,
                            @Field("adminId") adminNewPassword: String):
            Call<AdminResponse>

    @POST(END_POINT_ACTIVATED_ADMIN)
    fun adminUpdateActive(@Body jsonObject: JsonObject):
            Call<AdminResponse>

    @POST(END_POINT_RESET_PASSWORD_ADMIN)
    fun adminResetPassword(@Body jsonObject: JsonObject):
            Call<AdminResponse>

    @POST(END_POINT_DELETE_ADMIN)
    fun adminDelete(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @POST(END_POINT_PROVINCE)
    fun province(
        @Query("searchBy") searchBy: String,
        @Query("pageNo") pageNo: String,
        @Body jsonObject: JsonObject
    ):
            Call<ProvinceResponse>

    @POST(END_POINT_DETAIL_PROVINCE)
    fun provinceDetail(@Body jsonObject: JsonObject):
            Call<ProvinceResponse>

    @POST(END_POINT_ADD_PROVINCE)
    fun provinceAdd(@Body jsonObject: JsonObject):
            Call<ProvinceResponse>

    @POST(END_POINT_UPDATE_PROVINCE)
    fun provinceUpdate(@Body jsonObject: JsonObject):
            Call<ProvinceResponse>

    @POST(END_POINT_DELETE_PROVINCE)
    fun provinceDelete(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @POST(END_POINT_CITY)
    fun city(
        @Query("searchBy") searchBy: String,
        @Query("pageNo") pageNo: String,
        @Body jsonObject: JsonObject
    ):
            Call<CityResponse>

    @POST(END_POINT_DETAIL_CITY)
    fun cityDetail(@Body jsonObject: JsonObject):
            Call<CityResponse>

    @POST(END_POINT_ADD_CITY)
    fun cityAdd(@Body jsonObject: JsonObject):
            Call<CityResponse>

    @POST(END_POINT_UPDATE_CITY)
    fun cityUpdate(@Body jsonObject: JsonObject):
            Call<CityResponse>

    @POST(END_POINT_DELETE_CITY)
    fun cityDelete(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @POST(END_POINT_SUB_DISTRICT)
    fun subDistrict(
        @Query("searchBy") searchBy: String,
        @Query("pageNo") pageNo: String,
        @Body jsonObject: JsonObject
    ):
            Call<SubDistrictResponse>

    @POST(END_POINT_DETAIL_SUB_DISTRICT)
    fun subDistrictDetail(@Body jsonObject: JsonObject):
            Call<SubDistrictResponse>

    @POST(END_POINT_ADD_SUB_DISTRICT)
    fun subDistrictAdd(@Body jsonObject: JsonObject):
            Call<SubDistrictResponse>

    @POST(END_POINT_UPDATE_SUB_DISTRICT)
    fun subDistrictUpdate(@Body jsonObject: JsonObject):
            Call<SubDistrictResponse>

    @POST(END_POINT_DELETE_SUB_DISTRICT)
    fun subDistrictDelete(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @POST(END_POINT_URBAN_VILLAGE)
    fun urbanVillage(
        @Query("searchBy") searchBy: String,
        @Query("pageNo") pageNo: String,
        @Body jsonObject: JsonObject
    ):
            Call<UrbanVillageResponse>

    @POST(END_POINT_DETAIL_URBAN_VILLAGE)
    fun urbanVillageDetail(@Body jsonObject: JsonObject):
            Call<UrbanVillageResponse>

    @POST(END_POINT_ADD_URBAN_VILLAGE)
    fun urbanVillageAdd(@Body jsonObject: JsonObject):
            Call<UrbanVillageResponse>

    @POST(END_POINT_UPDATE_URBAN_VILLAGE)
    fun urbanVillageUpdate(@Body jsonObject: JsonObject):
            Call<UrbanVillageResponse>

    @POST(END_POINT_DELETE_URBAN_VILLAGE)
    fun urbanVillageDelete(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @POST(END_POINT_CRIME_LOCATION)
    fun crimeLocation(
        @Query("searchBy") searchBy: String,
        @Query("pageNo") pageNo: String,
        @Body jsonObject: JsonObject
    ):
            Call<CrimeLocationResponse>

    @POST(END_POINT_DETAIL_CRIME_LOCATION)
    fun crimeLocationDetail(@Body jsonObject: JsonObject):
            Call<CrimeLocationResponse>

    @Multipart
    @POST(END_POINT_ADD_CRIME_LOCATION)
    fun crimeLocationAdd(
        @Part("crimeLocationEntity") crimeLocationEntity: RequestBody,
        @Part crimeLocationPhoto: List<MultipartBody.Part>
    ):
            Call<CrimeLocationResponse>

    @Multipart
    @POST(END_POINT_ADD_IMAGE_CRIME_LOCATION)
    fun crimeLocationAddImage(
        @Part("crimeLocationEntity") crimeLocationEntity: RequestBody,
        @Part crimeLocationPhoto: List<MultipartBody.Part>
    ):
            Call<CrimeLocationResponse>

    @POST(END_POINT_UPDATE_CRIME_LOCATION)
    fun crimeLocationUpdate(@Body jsonObject: JsonObject):
            Call<CrimeLocationResponse>

    @POST(END_POINT_DELETE_IMAGE_CRIME_LOCATION)
    fun crimeLocationDeleteImage(@Body jsonObject: JsonObject):
            Call<MessageResponse>

    @POST(END_POINT_DELETE_CRIME_LOCATION)
    fun crimeLocationDelete(@Body jsonObject: JsonObject):
            Call<MessageResponse>
}