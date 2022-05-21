package com.harifrizki.crimemapsapps.utils

import com.harifrizki.crimemapsapps.ui.module.admin.ListOfAdminActivity
import com.harifrizki.crimemapsapps.ui.module.dashboard.DashboardActivity
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
    MENU_AREA_ADMIN_ID
}

enum class MenuSetting {
    MENU_NONE,
    MENU_SETTING_DEBUG_MODE,
    MENU_SETTING_PROFILE,
    MENU_SETTING_EXIT
}

enum class Error {
    IS_NO_NETWORK,
    IS_API_RESPONSE
}

enum class CRUD {
    NONE,
    CREATE,
    READ,
    UPDATE,
    DELETE;

    companion object {
        fun getEnumCRUD(crud: String) = values().find { it.name == crud }
    }
}

enum class ActivityName(val nameOfActivity: String) {
    DASHBOARD(DashboardActivity::class.java.name),
    PROFILE(ProfileActivity::class.java.name),
    LIST_OF_ADMIN(ListOfAdminActivity::class.java.name);

    companion object {
        fun getNameOfActivity(activityName: ActivityName): String = activityName.nameOfActivity
        fun getEnumActivityName(nameOfActivity: String) = values().find { it.nameOfActivity == nameOfActivity }
    }
}