package com.harifrizki.core.utils

import com.harifrizki.core.data.remote.response.ErrorResponse
import com.harifrizki.core.utils.ResponseStatus.*

class DataResource<T>(val responseStatus: ResponseStatus, val data: T?, val errorResponse: ErrorResponse?) {
    companion object {
        fun <T> success(data: T?): DataResource<T>                              = DataResource(SUCCESS, data, null)
        fun <T> error(errorResponse: ErrorResponse?, data: T?): DataResource<T> = DataResource(ERROR,   data, errorResponse)
        fun <T> loading(data: T?): DataResource<T>                              = DataResource(LOADING, data, null)
    }
}

class ApiResource<T>(val responseStatus: ResponseStatus, val body: T, val errorResponse: ErrorResponse?) {
    companion object {
        fun <T> success(body: T): ApiResource<T>                              = ApiResource(SUCCESS, body, null)
        fun <T> empty(body: T, errorResponse: ErrorResponse): ApiResource<T>  = ApiResource(EMPTY,   body, errorResponse)
        fun <T> error(body: T, errorResponse: ErrorResponse):  ApiResource<T> = ApiResource(ERROR,   body, errorResponse)
    }
}