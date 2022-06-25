package com.harifrizki.crimemapsappsuser.module.maps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.*
import com.harifrizki.crimemapsappsuser.R
import com.harifrizki.crimemapsappsuser.databinding.ActivityMapsBinding
import com.harifrizki.crimemapsappsuser.helper.GeofenceHelper
import com.harifrizki.crimemapsappsuser.module.splash.SplashScreenActivity
import com.orhanobut.logger.Logger
import java.util.*

class MapsActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var geofencingClient: GeofencingClient
    private lateinit var geofenceHelper: GeofenceHelper

    private val geofenceRadius = 200

    private var map: HashMap<String, Any>? = null
    private var crimeLocation: CrimeLocation? = null
    private var fromActivity: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        create(this, resultLauncher)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        map = getMap(intent)
        fromActivity = map!![FROM_ACTIVITY] as String
        crimeLocation = map!![CRIME_LOCATION_MODEL] as CrimeLocation

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationCallback = object : LocationCallback() { }
        geofencingClient = LocationServices.getGeofencingClient(this)
        geofenceHelper = GeofenceHelper(this).apply {
            this.crimeLocation = this@MapsActivity.crimeLocation
        }

        createLocationRequest()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdate()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdate()
        }
    }

    override fun onBackPressed() {
        if (fromActivity.equals(PUSH_NOTIFICATION, true))
        {
            Intent(this, SplashScreenActivity::class.java)
                .apply {
                    startActivity(this)
                    finishAffinity()
                }
        } else super.onBackPressed()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        if (isNetworkConnected(this))
        {
            mMap = googleMap
            mMap.uiSettings.isZoomControlsEnabled = true
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

            setUpMap()
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            showWarning(
                message = getString(com.harifrizki.core.R.string.message_error_permission_location),
                onClick = { onBackPressed() })
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.clear()
                mMap.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                        .title(getString(com.harifrizki.core.R.string.message_your_location))
                        .icon(getBitmapDescriptorFromVector(
                            this, R.drawable.ic_round_account_circle_primary_24)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

                val latLng = LatLng(
                    crimeLocation?.crimeMapsLatitude!!.toDouble(),
                    crimeLocation?.crimeMapsLongitude!!.toDouble())
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16F))
                addMarker(latLng, crimeLocation?.crimeMapsName)
                addCircle(latLng)
                addGeofence(latLng)
            }
        }
    }

    private fun startLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest.interval = WAIT_FOR_RUN_HANDLER_3000_MS.toLong()
        locationRequest.fastestInterval = WAIT_FOR_RUN_HANDLER_3000_MS.toLong()
        locationRequest.priority = Priority.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdate()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@MapsActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                }
                catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun addGeofence(latLng: LatLng?) {
        val geofence = geofenceHelper.getGeofence(
            GEOFENCE_ID,
            latLng,
            radius = geofenceRadius.toFloat(),
            Geofence.GEOFENCE_TRANSITION_ENTER
                    or Geofence.GEOFENCE_TRANSITION_DWELL
                    or Geofence.GEOFENCE_TRANSITION_EXIT)
        val geofencingRequest = geofenceHelper.getGeofencingRequest(geofence)
        val pendingIntent = geofenceHelper.getPendingIntent()
        PreferencesManager.getInstance(this)
            .setPreferences(CRIME_LOCATION_MODEL, crimeLocation)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            showWarning(
                message = getString(com.harifrizki.core.R.string.message_error_permission_location),
                onClick = { onBackPressed() })
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent!!)
            .addOnSuccessListener {
                OnSuccessListener<Void> {
                    Logger.i("Success")
                }
            }
            .addOnFailureListener {
                OnFailureListener {
                    showError(getString(R.string.app_user_name), it.message!!)
                    Logger.e(it.message!!)
                }
            }
    }

    private fun addMarker(latLng: LatLng?, title: String?) {
        mMap.addMarker(
            MarkerOptions().position(latLng!!).title(title)
        )
    }

    private fun addCircle(latLng: LatLng?) {
        mMap.addCircle(
            CircleOptions()
                .center(latLng!!)
                .radius(geofenceRadius.toDouble())
                .strokeColor(Color.rgb(247, 8, 16))
                .fillColor(Color.rgb(255, 182, 188))
                .strokeWidth(2F)
        )
    }
}