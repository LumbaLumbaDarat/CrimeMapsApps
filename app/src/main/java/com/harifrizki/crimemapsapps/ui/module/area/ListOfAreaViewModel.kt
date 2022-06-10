package com.harifrizki.crimemapsapps.ui.module.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.City
import com.harifrizki.crimemapsapps.model.Province
import com.harifrizki.crimemapsapps.model.SubDistrict
import com.harifrizki.crimemapsapps.model.UrbanVillage
import com.harifrizki.crimemapsapps.utils.DataResource

class ListOfAreaViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun province(pageNo: String?, province: Province?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.province(pageNo, province)
    fun provinceDelete(province: Province?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.provinceDelete(province)
    fun city(pageNo: String?, city: City?): LiveData<DataResource<CityResponse>> =
        crimeMapsRepository.city(pageNo, city)
    fun cityByProvince(pageNo: String?, city: City?): LiveData<DataResource<CityResponse>> =
        crimeMapsRepository.cityByProvince(pageNo, city)
    fun cityDelete(city: City?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.cityDelete(city)
    fun subDistrict(pageNo: String?, subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> =
        crimeMapsRepository.subDistrict(pageNo, subDistrict)
    fun subDistrictByCity(pageNo: String?, subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> =
        crimeMapsRepository.subDistrictByCity(pageNo, subDistrict)
    fun subDistrictDelete(subDistrict: SubDistrict?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.subDistrictDelete(subDistrict)
    fun urbanVillage(pageNo: String?, urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> =
        crimeMapsRepository.urbanVillage(pageNo, urbanVillage)
    fun urbanVillageBySubDistrict(pageNo: String?, urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> =
        crimeMapsRepository.urbanVillageBySubDistrict(pageNo, urbanVillage)
    fun urbanVillageDelete(urbanVillage: UrbanVillage?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.urbanVillageDelete(urbanVillage)
}