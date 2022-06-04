package com.harifrizki.crimemapsapps.model

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.harifrizki.crimemapsapps.utils.EMPTY_STRING
import com.harifrizki.crimemapsapps.utils.MenuSetting
import com.harifrizki.crimemapsapps.utils.MenuSetting.MENU_NONE
import com.harifrizki.crimemapsapps.utils.ZERO
import kotlinx.parcelize.Parcelize

@Parcelize
data class Admin(
    @field:SerializedName("adminId") var adminId: String? = null,
    @field:SerializedName("adminName") var adminName: String? = null,
    @field:SerializedName("adminUsername") var adminUsername: String? = null,
    @field:SerializedName("adminPassword") var adminPassword: String? = null,
    @field:SerializedName("adminRole") var adminRole: String? = null,
    @field:SerializedName("adminImage") var adminImage: String? = null,
    @field:SerializedName("isLogin") var isLogin: Boolean? = null,
    @field:SerializedName("isActive") var isActive: Boolean? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    var createdByUUID: String? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    var updatedByUUID: String? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null
) : Parcelable {
    companion object {
        fun jsonObject(admin: Admin?): JsonObject {
            return JsonObject().apply {
                addProperty("adminId", admin?.adminId)
                addProperty("adminName", admin?.adminName)
                addProperty("adminUsername", admin?.adminUsername)
                addProperty("adminRole", admin?.adminRole)
                addProperty("adminPassword", admin?.adminPassword)
                addProperty("adminImage", admin?.adminImage)
                addProperty("isLogin", admin?.isLogin)
                addProperty("active", admin?.isActive)
                addProperty("createdDate", admin?.createdDate)
                addProperty("createdBy", admin?.createdByUUID)
                addProperty("updatedDate", admin?.updatedDate)
                addProperty("updatedBy", admin?.updatedByUUID)
            }
        }
    }
}

@Parcelize
data class Province(
    @field:SerializedName("provinceId") var provinceId: String? = null,
    @field:SerializedName("provinceName") var provinceName: String? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null,
) : Parcelable {
    companion object {
        fun jsonObject(province: Province?): JsonObject {
            return Gson().fromJson(Gson().toJson(province), JsonObject::class.java)
//            return JsonObject().apply {
//                addProperty("provinceId", province?.provinceId)
//                addProperty("provinceName", province?.provinceName)
//                addProperty(
//                    "createdBy", Gson().toJson(
//                        Admin.jsonObject(
//                            province?.createdBy
//                        )
//                    )
//                )
//                addProperty("createdDate", province?.createdDate)
//                addProperty(
//                    "updatedBy", Gson().toJson(
//                        Admin.jsonObject(
//                            province?.updatedBy
//                        )
//                    )
//                )
//                addProperty("updatedDate", province?.updatedDate)
//            }
        }
    }
}

@Parcelize
data class City(
    @field:SerializedName("cityId") var cityId: String? = null,
    @field:SerializedName("province") var province: Province? = null,
    @field:SerializedName("cityName") var cityName: String? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null,
) : Parcelable {
    companion object {
        fun jsonObject(city: City?): JsonObject {
            return JsonObject().apply {
                addProperty("cityId", city?.cityId)
                addProperty("province", Province.jsonObject(city?.province).toString())
                addProperty("cityName", city?.cityName)
                addProperty("createdBy", Admin.jsonObject(city?.createdBy).toString())
                addProperty("createdDate", city?.createdDate)
                addProperty("updatedBy", Admin.jsonObject(city?.updatedBy).toString())
                addProperty("updatedDate", city?.updatedDate)
            }
        }
    }
}

@Parcelize
data class SubDistrict(
    @field:SerializedName("subDistrictId") var subDistrictId: String? = null,
    @field:SerializedName("city") var city: City? = null,
    @field:SerializedName("subDistrictName") var subDistrictName: String? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null,
) : Parcelable {
    companion object {
        fun jsonObject(subDistrict: SubDistrict?): JsonObject {
            return JsonObject().apply {
                addProperty("subDistrictId", subDistrict?.subDistrictId)
                addProperty("city", City.jsonObject(subDistrict?.city).toString())
                addProperty("subDistrictName", subDistrict?.subDistrictName)
                addProperty("createdBy", Admin.jsonObject(subDistrict?.createdBy).toString())
                addProperty("createdDate", subDistrict?.createdDate)
                addProperty("updatedBy", Admin.jsonObject(subDistrict?.updatedBy).toString())
                addProperty("updatedDate", subDistrict?.updatedDate)
            }
        }
    }
}

