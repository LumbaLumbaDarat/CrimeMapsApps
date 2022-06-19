package com.harifrizki.crimemapsapps.module.maps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.harifrizki.core.data.remote.response.ErrorResponse
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ActivityName.*
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.utils.CRIME_LOCATION_MODEL
import com.harifrizki.core.utils.EMPTY_STRING
import com.harifrizki.core.utils.FROM_ACTIVITY
import com.harifrizki.core.utils.IS_AFTER_ERROR
import com.orhanobut.logger.Logger
import java.io.IOException
import java.util.*
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.crimemapsapps.databinding.ActivityMapsBinding
import com.harifrizki.core.R
import com.harifrizki.core.utils.ActivityName.Companion.getNameOfActivity

class MapsActivity : BaseActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private var crimeLocation: CrimeLocation? = null

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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(com.harifrizki.crimemapsapps.R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.apply {
            ivSearchLocation.setOnClickListener { loadPlacePicker() }
            ivAddLocation.setOnClickListener {
                if (crimeLocation != null)
                    onBackPressed(
                        hashMapOf(
                            FROM_ACTIVITY to getNameOfActivity(MAPS),
                            CRIME_LOCATION_MODEL to crimeLocation!!
                        ), true
                    )
                else showWarning(
                    message = getString(
                        R.string.message_error_empty,
                        getString(R.string.label_plus_two_string,
                            getString(R.string.label_address),
                            getString(R.string.crime_location_menu))
                    )
                )
            }
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation!!
                placeMarkerOnMap(
                    LatLng(
                        lastLocation.latitude,
                        lastLocation.longitude))
                crimeLocation = CrimeLocation().apply {
                    crimeMapsLatitude = lastLocation.latitude.toString()
                    crimeMapsLongitude = lastLocation.longitude.toString()
                    crimeMapsAddress = getAddress(
                        LatLng(
                            lastLocation.latitude,
                            lastLocation.longitude))
                }
            }
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
            else {
                val place = Autocomplete.getPlaceFromIntent(it.data!!)
                crimeLocation = CrimeLocation().apply {
                    crimeMapsAddress = place.address
                    crimeMapsLatitude = place.latLng?.latitude.toString()
                    crimeMapsLongitude = place.latLng?.longitude.toString()
                }
                placeMarkerOnMap(place.latLng!!)
            }
        }
        else if (it.resultCode == AutocompleteActivity.RESULT_ERROR) {
            val status = Autocomplete.getStatusFromIntent(it.data!!)
            Logger.e(status.statusMessage.toString())
            showWarning(
                message = status.statusMessage,
                onClick = { onBackPressed() })
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
            mMap.setOnMarkerClickListener(this)
            mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN

            setUpMap()
        }
    }

    override fun onMarkerClick(p0: Marker) = false

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
                message = getString(R.string.message_error_permission_location),
                onClick = { onBackPressed() })
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mMap.addMarker(MarkerOptions().position(currentLatLng)
                    .title(getString(R.string.message_your_location)))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location).draggable(true)
        mMap.addMarker(markerOptions)
    }

    private fun loadPlacePicker() {
        var errorResponse: ErrorResponse? = null
        try {
            /*
            /**
             * Initialize Places. For simplicity, the API key is hard-coded. In a production
             * environment we recommend using a secure mechanism to manage API keys.
             */
            if (!Places.isInitialized())
                Places.initialize(this, this.resources?.getString(R.string.google_maps_key)!!)

            // Set the fields to specify which types of place data to return.
            val fields = listOf(Place.Field.ID, Place.Field.NAME)

            // Start the autocomplete intent.
            intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this)
            resultLauncher.launch(intent)
             */
        }
        catch (e: GooglePlayServicesRepairableException) {
            Logger.e(e.printStackTrace().toString())
            errorResponse = ErrorResponse.errorResponse(e)
            goTo(errorResponse)
        }
        catch (e: GooglePlayServicesNotAvailableException) {
            Logger.e(e.printStackTrace().toString())
            errorResponse = ErrorResponse.errorResponse(e)
            goTo(errorResponse)
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
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
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

    private fun getAddress(latLng: LatLng): String? {
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressInString: String? = EMPTY_STRING

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (null != addresses && addresses.isNotEmpty())
            {
                address = addresses[0]
                addressInString = address.getAddressLine(0)
            }
        }
        catch (e: IOException) {
            Logger.e(e.printStackTrace().toString())
            showWarning(
                message = e.message,
                onClick = { onBackPressed() })
        }

        return addressInString
    }
}
