package com.harifrizki.crimemapsappsuser.module.dashboard

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.CrimeLocationResponse
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class DashboardViewModel (private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun crimeLocationByNearestLocation(pageNo: String?, crimeLocation: CrimeLocation?):
            LiveData<DataResource<CrimeLocationResponse>>
    = crimeMapsRepository.crimeLocationByNearestLocation(pageNo, crimeLocation)
}