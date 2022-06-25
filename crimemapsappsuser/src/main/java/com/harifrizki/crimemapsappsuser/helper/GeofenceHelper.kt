package com.harifrizki.crimemapsappsuser.helper

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.maps.model.LatLng
import com.harifrizki.core.utils.REQUEST_CODE_FOR_GEOFENCE_PENDING_INTENT
import com.harifrizki.core.utils.WAIT_FOR_RUN_HANDLER_3000_MS
import com.harifrizki.crimemapsappsuser.R
import com.harifrizki.crimemapsappsuser.broadcastreceiver.GeofenceBroadcastReceiver

class GeofenceHelper(base: Context?) : ContextWrapper(base) {

    private var pendingIntent: PendingIntent? = null

    fun getGeofencingRequest(geofence: Geofence?): GeofencingRequest {
        return GeofencingRequest.Builder()
            .addGeofence(geofence!!)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    fun getGeofence(id: String?, latLng: LatLng?, radius: Float, transitionTypes: Int): Geofence {
        return Geofence.Builder()
            .setCircularRegion(latLng?.latitude!!, latLng.longitude, radius)
            .setRequestId(id!!)
            .setTransitionTypes(transitionTypes)
            .setLoiteringDelay(WAIT_FOR_RUN_HANDLER_3000_MS)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()
    }

    @JvmName("getPendingIntentGeofenceBroadcastReceiver")
    @SuppressLint("UnspecifiedImmutableFlag")
    fun getPendingIntent(): PendingIntent? {
        return if (pendingIntent != null)
            pendingIntent!!
        else {
            pendingIntent = PendingIntent
                .getBroadcast(
                    this,
                    REQUEST_CODE_FOR_GEOFENCE_PENDING_INTENT,
                    Intent(this, GeofenceBroadcastReceiver::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT)
            pendingIntent
        }
    }

    fun getErrorString(e: Exception): String {
        return if (e is ApiException) {
            when (e.statusCode) {
                GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> {
                    getString(R.string.message_geofence_not_available)
                }
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> {
                    getString(R.string.message_geofence_to_many)
                }
                GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> {
                    getString(R.string.message_geofence_to_many_pending_intent)
                }
                else -> {
                    e.message.toString()
                }
            }
        } else e.message.toString()
    }
}