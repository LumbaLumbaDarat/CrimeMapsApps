package com.harifrizki.crimemapsapps.module.splash

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.HandshakeResponse
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class SplashViewModel (private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun handshake(): LiveData<DataResource<HandshakeResponse>> = crimeMapsRepository.handshake()
}