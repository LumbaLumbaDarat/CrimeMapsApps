package com.harifrizki.crimemapsapps.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse.Companion.errorResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.Admin.Companion.jsonObject
import com.harifrizki.crimemapsapps.utils.ApiResource
import com.harifrizki.crimemapsapps.utils.ResponseStatus
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource: DataSource {
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

    override fun handshake(): LiveData<ApiResource<HandshakeResponse>> {
        val result = MutableLiveData<ApiResource<HandshakeResponse>>()
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

    override fun login(admin: Admin?): LiveData<ApiResource<LoginResponse>> {
        val result = MutableLiveData<ApiResource<LoginResponse>>()
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

    override fun logout(admin: Admin?): LiveData<ApiResource<MessageResponse>> {
        val result = MutableLiveData<ApiResource<MessageResponse>>()
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

    override fun utilization(): LiveData<ApiResource<UtilizationResponse>> {
        val result = MutableLiveData<ApiResource<UtilizationResponse>>()
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

    override fun adminById(adminId: String?): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try
        {
            val client = adminId?.let { NetworkApi.connectToApi().adminById(it) }
            client?.enqueue(object : Callback<AdminResponse> {
                override fun onResponse(
                    call: Call<AdminResponse>,
                    response: Response<AdminResponse>
                ) {
                    convertResponse(response, AdminResponse(), result)
                }

                override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                    convertResponse(AdminResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(AdminResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun adminUpdate(admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try
        {
            val client = NetworkApi.connectToApi().adminUpdate(jsonObject(admin))
            client.enqueue(object : Callback<AdminResponse> {
                override fun onResponse(
                    call: Call<AdminResponse>,
                    response: Response<AdminResponse>
                ) {
                    convertResponse(response, AdminResponse(), result)
                }

                override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                    convertResponse(AdminResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(AdminResponse(), result, e, EMPTY)
        }
        return result
    }

    private fun <T> convertResponse(
        response: Response<T>,
        modelResponse: T,
        result: MutableLiveData<ApiResource<T>>)
    {
        if (response.isSuccessful)
            result.value = ApiResource.success(response.body()!!)
        else result.value = ApiResource.error(modelResponse,
            errorResponse(response, JSONObject(response.errorBody()?.string())))
    }

    private fun <T> convertResponse(
        modelResponse: T,
        result: MutableLiveData<ApiResource<T>>,
        throwable: Throwable,
        responseStatus: ResponseStatus
    )
    {
        Logger.e(throwable.message.toString());
        when (responseStatus)
        {
            ERROR -> {
                result.value = ApiResource.error(modelResponse, errorResponse(throwable))
            }
            else -> {
                result.value = ApiResource.empty(modelResponse, errorResponse(throwable))
            }
        }
    }
}