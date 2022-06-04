package com.harifrizki.crimemapsapps.data.remote

const val MAIN_URL_API = "https://crime-maps-apps-api.herokuapp.com/api/"
const val VERSION_API  = "v2"

const val ADD    = "add"
const val UPDATE = "update"
const val DELETE = "delete"

const val SEARCH_BY_NAME = "search-by-name"
const val SEARCH_BY_ID   = "search-by-id"

const val HANDSHAKE   = "$VERSION_API/handshake/"
const val LOGIN       = "$VERSION_API/login/"
const val LOGOUT      = "$VERSION_API/logout/"
const val UTILIZATION = "$VERSION_API/utilization/"

const val ADMIN                      = "$VERSION_API/admin"
const val ADMIN_BY_NAME              = "$ADMIN/$SEARCH_BY_NAME/"
const val ADMIN_BY_ID                = "$ADMIN/$SEARCH_BY_ID/"
const val ADMIN_ADD                  = "$ADMIN/$ADD/"
const val ADMIN_UPDATE               = "$ADMIN/$UPDATE/"
const val ADMIN_UPDATE_PHOTO_PROFILE = "$ADMIN/change-image-profile/"
const val ADMIN_UPDATE_PASSWORD      = "$ADMIN/update-password/"
const val ADMIN_UPDATE_ACTIVE        = "$ADMIN/activated/"
const val ADMIN_RESET_PASSWORD       = "$ADMIN/reset-password/"
const val ADMIN_DELETE               = "$ADMIN/$DELETE/"

const val PROVINCE         = "$VERSION_API/province"
const val PROVINCE_BY_NAME = "$PROVINCE/$SEARCH_BY_NAME/"
const val PROVINCE_BY_ID   = "$PROVINCE/$SEARCH_BY_ID/"
const val PROVINCE_ADD     = "$PROVINCE/$ADD/"
const val PROVINCE_UPDATE  = "$PROVINCE/$UPDATE/"
const val PROVINCE_DELETE  = "$PROVINCE/$DELETE/"

const val CITY         = "$VERSION_API/city"
const val CITY_BY_NAME = "$CITY/$SEARCH_BY_NAME/"

const val SUB_DISTRICT         = "$VERSION_API/sub-district"
const val SUB_DISTRICT_BY_NAME = "$SUB_DISTRICT/$SEARCH_BY_NAME/"

const val URBAN_VILLAGE         = "$VERSION_API/urban-village"
const val URBAN_VILLAGE_BY_NAME = "$URBAN_VILLAGE/$SEARCH_BY_NAME/"

const val CONNECT_TIME_OUT: Long = 5
const val READ_TIME_OUT: Long    = 30
const val WRITE_TIME_OUT: Long   = 15