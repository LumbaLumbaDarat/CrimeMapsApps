package com.harifrizki.crimemapsapps.ui.module.formarea

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.City
import com.harifrizki.crimemapsapps.model.Province
import com.harifrizki.crimemapsapps.model.SubDistrict
import com.harifrizki.crimemapsapps.model.UrbanVillage
import com.harifrizki.crimemapsapps.utils.DataResource

class FormAreaViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun provinceDetail(province: Province?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceDetail(province)
    fun provinceAdd(province: Province?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceAdd(province)
    fun provinceUpdate(province: Province?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceUpdate(province)
    fun provinceDelete(province: Province?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.provinceDelete(province)
    fun cityDetail(city: City?): LiveData<DataResource<CityResponse>> =
        crimeMapsRepository.cityDetail(city)
    fun cityAdd(city: City?): LiveData<DataResource<CityResponse>> =
        crimeMapsRepository.cityAdd(city)
    fun cityUpdate(city: City?): LiveData<DataResource<CityResponse>> =
        crimeMapsRepository.cityUpdate(city)
    fun cityDelete(city: City?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.cityDelete(city)
    fun subDistrictDetail(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> =
        crimeMapsRepository.subDistrictDetail(subDistrict)
    fun subDistrictAdd(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> =
        crimeMapsRepository.subDistrictAdd(subDistrict)
    fun subDistrictUpdate(subDistrict: SubDistrict?): LiveData<DataResource<SubDistrictResponse>> =
        crimeMapsRepository.subDistrictUpdate(subDistrict)
    fun subDistrictDelete(subDistrict: SubDistrict?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.subDistrictDelete(subDistrict)
    fun urbanVillageDetail(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> =
        crimeMapsRepository.urbanVillageDetail(urbanVillage)
    fun urbanVillageAdd(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> =
        crimeMapsRepository.urbanVillageAdd(urbanVillage)
    fun urbanVillageUpdate(urbanVillage: UrbanVillage?): LiveData<DataResource<UrbanVillageResponse>> =
        crimeMapsRepository.urbanVillageUpdate(urbanVillage)
    fun urbanVillageDelete(urbanVillage: UrbanVillage?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.urbanVillageDelete(urbanVillage)
}