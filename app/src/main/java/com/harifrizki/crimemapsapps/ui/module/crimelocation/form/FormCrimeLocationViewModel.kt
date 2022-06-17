package com.harifrizki.crimemapsapps.ui.module.crimelocation.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.CrimeLocationResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.model.CrimeLocation
import com.harifrizki.crimemapsapps.utils.DataResource
import java.io.File

class FormCrimeLocationViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
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