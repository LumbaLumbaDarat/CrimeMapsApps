package com.harifrizki.crimemapsapps.ui.module.crimelocation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.CrimeLocationResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.model.CrimeLocation
import com.harifrizki.crimemapsapps.utils.DataResource

class ListOfCrimeLocationViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun crimeLocation(pageNo: String?, crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>> =
        crimeMapsRepository.crimeLocation(pageNo, crimeLocation)
    fun crimeLocationDelete(crimeLocation: CrimeLocation?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.crimeLocationDelete(crimeLocation)
}