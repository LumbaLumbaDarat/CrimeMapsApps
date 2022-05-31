package com.harifrizki.crimemapsapps.data

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.ApiResource
import com.harifrizki.crimemapsapps.utils.DataResource
import java.io.File

interface CrimeMapsDataSource {
    fun handshake(): LiveData<DataResource<HandshakeResponse>>
    fun login(admin: Admin?): LiveData<DataResource<LoginResponse>>
    fun logout(admin: Admin?): LiveData<DataResource<MessageResponse>>
    fun utilization(): LiveData<DataResource<UtilizationResponse>>

    fun adminByName(pageNo: String?, name: String?): LiveData<DataResource<AdminResponse>>
    fun adminById(adminId: String?): LiveData<DataResource<AdminResponse>>
    fun adminAdd(admin: Admin?, photoProfile: File?): LiveData<DataResource<AdminResponse>>
    fun adminUpdate(admin: Admin?): LiveData<DataResource<AdminResponse>>
    fun adminUpdatePhotoProfile(admin: Admin?, photoProfile: File?): LiveData<DataResource<AdminResponse>>
    fun adminUpdatePassword(adminId: String?, oldPassword: String?, newPassword: String?): LiveData<DataResource<AdminResponse>>
    fun adminResetPassword(admin: Admin?): LiveData<DataResource<AdminResponse>>
    fun adminUpdateActive(admin: Admin?): LiveData<DataResource<AdminResponse>>
    fun adminDelete(admin: Admin?): LiveData<DataResource<MessageResponse>>

    fun provinceByName(pageNo: String?, name: String?): LiveData<DataResource<ProvinceResponse>>

    fun cityByName(pageNo: String?, name: String?): LiveData<DataResource<CityResponse>>

    fun subDistrictByName(pageNo: String?, name: String?): LiveData<DataResource<SubDistrictResponse>>

    fun urbanVillageByName(pageNo: String?, name: String?): LiveData<DataResource<UrbanVillageResponse>>
}