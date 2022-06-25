package com.harifrizki.crimemapsappsuser.module.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.core.R
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.data.remote.response.CrimeLocationResponse
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.model.Page
import com.harifrizki.core.utils.*
import com.harifrizki.crimemapsappsuser.adapter.CrimeLocationNearestAdapter
import com.harifrizki.crimemapsappsuser.databinding.ActivityDashboardBinding
import com.harifrizki.crimemapsappsuser.module.crimelocation.CrimeLocationDetailActivity
import com.harifrizki.crimemapsappsuser.module.splash.SplashScreenActivity
import com.lumbalumbadrt.colortoast.ColorToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity() {
    private val binding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<DashboardViewModel>()

    private var crimeLocationNearestAdapter: CrimeLocationNearestAdapter? = null

    private var page: Page? = null
    private var doNotLoadData: Boolean? = true
    private var searchName: String? = EMPTY_STRING

    private var locationManager: LocationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        appBar(
            binding.iAppBarNearestCrimeLocation,
            getString(R.string.crime_location_menu),
            R.drawable.ic_round_location_on_24,
            R.color.primary,
            R.drawable.frame_background_secondary
        )
        binding.apply {
            initializeCrimeLocationByNearest()
            createSearch(iSearchNearestCrimeLocation)
            createRootView(rvListNearestCrimeLocation, sflListNearestCrimeLocation)
            createEmpty(iEmptyListNearestCrimeLocation)
            createNotificationAndOptionMessage(iNotificationListNearestCrimeLocation)
            srlListNearestCrimeLocation.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@DashboardActivity, this
                )
                setOnRefreshListener(this@DashboardActivity)
            }
        }
        search(
            getString(
                R.string.label_title_search_by,
                getString(R.string.crime_location_menu),
                getString(R.string.label_name)
            ),
            onSearch = {
                searchName = it
                crimeLocationByNearest()
            }
        )
        crimeLocationByNearest()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                crimeLocationByNearest()
        }
    }

    private val resultLauncherLocationPermission =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            var invalidCountPermission = ZERO
            permissions.entries.forEach {
                val isGranted = it.value
                if (!isGranted)
                    invalidCountPermission++
            }

            if (invalidCountPermission >= ONE)
                showWarning(
                    message = getString(R.string.message_error_permission_location),
                    onClick = {
                        dismissNotification()
                        Intent(this, SplashScreenActivity::class.java)
                            .apply {
                                startActivity(this)
                                finish()
                            }
                    })
            else crimeLocationByNearest()
        }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlListNearestCrimeLocation.isRefreshing = false
        crimeLocationByNearest()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val crimeLocationByNearest = Observer<DataResource<CrimeLocationResponse>> {
        when (it.responseStatus) {
            ResponseStatus.LOADING -> {
                disableAccess()
                loadingList(true)
            }
            ResponseStatus.SUCCESS -> {
                if (isResponseSuccess(it.data?.message)) {
                    if (it.data?.crimeLocationArrayList?.size!! > getMinItemForValidList()) {
                        if (it.data!!.page?.totalContentSize!! >= getMaxItemInList()) {
                            if (doNotLoadData!!) {
                                showNotificationAndOptionMessageInformation(
                                    title = titleOverloadData(getString(R.string.crime_location_menu)),
                                    message = messageOverloadData(
                                        it.data!!.page?.totalContentSize,
                                        getString(R.string.crime_location_menu),
                                        getString(R.string.label_name)
                                    ),
                                    useOption = true,
                                    buttonPositive = getString(
                                        R.string.label_title_search,
                                        getString(R.string.label_name)
                                    ),
                                    buttonNegative = getString(
                                        R.string.label_show_overload
                                    ),
                                    onPositive = {
                                        binding.iSearchNearestCrimeLocation.tieSearch.requestFocus()
                                    },
                                    onNegative = {
                                        doNotLoadData = false
                                        setCrimeLocationNearestAdapter(it.data)
                                    }
                                )
                                loadingList(
                                    isOn = false,
                                    isGetData = true,
                                    isOverloadData = true
                                )
                            } else setCrimeLocationNearestAdapter(it.data)
                        } else setCrimeLocationNearestAdapter(it.data)
                    } else {
                        showEmpty(
                            getString(R.string.label_not_found),
                            getString(
                                R.string.message_empty_list,
                                getString(R.string.crime_location_menu)
                            )
                        )
                        loadingList(
                            isOn = false,
                            isGetData = false,
                            isOverloadData = false
                        )
                    }
                }
                enableAccess()
            }
            ResponseStatus.ERROR -> {
                enableAccess()
                loadingList(
                    isOn = false,
                    isGetData = false,
                    isOverloadData = false
                )
                showEmptyError(
                    getString(R.string.message_error_something_wrong),
                    it.errorResponse?.errorMessage
                )
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun crimeLocationByNearest() {
        if (networkConnected()) {
            var pageNo: String? = INITIALIZE_PAGE_NO.toString()
            if (page != null) {
                if (page?.nextPage!!.isEmpty())
                    ColorToast.roundLineInfo(
                        this,
                        getString(R.string.app_user_name),
                        getString(
                            R.string.message_all_data_was_load,
                            getString(R.string.admin_menu)
                        ),
                        Toast.LENGTH_LONG
                    )
                else pageNo = page?.nextPage!!
            }
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    resultLauncherLocationPermission.launch(APP_PERMISSION_GEOFENCE_LOCATION_Q)
                else resultLauncherLocationPermission.launch(APP_PERMISSION_GEOFENCE_LOCATION)
            }
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val providers = locationManager?.getProviders(true)
            var resultSearchLocation: Location? = null
            for (provider: String? in providers!!) {
                val searchLocation = locationManager
                    ?.getLastKnownLocation(provider!!) ?: continue

                if (resultSearchLocation == null ||
                    searchLocation.accuracy < resultSearchLocation.accuracy
                ) {
                    resultSearchLocation = searchLocation
                }
            }
            if (resultSearchLocation != null) {
                viewModel.crimeLocationByNearestLocation(
                    pageNo,
                    CrimeLocation().apply {
                        crimeMapsName = searchName
                        crimeMapsLatitude = resultSearchLocation.latitude.toString()
                        crimeMapsLongitude = resultSearchLocation.longitude.toString()
                    })
                    .observe(this, crimeLocationByNearest)
            }
        }
    }

    private fun initializeCrimeLocationByNearest() {
        crimeLocationNearestAdapter = CrimeLocationNearestAdapter(
            context = this,
            isShimmer = false
        ).apply {
            onClickCrimeLocation = {
                goTo(
                    CrimeLocationDetailActivity(),
                    hashMapOf(
                        DISTANCE_CRIME_LOCATION_MODEL to it?.distance!!,
                        CRIME_LOCATION_MODEL to it
                    )
                )
            }
            onClickPreviewImage = {
                showImagePreview(
                    it.imageCrimeLocationName,
                    PreferencesManager
                        .getInstance(this@DashboardActivity)
                        .getPreferences(URL_CONNECTION_API_IMAGE_CRIME_LOCATION)
                )
            }
        }
        binding.apply {
            rvListNearestCrimeLocation.apply {
                layoutManager = LinearLayoutManager(this@DashboardActivity)
                adapter = crimeLocationNearestAdapter
            }
            rvListNearestCrimeLocationShimmer.apply {
                layoutManager = LinearLayoutManager(this@DashboardActivity)
                adapter = CrimeLocationNearestAdapter(
                    context = this@DashboardActivity,
                    isShimmer = true
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCrimeLocationNearestAdapter(crimeLocationResponse: CrimeLocationResponse?) {
        page = crimeLocationResponse?.page
        crimeLocationNearestAdapter?.apply {
            if (crimeLocationResponse?.page?.pageNo == INITIALIZE_PAGE_NO)
                setCrimeLocations(crimeLocationResponse.crimeLocationArrayList)
            else addCrimeLocations(crimeLocationResponse?.crimeLocationArrayList)
            notifyDataSetChanged()
            loadingList(
                isOn = false,
                isGetData = true,
                isOverloadData = false
            )
        }
    }

    private fun disableAccess() {
        disableAccess(
            arrayOf(
                binding.iAppBarNearestCrimeLocation.ivBtnRightAppBar,
                binding.iSearchNearestCrimeLocation.ibSearch
            )
        )
    }

    private fun enableAccess() {
        enableAccess(
            arrayOf(
                binding.iAppBarNearestCrimeLocation.ivBtnRightAppBar,
                binding.iSearchNearestCrimeLocation.ibSearch
            )
        )
    }
}