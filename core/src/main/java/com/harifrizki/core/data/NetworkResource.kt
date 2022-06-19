package com.harifrizki.core.data

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.harifrizki.core.utils.ApiResource
import com.harifrizki.core.utils.ResponseStatus.*
import com.harifrizki.core.utils.DataResource
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

abstract class NetworkResource<RequestType>() {
    private val result = MediatorLiveData<DataResource<RequestType>>()

    init {
        Logger.addLogAdapter(AndroidLogAdapter());
        setValue(DataResource.loading(null))
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: DataResource<RequestType>) {
        if (result.value != newValue)
            result.value = newValue
    }

    protected open fun onFetchFailed() {}
    protected abstract fun createCall(): LiveData<ApiResource<RequestType>>

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response.responseStatus) {
                SUCCESS -> {
                    setValue(DataResource.success(response.body))
                }
                EMPTY -> {
                    setValue(DataResource.error(response.errorResponse, response.body))
                }
                ERROR -> {
                    onFetchFailed()
                    setValue(DataResource.error(response.errorResponse, response.body))
                }
                else -> {
                    Logger.e("Status " + response.responseStatus + " was Not Found !")
                }
            }
        }
    }

    fun asLiveData(): LiveData<DataResource<RequestType>> = result
}