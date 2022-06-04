package com.harifrizki.crimemapsapps.ui.module.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.Province
import com.harifrizki.crimemapsapps.utils.DataResource

class ListOfAreaViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun province(pageNo: String?, name: String?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceByName(pageNo, name)
    fun provinceDelete(province: Province?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.provinceDelete(province)
    fun city(pageNo: String?, name: String?): LiveData<DataResource<CityResponse>> =
        crimeMapsRepository.cityByName(pageNo, name)
    fun subDistrict(pageNo: String?, name: String?): LiveData<DataResource<SubDistrictResponse>> =
        crimeMapsRepository.subDistrictByName(pageNo, name)
    fun urbanVillage(pageNo: String?, name: String?): LiveData<DataResource<UrbanVillageResponse>> =
        crimeMapsRepository.urbanVillageByName(pageNo, name)
}