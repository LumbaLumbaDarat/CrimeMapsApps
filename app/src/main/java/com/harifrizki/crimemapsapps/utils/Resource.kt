package com.harifrizki.crimemapsapps.utils

import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse
import com.harifrizki.crimemapsapps.utils.Status.SUCCESS
import com.harifrizki.crimemapsapps.utils.Status.ERROR
import com.harifrizki.crimemapsapps.utils.Status.LOADING

class Resource<T>(val status: Status, val data: T?, val errorResponse: ErrorResponse?) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(SUCCESS, data, null)
        fun <T> error(errorResponse: ErrorResponse?, data: T?): Resource<T> = Resource(ERROR, data, errorResponse)
        fun <T> loading(data: T?): Resource<T> = Resource(LOADING, data, null)
    }
}