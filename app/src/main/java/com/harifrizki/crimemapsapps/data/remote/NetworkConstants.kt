package com.harifrizki.crimemapsapps.data.remote

const val MAIN_URL_API = "https://crime-maps-apps-api.herokuapp.com/api/";
const val VERSION_API  = "v2";

const val HANDSHAKE   = "$VERSION_API/handshake";
const val LOGIN       = "$VERSION_API/login";
const val LOGOUT      = "$VERSION_API/logout";
const val UTILIZATION = "$VERSION_API/utilization";

const val CONNECT_TIME_OUT: Long = 5
const val READ_TIME_OUT: Long    = 30
const val WRITE_TIME_OUT: Long   = 15