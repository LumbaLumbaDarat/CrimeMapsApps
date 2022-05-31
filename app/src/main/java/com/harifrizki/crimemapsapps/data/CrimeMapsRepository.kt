package com.harifrizki.crimemapsapps.data

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.*
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.ApiResource
import com.harifrizki.crimemapsapps.utils.AppExecutor
import com.harifrizki.crimemapsapps.utils.DataResource
import java.io.File

class CrimeMapsRepository(
    private val remote: RemoteDataSource,
    private val executor: AppExecutor
) : CrimeMapsDataSource {
    companion object {
        @Volatile
        private var instance: CrimeMapsRepository? = null
        fun getInstance(
            remoteData: RemoteDataSource,
            appExecutor: AppExecutor
        ): CrimeMapsRepository =
            instance ?: synchronized(this) {
                instance ?: CrimeMapsRepository(remoteData, appExecutor)
            }
    }

    override fun handshake(): LiveData<DataResource<HandshakeResponse>> {
        return object : NetworkResource<HandshakeResponse>() {
            override fun createCall(): LiveData<ApiResource<HandshakeResponse>> =
                remote.handshake()
        }.asLiveData()
    }

    override fun login(admin: Admin?): LiveData<DataResource<LoginResponse>> {
        return object : NetworkResource<LoginResponse>() {
            override fun createCall(): LiveData<ApiResource<LoginResponse>> =
                remote.login(admin)
        }.asLiveData()
    }

    override fun logout(admin: Admin?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.logout(admin)
        }.asLiveData()
    }

    override fun utilization(): LiveData<DataResource<UtilizationResponse>> {
        return object : NetworkResource<UtilizationResponse>() {
            override fun createCall(): LiveData<ApiResource<UtilizationResponse>> =
                remote.utilization()
        }.asLiveData()
    }

    override fun adminByName(
        pageNo: String?,
        name: String?
    ): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminByName(pageNo, name)
        }.asLiveData()
    }

    override fun adminById(adminId: String?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminById(adminId)
        }.asLiveData()
    }

    override fun adminAdd(
        admin: Admin?,
        photoProfile: File?
    ): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminAdd(admin, photoProfile)
        }.asLiveData()
    }

    override fun adminUpdate(admin: Admin?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminUpdate(admin)
        }.asLiveData()
    }

    override fun adminUpdatePhotoProfile(
        admin: Admin?,
        photoProfile: File?
    ): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminUpdatePhotoProfile(admin, photoProfile)
        }.asLiveData()
    }

    override fun adminUpdatePassword(
        adminId: String?,
        oldPassword: String?,
        newPassword: String?
    ): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminUpdatePassword(adminId, oldPassword, newPassword)
        }.asLiveData()
    }

    override fun adminResetPassword(admin: Admin?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminResetPassword(admin)
        }.asLiveData()
    }

    override fun adminUpdateActive(admin: Admin?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminUpdateActive(admin)
        }.asLiveData()
    }

    override fun adminDelete(admin: Admin?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.adminDelete(admin)
        }.asLiveData()
    }

    override fun provinceByName(
        pageNo: String?,
        name: String?
    ): LiveData<DataResource<ProvinceResponse>> {
        return object : NetworkResource<ProvinceResponse>() {
            override fun createCall(): LiveData<ApiResource<ProvinceResponse>> =
                remote.provinceByName(pageNo, name)
        }.asLiveData()
    }

    override fun cityByName(pageNo: String?, name: String?): LiveData<DataResource<CityResponse>> {
        return object : NetworkResource<CityResponse>() {
            override fun createCall(): LiveData<ApiResource<CityResponse>> =
                remote.cityByName(pageNo, name)
        }.asLiveData()
    }

    override fun subDistrictByName(
        pageNo: String?,
        name: String?
    ): LiveData<DataResource<SubDistrictResponse>> {
        return object : NetworkResource<SubDistrictResponse>() {
            override fun createCall(): LiveData<ApiResource<SubDistrictResponse>> =
                remote.subDistrictByName(pageNo, name)
        }.asLiveData()
    }

    override fun urbanVillageByName(
        pageNo: String?,
        name: String?
    ): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillageByName(pageNo, name)
        }.asLiveData()
    }
}