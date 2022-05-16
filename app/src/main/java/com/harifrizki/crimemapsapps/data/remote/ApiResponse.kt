package com.harifrizki.crimemapsapps.data.remote

import com.harifrizki.crimemapsapps.data.remote.response.ErrorResponse
import com.harifrizki.crimemapsapps.utils.Status.SUCCESS
import com.harifrizki.crimemapsapps.utils.Status.EMPTY
import com.harifrizki.crimemapsapps.utils.Status.ERROR
import com.harifrizki.crimemapsapps.utils.EMPTY_STRING
import com.harifrizki.crimemapsapps.utils.Status

class ApiResponse<T>(val status: Status, val body: T, val errorResponse: ErrorResponse?) {
    companion object {
        fun <T> success(body: T): ApiResponse<T>                              = ApiResponse(SUCCESS, body, null)
        fun <T> empty(body: T, errorResponse: ErrorResponse): ApiResponse<T>  = ApiResponse(EMPTY,   body, errorResponse)
        fun <T> error(body: T, errorResponse: ErrorResponse):  ApiResponse<T> = ApiResponse(ERROR,   body, errorResponse)
    }
}