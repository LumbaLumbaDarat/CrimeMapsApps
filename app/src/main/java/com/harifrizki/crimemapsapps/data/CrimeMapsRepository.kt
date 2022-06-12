package com.harifrizki.crimemapsapps.data

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.*
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.*
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

    override fun admin(pageNo: String?, admin: Admin?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.admin(pageNo, admin)
        }.asLiveData()
    }

    override fun adminDetail(adminId: String?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminDetail(adminId)
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

    override fun province(
        pageNo: String?,
        province: Province?
    ): LiveData<DataResource<ProvinceResponse>> {
        return object : NetworkResource<ProvinceResponse>() {
            override fun createCall(): LiveData<ApiResource<ProvinceResponse>> =
                remote.province(pageNo, province)
        }.asLiveData()
    }

    override fun provinceDetail(province: Province?): LiveData<DataResource<ProvinceResponse>> {
        return object : NetworkResource<ProvinceResponse>() {
            override fun createCall(): LiveData<ApiResource<ProvinceResponse>> =
                remote.provinceDetail(province)
        }.asLiveData()
    }

    override fun provinceAdd(province: Province?): LiveData<DataResource<ProvinceResponse>> {
        return object : NetworkResource<ProvinceResponse>() {
            override fun createCall(): LiveData<ApiResource<ProvinceResponse>> =
                remote.provinceAdd(province)
        }.asLiveData()
    }

    override fun provinceUpdate(province: Province?): LiveData<DataResource<ProvinceResponse>> {
        return object : NetworkResource<ProvinceResponse>() {
            override fun createCall(): LiveData<ApiResource<ProvinceResponse>> =
                remote.provinceUpdate(province)
        }.asLiveData()
    }

    override fun provinceDelete(province: Province?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.provinceDelete(province)
        }.asLiveData()
    }

    override fun city(pageNo: String?, city: City?): LiveData<DataResource<CityResponse>> {
        return object : NetworkResource<CityResponse>() {
            override fun createCall(): LiveData<ApiResource<CityResponse>> =
                remote.city(pageNo, city)
        }.asLiveData()
    }

    override fun cityByProvince(
        pageNo: String?,
        city: City?
    ): LiveData<DataResource<CityResponse>> {
        return object : NetworkResource<CityResponse>() {
            override fun createCall(): LiveData<ApiResource<CityResponse>> =
                remote.cityByProvince(pageNo, city)
        }.asLiveData()
    }

    override fun cityDetail(city: City?): LiveData<DataResource<CityResponse>> {
        return object : NetworkResource<CityResponse>() {
            override fun createCall(): LiveData<ApiResource<CityResponse>> =
                remote.cityDetail(city)
        }.asLiveData()
    }

    override fun cityAdd(city: City?): LiveData<DataResource<CityResponse>> {
        return object : NetworkResource<CityResponse>() {
            override fun createCall(): LiveData<ApiResource<CityResponse>> =
                remote.cityAdd(city)
        }.asLiveData()
    }

    override fun cityUpdate(city: City?): LiveData<DataResource<CityResponse>> {
        return object : NetworkResource<CityResponse>() {
            override fun createCall(): LiveData<ApiResource<CityResponse>> =
                remote.cityUpdate(city)
        }.asLiveData()
    }

    override fun cityDelete(city: City?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.cityDelete(city)
        }.asLiveData()
    }

    override fun subDistrict(
        pageNo: String?,
        subDistrict: SubDistrict?
    ): LiveData<DataResource<SubDistrictResponse>> {
        return object : NetworkResource<SubDistrictResponse>() {
            override fun createCall(): LiveData<ApiResource<SubDistrictResponse>> =
                remote.subDistrict(pageNo, subDistrict)
        }.asLiveData()
    }

    override fun subDistrictByProvince(
        pageNo: String?,
        subDistrict: SubDistrict?
    ): LiveData<DataResource<SubDistrictResponse>> {
        return object : NetworkResource<SubDistrictResponse>() {
            override fun createCall(): LiveData<ApiResource<SubDistrictResponse>> =
                remote.subDistrictByProvince(pageNo, subDistrict)
        }.asLiveData()
    }

    override fun subDistrictByCity(
        pageNo: String?,
        subDistrict: SubDistrict?
    ): LiveData<DataResource<SubDistrictResponse>> {
        return object : NetworkResource<SubDistrictResponse>() {
            override fun createCall(): LiveData<ApiResource<SubDistrictResponse>> =
                remote.subDistrictByCity(pageNo, subDistrict)
        }.asLiveData()
    }

    override fun subDistrictDetail(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> {
        return object : NetworkResource<SubDistrictResponse>() {
            override fun createCall(): LiveData<ApiResource<SubDistrictResponse>> =
                remote.subDistrictDetail(subDistrict)
        }.asLiveData()
    }

    override fun subDistrictAdd(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> {
        return object : NetworkResource<SubDistrictResponse>() {
            override fun createCall(): LiveData<ApiResource<SubDistrictResponse>> =
                remote.subDistrictAdd(subDistrict)
        }.asLiveData()
    }

    override fun subDistrictUpdate(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> {
        return object : NetworkResource<SubDistrictResponse>() {
            override fun createCall(): LiveData<ApiResource<SubDistrictResponse>> =
                remote.subDistrictUpdate(subDistrict)
        }.asLiveData()
    }

    override fun subDistrictDelete(subDistrict: SubDistrict?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.subDistrictDelete(subDistrict)
        }.asLiveData()
    }

    override fun urbanVillage(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillage(pageNo, urbanVillage)
        }.asLiveData()
    }

    override fun urbanVillageByProvince(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillageByProvince(pageNo, urbanVillage)
        }.asLiveData()
    }

    override fun urbanVillageByCity(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillageByCity(pageNo, urbanVillage)
        }.asLiveData()
    }

    override fun urbanVillageBySubDistrict(
        pageNo: String?,
        urbanVillage: UrbanVillage?
    ): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillageBySubDistrict(pageNo, urbanVillage)
        }.asLiveData()
    }

    override fun urbanVillageDetail(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillageDetail(urbanVillage)
        }.asLiveData()
    }

    override fun urbanVillageAdd(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillageAdd(urbanVillage)
        }.asLiveData()
    }

    override fun urbanVillageUpdate(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> {
        return object : NetworkResource<UrbanVillageResponse>() {
            override fun createCall(): LiveData<ApiResource<UrbanVillageResponse>> =
                remote.urbanVillageUpdate(urbanVillage)
        }.asLiveData()
    }

    override fun urbanVillageDelete(urbanVillage: UrbanVillage?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.urbanVillageDelete(urbanVillage)
        }.asLiveData()
    }

    override fun crimeLocation(
        pageNo: String?,
        crimeLocation: CrimeLocation?
    ): LiveData<DataResource<CrimeLocationResponse>> {
        return object : NetworkResource<CrimeLocationResponse>() {
            override fun createCall(): LiveData<ApiResource<CrimeLocationResponse>> =
                remote.crimeLocation(pageNo, crimeLocation)
        }.asLiveData()
    }

    override fun crimeLocationDetail(crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>> {
        return object : NetworkResource<CrimeLocationResponse>() {
            override fun createCall(): LiveData<ApiResource<CrimeLocationResponse>> =
                remote.crimeLocationDetail(crimeLocation)
        }.asLiveData()
    }

    override fun crimeLocationAdd(
        crimeLocation: CrimeLocation?,
        photoCrimeLocation: ArrayList<File>?
    ): LiveData<DataResource<CrimeLocationResponse>> {
        return object : NetworkResource<CrimeLocationResponse>() {
            override fun createCall(): LiveData<ApiResource<CrimeLocationResponse>> =
                remote.crimeLocationAdd(crimeLocation, photoCrimeLocation)
        }.asLiveData()
    }

    override fun crimeLocationAddImage(
        crimeLocation: CrimeLocation?,
        photoCrimeLocation: ArrayList<File>?
    ): LiveData<DataResource<CrimeLocationResponse>> {
        return object : NetworkResource<CrimeLocationResponse>() {
            override fun createCall(): LiveData<ApiResource<CrimeLocationResponse>> =
                remote.crimeLocationAddImage(crimeLocation, photoCrimeLocation)
        }.asLiveData()
    }

    override fun crimeLocationUpdate(crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>> {
        return object : NetworkResource<CrimeLocationResponse>() {
            override fun createCall(): LiveData<ApiResource<CrimeLocationResponse>> =
                remote.crimeLocationUpdate(crimeLocation)
        }.asLiveData()
    }

    override fun crimeLocationDeleteImage(crimeLocation: CrimeLocation?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.crimeLocationDeleteImage(crimeLocation)
        }.asLiveData()
    }

    override fun crimeLocationDelete(crimeLocation: CrimeLocation?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.crimeLocationDelete(crimeLocation)
        }.asLiveData()
    }
}