package com.harifrizki.crimemapsapps.data.remote

import com.google.gson.JsonObject
import com.harifrizki.crimemapsapps.data.remote.response.HandshakeResponse
import com.harifrizki.crimemapsapps.data.remote.response.LoginResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.data.remote.response.UtilizationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface EndPoint {
    @Headers("Content-Type: application/json")
    @GET(HANDSHAKE)
    fun handshake(): Call<HandshakeResponse>

    @Headers("Content-Type: application/json")
    @GET(UTILIZATION)
    fun utilization(): Call<UtilizationResponse>

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    fun login(@Body jsonObject: JsonObject): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(LOGOUT)
    fun logout(@Body jsonObject: JsonObject): Call<MessageResponse>
}