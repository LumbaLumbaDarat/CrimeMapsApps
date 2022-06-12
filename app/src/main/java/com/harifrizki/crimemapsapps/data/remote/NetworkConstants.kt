package com.harifrizki.crimemapsapps.data.remote

const val MAIN_URL_API = "https://crime-maps-apps-api.herokuapp.com/"
const val VERSION_API  = "v2"
const val GENERAL_END_POINT = "api/$VERSION_API"

const val ID     = "id/"
const val ADD    = "add/"
const val UPDATE = "update/"
const val DELETE = "delete/"

const val PARAM_NAME             = "name"
const val PARAM_PROVINCE_ID      = "provinceId"
const val PARAM_CITY_ID          = "cityId"
const val PARAM_SUB_DISTRICT_ID  = "subDistrictId"
const val PARAM_URBAN_VILLAGE_ID = "urbanVillageId"
const val PARAM_NEAREST_LOCATION = "nearestLocation"

const val PASSWORD = "password/";

const val HANDSHAKE   = "$GENERAL_END_POINT/handshake/"
const val LOGIN       = "$GENERAL_END_POINT/login/"
const val LOGOUT      = "$GENERAL_END_POINT/logout/"
const val UTILIZATION = "$GENERAL_END_POINT/utilization/"

const val END_POINT_ADMIN                      = "$GENERAL_END_POINT/admin/"
const val END_POINT_DETAIL_ADMIN               = "$END_POINT_ADMIN$ID"
const val END_POINT_ADD_ADMIN                  = "$END_POINT_ADMIN$ADD"
const val END_POINT_UPDATE_ADMIN               = "$END_POINT_ADMIN$UPDATE";
const val END_POINT_UPDATE_PHOTO_PROFILE_ADMIN = END_POINT_ADMIN + "change-photo-profile/"
const val END_POINT_UPDATE_PASSWORD_ADMIN      = "$END_POINT_ADMIN$UPDATE$PASSWORD"
const val END_POINT_RESET_PASSWORD_ADMIN       = END_POINT_ADMIN + "reset/" + PASSWORD
const val END_POINT_ACTIVATED_ADMIN            = END_POINT_ADMIN + "activated/"
const val END_POINT_DELETE_ADMIN               = "$END_POINT_ADMIN$DELETE"

const val END_POINT_PROVINCE        = "$GENERAL_END_POINT/province/";
const val END_POINT_DETAIL_PROVINCE = "$END_POINT_PROVINCE$ID"
const val END_POINT_ADD_PROVINCE    = "$END_POINT_PROVINCE$ADD"
const val END_POINT_UPDATE_PROVINCE = "$END_POINT_PROVINCE$UPDATE"
const val END_POINT_DELETE_PROVINCE = "$END_POINT_PROVINCE$DELETE"

const val END_POINT_CITY        = "$GENERAL_END_POINT/city/";
const val END_POINT_DETAIL_CITY = "$END_POINT_CITY$ID"
const val END_POINT_ADD_CITY    = "$END_POINT_CITY$ADD"
const val END_POINT_UPDATE_CITY = "$END_POINT_CITY$UPDATE"
const val END_POINT_DELETE_CITY  = "$END_POINT_CITY$DELETE"

const val END_POINT_SUB_DISTRICT        = "$GENERAL_END_POINT/sub-district/";
const val END_POINT_DETAIL_SUB_DISTRICT = "$END_POINT_SUB_DISTRICT$ID"
const val END_POINT_ADD_SUB_DISTRICT    = "$END_POINT_SUB_DISTRICT$ADD"
const val END_POINT_UPDATE_SUB_DISTRICT = "$END_POINT_SUB_DISTRICT$UPDATE"
const val END_POINT_DELETE_SUB_DISTRICT = "$END_POINT_SUB_DISTRICT$DELETE"

const val END_POINT_URBAN_VILLAGE        = "$GENERAL_END_POINT/urban-village/"
const val END_POINT_DETAIL_URBAN_VILLAGE = "$END_POINT_URBAN_VILLAGE$ID"
const val END_POINT_ADD_URBAN_VILLAGE    = "$END_POINT_URBAN_VILLAGE$ADD"
const val END_POINT_UPDATE_URBAN_VILLAGE = "$END_POINT_URBAN_VILLAGE$UPDATE"
const val END_POINT_DELETE_URBAN_VILLAGE = "$END_POINT_URBAN_VILLAGE$DELETE"

const val END_POINT_CRIME_LOCATION              = "$GENERAL_END_POINT/crime-location/";
const val END_POINT_DETAIL_CRIME_LOCATION       = "$END_POINT_CRIME_LOCATION$ID"
const val END_POINT_ADD_CRIME_LOCATION          = "$END_POINT_CRIME_LOCATION$ADD"
const val END_POINT_ADD_IMAGE_CRIME_LOCATION    = END_POINT_CRIME_LOCATION + "add-image/"
const val END_POINT_UPDATE_CRIME_LOCATION       = "$END_POINT_CRIME_LOCATION$UPDATE"
const val END_POINT_DELETE_CRIME_LOCATION       = "$END_POINT_CRIME_LOCATION$DELETE"
const val END_POINT_DELETE_IMAGE_CRIME_LOCATION = END_POINT_CRIME_LOCATION + "delete-image/"

const val ERROR_CODE_200_SUCCESS        = "200"
const val ERROR_CODE_404_PAGE_NOT_FOUND = "400"

const val CONNECT_TIME_OUT: Long = 5
const val READ_TIME_OUT: Long    = 30
const val WRITE_TIME_OUT: Long   = 15