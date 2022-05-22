package com.harifrizki.crimemapsapps.data.remote

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.ApiResource

interface DataSource {
    fun handshake(): LiveData<ApiResource<HandshakeResponse>>
    fun login(admin: Admin?): LiveData<ApiResource<LoginResponse>>
    fun logout(admin: Admin?): LiveData<ApiResource<MessageResponse>>
    fun utilization(): LiveData<ApiResource<UtilizationResponse>>

    fun adminById(adminId: String?): LiveData<ApiResource<AdminResponse>>
    fun adminUpdate(admin: Admin?): LiveData<ApiResource<AdminResponse>>
    fun adminUpdatePassword(adminId: String?, oldPassword: String?, newPassword: String?): LiveData<ApiResource<AdminResponse>>
}