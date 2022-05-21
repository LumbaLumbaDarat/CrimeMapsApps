package com.harifrizki.crimemapsapps.ui.module.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.HandshakeResponse
import com.harifrizki.crimemapsapps.utils.DataResource

class SplashViewModel (private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun handshake(): LiveData<DataResource<HandshakeResponse>> = crimeMapsRepository.handshake()
}