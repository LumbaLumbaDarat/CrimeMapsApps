package com.harifrizki.crimemapsapps.data

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.response.HandshakeResponse
import com.harifrizki.crimemapsapps.data.remote.response.LoginResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.data.remote.response.UtilizationResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.Resource

interface CrimeMapsDataSource {
    fun handshake(): LiveData<Resource<HandshakeResponse>>
    fun login(admin: Admin?): LiveData<Resource<LoginResponse>>
    fun logout(admin: Admin?): LiveData<Resource<MessageResponse>>
    fun utilization(): LiveData<Resource<UtilizationResponse>>
}