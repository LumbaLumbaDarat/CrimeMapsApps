package com.harifrizki.crimemapsapps.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse.Companion.errorResponse
import com.harifrizki.crimemapsapps.data.remote.response.HandshakeResponse
import com.harifrizki.crimemapsapps.data.remote.response.LoginResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.data.remote.response.UtilizationResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.Admin.Companion.jsonObject
import com.harifrizki.crimemapsapps.utils.Status
import com.harifrizki.crimemapsapps.utils.Status.EMPTY
import com.harifrizki.crimemapsapps.utils.Status.ERROR
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {
    companion object {
        private val TAG: String =
            RemoteDataSource::javaClass.name
        private var INSTANCE: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            INSTANCE ?: RemoteDataSource()
    }

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    fun handshake(): LiveData<ApiResponse<HandshakeResponse>> {
        val result = MutableLiveData<ApiResponse<HandshakeResponse>>()
        try
        {
            val client = NetworkApi.connectToApi().handshake()
            client.enqueue(object : Callback<HandshakeResponse> {
                override fun onResponse(
                    call: Call<HandshakeResponse>,
                    response: Response<HandshakeResponse>
                ) {
                    convertResponse(response, HandshakeResponse(), result)
                }

                override fun onFailure(call: Call<HandshakeResponse>, t: Throwable) {
                    convertResponse(HandshakeResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(HandshakeResponse(), result, e, EMPTY)
        }
        return result
    }

    fun login(admin: Admin?): LiveData<ApiResponse<LoginResponse>> {
        val result = MutableLiveData<ApiResponse<LoginResponse>>()
        try
        {
            val client = NetworkApi.connectToApi().login(jsonObject(admin))
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    convertResponse(response, LoginResponse(), result)
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    convertResponse(LoginResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(LoginResponse(), result, e, EMPTY)
        }
        return result
    }

    fun logout(admin: Admin?): LiveData<ApiResponse<MessageResponse>> {
        val result = MutableLiveData<ApiResponse<MessageResponse>>()
        try
        {
            val client = NetworkApi.connectToApi().logout(jsonObject(admin))
            client.enqueue(object : Callback<MessageResponse> {
                override fun onResponse(
                    call: Call<MessageResponse>,
                    response: Response<MessageResponse>
                ) {
                    convertResponse(response, MessageResponse(), result)
                }

                override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                    convertResponse(MessageResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(MessageResponse(), result, e, EMPTY)
        }
        return result
    }

    fun utilization(): LiveData<ApiResponse<UtilizationResponse>> {
        val result = MutableLiveData<ApiResponse<UtilizationResponse>>()
        try
        {
            val client = NetworkApi.connectToApi().utilization()
            client.enqueue(object : Callback<UtilizationResponse> {
                override fun onResponse(
                    call: Call<UtilizationResponse>,
                    response: Response<UtilizationResponse>
                ) {
                    convertResponse(response, UtilizationResponse(), result)
                }

                override fun onFailure(call: Call<UtilizationResponse>, t: Throwable) {
                    convertResponse(UtilizationResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(UtilizationResponse(), result, e, EMPTY)
        }
        return result
    }

    private fun <T> convertResponse(
        response: Response<T>,
        modelResponse: T,
        result: MutableLiveData<ApiResponse<T>>)
    {
        if (response.isSuccessful)
            result.value = ApiResponse.success(response.body()!!)
        else result.value = ApiResponse.error(modelResponse,
            errorResponse(response, JSONObject(response.errorBody()?.string())))
    }

    private fun <T> convertResponse(
        modelResponse: T,
        result: MutableLiveData<ApiResponse<T>>,
        throwable: Throwable,
        status: Status
    )
    {
        Logger.e(throwable.message.toString());
        when (status)
        {
            ERROR -> {
                result.value = ApiResponse.error(modelResponse, errorResponse(throwable))
            }
            else -> {
                result.value = ApiResponse.empty(modelResponse, errorResponse(throwable))
            }
        }
    }
}