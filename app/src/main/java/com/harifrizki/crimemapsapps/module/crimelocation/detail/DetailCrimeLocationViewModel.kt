package com.harifrizki.crimemapsapps.module.crimelocation.detail

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.CrimeLocationResponse
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class DetailCrimeLocationViewModel(private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun crimeLocationDetail(crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>> =
        crimeMapsRepository.crimeLocationDetail(crimeLocation)
}