@Parcelize
data class UrbanVillage(
    @field:SerializedName("urbanVillageId") var urbanVillageId: String? = null,
    @field:SerializedName("subDistrict") var subDistrict: SubDistrict? = null,
    @field:SerializedName("urbanVillageName") var urbanVillageName: String? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null,
) : Parcelable {
    companion object {
        fun jsonObject(urbanVillage: UrbanVillage?): JsonObject {
            return JsonObject().apply {
                addProperty("urbanVillageId", urbanVillage?.urbanVillageId)
                addProperty(
                    "subDistrict",
                    SubDistrict.jsonObject(urbanVillage?.subDistrict).toString()
                )
                addProperty("urbanVillageName", urbanVillage?.urbanVillageName)
                addProperty("createdBy", Admin.jsonObject(urbanVillage?.createdBy).toString())
                addProperty("createdDate", urbanVillage?.createdDate)
                addProperty("updatedBy", Admin.jsonObject(urbanVillage?.updatedBy).toString())
                addProperty("updatedDate", urbanVillage?.updatedDate)
            }
        }
    }
}

@Parcelize
data class Page(
    @field:SerializedName("pageNo") var pageNo: Int? = null,
    @field:SerializedName("totalPage") var totalPage: Int? = null,
    @field:SerializedName("contentSizePerPage") var contentSizePerPage: Int? = null,
    @field:SerializedName("firstPage") var firstPage: String? = null,
    @field:SerializedName("lastPage") var lastPage: String? = null,
    @field:SerializedName("currentPage") var currentPage: String? = null,
    @field:SerializedName("nextPage") var nextPage: String? = null,
    @field:SerializedName("prevPage") var prevPage: String? = null,
    @field:SerializedName("totalContentSize") var totalContentSize: Int? = null,
) : Parcelable

@Parcelize
data class Handshake(
    @field:SerializedName("urlApi") var urlApi: String? = null,
    @field:SerializedName("roleAdminRoot") var roleAdminRoot: String? = null,
    @field:SerializedName("roleAdmin") var roleAdmin: String? = null,
    @field:SerializedName("defaultImageAdmin") var defaultImageAdmin: String? = null,
    @field:SerializedName("firstRootAdmin") var firstRootAdmin: String? = null,
    @field:SerializedName("urlImageStorageApi") var urlImageStorageApi: List<String> = arrayListOf()
) : Parcelable

@Parcelize
data class Utilization(
    @field:SerializedName("countAdmin") var countAdmin: Int? = null,
    @field:SerializedName("countAdminToday") var countAdminToday: Int? = null,
    @field:SerializedName("countAdminMonth") var countAdminMonth: Int? = null,
    @field:SerializedName("countProvince") var countProvince: Int? = null,
    @field:SerializedName("countProvinceToday") var countProvinceToday: Int? = null,
    @field:SerializedName("countProvinceMonth") var countProvinceMonth: Int? = null,
    @field:SerializedName("countCity") var countCity: Int? = null,
    @field:SerializedName("countCityToday") var countCityToday: Int? = null,
    @field:SerializedName("countCityMonth") var countCityMonth: Int? = null,
    @field:SerializedName("countSubDistrict") var countSubDistrict: Int? = null,
    @field:SerializedName("countSubDistrictToday") var countSubDistrictToday: Int? = null,
    @field:SerializedName("countSubDistrictMonth") var countSubDistrictMonth: Int? = null,
    @field:SerializedName("countUrbanVillage") var countUrbanVillage: Int? = null,
    @field:SerializedName("countUrbanVillageToday") var countUrbanVillageToday: Int? = null,
    @field:SerializedName("countUrbanVillageMonth") var countUrbanVillageMonth: Int? = null,
    @field:SerializedName("countCrimeLocation") var countCrimeLocation: Int? = null,
    @field:SerializedName("countCrimeLocationToday") var countCrimeLocationToday: Int? = null,
    @field:SerializedName("countCrimeLocationMonth") var countCrimeLocationMonth: Int? = null
) : Parcelable {
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
) : Parcelable

@Parcelize
data class Menu(
    var menuSetting: MenuSetting?,
    var name: String? = null,
    var useIcon: Int? = null,
    var useAnimation: String? = null,
    var visibility: Boolean? = null,
    var iconColor: Int? = null,
    var nameColor: Int? = null,
    var background: Int? = null
) : Parcelable {
    constructor() : this(
        MENU_NONE, EMPTY_STRING, ZERO, EMPTY_STRING, false, ZERO, ZERO, ZERO
    )
}