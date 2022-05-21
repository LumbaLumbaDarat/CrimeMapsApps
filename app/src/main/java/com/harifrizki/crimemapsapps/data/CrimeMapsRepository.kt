package com.harifrizki.crimemapsapps.data

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.*
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.ApiResource
import com.harifrizki.crimemapsapps.utils.AppExecutor
import com.harifrizki.crimemapsapps.utils.DataResource

class CrimeMapsRepository(
    private val remote: RemoteDataSource,
    private val executor: AppExecutor
): CrimeMapsDataSource {
    companion object {
        @Volatile
        private var instance: CrimeMapsRepository? = null
        fun getInstance(
            remoteData: RemoteDataSource,
            appExecutor: AppExecutor
        ): CrimeMapsRepository =
            instance ?: synchronized(this) {
                instance ?: CrimeMapsRepository(remoteData, appExecutor)
            }
    }

    override fun handshake(): LiveData<DataResource<HandshakeResponse>> {
        return object : NetworkResource<HandshakeResponse>() {
            override fun createCall(): LiveData<ApiResource<HandshakeResponse>> =
                remote.handshake()
        }.asLiveData()
    }

    override fun login(admin: Admin?): LiveData<DataResource<LoginResponse>> {
        return object : NetworkResource<LoginResponse>() {
            override fun createCall(): LiveData<ApiResource<LoginResponse>> =
                remote.login(admin)
        }.asLiveData()
    }

    override fun logout(admin: Admin?): LiveData<DataResource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResource<MessageResponse>> =
                remote.logout(admin)
        }.asLiveData()
    }

    override fun utilization(): LiveData<DataResource<UtilizationResponse>> {
        return object : NetworkResource<UtilizationResponse>() {
            override fun createCall(): LiveData<ApiResource<UtilizationResponse>> =
                remote.utilization()
        }.asLiveData()
    }

    override fun adminById(adminId: String?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminById(adminId)
        }.asLiveData()
    }

    override fun adminUpdate(admin: Admin?): LiveData<DataResource<AdminResponse>> {
        return object : NetworkResource<AdminResponse>() {
            override fun createCall(): LiveData<ApiResource<AdminResponse>> =
                remote.adminUpdate(admin)
        }.asLiveData()
    }
}