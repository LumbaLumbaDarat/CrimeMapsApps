package com.harifrizki.crimemapsapps.module.crimelocation.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.data.remote.response.CrimeLocationResponse
import com.harifrizki.core.model.CrimeLocation
import com.harifrizki.core.model.RegistrationArea
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ActivityName.*
import com.harifrizki.core.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.core.utils.CRUD.*
import com.harifrizki.core.adapter.RegistrationAreaAdapter
import com.harifrizki.crimemapsapps.databinding.ActivityDetailCrimeLocationBinding
import com.harifrizki.crimemapsapps.module.crimelocation.form.FormCrimeLocationActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import com.harifrizki.core.R

class DetailCrimeLocationActivity : BaseActivity() {
    private val binding by lazy {
        ActivityDetailCrimeLocationBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<DetailCrimeLocationViewModel>()

    private var registrationAreaAdapter: RegistrationAreaAdapter? = null

    private var map: HashMap<String, Any>? = null
    private var isAfterCRUD: CRUD? = NONE
    private var crimeLocation: CrimeLocation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        crimeLocation = map!![CRIME_LOCATION_MODEL] as CrimeLocation

        appBar(binding.iAppBarDetailCrimeLocation,
            getString(R.string.crime_location_menu),
            R.drawable.ic_round_location_on_24,
            R.color.primary,
            R.drawable.frame_background_secondary,
            R.drawable.ic_round_edit_24,
            R.color.white,
            R.drawable.button_primary_ripple_white,
            onClick = {
                goToEdit(UPDATE_DATA_NON_IMAGE)
            })
        binding.apply {
            initializeDetailCrimeLocation()
            createImageSlider(iCrimeLocationImageSlider)
            srlDetailCrimeLocation.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@DetailCrimeLocationActivity, this
                )
                setOnRefreshListener(this@DetailCrimeLocationActivity)
            }
            btnBackDetailCrimeLocation.setOnClickListener { onBackPressed() }
        }

        crimeLocationDetail(crimeLocation)
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                crimeLocationDetail(crimeLocation)
            else {
                if (showMessage(getMap(it.data))) {
                    isAfterCRUD = getMap(it.data)[OPERATION_CRUD] as CRUD
                    crimeLocationDetail(crimeLocation)
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlDetailCrimeLocation.isRefreshing = false
        crimeLocationDetail(crimeLocation)
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(DETAIL_CRIME_LOCATION),
            isAfterCRUD
        )
        super.onBackPressed()
    }

    private val crimeLocationDetail = Observer<DataResource<CrimeLocationResponse>> {
        when (it.responseStatus) {
            ResponseStatus.LOADING -> {
                disableAccess()
                loadingOn()
            }
            ResponseStatus.SUCCESS -> {
                if (isResponseSuccess(it.data?.message)) {
                    setCrimeLocation(it.data?.crimeLocation)
                    enableAccess()
                }
            }
            ResponseStatus.ERROR -> {
                loadingNameOff()
                loadingAddressOff()
                loadingDescriptionOff()
                loadingAreaRegistrationOff()
                loadingCreatedAndUpdateOff()
                enableAccess()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun crimeLocationDetail(crimeLocation: CrimeLocation?) {
        if (networkConnected()) {
            viewModel.crimeLocationDetail(crimeLocation).observe(this, crimeLocationDetail)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeDetailCrimeLocation() {
        registrationAreaAdapter = RegistrationAreaAdapter(
            context = this,
            isShimmer = false
        )
        binding.apply {
            widgetStartDrawableShimmer(
                arrayOf(
                    iNameShimmer.tvId,
                    iNameShimmer.tvName,

                    iAddressShimmer.tvTitleAddress,
                    iAddressShimmer.tvAddress,
                    iAddressShimmer.tvLatitude,
                    iAddressShimmer.tvLongitude,
                    iAddressShimmer.btnSeeLocation,

                    iDescriptionShimmer.tvTitleDescription,
                    iDescriptionShimmer.tvDescription,

                    iAreaRegistrationShimmer.tvTitleAreaRegistration,

                    iCreatedAndUpdatedShimmer.tvCreated,
                    iCreatedAndUpdatedShimmer.tvUpdated
                ), this@DetailCrimeLocationActivity
            )
            widgetStartDrawableShimmer(
                arrayOf(ivShimmer), this@DetailCrimeLocationActivity
            )
            iAreaRegistrationShimmer.rvListAreaRegistration.apply {
                layoutManager = LinearLayoutManager(this@DetailCrimeLocationActivity)
                adapter = RegistrationAreaAdapter(
                    context = this@DetailCrimeLocationActivity,
                    isShimmer = true
                )
            }
        }
    }

    private fun loadingOn() {
        binding.apply {
            iCrimeLocationImageSlider.root.visibility = View.GONE
            iName.root.visibility = View.GONE
            iAddress.root.visibility = View.GONE
            iAddressShimmer.btnSeeLocation.visibility = View.GONE
            iDescription.root.visibility = View.GONE
            iAreaRegistration.root.visibility = View.GONE
            iCreatedAndUpdated.root.visibility = View.GONE

            shimmerOn(
                sflCrimeLocationImageSlider,
                true)
            shimmerOn(
                sflName,
                true
            )
            shimmerOn(
                sflAddress,
                true
            )
            shimmerOn(
                sflDescription,
                true
            )
            shimmerOn(
                sflAreaRegistration,
                true
            )
            shimmerOn(
                sflCreatedAndUpdated,
                true
            )
        }
    }

    private fun loadingImageSliderOff() {
        binding.apply {
            shimmerOn(
                binding.sflCrimeLocationImageSlider,
                false
            )
            binding.iCrimeLocationImageSlider.root.visibility = View.VISIBLE
        }
    }

    private fun loadingNameOff() {
        binding.apply {
            shimmerOn(
                binding.sflName,
                false
            )
            binding.iName.root.visibility = View.VISIBLE
        }
    }

    private fun loadingAddressOff() {
        binding.apply {
            shimmerOn(
                binding.sflAddress,
                false
            )
            binding.iAddress.root.visibility = View.VISIBLE
        }
    }

    private fun loadingDescriptionOff() {
        binding.apply {
            shimmerOn(
                binding.sflDescription,
                false
            )
            binding.iDescription.root.visibility = View.VISIBLE
        }
    }

    private fun loadingAreaRegistrationOff() {
        binding.apply {
            shimmerOn(
                binding.sflAreaRegistration,
                false
            )
            binding.iAreaRegistration.root.visibility = View.VISIBLE
        }
    }

    private fun loadingCreatedAndUpdateOff() {
        binding.apply {
            shimmerOn(
                binding.sflCreatedAndUpdated,
                false
            )
            binding.iCreatedAndUpdated.root.visibility = View.VISIBLE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCrimeLocation(crimeLocation: CrimeLocation?) {
        this.crimeLocation = crimeLocation
        binding.apply {
            setImageSlider(
                imageCrimeLocations = crimeLocation?.imageCrimeLocations,
                isCanEdit = true,
                onClickPreview = {
                    showImagePreview(it.imageCrimeLocationName,
                        PreferencesManager
                            .getInstance(this@DetailCrimeLocationActivity)
                            .getPreferences(URL_CONNECTION_API_IMAGE_CRIME_LOCATION))
                },
                onClickEdit = {
                    goToEdit(UPDATE_IMAGE)
                }
            )
            loadingImageSliderOff()
            iName.apply {
                tvId.text = crimeLocation?.crimeLocationId?.uppercase()
                tvName.text = crimeLocation?.crimeMapsName
            }
            loadingNameOff()
            iAddress.apply {
                tvAddress.text = crimeLocation?.crimeMapsAddress
                tvLatitude.text = makeSpannable(
                    text = getString(R.string.label_latitude_of,
                        crimeLocation?.crimeMapsLatitude))
                tvLongitude.text = makeSpannable(
                    text = getString(R.string.label_longitude_of,
                        crimeLocation?.crimeMapsLongitude)
                )
                btnSeeLocation.setOnClickListener {
                    Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.geo_google_maps,
                            crimeLocation?.crimeMapsLatitude,
                            crimeLocation?.crimeMapsLongitude))).apply {
                        setPackage("com.google.android.apps.maps")
                        resolveActivity(packageManager)
                        resultLauncher.launch(this)
                    }
                }
            }
            loadingAddressOff()
            iDescription.apply {
                tvDescription.text = crimeLocation?.crimeMapsDescription
            }
            loadingDescriptionOff()
            iAreaRegistration.rvListAreaRegistration.apply {
                layoutManager = LinearLayoutManager(this@DetailCrimeLocationActivity)
                adapter = registrationAreaAdapter?.apply {
                    setRegistrationAreas(getListAreaRegistration(crimeLocation))
                    notifyDataSetChanged()
                }
            }
            loadingAreaRegistrationOff()
            iCreatedAndUpdated.apply {
                tvCreated.text = makeSpannable(
                    isSpanBold = true,
                    getCreated(
                        crimeLocation?.createdBy,
                        crimeLocation?.createdDate
                    ),
                    color = Color.BLACK
                )
                tvUpdated.text = makeSpannable(
                    isSpanBold = true,
                    getUpdated(
                        crimeLocation?.updatedBy,
                        crimeLocation?.updatedDate
                    ),
                    color = Color.BLACK
                )
            }
            loadingCreatedAndUpdateOff()
        }
    }

    private fun getListAreaRegistration(crimeLocation: CrimeLocation?): ArrayList<RegistrationArea> {
        val areaRegistrations: ArrayList<RegistrationArea> = ArrayList()
        areaRegistrations.add(RegistrationArea(
            label = getString(R.string.label_registered_of,
                getString(R.string.province_menu)),
            areaRegistration = getContentArea(
                this@DetailCrimeLocationActivity,
                MenuAreaType.MENU_AREA_PROVINCE_ID,
                crimeLocation?.province,
                getString(R.string.province_menu)
            )))
        areaRegistrations.add(RegistrationArea(
            label = getString(R.string.label_registered_of,
                getString(R.string.city_menu)),
            areaRegistration = getContentArea(
                this@DetailCrimeLocationActivity,
                MenuAreaType.MENU_AREA_CITY_ID,
                crimeLocation?.city,
                getString(R.string.city_menu)
            )))
        areaRegistrations.add(RegistrationArea(
            label = getString(R.string.label_registered_of,
                getString(R.string.sub_district_menu)),
            areaRegistration = getContentArea(
                this@DetailCrimeLocationActivity,
                MenuAreaType.MENU_AREA_SUB_DISTRICT_ID,
                crimeLocation?.subDistrict,
                getString(R.string.sub_district_menu)
            )))
        areaRegistrations.add(RegistrationArea(
            label = getString(R.string.label_registered_of,
                getString(R.string.urban_village_menu)),
            areaRegistration = getContentArea(
                this@DetailCrimeLocationActivity,
                MenuAreaType.MENU_AREA_URBAN_VILLAGE_ID,
                crimeLocation?.urbanVillage,
                getString(R.string.urban_village_menu)
            )))
        return areaRegistrations
    }

    private fun goToEdit(crud: CRUD?) {
        goTo(
            FormCrimeLocationActivity(),
            hashMapOf(
                FROM_ACTIVITY to getNameOfActivity(DETAIL_CRIME_LOCATION),
                OPERATION_CRUD to crud!!,
                CRIME_LOCATION_MODEL to crimeLocation!!,

            )
        )
    }

    private fun disableAccess() {
        disableAccess(
            arrayOf(
                binding.iAppBarDetailCrimeLocation.ivBtnRightAppBar
            )
        )
        disableAccess(arrayOf(binding.btnBackDetailCrimeLocation))
    }

    private fun enableAccess() {
        enableAccess(
            arrayOf(
                binding.iAppBarDetailCrimeLocation.ivBtnRightAppBar
            )
        )
        enableAccess(arrayOf(binding.btnBackDetailCrimeLocation))
    }
}