package com.harifrizki.crimemapsapps.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse.Companion.errorResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.Admin.Companion.jsonObject
import com.harifrizki.crimemapsapps.model.Province.Companion.jsonObject
import com.harifrizki.crimemapsapps.model.Province
import com.harifrizki.crimemapsapps.utils.ApiResource
import com.harifrizki.crimemapsapps.utils.ResponseStatus
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import com.harifrizki.crimemapsapps.utils.toMultipartBody
import com.harifrizki.crimemapsapps.utils.toRequestBody
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RemoteDataSource : DataSource {
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
        try {
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
        try {
            val client = NetworkApi.connectToApi().login(jsonObject(admin))
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
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
        try {
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
        try {
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

    override fun adminByName(pageNo: String?, name: String?): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
            val client = pageNo?.let {
                name?.let { name ->
                    NetworkApi.connectToApi().adminByName(it, name)
                }
            }
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

    override fun adminById(adminId: String?): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
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

    override fun adminAdd(
        admin: Admin?,
        photoProfile: File?
    ): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
            val client = NetworkApi.connectToApi().adminAdd(
                toRequestBody(jsonObject(admin).toString(), "multipart/form-data"),
                toMultipartBody(
                    photoProfile,
                    "adminPhotoProfile",
                    "multipart/form-data"
                )
            )
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

    override fun adminUpdate(admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
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

    override fun adminUpdatePhotoProfile(
        admin: Admin?,
        photoProfile: File?
    ): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
            val client = NetworkApi.connectToApi().adminUpdatePhotoProfile(
                toRequestBody(jsonObject(admin).toString(), "multipart/form-data"),
                toMultipartBody(
                    photoProfile,
                    "adminPhotoProfile",
                    "multipart/form-data"
                )
            )
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

    override fun adminUpdatePassword(
        adminId: String?,
        oldPassword: String?,
        newPassword: String?
    ): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
            val client = adminId?.let {
                oldPassword?.let { oldPassword ->
                    newPassword?.let { newPassword ->
                        NetworkApi.connectToApi().adminUpdatePassword(
                            it, oldPassword, newPassword
                        )
                    }
                }
            }
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

    override fun adminResetPassword(admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
            val client = NetworkApi.connectToApi().adminResetPassword(jsonObject(admin))
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

    override fun adminUpdateActive(admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        val result = MutableLiveData<ApiResource<AdminResponse>>()
        try {
            val client = NetworkApi.connectToApi().adminUpdateActive(jsonObject(admin))
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

    override fun adminDelete(admin: Admin?): LiveData<ApiResource<MessageResponse>> {
        val result = MutableLiveData<ApiResource<MessageResponse>>()
        try {
            val client = NetworkApi.connectToApi().adminDelete(jsonObject(admin))
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

    override fun provinceByName(
        pageNo: String?,
        name: String?
    ): LiveData<ApiResource<ProvinceResponse>> {
        val result = MutableLiveData<ApiResource<ProvinceResponse>>()
        try {
            val client = pageNo?.let {
                name?.let { name ->
                    NetworkApi.connectToApi().provinceByName(it, name)
                }
            }
            client?.enqueue(object : Callback<ProvinceResponse> {
                override fun onResponse(
                    call: Call<ProvinceResponse>,
                    response: Response<ProvinceResponse>
                ) {
                    convertResponse(response, ProvinceResponse(), result)
                }

                override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                    convertResponse(ProvinceResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(ProvinceResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun provinceById(provinceId: String?): LiveData<ApiResource<ProvinceResponse>> {
        val result = MutableLiveData<ApiResource<ProvinceResponse>>()
        try {
            val client = provinceId?.let { NetworkApi.connectToApi().provinceById(it) }
            client?.enqueue(object : Callback<ProvinceResponse> {
                override fun onResponse(
                    call: Call<ProvinceResponse>,
                    response: Response<ProvinceResponse>
                ) {
                    convertResponse(response, ProvinceResponse(), result)
                }

                override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                    convertResponse(ProvinceResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(ProvinceResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun provinceAdd(province: Province?): LiveData<ApiResource<ProvinceResponse>> {
        val result = MutableLiveData<ApiResource<ProvinceResponse>>()
        try {
            val client = NetworkApi.connectToApi().provinceAdd(jsonObject(province))
            client.enqueue(object : Callback<ProvinceResponse> {
                override fun onResponse(
                    call: Call<ProvinceResponse>,
                    response: Response<ProvinceResponse>
                ) {
                    convertResponse(response, ProvinceResponse(), result)
                }

                override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                    convertResponse(ProvinceResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(ProvinceResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun provinceUpdate(province: Province?): LiveData<ApiResource<ProvinceResponse>> {
        val result = MutableLiveData<ApiResource<ProvinceResponse>>()
        try {
            val client = NetworkApi.connectToApi().provinceUpdate(jsonObject(province))
            client.enqueue(object : Callback<ProvinceResponse> {
                override fun onResponse(
                    call: Call<ProvinceResponse>,
                    response: Response<ProvinceResponse>
                ) {
                    convertResponse(response, ProvinceResponse(), result)
                }

                override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                    convertResponse(ProvinceResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(ProvinceResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun provinceDelete(province: Province?): LiveData<ApiResource<ProvinceResponse>> {
        val result = MutableLiveData<ApiResource<ProvinceResponse>>()
        try {
            val client = NetworkApi.connectToApi().provinceDelete(jsonObject(province))
            client.enqueue(object : Callback<ProvinceResponse> {
                override fun onResponse(
                    call: Call<ProvinceResponse>,
                    response: Response<ProvinceResponse>
                ) {
                    convertResponse(response, ProvinceResponse(), result)
                }

                override fun onFailure(call: Call<ProvinceResponse>, t: Throwable) {
                    convertResponse(ProvinceResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(ProvinceResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun cityByName(pageNo: String?, name: String?): LiveData<ApiResource<CityResponse>> {
        val result = MutableLiveData<ApiResource<CityResponse>>()
        try {
            val client = pageNo?.let {
                name?.let { name ->
                    NetworkApi.connectToApi().cityByName(it, name)
                }
            }
            client?.enqueue(object : Callback<CityResponse> {
                override fun onResponse(
                    call: Call<CityResponse>,
                    response: Response<CityResponse>
                ) {
                    convertResponse(response, CityResponse(), result)
                }

                override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                    convertResponse(CityResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(CityResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun subDistrictByName(
        pageNo: String?,
        name: String?
    ): LiveData<ApiResource<SubDistrictResponse>> {
        val result = MutableLiveData<ApiResource<SubDistrictResponse>>()
        try {
            val client = pageNo?.let {
                name?.let { name ->
                    NetworkApi.connectToApi().subDistrictByName(it, name)
                }
            }
            client?.enqueue(object : Callback<SubDistrictResponse> {
                override fun onResponse(
                    call: Call<SubDistrictResponse>,
                    response: Response<SubDistrictResponse>
                ) {
                    convertResponse(response, SubDistrictResponse(), result)
                }

                override fun onFailure(call: Call<SubDistrictResponse>, t: Throwable) {
                    convertResponse(SubDistrictResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(SubDistrictResponse(), result, e, EMPTY)
        }
        return result
    }

    override fun urbanVillageByName(
        pageNo: String?,
        name: String?
    ): LiveData<ApiResource<UrbanVillageResponse>> {
        val result = MutableLiveData<ApiResource<UrbanVillageResponse>>()
        try {
            val client = pageNo?.let {
                name?.let { name ->
                    NetworkApi.connectToApi().urbanVillageByName(it, name)
                }
            }
            client?.enqueue(object : Callback<UrbanVillageResponse> {
                override fun onResponse(
                    call: Call<UrbanVillageResponse>,
                    response: Response<UrbanVillageResponse>
                ) {
                    convertResponse(response, UrbanVillageResponse(), result)
                }

                override fun onFailure(call: Call<UrbanVillageResponse>, t: Throwable) {
                    convertResponse(UrbanVillageResponse(), result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(UrbanVillageResponse(), result, e, EMPTY)
        }
        return result
    }

    private fun <T> convertResponse(
        response: Response<T>,
        modelResponse: T,
        result: MutableLiveData<ApiResource<T>>
    ) {
        if (response.isSuccessful)
            result.value = ApiResource.success(response.body()!!)
        else result.value = ApiResource.error(
            modelResponse,
            errorResponse(response, JSONObject(response.errorBody()?.string()))
        )
    }

    private fun <T> convertResponse(
        modelResponse: T,
        result: MutableLiveData<ApiResource<T>>,
        throwable: Throwable,
        responseStatus: ResponseStatus
    ) {
        Logger.e(throwable.message.toString());
        when (responseStatus) {
            ERROR -> {
                result.value = ApiResource.error(modelResponse, errorResponse(throwable))
            }
            else -> {
                result.value = ApiResource.empty(modelResponse, errorResponse(throwable))
            }
        }
    }
}