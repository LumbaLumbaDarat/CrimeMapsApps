package com.harifrizki.core.utils

import android.app.Activity
import com.harifrizki.core.component.activity.CropPhotoActivity

enum class ResponseStatus {
    SUCCESS,
    EMPTY,
    ERROR,
    LOADING
}

enum class NotificationType {
    NOTIFICATION_ERROR,
    NOTIFICATION_WARNING,
    NOTIFICATION_SUCCESS,
    NOTIFICATION_INFORMATION
}

enum class MenuAreaType {
    MENU_NONE,
    MENU_AREA_CRIME_LOCATION_ID,
    MENU_AREA_PROVINCE_ID,
    MENU_AREA_CITY_ID,
    MENU_AREA_SUB_DISTRICT_ID,
    MENU_AREA_URBAN_VILLAGE_ID,
    MENU_AREA_ADMIN_ID;

    companion object {
        fun getEnumMenuAreaType(menuAreaType: String) = values().find { it.name == menuAreaType }
    }
}

enum class MenuSetting {
    MENU_NONE,
    MENU_SETTING_DEBUG_MODE,
    MENU_SETTING_PROFILE,
    MENU_SETTING_EXIT,
    MENU_CAMERA,
    MENU_GALLERY
}

enum class Error {
    IS_NO_NETWORK,
    IS_API_RESPONSE
}

enum class ImageType {
    IMAGE_PROFILE,
    IMAGE_ADMIN,
    IMAGE_CRIME_LOCATION
}

enum class CRUD {
    NONE,
    CREATE,
    READ,
    UPDATE,
    UPDATE_IMAGE,
    UPDATE_DATA_NON_IMAGE,
    DELETE;
}

enum class ParentAreaAction {
    PARENT_AREA_ACTION_NONE,
    PARENT_AREA_ICON_RIGHT,
    PARENT_AREA_ACTION_RIGHT,
    PARENT_AREA_ICON_LEFT,
    PARENT_AREA_ACTION_LEFT,
    PARENT_AREA_ICON_BOTH,
    PARENT_AREA_ACTION_BOTH
}

enum class ParamArea {
    ID,
    NAME,
    PARENT_ID,
    PARENT_NAME
}

enum class ImageState {
    ADD_IMAGE,
    IS_IMAGE
}

enum class ActivityName(val nameOfActivity: String) {
    DASHBOARD(
        getClassName("com.harifrizki.crimemapsapps.module.dashboard.DashboardActivity")),
    PROFILE(
        getClassName("com.harifrizki.crimemapsapps.module.admin.profile.ProfileActivity")),
    PASSWORD(
        getClassName("com.harifrizki.crimemapsapps.module.admin.changepassword.ChangePasswordActivity")),
    CROP_PHOTO(CropPhotoActivity::class.java.name),
    LIST_OF_ADMIN(
        getClassName("com.harifrizki.crimemapsapps.module.admin.list.ListOfAdminActivity")),
    LIST_OF_AREA(
        getClassName("com.harifrizki.crimemapsapps.module.area.list.ListOfAreaActivity")),
    FORM_AREA(
        getClassName("com.harifrizki.crimemapsapps.module.area.form.FormAreaActivity")),
    LIST_OF_CRIME_LOCATION(
        getClassName("com.harifrizki.crimemapsapps.module.crimelocation.list.ListOfCrimeLocationActivity")),
    DETAIL_CRIME_LOCATION(
        getClassName("com.harifrizki.crimemapsapps.module.crimelocation.detail.DetailCrimeLocationActivity")),
    FORM_CRIME_LOCATION(
        getClassName("com.harifrizki.crimemapsapps.module.crimelocation.form.FormCrimeLocationActivity")),
    MAPS(
        getClassName("com.harifrizki.crimemapsapps.module.maps.MapsActivity")),
    MAPS_USER(
        getClassName("com.harifrizki.crimemapsappsuser.module.maps.MapsActivity"));

    companion object {
        fun getNameOfActivity(activityName: ActivityName): String = activityName.nameOfActivity
        fun getEnumActivityName(nameOfActivity: String) = values().find { it.nameOfActivity == nameOfActivity }
    }
}