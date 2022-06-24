package com.harifrizki.core.model

import android.net.Uri
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.CRUD.*
import com.harifrizki.core.data.remote.ID
import com.harifrizki.core.data.remote.PARAM_NAME
import kotlinx.parcelize.Parcelize
import java.io.File

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
        fun jsonObject(
            province: Province?,
            jsonForCRUD: CRUD?,
            searchBy: String? = EMPTY_STRING
        ): JsonObject {
            return createJson(province, jsonForCRUD, searchBy)
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
        fun jsonObject(
            city: City?,
            jsonForCRUD: CRUD?,
            searchBy: String? = EMPTY_STRING
        ): JsonObject {
            return createJson(city, jsonForCRUD, searchBy)
        }
    }
}

@Parcelize
data class SubDistrict(
    @field:SerializedName("subDistrictId") var subDistrictId: String? = null,
    @field:SerializedName("province") var province: Province? = null,
    @field:SerializedName("city") var city: City? = null,
    @field:SerializedName("subDistrictName") var subDistrictName: String? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null,
) : Parcelable {
    companion object {
        fun jsonObject(
            subDistrict: SubDistrict?,
            jsonForCRUD: CRUD?,
            searchBy: String? = EMPTY_STRING
        ): JsonObject {
            return createJson(subDistrict, jsonForCRUD, searchBy)
        }
    }
}

@Parcelize
data class UrbanVillage(
    @field:SerializedName("urbanVillageId") var urbanVillageId: String? = null,
    @field:SerializedName("province") var province: Province? = null,
    @field:SerializedName("city") var city: City? = null,
    @field:SerializedName("subDistrict") var subDistrict: SubDistrict? = null,
    @field:SerializedName("urbanVillageName") var urbanVillageName: String? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null,
) : Parcelable {
    companion object {
        fun jsonObject(
            urbanVillage: UrbanVillage?,
            jsonForCRUD: CRUD?,
            searchBy: String? = EMPTY_STRING
        ): JsonObject {
            return createJson(urbanVillage, jsonForCRUD, searchBy)
        }
    }
}

@Parcelize
data class CrimeLocation(
    @field:SerializedName("crimeLocationId") var crimeLocationId: String? = null,
    @field:SerializedName("province") var province: Province? = null,
    @field:SerializedName("city") var city: City? = null,
    @field:SerializedName("subDistrict") var subDistrict: SubDistrict? = null,
    @field:SerializedName("urbanVillage") var urbanVillage: UrbanVillage? = null,
    @field:SerializedName("crimeMapsName") var crimeMapsName: String? = null,
    @field:SerializedName("crimeMapsAddress") var crimeMapsAddress: String? = null,
    @field:SerializedName("crimeMapsDescription") var crimeMapsDescription: String? = null,
    @field:SerializedName("crimeMapsLatitude") var crimeMapsLatitude: String? = null,
    @field:SerializedName("crimeMapsLongitude") var crimeMapsLongitude: String? = null,
    @field:SerializedName("imageCrimeLocations") var imageCrimeLocations: ArrayList<ImageCrimeLocation>? = null,
    @field:SerializedName("distance") var distance: Double? = null,
    @field:SerializedName("distanceUnit") var distanceUnit: String? = null,
    @field:SerializedName("createdBy") var createdBy: Admin? = null,
    @field:SerializedName("createdDate") var createdDate: String? = null,
    @field:SerializedName("updatedBy") var updatedBy: Admin? = null,
    @field:SerializedName("updatedDate") var updatedDate: String? = null,
) : Parcelable {
    companion object {
        fun jsonObject(
            crimeLocation: CrimeLocation?,
            jsonForCRUD: CRUD?,
            searchBy: String? = EMPTY_STRING
        ): JsonObject {
            return createJson(crimeLocation, jsonForCRUD, searchBy)
        }
    }
}

