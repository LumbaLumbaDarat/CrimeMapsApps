package com.harifrizki.crimemapsappsuser.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.CRIME_LOCATION_MODEL
import com.harifrizki.core.utils.EMPTY_STRING
import com.harifrizki.core.utils.PreferencesManager
import com.harifrizki.crimemapsappsuser.R
import com.harifrizki.crimemapsappsuser.helper.NotificationHelper
import com.harifrizki.crimemapsappsuser.module.maps.MapsActivity
import com.orhanobut.logger.Logger

class GeofenceBroadcastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationHelper = NotificationHelper(context)
        val geofencingEvent = GeofencingEvent.fromIntent(intent!!)

        //bugs in here
        //val crimeLocation = intent.getParcelableExtra<CrimeLocation>(CRIME_LOCATION_MODEL)

        //temporary fix
        val crimeLocation = context?.let {
            PreferencesManager.getInstance(it)
                .getPreferences(CRIME_LOCATION_MODEL, CrimeLocation::class.java)
        }

        if (geofencingEvent!!.hasError()) {
            Logger.e("onReceive, geofencingEvent.hasError(), with Error Code "
                .plus(GeofenceStatusCodes.getStatusCodeString(geofencingEvent.errorCode)))
            return
        }

        val listOfGeofence = geofencingEvent.triggeringGeofences
        when (geofencingEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                notificationHelper
                    .sendHighPriorityNotification(
                        title = context?.getString(R.string.message_geofence_enter),
                        summary = context?.getString(R.string.message_geofence_enter_body),
                        body = context?.getString(R.string.message_geofence_enter_body),
                        crimeLocation)
            }
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                notificationHelper
                    .sendHighPriorityNotification(
                        title = context?.getString(R.string.message_geofence_dwell),
                        summary = context?.getString(R.string.message_geofence_dwell_body),
                        body = context?.getString(R.string.message_geofence_dwell_body),
                        crimeLocation)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                notificationHelper
                    .sendHighPriorityNotification(
                        title = context?.getString(R.string.message_geofence_exit),
                        summary = context?.getString(R.string.message_geofence_exit_body),
                        body = context?.getString(R.string.message_geofence_exit_body),
                        crimeLocation)
            }
        }
    }
}