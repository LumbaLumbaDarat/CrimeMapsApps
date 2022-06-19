package com.harifrizki.crimemapsapps.module.crimelocation.form

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.CrimeLocationResponse
import com.harifrizki.core.data.remote.response.MessageResponse
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource
import java.io.File

class FormCrimeLocationViewModel(private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun crimeLocationAdd(crimeLocation: CrimeLocation?,
                            photoCrimeLocation: ArrayList<File>?):
            LiveData<DataResource<CrimeLocationResponse>> =
        crimeMapsRepository.crimeLocationAdd(crimeLocation, photoCrimeLocation)
    fun crimeLocationAddImage(crimeLocation: CrimeLocation?,
                         photoCrimeLocation: ArrayList<File>?):
            LiveData<DataResource<CrimeLocationResponse>> =
        crimeMapsRepository.crimeLocationAddImage(crimeLocation, photoCrimeLocation)
    fun crimeLocationUpdate(crimeLocation: CrimeLocation?):
            LiveData<DataResource<CrimeLocationResponse>> =
        crimeMapsRepository.crimeLocationUpdate(crimeLocation)
    fun crimeLocationDeleteImage(crimeLocation: CrimeLocation?):
            LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.crimeLocationDeleteImage(crimeLocation)
}