@Parcelize
data class ImageCrimeLocation(
    @field:SerializedName("imageCrimeLocationId") var imageCrimeLocationId: String? = null,
    @field:SerializedName("imageName") var imageCrimeLocationName: String? = null
) : Parcelable {
    companion object {
        fun jsonObject(
            imageCrimeLocation: ImageCrimeLocation?,
            jsonForCRUD: CRUD?,
            searchBy: String? = EMPTY_STRING
        ): JsonObject {
            return createJson(imageCrimeLocation, jsonForCRUD, searchBy)
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
    @field:SerializedName("distanceUnit") var distanceUnit: String? = null,
    @field:SerializedName("maxDistance") var maxDistance: Int? = null,
    @field:SerializedName("maxUploadImageCrimeLocation") var maxUploadImageCrimeLocation: Int? = null,
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
        MenuSetting.MENU_NONE, EMPTY_STRING, ZERO, EMPTY_STRING, false, ZERO, ZERO, ZERO
    )
}

@Parcelize
data class RegistrationArea(
    var label: String? = null,
    var areaRegistration: String? = null
) : Parcelable

@Parcelize
data class ImageResource(
    var imageId: String? = null,
    var imageState: ImageState? = null,
    var imageName: String? = null,
    var imagePath: String? = null,
    var imageUri: Uri? = null,
    var imageFile: File? = null
) : Parcelable

fun createJson(
    any: Any?,
    jsonForCRUD: CRUD?,
    searchBy: String? = EMPTY_STRING
): JsonObject {
    return when (jsonForCRUD) {
        READ -> {
            when (searchBy) {
                PARAM_NAME -> {
                    when (any) {
                        is Province -> {
                            JsonObject().apply {
                                addProperty(any::provinceName.name, any.provinceName)
                            }
                        }
                        is City -> {
                            JsonObject().apply {
                                addProperty(
                                    any::cityName.name, any.cityName
                                )
                            }
                        }
                        is SubDistrict -> {
                            JsonObject().apply {
                                addProperty(
                                    any::subDistrictName.name, any.subDistrictName
                                )
                            }
                        }
                        is UrbanVillage -> {
                            JsonObject().apply {
                                addProperty(
                                    any::urbanVillageName.name,
                                    any.urbanVillageName
                                )
                            }
                        }
                        is CrimeLocation -> {
                            JsonObject().apply {
                                addProperty(any::crimeMapsName.name, any.crimeMapsName)
                            }
                        }
                        else -> {
                            JsonObject()
                        }
                    }
                }
                ID -> {
                    when (any) {
                        is Province -> {
                            JsonObject().apply {
                                addProperty(any::provinceId.name, any.provinceId)
                            }
                        }
                        is City -> {
                            JsonObject().apply {
                                addProperty(
                                    any::cityId.name, any.cityId
                                )
                            }
                        }
                        is SubDistrict -> {
                            JsonObject().apply {
                                addProperty(
                                    any::subDistrictId.name, any.subDistrictId
                                )
                            }
                        }
                        is UrbanVillage -> {
                            JsonObject().apply {
                                addProperty(
                                    any::urbanVillageId.name, any.urbanVillageId
                                )
                            }
                        }
                        is CrimeLocation -> {
                            JsonObject().apply {
                                addProperty(any::crimeLocationId.name, any.crimeLocationId)
                            }
                        }
                        else -> {
                            JsonObject()
                        }
                    }
                }
                else -> {
                    Gson().fromJson(Gson().toJson(any), JsonObject::class.java)
                }
            }
        }
        CREATE,
        UPDATE -> {
            Gson().fromJson(Gson().toJson(any), JsonObject::class.java)
        }
        DELETE -> {
            when (any) {
                is Province -> {
                    JsonObject().apply {
                        addProperty(any::provinceId.name, any.provinceId)
                    }
                }
                is City -> {
                    JsonObject().apply {
                        addProperty(
                            any::cityId.name, any.cityId
                        )
                    }
                }
                is SubDistrict -> {
                    JsonObject().apply {
                        addProperty(
                            any::subDistrictId.name, any.subDistrictId
                        )
                    }
                }
                is UrbanVillage -> {
                    JsonObject().apply {
                        addProperty(
                            any::urbanVillageId.name, any.urbanVillageId
                        )
                    }
                }
                is CrimeLocation -> {
                    JsonObject().apply {
                        addProperty(
                            any::crimeLocationId.name, any.crimeLocationId
                        )
                    }
                }
                is ImageCrimeLocation -> {
                    JsonObject().apply {
                        addProperty(
                            any::imageCrimeLocationId.name, any.imageCrimeLocationId
                        )
                    }
                }
                else -> {
                    JsonObject()
                }
            }
        }
        else -> {
            JsonObject()
        }
    }
}