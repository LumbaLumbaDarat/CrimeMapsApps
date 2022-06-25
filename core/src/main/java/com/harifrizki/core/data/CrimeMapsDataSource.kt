package com.harifrizki.core.data

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.remote.response.*
import com.harifrizki.core.model.*
import com.harifrizki.core.utils.DataResource
import java.io.File

interface CrimeMapsDataSource {
    fun handshake(): LiveData<DataResource<HandshakeResponse>>
    fun login(admin: Admin?): LiveData<DataResource<LoginResponse>>
    fun logout(admin: Admin?): LiveData<DataResource<MessageResponse>>
    fun utilization(): LiveData<DataResource<UtilizationResponse>>

    fun admin(pageNo: String?, admin: Admin?): LiveData<DataResource<AdminResponse>>
    fun adminDetail(adminId: String?): LiveData<DataResource<AdminResponse>>
    fun adminAdd(admin: Admin?, photoProfile: File?): LiveData<DataResource<AdminResponse>>
    fun adminUpdate(admin: Admin?): LiveData<DataResource<AdminResponse>>
    fun adminUpdatePhotoProfile(admin: Admin?, photoProfile: File?): LiveData<DataResource<AdminResponse>>
    fun adminUpdatePassword(adminId: String?, oldPassword: String?, newPassword: String?): LiveData<DataResource<AdminResponse>>
    fun adminResetPassword(admin: Admin?): LiveData<DataResource<AdminResponse>>
    fun adminUpdateActive(admin: Admin?): LiveData<DataResource<AdminResponse>>
    fun adminDelete(admin: Admin?): LiveData<DataResource<MessageResponse>>

    fun province(pageNo: String?, province: Province?): LiveData<DataResource<ProvinceResponse>>
    fun provinceDetail(province: Province?): LiveData<DataResource<ProvinceResponse>>
    fun provinceAdd(province: Province?): LiveData<DataResource<ProvinceResponse>>
    fun provinceUpdate(province: Province?): LiveData<DataResource<ProvinceResponse>>
    fun provinceDelete(province: Province?): LiveData<DataResource<MessageResponse>>

    fun city(pageNo: String?, city: City?): LiveData<DataResource<CityResponse>>
    fun cityByProvince(pageNo: String?, city: City?): LiveData<DataResource<CityResponse>>
    fun cityDetail(city: City?): LiveData<DataResource<CityResponse>>
    fun cityAdd(city: City?): LiveData<DataResource<CityResponse>>
    fun cityUpdate(city: City?): LiveData<DataResource<CityResponse>>
    fun cityDelete(city: City?): LiveData<DataResource<MessageResponse>>

    fun subDistrict(pageNo: String?, subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>>
    fun subDistrictByProvince(pageNo: String?, subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>>
    fun subDistrictByCity(pageNo: String?, subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>>
    fun subDistrictDetail(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>>
    fun subDistrictAdd(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>>
    fun subDistrictUpdate(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>>
    fun subDistrictDelete(subDistrict: SubDistrict?): LiveData<DataResource<MessageResponse>>

    fun urbanVillage(pageNo: String?, urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>>
    fun urbanVillageByProvince(pageNo: String?, urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>>
    fun urbanVillageByCity(pageNo: String?, urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>>
    fun urbanVillageBySubDistrict(pageNo: String?, urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>>
    fun urbanVillageDetail(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>>
    fun urbanVillageAdd(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>>
    fun urbanVillageUpdate(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>>
    fun urbanVillageDelete(urbanVillage: UrbanVillage?): LiveData<DataResource<MessageResponse>>

    fun crimeLocation(pageNo: String?, crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>>
    fun crimeLocationByNearestLocation(pageNo: String?, crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>>
    fun crimeLocationDetail(crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>>
    fun crimeLocationAdd(crimeLocation: CrimeLocation?, photoCrimeLocation: ArrayList<File>?): LiveData<DataResource<CrimeLocationResponse>>
    fun crimeLocationAddImage(crimeLocation: CrimeLocation?, photoCrimeLocation: ArrayList<File>?): LiveData<DataResource<CrimeLocationResponse>>
    fun crimeLocationUpdate(crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>>
    fun crimeLocationDeleteImage(crimeLocation: CrimeLocation?): LiveData<DataResource<MessageResponse>>
    fun crimeLocationDelete(crimeLocation: CrimeLocation?): LiveData<DataResource<MessageResponse>>
}