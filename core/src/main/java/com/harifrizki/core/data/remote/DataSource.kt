package com.harifrizki.core.data.remote

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.remote.response.*
import com.harifrizki.core.model.*
import com.harifrizki.core.utils.ApiResource
import java.io.File

interface DataSource {
    fun handshake():
            LiveData<ApiResource<HandshakeResponse>>

    fun login(admin: Admin?):
            LiveData<ApiResource<LoginResponse>>

    fun logout(admin: Admin?):
            LiveData<ApiResource<MessageResponse>>

    fun utilization():
            LiveData<ApiResource<UtilizationResponse>>

    fun admin(pageNo: String?, admin: Admin?):
            LiveData<ApiResource<AdminResponse>>

    fun adminDetail(adminId: String?):
            LiveData<ApiResource<AdminResponse>>

    fun adminAdd(
        admin: Admin?,
        photoProfile: File?
    ):
            LiveData<ApiResource<AdminResponse>>

    fun adminUpdate(admin: Admin?):
            LiveData<ApiResource<AdminResponse>>

    fun adminUpdatePhotoProfile(
        admin: Admin?,
        photoProfile: File?
    ):
            LiveData<ApiResource<AdminResponse>>

    fun adminUpdatePassword(
        adminId: String?,
        oldPassword: String?,
        newPassword: String?
    ):
            LiveData<ApiResource<AdminResponse>>

    fun adminResetPassword(admin: Admin?):
            LiveData<ApiResource<AdminResponse>>

    fun adminUpdateActive(admin: Admin?):
            LiveData<ApiResource<AdminResponse>>

    fun adminDelete(admin: Admin?):
            LiveData<ApiResource<MessageResponse>>

    fun province(pageNo: String?, province: Province?):
            LiveData<ApiResource<ProvinceResponse>>

    fun provinceDetail(province: Province?):
            LiveData<ApiResource<ProvinceResponse>>

    fun provinceAdd(province: Province?):
            LiveData<ApiResource<ProvinceResponse>>

    fun provinceUpdate(province: Province?):
            LiveData<ApiResource<ProvinceResponse>>

    fun provinceDelete(province: Province?):
            LiveData<ApiResource<MessageResponse>>

    fun city(pageNo: String?, city: City?):
            LiveData<ApiResource<CityResponse>>

    fun cityByProvince(pageNo: String?, city: City?):
            LiveData<ApiResource<CityResponse>>

    fun cityDetail(city: City?):
            LiveData<ApiResource<CityResponse>>

    fun cityAdd(city: City?):
            LiveData<ApiResource<CityResponse>>

    fun cityUpdate(city: City?):
            LiveData<ApiResource<CityResponse>>

    fun cityDelete(city: City?):
            LiveData<ApiResource<MessageResponse>>

    fun subDistrict(pageNo: String?, subDistrict: SubDistrict?):
            LiveData<ApiResource<SubDistrictResponse>>

    fun subDistrictByProvince(pageNo: String?, subDistrict: SubDistrict?):
            LiveData<ApiResource<SubDistrictResponse>>

    fun subDistrictByCity(pageNo: String?, subDistrict: SubDistrict?):
            LiveData<ApiResource<SubDistrictResponse>>

    fun subDistrictDetail(subDistrict: SubDistrict?):
            LiveData<ApiResource<SubDistrictResponse>>

    fun subDistrictAdd(subDistrict: SubDistrict?):
            LiveData<ApiResource<SubDistrictResponse>>

    fun subDistrictUpdate(subDistrict: SubDistrict?):
            LiveData<ApiResource<SubDistrictResponse>>

    fun subDistrictDelete(subDistrict: SubDistrict?):
            LiveData<ApiResource<MessageResponse>>

    fun urbanVillage(pageNo: String?, urbanVillage: UrbanVillage?):
            LiveData<ApiResource<UrbanVillageResponse>>

    fun urbanVillageByProvince(pageNo: String?, urbanVillage: UrbanVillage?):
            LiveData<ApiResource<UrbanVillageResponse>>

    fun urbanVillageByCity(pageNo: String?, urbanVillage: UrbanVillage?):
            LiveData<ApiResource<UrbanVillageResponse>>

    fun urbanVillageBySubDistrict(pageNo: String?, urbanVillage: UrbanVillage?):
            LiveData<ApiResource<UrbanVillageResponse>>

    fun urbanVillageDetail(urbanVillage: UrbanVillage?):
            LiveData<ApiResource<UrbanVillageResponse>>

    fun urbanVillageAdd(urbanVillage: UrbanVillage?):
            LiveData<ApiResource<UrbanVillageResponse>>

    fun urbanVillageUpdate(urbanVillage: UrbanVillage?):
            LiveData<ApiResource<UrbanVillageResponse>>

    fun urbanVillageDelete(urbanVillage: UrbanVillage?):
            LiveData<ApiResource<MessageResponse>>

    fun crimeLocation(pageNo: String?, crimeLocation: CrimeLocation?):
            LiveData<ApiResource<CrimeLocationResponse>>

    fun crimeLocationDetail(crimeLocation: CrimeLocation?):
            LiveData<ApiResource<CrimeLocationResponse>>

    fun crimeLocationAdd(
        crimeLocation: CrimeLocation?,
        photoCrimeLocation: ArrayList<File>?
    ):
            LiveData<ApiResource<CrimeLocationResponse>>

    fun crimeLocationAddImage(
        crimeLocation: CrimeLocation?,
        photoCrimeLocation: ArrayList<File>?
    ):
            LiveData<ApiResource<CrimeLocationResponse>>

    fun crimeLocationUpdate(crimeLocation: CrimeLocation?):
            LiveData<ApiResource<CrimeLocationResponse>>

    fun crimeLocationDeleteImage(crimeLocation: CrimeLocation?):
            LiveData<ApiResource<MessageResponse>>

    fun crimeLocationDelete(crimeLocation: CrimeLocation?):
            LiveData<ApiResource<MessageResponse>>
}