package com.harifrizki.crimemapsapps.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.Handshake
import com.harifrizki.crimemapsapps.model.Message
import com.harifrizki.crimemapsapps.model.Utilization
import kotlinx.parcelize.Parcelize
import retrofit2.Response

data class LoginResponse(
    @SerializedName("login")   var login : Admin? = null,
    @SerializedName("message") var message : Message? = null
)

data class HandshakeResponse(
    @SerializedName("handshake") var handshake : Handshake? = null,
    @SerializedName("message")   var message : Message?   = null
)

data class UtilizationResponse(
    @SerializedName("utilization") var utilization : Utilization? = null,
    @SerializedName("message")     var message : Message? = null
)

data class MessageResponse(
    @SerializedName("message") var message : Message? = null
)

@Parcelize
data class ErrorResponse(
    var errorCode: String? = null,
    var errorMessage: String? = null,
    var errorHeader: String? = null,
    var errorRaw: String? = null,
    var errorThrow: String? = null
) : Parcelable {
    companion object {
        fun errorResponse(throwable: Throwable): ErrorResponse {
            return ErrorResponse().apply {
                errorMessage = throwable.message
                errorThrow = throwable.printStackTrace().toString()
            }
        }

        fun <T> errorResponse(response: Response<T>): ErrorResponse {
            return ErrorResponse().apply {
                errorHeader = response.headers().toString()
                errorCode = response.code().toString()
                errorRaw = response.raw().toString()
                errorMessage = response.errorBody().toString()
                errorThrow = response.message()
            }
        }
    }
}