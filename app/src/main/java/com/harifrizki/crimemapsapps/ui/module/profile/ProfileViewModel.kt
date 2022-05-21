package com.harifrizki.crimemapsapps.ui.module.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.utils.DataResource

class ProfileViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun adminById(adminId: String?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminById(adminId)
}