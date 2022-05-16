package com.harifrizki.crimemapsapps.model

import android.os.Parcelable
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.harifrizki.crimemapsapps.utils.EMPTY_STRING
import com.harifrizki.crimemapsapps.utils.ZERO
import kotlinx.parcelize.Parcelize

@Parcelize
data class Admin(
    @field:SerializedName("adminId")       var adminId: String? = null,
    @field:SerializedName("adminName")     var adminName: String? = null,
    @field:SerializedName("adminUsername") var adminUsername: String? = null,
    @field:SerializedName("adminPassword") var adminPassword: String? = null,
    @field:SerializedName("adminRole")     var adminRole: String? = null,
    @field:SerializedName("adminImage")    var adminImage: String? = null,
    @field:SerializedName("isLogin")       var isLogin: Boolean? = null,
    @field:SerializedName("isActive")      var isActive: Boolean? = null,
    @field:SerializedName("createdBy")     var createdBy: Admin? = null,
    var createdByUUID: String? = null,
    @field:SerializedName("createdDate")   var createdDate : String? = null,
    @field:SerializedName("updatedBy")     var updatedBy: Admin? = null,
    var updatedByUUID: String? = null,
    @field:SerializedName("updatedDate")   var updatedDate: String? = null
): Parcelable {
    companion object {
        fun jsonObject(admin: Admin?) : JsonObject {
            return JsonObject().apply {
                addProperty("adminId", admin?.adminId)
                addProperty("adminName", admin?.adminName)
                addProperty("adminUsername", admin?.adminUsername)
                addProperty("adminRole", admin?.adminRole)
                addProperty("adminPassword", admin?.adminPassword)
                addProperty("adminImage", admin?.adminImage)
                addProperty("createdDate", admin?.createdDate)
                addProperty("createdBy", admin?.createdByUUID)
                addProperty("updatedDate", admin?.updatedDate)
                addProperty("updatedBy", admin?.updatedByUUID)
            }
        }
    }
}

@Parcelize
data class Handshake(
    @field:SerializedName("urlApi")             var urlApi: String? = null,
    @field:SerializedName("roleAdminRoot")      var roleAdminRoot: String? = null,
    @field:SerializedName("roleAdmin")          var roleAdmin: String? = null,
    @field:SerializedName("defaultImageAdmin")  var defaultImageAdmin: String? = null,
    @field:SerializedName("firstRootAdmin")     var firstRootAdmin: String? = null,
    @field:SerializedName("urlImageStorageApi") var urlImageStorageApi: List<String> = arrayListOf()
): Parcelable

@Parcelize
data class Utilization(
    @field:SerializedName("countAdmin")              var countAdmin: Int? = null,
    @field:SerializedName("countAdminToday")         var countAdminToday: Int? = null,
    @field:SerializedName("countAdminMonth")         var countAdminMonth: Int? = null,
    @field:SerializedName("countProvince")           var countProvince: Int? = null,
    @field:SerializedName("countProvinceToday")      var countProvinceToday: Int? = null,
    @field:SerializedName("countProvinceMonth")      var countProvinceMonth: Int? = null,
    @field:SerializedName("countCity")               var countCity: Int? = null,
    @field:SerializedName("countCityToday")          var countCityToday: Int? = null,
    @field:SerializedName("countCityMonth")          var countCityMonth: Int? = null,
    @field:SerializedName("countSubDistrict")        var countSubDistrict: Int? = null,
    @field:SerializedName("countSubDistrictToday")   var countSubDistrictToday: Int? = null,
    @field:SerializedName("countSubDistrictMonth")   var countSubDistrictMonth: Int? = null,
    @field:SerializedName("countUrbanVillage")       var countUrbanVillage: Int? = null,
    @field:SerializedName("countUrbanVillageToday")  var countUrbanVillageToday: Int? = null,
    @field:SerializedName("countUrbanVillageMonth")  var countUrbanVillageMonth: Int? = null,
    @field:SerializedName("countCrimeLocation")      var countCrimeLocation: Int? = null,
    @field:SerializedName("countCrimeLocationToday") var countCrimeLocationToday: Int? = null,
    @field:SerializedName("countCrimeLocationMonth") var countCrimeLocationMonth: Int? = null
): Parcelable {
    constructor() : this(
        ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO,
        ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO,
        ZERO, ZERO
    )
}

@Parcelize
data class Message(
    @field:SerializedName("success") var success: Boolean? = null,
    @field:SerializedName("message") var message: String? = null
): Parcelable

@Parcelize
data class Menu(
    var idMenu: Int? = null,
    var name: String? = null,
    var useIcon: Int? = null,
    var useAnimation: String? = null,
    var visibility: Boolean? = null,
    var iconColor: Int? = null,
    var nameColor: Int? = null,
    var background: Int? = null
): Parcelable {
    constructor(): this(
        ZERO, EMPTY_STRING, ZERO, EMPTY_STRING, false, ZERO, ZERO, ZERO
    )
}