package com.harifrizki.crimemapsapps.module.crimelocation.list

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.CrimeLocationResponse
import com.harifrizki.core.data.remote.response.MessageResponse
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class ListOfCrimeLocationViewModel(private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun crimeLocation(pageNo: String?, crimeLocation: CrimeLocation?): LiveData<DataResource<CrimeLocationResponse>> =
        crimeMapsRepository.crimeLocation(pageNo, crimeLocation)
    fun crimeLocationDelete(crimeLocation: CrimeLocation?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.crimeLocationDelete(crimeLocation)
}