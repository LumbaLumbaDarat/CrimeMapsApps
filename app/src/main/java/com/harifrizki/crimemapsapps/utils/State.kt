package com.harifrizki.crimemapsapps.utils

import com.harifrizki.crimemapsapps.ui.module.admin.ListOfAdminActivity
import com.harifrizki.crimemapsapps.ui.module.area.ListOfAreaActivity
import com.harifrizki.crimemapsapps.ui.module.cropphoto.CropPhotoActivity
import com.harifrizki.crimemapsapps.ui.module.dashboard.DashboardActivity
import com.harifrizki.crimemapsapps.ui.module.formarea.FormAreaActivity
import com.harifrizki.crimemapsapps.ui.module.password.PasswordActivity
import com.harifrizki.crimemapsapps.ui.module.profile.ProfileActivity

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

enum class ActivityName(val nameOfActivity: String) {
    DASHBOARD(DashboardActivity::class.java.name),
    PROFILE(ProfileActivity::class.java.name),
    PASSWORD(PasswordActivity::class.java.name),
    CROP_PHOTO(CropPhotoActivity::class.java.name),
    LIST_OF_ADMIN(ListOfAdminActivity::class.java.name),
    LIST_OF_AREA(ListOfAreaActivity::class.java.name),
    FORM_AREA(FormAreaActivity::class.java.name);

    companion object {
        fun getNameOfActivity(activityName: ActivityName): String = activityName.nameOfActivity
        fun getEnumActivityName(nameOfActivity: String) = values().find { it.nameOfActivity == nameOfActivity }
    }
}