package com.harifrizki.crimemapsapps.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.harifrizki.crimemapsapps.model.*
import com.harifrizki.crimemapsapps.utils.EMPTY_STRING
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
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

data class AdminResponse(
    @SerializedName("admin")   var admin: Admin? = null,
    @SerializedName("admins")  var adminArrayList: ArrayList<Admin>? = null,
    @SerializedName("page")    var page: Page? = null,
    @SerializedName("message") var message : Message? = null
)

data class MessageResponse(
    @SerializedName("message") var message : Message? = null
)

@Parcelize
data class ErrorResponse(
    var errorCode: String? = null,
    var errorMessage: String? = null,
    var errorUrl: String? = null,
    var errorTime: String? = null,
    var errorThrow: String? = null
) : Parcelable {
    companion object {
        fun errorResponse(throwable: Throwable): ErrorResponse {
            return ErrorResponse().apply {
                errorMessage = throwable.message
                errorThrow = throwable.printStackTrace().toString()
            }
        }

        fun <T> errorResponse(response: Response<T>, jsonError: JSONObject): ErrorResponse {
            return ErrorResponse().apply {
                errorUrl = response.raw().networkResponse?.request?.url?.toUrl().toString()
                errorCode = response.raw().code.toString()
                errorTime = jsonError.getString("timestamp")
                errorMessage = jsonError.getString("error")
            }
        }
    }
}