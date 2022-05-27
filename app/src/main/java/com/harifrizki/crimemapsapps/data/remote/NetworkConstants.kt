package com.harifrizki.crimemapsapps.data.remote

const val MAIN_URL_API = "https://crime-maps-apps-api.herokuapp.com/api/"
const val VERSION_API  = "v2"

const val ADD    = "add"
const val UPDATE = "update"
const val DELETE = "delete"

const val HANDSHAKE   = "$VERSION_API/handshake"
const val LOGIN       = "$VERSION_API/login"
const val LOGOUT      = "$VERSION_API/logout"
const val UTILIZATION = "$VERSION_API/utilization"

const val ADMIN                      = "$VERSION_API/admin"
const val ADMIN_BY_NAME              = "$ADMIN/search-by-name"
const val ADMIN_BY_ID                = "$ADMIN/search-by-id/"
const val ADMIN_ADD                  = "$ADMIN/$ADD/"
const val ADMIN_UPDATE               = "$ADMIN/$UPDATE/"
const val ADMIN_UPDATE_PHOTO_PROFILE = "$ADMIN/change-image-profile/"
const val ADMIN_UPDATE_PASSWORD      = "$ADMIN/update-password/"
const val ADMIN_UPDATE_ACTIVE        = "$ADMIN/activated/"
const val ADMIN_RESET_PASSWORD       = "$ADMIN/reset-password/"
const val ADMIN_DELETE               = "$ADMIN/$DELETE/"

const val CONNECT_TIME_OUT: Long = 5
const val READ_TIME_OUT: Long    = 30
const val WRITE_TIME_OUT: Long   = 15