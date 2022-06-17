package com.harifrizki.crimemapsapps.ui.module.crimelocation.list

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.CrimeLocationResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.databinding.ActivityListOfCrimeLocationBinding
import com.harifrizki.crimemapsapps.model.CrimeLocation
import com.harifrizki.crimemapsapps.model.Page
import com.harifrizki.crimemapsapps.ui.adapter.CrimeLocationAdapter
import com.harifrizki.crimemapsapps.ui.component.activity.BaseActivity
import com.harifrizki.crimemapsapps.ui.module.crimelocation.detail.DetailCrimeLocationActivity
import com.harifrizki.crimemapsapps.ui.module.crimelocation.form.FormCrimeLocationActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.lumbalumbadrt.colortoast.ColorToast

class ListOfCrimeLocationActivity : BaseActivity() {
    private val binding by lazy {
        ActivityListOfCrimeLocationBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[ListOfCrimeLocationViewModel::class.java]
    }

    private var crimeLocationAdapter: CrimeLocationAdapter? = null

    private var page: Page? = null
    private var isAfterCRUD: CRUD? = NONE
    private var doNotLoadData: Boolean? = true
    private var searchName: String? = EMPTY_STRING
    private var stateActive: String? = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        appBar(binding.iAppBarListOfCrimeLocation,
            getString(R.string.crime_location_menu),
            R.drawable.ic_round_location_on_24,
            R.color.primary,
            R.drawable.frame_background_secondary,
            R.drawable.ic_round_add_24,
            R.color.white,
            R.drawable.button_primary_ripple_white,
            onClick = {
                goTo(
                    FormCrimeLocationActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(LIST_OF_CRIME_LOCATION),
                        OPERATION_CRUD to CREATE
                    )
                )
            })
        binding.apply {
            initializeCrimeLocation()
            createSearch(iSearchListOfCrimeLocation)
            createRootView(rvListOfCrimeLocation, sflListOfCrimeLocation)
            createEmpty(iEmptyListOfCrimeLocation)
            createNotificationAndOptionMessage(iNotificationListOfCrimeLocation)
            srlListOfCrimeLocation.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@ListOfCrimeLocationActivity, this
                )
                setOnRefreshListener(this@ListOfCrimeLocationActivity)
            }
            btnBackListOfCrimeLocation.setOnClickListener { onBackPressed() }
        }
        search(
            getString(
                R.string.label_title_search_by,
                getString(R.string.crime_location_menu),
                getString(R.string.label_name)
            ),
            onSearch = {
                searchName = it
                crimeLocation()
            }
        )
        crimeLocation()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                crimeLocation()
            else {
                if (showMessage(getMap(it.data))) {
                    isAfterCRUD = getMap(it.data)[OPERATION_CRUD] as CRUD
                    crimeLocation()
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlListOfCrimeLocation.isRefreshing = false
        crimeLocation()
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(LIST_OF_CRIME_LOCATION),
            isAfterCRUD
        )
        super.onBackPressed()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val crimeLocation = Observer<DataResource<CrimeLocationResponse>> {
        when (it.responseStatus) {
            ResponseStatus.LOADING -> {
                disableAccess()
                loadingList(true)
            }
            ResponseStatus.SUCCESS -> {
                if (isResponseSuccess(it.data?.message)) {
                    if (it.data?.crimeLocationArrayList?.size!! > getMinItemForValidList()) {
                        if (it.data.page?.totalContentSize!! >= getMaxItemInList()) {
                            if (doNotLoadData!!) {
                                showNotificationAndOptionMessageInformation(
                                    title = titleOverloadData(getString(R.string.crime_location_menu)),
                                    message = messageOverloadData(
                                        it.data.page?.totalContentSize,
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
                                        binding.iSearchListOfCrimeLocation.tieSearch.requestFocus()
                                    },
                                    onNegative = {
                                        doNotLoadData = false
                                        setCrimeLocationAdapter(it.data)
                                    }
                                )
                                loadingList(
                                    isOn = false,
                                    isGetData = true,
                                    isOverloadData = true
                                )
                            }
                            else setCrimeLocationAdapter(it.data)
                        }
                        else setCrimeLocationAdapter(it.data)
                    }
                    else {
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

    private fun crimeLocation() {
        if (networkConnected()) {
            var pageNo: String? = INITIALIZE_PAGE_NO.toString()
            if (page != null) {
                if (page?.nextPage!!.isEmpty())
                    ColorToast.roundLineInfo(
                        this,
                        getString(R.string.app_name),
                        getString(
                            R.string.message_all_data_was_load,
                            getString(R.string.admin_menu)
                        ),
                        Toast.LENGTH_LONG
                    )
                else pageNo = page?.nextPage!!
            }
            viewModel.crimeLocation(pageNo, CrimeLocation().apply { crimeMapsName = searchName }).observe(this, crimeLocation)
        }
    }

    private val crimeLocationDelete = Observer<DataResource<MessageResponse>> {
        when (it.responseStatus) {
            ResponseStatus.LOADING -> {
                disableAccess()
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_delete_append,
                            getString(R.string.crime_location_menu)
                        )
                    )
                )
            }
            ResponseStatus.SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = DELETE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_delete,
                            getString(R.string.crime_location_menu)),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            crimeLocation()
                        }
                    )
                }
                enableAccess()
            }
            ResponseStatus.ERROR -> {
                enableAccess()
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun crimeLocationDelete(crimeLocation: CrimeLocation?) {
        if (networkConnected()) {
            viewModel.crimeLocationDelete(crimeLocation).observe(this, crimeLocationDelete)
        }
    }

    private fun initializeCrimeLocation() {
        crimeLocationAdapter = CrimeLocationAdapter(
            context = this,
            isShimmer = false
        ).apply {
            onClickCrimeLocation = {
                goTo(
                    DetailCrimeLocationActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(DETAIL_CRIME_LOCATION),
                        OPERATION_CRUD to READ,
                        CRIME_LOCATION_MODEL to it!!
                    )
                )
            }
            onClickDelete = {
                showOption(
                    titleOption = getString(
                        R.string.message_title_delete,
                        getString(R.string.crime_location_menu)
                    ),
                    message = getString(
                        R.string.message_delete,
                        getString(R.string.crime_location_menu), it?.crimeMapsName
                    ),
                    titlePositive = getString(R.string.yes_delete),
                    titleNegative = getString(R.string.no),
                    colorButtonPositive = R.color.red,
                    onPositive = {
                        dismissOption()
                        crimeLocationDelete(
                            CrimeLocation().apply { crimeLocationId = it?.crimeLocationId })
                    },
                )
            }
        }
        binding.apply {
            rvListOfCrimeLocation.apply {
                layoutManager = LinearLayoutManager(this@ListOfCrimeLocationActivity)
                adapter = crimeLocationAdapter
            }
            rvListOfCrimeLocationShimmer.apply {
                layoutManager = LinearLayoutManager(this@ListOfCrimeLocationActivity)
                adapter = CrimeLocationAdapter(
                    context = this@ListOfCrimeLocationActivity,
                    isShimmer = true
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCrimeLocationAdapter(crimeLocationResponse: CrimeLocationResponse?) {
        page = crimeLocationResponse?.page
        crimeLocationAdapter?.apply {
            if (crimeLocationResponse?.page?.pageNo == INITIALIZE_PAGE_NO)
                setCrimeLocations(crimeLocationResponse?.crimeLocationArrayList)
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
                binding.iAppBarListOfCrimeLocation.ivBtnRightAppBar,
                binding.iSearchListOfCrimeLocation.ibSearch
            )
        )
        disableAccess(arrayOf(binding.btnBackListOfCrimeLocation))
    }

    private fun enableAccess() {
        enableAccess(
            arrayOf(
                binding.iAppBarListOfCrimeLocation.ivBtnRightAppBar,
                binding.iSearchListOfCrimeLocation.ibSearch
            )
        )
        enableAccess(arrayOf(binding.btnBackListOfCrimeLocation))
    }
}