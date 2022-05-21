package com.harifrizki.crimemapsapps.ui.module.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.data.remote.response.UtilizationResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.DataResource

class DashboardViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun utilization(): LiveData<DataResource<UtilizationResponse>> =
        crimeMapsRepository.utilization()
    fun logout(admin: Admin?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.logout(admin)
}