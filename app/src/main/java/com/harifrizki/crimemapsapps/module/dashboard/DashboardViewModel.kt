package com.harifrizki.crimemapsapps.module.dashboard

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.MessageResponse
import com.harifrizki.core.data.remote.response.UtilizationResponse
import com.harifrizki.core.model.Admin
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class DashboardViewModel(private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun utilization(): LiveData<DataResource<UtilizationResponse>> =
        crimeMapsRepository.utilization()
    fun logout(admin: Admin?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.logout(admin)
}