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

    @Headers("Content-Type: application/json")
    @GET(ADMIN_BY_NAME)
    fun admin(@Query("pageNo") pageNo: String):
            Call<AdminResponse>

    @Headers("Content-Type: application/json")
    @GET(ADMIN_BY_NAME)
    fun adminByName(@Query("pageNo") pageNo: String,
                    @Field("adminName") adminName: String):
            Call<AdminResponse>

    @FormUrlEncoded
    @POST(ADMIN_BY_ID)
    fun adminById(@Field("adminId") adminId: String):
            Call<AdminResponse>

    @Multipart
    @POST(ADMIN_ADD)
    fun adminAdd(@PartMap() partMap: Map<String, RequestBody>,
                 @Part files: List<MultipartBody.Part>):
            Call<AdminResponse>

    @POST(ADMIN_UPDATE)
    fun adminUpdate(@Body jsonObject: JsonObject):
            Call<AdminResponse>

    @Multipart
    @POST(ADMIN_UPDATE_PHOTO_PROFILE)
    fun adminUpdatePhotoProfile(@PartMap() partMap: Map<String, RequestBody>,
                                @Part files: List<MultipartBody.Part>):
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
}