package com.harifrizki.crimemapsapps.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.data.remote.response.UtilizationResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.Resource

class DashboardViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun utilization(): LiveData<Resource<UtilizationResponse>> = crimeMapsRepository.utilization()
    fun logout(admin: Admin?): LiveData<Resource<MessageResponse>> = crimeMapsRepository.logout(admin)
}