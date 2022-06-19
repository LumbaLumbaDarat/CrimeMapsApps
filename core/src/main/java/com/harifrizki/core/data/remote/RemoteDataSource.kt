package com.harifrizki.core.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harifrizki.core.data.remote.response.*
import com.harifrizki.core.data.remote.response.ErrorResponse.Companion.errorResponse
import com.harifrizki.core.model.*
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ResponseStatus.EMPTY
import com.harifrizki.core.utils.ResponseStatus.ERROR
import com.harifrizki.core.utils.CRUD.*
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
        return response(
            NetworkApi.connectToApi().handshake(),
            HandshakeResponse()
        )
    }

    override fun login(admin: Admin?): LiveData<ApiResource<LoginResponse>> {
        return response(
            NetworkApi.connectToApi().login(Admin.jsonObject(admin)),
            LoginResponse()
        )
    }

    override fun logout(admin: Admin?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().logout(Admin.jsonObject(admin)),
            MessageResponse()
        )
    }

    override fun utilization(): LiveData<ApiResource<UtilizationResponse>> {
        return response(
            NetworkApi.connectToApi().utilization(),
            UtilizationResponse()
        )
    }

    override fun admin(pageNo: String?, admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().admin(
                PARAM_NAME,
                pageNo!!,
                Admin.jsonObject(admin)
            ),
            AdminResponse()
        )
    }

    override fun adminDetail(adminId: String?): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().adminDetail(adminId!!),
            AdminResponse()
        )
    }

    override fun adminAdd(
        admin: Admin?,
        photoProfile: File?
    ): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().adminAdd(
                toRequestBody(
                    Admin.jsonObject(admin).toString(),
                    "multipart/form-data"
                ),
                toMultipartBody(
                    photoProfile,
                    "adminPhotoProfile",
                    "multipart/form-data"
                )
            ),
            AdminResponse()
        )
    }

    override fun adminUpdate(admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().adminUpdate(Admin.jsonObject(admin)),
            AdminResponse()
        )
    }

    override fun adminUpdatePhotoProfile(
        admin: Admin?,
        photoProfile: File?
    ): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().adminUpdatePhotoProfile(
                toRequestBody(
                    Admin.jsonObject(admin).toString(),
                    "multipart/form-data"
                ),
                toMultipartBody(
                    photoProfile,
                    "adminPhotoProfile",
                    "multipart/form-data"
                )
            ),
            AdminResponse()
        )
    }

    override fun adminUpdatePassword(
        adminId: String?,
        oldPassword: String?,
        newPassword: String?
    ): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().adminUpdatePassword(
                adminId!!,
                oldPassword!!,
                newPassword!!
            ),
            AdminResponse()
        )
    }

    override fun adminResetPassword(admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().adminResetPassword(Admin.jsonObject(admin)),
            AdminResponse()
        )
    }

    override fun adminUpdateActive(admin: Admin?): LiveData<ApiResource<AdminResponse>> {
        return response(
            NetworkApi.connectToApi().adminUpdateActive(Admin.jsonObject(admin)),
            AdminResponse()
        )
    }

    override fun adminDelete(admin: Admin?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().adminDelete(Admin.jsonObject(admin)),
            MessageResponse()
        )
    }

    override fun province(
        pageNo: String?,
        province: Province?
    ): LiveData<ApiResource<ProvinceResponse>> {
        return response(
            NetworkApi.connectToApi().province(
                PARAM_NAME,
                pageNo!!,
                Province.jsonObject(province, READ, PARAM_NAME)
            ),
            ProvinceResponse()
        )
    }

    override fun provinceDetail(province: Province?): LiveData<ApiResource<ProvinceResponse>> {
        return response(
            NetworkApi.connectToApi().provinceDetail(
                Province.jsonObject(province, READ, ID)
            ),
            ProvinceResponse()
        )
    }

    override fun provinceAdd(province: Province?): LiveData<ApiResource<ProvinceResponse>> {
        return response(
            NetworkApi.connectToApi().provinceAdd(
                Province.jsonObject(province, CREATE)
            ),
            ProvinceResponse()
        )
    }

    override fun provinceUpdate(province: Province?): LiveData<ApiResource<ProvinceResponse>> {
        return response(
            NetworkApi.connectToApi().provinceUpdate(
                Province.jsonObject(province, CRUD.UPDATE)
            ),
            ProvinceResponse()
        )
    }

    override fun provinceDelete(province: Province?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().provinceDelete(
                Province.jsonObject(province, CRUD.DELETE)
            ),
            MessageResponse()
        )
    }

    override fun city(pageNo: String?, city: City?): LiveData<ApiResource<CityResponse>> {
        return response(
            NetworkApi.connectToApi().city(
                PARAM_NAME,
                pageNo!!,
                City.jsonObject(city, READ, PARAM_NAME)
            ),
            CityResponse()
        )
    }

    override fun cityByProvince(pageNo: String?, city: City?): LiveData<ApiResource<CityResponse>> {
        return response(
            NetworkApi.connectToApi().city(
                PARAM_PROVINCE_ID,
                pageNo!!,
                City.jsonObject(city, READ, PARAM_PROVINCE_ID)
            ),
            CityResponse()
        )
    }

    override fun cityDetail(city: City?): LiveData<ApiResource<CityResponse>> {
        return response(
            NetworkApi.connectToApi().cityDetail(
                City.jsonObject(city, READ, ID)
            ),
            CityResponse()
        )
    }

    override fun cityAdd(city: City?): LiveData<ApiResource<CityResponse>> {
        return response(
            NetworkApi.connectToApi().cityAdd(
                City.jsonObject(city, CREATE)
            ),
            CityResponse()
        )
    }

    override fun cityUpdate(city: City?): LiveData<ApiResource<CityResponse>> {
        return response(
            NetworkApi.connectToApi().cityUpdate(
                City.jsonObject(city, CRUD.UPDATE)
            ),
            CityResponse()
        )
    }

    override fun cityDelete(city: City?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().cityDelete(
                City.jsonObject(city, CRUD.DELETE)
            ),
            MessageResponse()
        )
    }

    override fun subDistrict(
        pageNo: String?,
        subDistrict: SubDistrict?
    ): LiveData<ApiResource<SubDistrictResponse>> {
        return response(
            NetworkApi.connectToApi().subDistrict(
                PARAM_NAME,
                pageNo!!,
                SubDistrict.jsonObject(subDistrict, READ, PARAM_NAME)
            ),
            SubDistrictResponse()
        )
    }

    override fun subDistrictByProvince(
        pageNo: String?,
        subDistrict: SubDistrict?
    ): LiveData<ApiResource<SubDistrictResponse>> {
        return response(
            NetworkApi.connectToApi().subDistrict(
                PARAM_PROVINCE_ID,
                pageNo!!,
                SubDistrict.jsonObject(subDistrict, READ, PARAM_PROVINCE_ID)
            ),
            SubDistrictResponse()
        )
    }

    override fun subDistrictByCity(
        pageNo: String?,
        subDistrict: SubDistrict?
    ): LiveData<ApiResource<SubDistrictResponse>> {
        return response(
            NetworkApi.connectToApi().subDistrict(
                PARAM_CITY_ID,
                pageNo!!,
                SubDistrict.jsonObject(subDistrict, READ, PARAM_CITY_ID)
            ),
            SubDistrictResponse()
        )
    }

    override fun subDistrictDetail(subDistrict: SubDistrict?): LiveData<ApiResource<SubDistrictResponse>> {
        return response(
            NetworkApi.connectToApi().subDistrictDetail(
                SubDistrict.jsonObject(subDistrict, READ, ID)
            ),
            SubDistrictResponse()
        )
    }

    override fun subDistrictAdd(subDistrict: SubDistrict?): LiveData<ApiResource<SubDistrictResponse>> {
        return response(
            NetworkApi.connectToApi().subDistrictAdd(
                SubDistrict.jsonObject(subDistrict, CREATE)
            ),
            SubDistrictResponse()
        )
    }

    override fun subDistrictUpdate(subDistrict: SubDistrict?): LiveData<ApiResource<SubDistrictResponse>> {
        return response(
            NetworkApi.connectToApi().subDistrictUpdate(
                SubDistrict.jsonObject(subDistrict, CRUD.UPDATE)
            ),
            SubDistrictResponse()
        )
    }

    override fun subDistrictDelete(subDistrict: SubDistrict?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().subDistrictDelete(
                SubDistrict.jsonObject(subDistrict, CRUD.DELETE)
            ),
            MessageResponse()
        )
    }

    override fun urbanVillage(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<ApiResource<UrbanVillageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillage(
                PARAM_NAME,
                pageNo!!,
                UrbanVillage.jsonObject(urbanVillage, READ, PARAM_NAME)
            ),
            UrbanVillageResponse()
        )
    }

    override fun urbanVillageByProvince(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<ApiResource<UrbanVillageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillage(
                PARAM_PROVINCE_ID,
                pageNo!!,
                UrbanVillage.jsonObject(urbanVillage, READ, PARAM_PROVINCE_ID)
            ),
            UrbanVillageResponse()
        )
    }

    override fun urbanVillageByCity(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<ApiResource<UrbanVillageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillage(
                PARAM_CITY_ID,
                pageNo!!,
                UrbanVillage.jsonObject(urbanVillage, READ, PARAM_CITY_ID)
            ),
            UrbanVillageResponse()
        )
    }

    override fun urbanVillageBySubDistrict(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<ApiResource<UrbanVillageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillage(
                PARAM_SUB_DISTRICT_ID,
                pageNo!!,
                UrbanVillage.jsonObject(urbanVillage, READ, PARAM_SUB_DISTRICT_ID)
            ),
            UrbanVillageResponse()
        )
    }

    override fun urbanVillageDetail(urbanVillage: UrbanVillage?): LiveData<ApiResource<UrbanVillageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillageDetail(
                UrbanVillage.jsonObject(urbanVillage, READ, ID)
            ),
            UrbanVillageResponse()
        )
    }

    override fun urbanVillageAdd(urbanVillage: UrbanVillage?): LiveData<ApiResource<UrbanVillageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillageAdd(
                UrbanVillage.jsonObject(urbanVillage, CREATE)
            ),
            UrbanVillageResponse()
        )
    }

    override fun urbanVillageUpdate(urbanVillage: UrbanVillage?): LiveData<ApiResource<UrbanVillageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillageUpdate(
                UrbanVillage.jsonObject(urbanVillage, CRUD.UPDATE)
            ),
            UrbanVillageResponse()
        )
    }

    override fun urbanVillageDelete(urbanVillage: UrbanVillage?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().urbanVillageDelete(
                UrbanVillage.jsonObject(urbanVillage, CRUD.DELETE)
            ),
            MessageResponse()
        )
    }

    override fun crimeLocation(
        pageNo: String?,
        crimeLocation: CrimeLocation?
    ): LiveData<ApiResource<CrimeLocationResponse>> {
        return response(
            NetworkApi.connectToApi().crimeLocation(
                PARAM_NAME,
                pageNo!!,
                CrimeLocation.jsonObject(crimeLocation, READ, PARAM_NAME)
            ),
            CrimeLocationResponse()
        )
    }

    override fun crimeLocationDetail(crimeLocation: CrimeLocation?): LiveData<ApiResource<CrimeLocationResponse>> {
        return response(
            NetworkApi.connectToApi().crimeLocationDetail(
                CrimeLocation.jsonObject(crimeLocation, READ, ID)
            ),
            CrimeLocationResponse()
        )
    }

    override fun crimeLocationAdd(
        crimeLocation: CrimeLocation?,
        photoCrimeLocation: ArrayList<File>?
    ): LiveData<ApiResource<CrimeLocationResponse>> {
        return response(
            NetworkApi.connectToApi().crimeLocationAdd(
                toRequestBody(
                    CrimeLocation.jsonObject(crimeLocation, CREATE).toString(),
                    "multipart/form-data"
                ),
                toMultipartBody(
                    photoCrimeLocation,
                    "crimeLocationPhoto",
                    "multipart/form-data")
            ),
            CrimeLocationResponse()
        )
    }

    override fun crimeLocationAddImage(
        crimeLocation: CrimeLocation?,
        photoCrimeLocation: ArrayList<File>?
    ): LiveData<ApiResource<CrimeLocationResponse>> {
        return response(
            NetworkApi.connectToApi().crimeLocationAdd(
                toRequestBody(
                    CrimeLocation.jsonObject(crimeLocation, CREATE).toString(),
                    "multipart/form-data"
                ),
                toMultipartBody(
                    photoCrimeLocation,
                    "crimeLocationPhoto",
                    "multipart/form-data")
            ),
            CrimeLocationResponse()
        )
    }

    override fun crimeLocationUpdate(crimeLocation: CrimeLocation?): LiveData<ApiResource<CrimeLocationResponse>> {
        return response(
            NetworkApi.connectToApi().crimeLocationUpdate(
                CrimeLocation.jsonObject(crimeLocation, CRUD.UPDATE)
            ),
            CrimeLocationResponse()
        )
    }

    override fun crimeLocationDeleteImage(crimeLocation: CrimeLocation?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().crimeLocationDeleteImage(
                CrimeLocation.jsonObject(crimeLocation, CRUD.DELETE)
            ),
            MessageResponse()
        )
    }

    override fun crimeLocationDelete(crimeLocation: CrimeLocation?): LiveData<ApiResource<MessageResponse>> {
        return response(
            NetworkApi.connectToApi().crimeLocationDelete(
                CrimeLocation.jsonObject(crimeLocation, CRUD.DELETE)
            ),
            MessageResponse()
        )
    }

    private fun <T> response(client: Call<T>, modelResponse: T):
            MutableLiveData<ApiResource<T>> {
        val result = MutableLiveData<ApiResource<T>>()
        try {
            client.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    convertResponse(response, modelResponse, result)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    convertResponse(modelResponse, result, t, ERROR)
                }
            })
        } catch (e: Exception) {
            convertResponse(modelResponse, result, e, EMPTY)
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