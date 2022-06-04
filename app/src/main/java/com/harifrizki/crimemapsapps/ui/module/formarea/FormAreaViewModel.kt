package com.harifrizki.crimemapsapps.ui.module.formarea

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.ProvinceResponse
import com.harifrizki.crimemapsapps.model.Province
import com.harifrizki.crimemapsapps.utils.DataResource

class FormAreaViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun provinceById(provinceId: String?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceById(provinceId)
    fun provinceAdd(province: Province?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceAdd(province)
    fun provinceUpdate(province: Province?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceUpdate(province)
    fun provinceDelete(province: Province?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceDelete(province)
}