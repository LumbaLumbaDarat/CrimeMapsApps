package com.harifrizki.core.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.harifrizki.core.model.*
import com.harifrizki.core.model.Admin
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

data class ProvinceResponse(
    @SerializedName("province")  var province: Province? = null,
    @SerializedName("provinces") var provinceArrayList: ArrayList<Province>? = null,
    @SerializedName("page")      var page: Page? = null,
    @SerializedName("message")   var message : Message? = null
)

data class CityResponse(
    @SerializedName("city")    var city: City? = null,
    @SerializedName("cities")  var cityArrayList: ArrayList<City>? = null,
    @SerializedName("page")    var page: Page? = null,
    @SerializedName("message") var message : Message? = null
)

data class SubDistrictResponse(
    @SerializedName("subDistrict")  var subDistrict: SubDistrict? = null,
    @SerializedName("subDistricts") var subDistrictArrayList: ArrayList<SubDistrict>? = null,
    @SerializedName("page")         var page: Page? = null,
    @SerializedName("message")      var message : Message? = null
)

data class UrbanVillageResponse(
    @SerializedName("urbanVillage")  var urbanVillage: UrbanVillage? = null,
    @SerializedName("urbanVillages") var urbanVillageArrayList: ArrayList<UrbanVillage>? = null,
    @SerializedName("page")          var page: Page? = null,
    @SerializedName("message")       var message : Message? = null
)

data class CrimeLocationResponse(
    @SerializedName("crimeLocation")  var crimeLocation: CrimeLocation? = null,
    @SerializedName("crimeLocations") var crimeLocationArrayList: ArrayList<CrimeLocation>? = null,
    @SerializedName("page")          var page: Page? = null,
    @SerializedName("message")       var message : Message? = null
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