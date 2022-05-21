package com.harifrizki.crimemapsapps.data

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.DataResource

interface CrimeMapsDataSource {
    fun handshake(): LiveData<DataResource<HandshakeResponse>>
    fun login(admin: Admin?): LiveData<DataResource<LoginResponse>>
    fun logout(admin: Admin?): LiveData<DataResource<MessageResponse>>
    fun utilization(): LiveData<DataResource<UtilizationResponse>>

    fun adminById(adminId: String?): LiveData<DataResource<AdminResponse>>
}