package com.harifrizki.crimemapsapps.ui.module.crimelocation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.CrimeLocationResponse
import com.harifrizki.crimemapsapps.model.CrimeLocation
import com.harifrizki.crimemapsapps.utils.DataResource

class DetailCrimeLocationViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun crimeLocationDetail(crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>> =
        crimeMapsRepository.crimeLocationDetail(crimeLocation)
}