package com.harifrizki.crimemapsapps.ui.module.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.utils.DataResource

class ListOfAdminViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun admin(pageNo: String?, name: String?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminByName(pageNo, name)
}