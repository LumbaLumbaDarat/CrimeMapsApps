package com.harifrizki.crimemapsapps.data.remote

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkApi {
    companion object {
        fun connectToApi(): EndPoint {
            val interceptor: HttpLoggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient =
                OkHttpClient().newBuilder().addInterceptor(interceptor)
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS).build()

            val gson: Gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder().baseUrl(MAIN_URL_API)
                .addConverterFactory(GsonConverterFactory.create(gson)).client(client).build()

            return retrofit.create(EndPoint::class.java)
        }
    }
}