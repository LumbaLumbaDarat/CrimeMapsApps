package com.harifrizki.crimemapsapps.data

import androidx.lifecycle.LiveData
import com.harifrizki.crimemapsapps.data.remote.*
import com.harifrizki.crimemapsapps.data.remote.response.HandshakeResponse
import com.harifrizki.crimemapsapps.data.remote.response.LoginResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.data.remote.response.UtilizationResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.AppExecutor
import com.harifrizki.crimemapsapps.utils.Resource

class CrimeMapsRepository(
    private val remote: RemoteDataSource,
    private val executor: AppExecutor
) : CrimeMapsDataSource {
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

    override fun handshake(): LiveData<Resource<HandshakeResponse>> {
        return object : NetworkResource<HandshakeResponse>() {
            override fun createCall(): LiveData<ApiResponse<HandshakeResponse>> =
                remote.handshake()
        }.asLiveData()
    }

    override fun login(admin: Admin?): LiveData<Resource<LoginResponse>> {
        return object : NetworkResource<LoginResponse>() {
            override fun createCall(): LiveData<ApiResponse<LoginResponse>> =
                remote.login(admin)
        }.asLiveData()
    }

    override fun logout(admin: Admin?): LiveData<Resource<MessageResponse>> {
        return object : NetworkResource<MessageResponse>() {
            override fun createCall(): LiveData<ApiResponse<MessageResponse>> =
                remote.logout(admin)
        }.asLiveData()
    }

    override fun utilization(): LiveData<Resource<UtilizationResponse>> {
        return object : NetworkResource<UtilizationResponse>() {
            override fun createCall(): LiveData<ApiResponse<UtilizationResponse>> =
                remote.utilization()
        }.asLiveData()
    }
}