package com.harifrizki.crimemapsapps.utils

import android.Manifest

const val PREF_NAME = "com.harifrizki.crimemapsapps.Preferences"

const val EMPTY_STRING      = ""
const val SPACE_STRING      = " "
const val UNDER_LINE_STRING = "_"
const val HYPHEN_STRING     = "-"
const val SPAN_REGEX        = "\\[.*?\\]"

const val IMAGE_FORMAT_PNG     = ".png"
const val IMAGE_FORMAT_GALLERY = "image/*"

val APP_PERMISSION_GET_IMAGE: Array<String> = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE
)

const val ZERO   = 0
const val ONE    = 1
const val TWENTY = 20

const val ROTATE_DEGREE             = 90
const val MAX_SIZE                  = 600
const val MAX_LIST_IN_RECYCLER_VIEW = 100
const val MAX_ITEM_LIST_SHIMMER     = 4
const val ROW_MENU_AREA             = 2
const val INITIALIZE_PAGE_NO        = 1

const val WAIT_FOR_RUN_HANDLER_100_MS  = 100
const val WAIT_FOR_RUN_HANDLER_500_MS  = 500
const val WAIT_FOR_RUN_HANDLER_1000_MS = 1000
const val WAIT_FOR_RUN_HANDLER_2000_MS = 2000
const val WAIT_FOR_RUN_HANDLER_3000_MS = 3000

const val DEFAULT_IMAGE_ADMIN         = "DEFAULT_IMAGE_ADMIN"
const val DEFAULT_ADMIN_ROOT_USERNAME = "DEFAULT_ADMIN_ROOT_USERNAME"

const val URL_CONNECTION_API_IMAGE_CRIME_LOCATION = "URL_CONNECTION_API_IMAGE_CRIME_LOCATION"
const val URL_CONNECTION_API_IMAGE_ADMIN          = "URL_CONNECTION_API_IMAGE_ADMIN"

const val ROLE_ROOT  = "ROLE_ROOT"
const val ROLE_ADMIN = "ROLE_ADMIN"

const val DISTANCE_UNIT = "DISTANCE_UNIT"
const val MAX_DISTANCE = "MAX_DISTANCE"
const val MAX_UPLOAD_IMAGE_CRIME_LOCATION = "MAX_UPLOAD_IMAGE_CRIME_LOCATION"

const val LOGIN_MODEL = "LOGIN_MODEL"
const val ADMIN_MODEL = "ADMIN_MODEL"
const val AREA_MODEL  = "AREA_MODEL"

const val ERROR_RESPONSE = "ERROR_RESPONSE"
const val IS_AFTER_ERROR = "IS_AFTER_ERROR"
const val ERROR_STATE    = "IS_NO_NETWORK"

const val LOTTIE_ERROR_JSON        = "error.json"
const val LOTTIE_WARNING_JSON      = "warning.json"
const val LOTTIE_INFORMATION_JSON  = "information.json"
const val LOTTIE_SUCCESS_JSON      = "success.json"
const val LOTTIE_QUESTION_JSON     = "question.json"
const val LOTTIE_DEBUG_MODE_JSON   = "debug_mode.json"

const val INTENT_DATA           = "INTENT_DATA"
const val FROM_ACTIVITY         = "FROM_ACTIVITY"
const val OPERATION_CRUD        = "OPERATION_CRUD"
const val AREA                  = "AREA"
const val PARENT_AREA           = "PARENT_AREA"

const val URI_IMAGE  = "URI_IMAGE"
const val NAME_IMAGE = "NAME_IMAGE"

const val IS_READ_ONLY = "IS_READ_ONLY"