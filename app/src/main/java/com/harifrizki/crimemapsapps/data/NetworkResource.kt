package com.harifrizki.crimemapsapps.data

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.harifrizki.crimemapsapps.data.remote.ApiResponse
import com.harifrizki.crimemapsapps.utils.Status.SUCCESS
import com.harifrizki.crimemapsapps.utils.Status.EMPTY
import com.harifrizki.crimemapsapps.utils.Status.ERROR
import com.harifrizki.crimemapsapps.utils.Resource
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

abstract class NetworkResource<RequestType>() {
    private val result = MediatorLiveData<Resource<RequestType>>()

    init {
        Logger.addLogAdapter(AndroidLogAdapter());
        setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<RequestType>) {
        if (result.value != newValue)
            result.value = newValue
    }

    protected open fun onFetchFailed() {}
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)

            when (response.status) {
                SUCCESS -> {
                    setValue(Resource.success(response.body))
                }
                EMPTY -> {
                    setValue(Resource.error(response.errorResponse, response.body))
                }
                ERROR -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorResponse, response.body))
                }
                else -> {
                    Logger.e("Status " + response.status + " was Not Found !")
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<RequestType>> = result
}