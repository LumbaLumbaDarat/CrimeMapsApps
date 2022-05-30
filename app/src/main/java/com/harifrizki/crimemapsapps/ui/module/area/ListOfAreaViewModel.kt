package com.harifrizki.crimemapsapps.ui.module.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.ProvinceResponse
import com.harifrizki.crimemapsapps.utils.DataResource

class ListOfAreaViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun province(pageNo: String?, name: String?): LiveData<DataResource<ProvinceResponse>> =
        crimeMapsRepository.provinceByName(pageNo, name)
}