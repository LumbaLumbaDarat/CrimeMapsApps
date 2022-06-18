package com.harifrizki.crimemapsapps.ui.module.area.list

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.*
import com.harifrizki.crimemapsapps.databinding.ActivityListOfAreaBinding
import com.harifrizki.crimemapsapps.model.*
import com.harifrizki.crimemapsapps.ui.adapter.AreaAdapter
import com.harifrizki.crimemapsapps.ui.component.activity.BaseActivity
import com.harifrizki.crimemapsapps.ui.module.area.form.FormAreaActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.MenuAreaType.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.ParamArea.*
import com.lumbalumbadrt.colortoast.ColorToast

class ListOfAreaActivity : BaseActivity() {
    private val binding by lazy {
        ActivityListOfAreaBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[ListOfAreaViewModel::class.java]
    }

    private var areaAdapter: AreaAdapter? = null

    private var appBarTitle: String? = null

    private var map: HashMap<String, Any>? = null
    private var menuAreaType: MenuAreaType? = MENU_NONE
    private var fromActivity: ActivityName? = null

    private var page: Page? = null
    private var isAfterCRUD: CRUD? = NONE
    private var doNotLoadData: Boolean? = true
    private var searchName: String? = EMPTY_STRING
    private var parentId: String? = EMPTY_STRING

    private var isReadOnly: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        menuAreaType = map!![AREA] as MenuAreaType
        isReadOnly = map?.getOrElse(IS_READ_ONLY) { false } as Boolean?
        fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        when (menuAreaType) {
            MENU_AREA_PROVINCE_ID -> {
                appBarTitle = getString(R.string.province_menu)
            }
            MENU_AREA_CITY_ID -> {
                appBarTitle = getString(R.string.city_menu)
            }
            MENU_AREA_SUB_DISTRICT_ID -> {
                appBarTitle = getString(R.string.sub_district_menu)
            }
            MENU_AREA_URBAN_VILLAGE_ID -> {
                appBarTitle = getString(R.string.urban_village_menu)
            }
            else -> {}
        }

        binding.apply {
            when (fromActivity) {
                DASHBOARD -> {
                    appBar(iAppBarListOfArea,
                        appBarTitle,
                        R.drawable.ic_round_location_city_24,
                        R.color.primary,
                        R.drawable.frame_background_secondary,
                        R.drawable.ic_round_add_24,
                        R.color.white,
                        R.drawable.button_primary_ripple_white,
                        onClick = {
                            goTo(
                                FormAreaActivity(),
                                hashMapOf(
                                    FROM_ACTIVITY to getNameOfActivity(LIST_OF_AREA),
                                    AREA to menuAreaType!!,
                                    OPERATION_CRUD to CREATE
                                )
                            )
                        })
                }
                FORM_CRIME_LOCATION,
                FORM_AREA -> {
                    appBar(
                        iAppBarListOfArea,
                        appBarTitle,
                        R.drawable.ic_round_location_city_24,
                        R.color.primary,
                        R.drawable.frame_background_secondary
                    )
                    parentId = map!![PARENT_AREA].toString()
                }
                else -> {}
            }
            createSearch(iSearchListOfArea)
            createRootView(rvListOfArea, sflListOfArea)
            createEmpty(iEmptyListOfArea)
            createNotificationAndOptionMessage(iNotificationListOfArea)
            initializeArea()
            srlListOfArea.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@ListOfAreaActivity, this
                )
                setOnRefreshListener(this@ListOfAreaActivity)
            }
            btnBackListOfArea.setOnClickListener { onBackPressed() }
        }
        search(
            getString(
                R.string.label_title_search_by,
                appBarTitle,
                getString(R.string.label_name)
            ),
            onSearch = {
                searchName = it
                area()
            }
        )
        area()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                area()
            else {
                if (showMessage(getMap(it.data))) {
                    isAfterCRUD = getMap(it.data)[OPERATION_CRUD] as CRUD
                    area()
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlListOfArea.isRefreshing = false
        area()
    }

    override fun onBackPressed() {
        if (fromActivity != FORM_AREA &&
            fromActivity != FORM_CRIME_LOCATION &&
            isReadOnly == false)
            onBackPressed(
                getNameOfActivity(LIST_OF_AREA),
                isAfterCRUD,
                menuAreaType
            )
        super.onBackPressed()
    }

    private val province = Observer<DataResource<ProvinceResponse>> {
        area(it as DataResource<Any?>)
    }

    private val city = Observer<DataResource<CityResponse>> {
        area(it as DataResource<Any?>)
    }

    private val subDistrict = Observer<DataResource<SubDistrictResponse>> {
        area(it as DataResource<Any?>)
    }

    private val urbanVillage = Observer<DataResource<UrbanVillageResponse>> {
        area(it as DataResource<Any?>)
    }

    private fun area(it: DataResource<Any?>) {
        when (it.responseStatus) {
            LOADING -> {
                disableAccess()
                loadingList(true)
            }
            SUCCESS -> {
                var message: Message? = null
                var size: Int? = null
                var page: Page? = null
                when (menuAreaType) {
                    MENU_AREA_PROVINCE_ID -> {
                        message = getModel(it.data, ProvinceResponse::class.java).message
                        size =
                            getModel(it.data, ProvinceResponse::class.java).provinceArrayList?.size
                        page = getModel(it.data, ProvinceResponse::class.java).page
                    }
                    MENU_AREA_CITY_ID -> {
                        message = getModel(it.data, CityResponse::class.java).message
                        size =
                            getModel(it.data, CityResponse::class.java).cityArrayList?.size
                        page = getModel(it.data, CityResponse::class.java).page
                    }
                    MENU_AREA_SUB_DISTRICT_ID -> {
                        message = getModel(it.data, SubDistrictResponse::class.java).message
                        size =
                            getModel(
                                it.data,
                                SubDistrictResponse::class.java
                            ).subDistrictArrayList?.size
                        page = getModel(it.data, SubDistrictResponse::class.java).page
                    }
                    MENU_AREA_URBAN_VILLAGE_ID -> {
                        message = getModel(it.data, UrbanVillageResponse::class.java).message
                        size =
                            getModel(
                                it.data,
                                UrbanVillageResponse::class.java
                            ).urbanVillageArrayList?.size
                        page = getModel(it.data, UrbanVillageResponse::class.java).page
                    }
                    else -> {}
                }
                if (isResponseSuccess(message)) {
                    if (size!! > getMinItemForValidList()) {
                        if (page?.totalContentSize!! >= getMaxItemInList()) {
                            if (doNotLoadData!!) {
                                showNotificationAndOptionMessageInformation(
                                    title = titleOverloadData(appBarTitle),
                                    message = messageOverloadData(
                                        page.totalContentSize,
                                        appBarTitle,
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
                                        binding.iSearchListOfArea.tieSearch.requestFocus()
                                    },
                                    onNegative = {
                                        doNotLoadData = false
                                        setArea(it.data)
                                    }
                                )
                                loadingList(
                                    isOn = false,
                                    isGetData = true,
                                    isOverloadData = true
                                )
                            }
                            else setArea(it.data)
                        }
                        else setArea(it.data)
                    }
                    else {
                        showEmpty(
                            getString(R.string.label_not_found),
                            getString(
                                R.string.message_empty_list,
                                appBarTitle
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
            ERROR -> {
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

    private fun area() {
        if (networkConnected()) {
            var pageNo: String? = INITIALIZE_PAGE_NO.toString()
            if (page != null) {
                if (page?.nextPage!!.isEmpty())
                    ColorToast.roundLineInfo(
                        this,
                        getString(R.string.app_name),
                        getString(
                            R.string.message_all_data_was_load,
                            appBarTitle
                        ),
                        Toast.LENGTH_LONG
                    )
                else pageNo = page?.nextPage!!
            }
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    viewModel.province(pageNo, Province().apply { provinceName = searchName })
                        .observe(this, province)
                }
                MENU_AREA_CITY_ID -> {
                    if (!parentId.isNullOrEmpty())
                        viewModel.cityByProvince(pageNo, City()
                            .apply {
                                province = Province().apply { provinceId = parentId }
                                cityName = searchName
                            })
                            .observe(this, city)
                    else viewModel.city(pageNo, City()
                        .apply { cityName = searchName })
                        .observe(this, city)
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    if (!parentId.isNullOrEmpty())
                        viewModel.subDistrictByCity(pageNo, SubDistrict()
                            .apply {
                                city = City().apply { cityId = parentId }
                                subDistrictName = searchName
                            })
                            .observe(this, subDistrict)
                    else viewModel.subDistrict(pageNo, SubDistrict()
                        .apply { subDistrictName = searchName })
                        .observe(this, subDistrict)
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    if (!parentId.isNullOrEmpty())
                        viewModel.urbanVillageBySubDistrict(pageNo, UrbanVillage()
                            .apply {
                                subDistrict = SubDistrict().apply { subDistrictId = parentId }
                                urbanVillageName = searchName
                            })
                            .observe(this, urbanVillage)
                    else viewModel.urbanVillage(
                        pageNo,
                        UrbanVillage().apply { urbanVillageName = searchName })
                        .observe(this, urbanVillage)
                }
                else -> {}
            }
        }
    }

    private val provinceDelete = Observer<DataResource<MessageResponse>> {
        areaDelete(it as DataResource<Any?>)
    }

    private val cityDelete = Observer<DataResource<MessageResponse>> {
        areaDelete(it as DataResource<Any?>)
    }

    private val subDistrictDelete = Observer<DataResource<MessageResponse>> {
        areaDelete(it as DataResource<Any?>)
    }

    private val urbanVillageDelete = Observer<DataResource<MessageResponse>> {
        areaDelete(it as DataResource<Any?>)
    }

    private fun areaDelete(it: DataResource<Any?>) {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_delete_append,
                            appBarTitle
                        )
                    )
                )
            }
            SUCCESS -> {
                val message: Message? = getModel(
                    it.data,
                    MessageResponse::class.java
                ).message
                dismissLoading()
                if (isResponseSuccess(message)) {
                    isAfterCRUD = DELETE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_delete,
                            appBarTitle
                        ),
                        message = message?.message,
                        onClick = {
                            dismissNotification()
                            area()
                        }
                    )
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun areaDelete(any: Any?) {
        if (networkConnected()) {
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    viewModel.provinceDelete(getModel(any, Province::class.java))
                        .observe(this, provinceDelete)
                }
                MENU_AREA_CITY_ID -> {
                    viewModel.cityDelete(getModel(any, City::class.java))
                        .observe(this, cityDelete)
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    viewModel.subDistrictDelete(getModel(any, SubDistrict::class.java))
                        .observe(this, subDistrictDelete)
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    viewModel.urbanVillageDelete(getModel(any, UrbanVillage::class.java))
                        .observe(this, urbanVillageDelete)
                }
                else -> {}
            }
        }
    }

    private fun initializeArea() {
        areaAdapter = AreaAdapter(
            context = this,
            menuAreaType = menuAreaType,
            isShimmer = false
        ).apply {
            onClickArea = {
                when (fromActivity) {
                    DASHBOARD -> {
                        goTo(
                            FormAreaActivity(),
                            hashMapOf(
                                FROM_ACTIVITY to getNameOfActivity(LIST_OF_AREA),
                                AREA to menuAreaType!!,
                                OPERATION_CRUD to READ,
                                AREA_MODEL to getParamArea(menuAreaType!!, it, ID)!!,
                            )
                        )
                    }
                    FORM_CRIME_LOCATION,
                    FORM_AREA -> {
                        if (isReadOnly!!)
                            goTo(
                                FormAreaActivity(),
                                hashMapOf(
                                    FROM_ACTIVITY to getNameOfActivity(LIST_OF_AREA),
                                    AREA to menuAreaType!!,
                                    OPERATION_CRUD to READ,
                                    AREA_MODEL to getParamArea(menuAreaType!!, it, ID)!!,
                                    IS_READ_ONLY to true
                                )
                            )
                        else onBackPressed(
                            hashMapOf(
                                FROM_ACTIVITY to getNameOfActivity(LIST_OF_AREA),
                                AREA to menuAreaType!!,
                                AREA_MODEL to it!!
                            ),
                            directOnBackPressed = true
                        )
                    }
                    else -> {}
                }
            }
            onClickDelete = {
                showOption(
                    titleOption = getString(
                        R.string.message_title_delete,
                        appBarTitle
                    ),
                    message = getString(
                        R.string.message_delete,
                        appBarTitle,
                        getParamArea(menuAreaType, it, NAME)
                    ),
                    titlePositive = getString(R.string.yes_delete),
                    titleNegative = getString(R.string.no),
                    colorButtonPositive = R.color.red,
                    onPositive = {
                        areaDelete(it)
                        dismissOption()
                    },
                )
            }
        }
        binding.apply {
            rvListOfArea.apply {
                layoutManager = LinearLayoutManager(this@ListOfAreaActivity)
                adapter = areaAdapter
            }
            rvListOfAreaShimmer.apply {
                layoutManager = LinearLayoutManager(this@ListOfAreaActivity)
                adapter = AreaAdapter(
                    context = this@ListOfAreaActivity,
                    menuAreaType = MENU_NONE,
                    isShimmer = true
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setArea(any: Any?) {
        when (menuAreaType) {
            MENU_AREA_PROVINCE_ID -> {
                val response = getModel(any, ProvinceResponse::class.java)
                page = response.page
                areaAdapter?.apply {
                    if (page?.pageNo == INITIALIZE_PAGE_NO)
                        setAreas(response.provinceArrayList as ArrayList<Any>)
                    else addAreas(response.provinceArrayList as ArrayList<Any>)
                    notifyDataSetChanged()
                    loadingList(
                        isOn = false,
                        isGetData = true,
                        isOverloadData = false
                    )
                }
            }
            MENU_AREA_CITY_ID -> {
                val response = getModel(any, CityResponse::class.java)
                page = response.page
                areaAdapter?.apply {
                    if (page?.pageNo == INITIALIZE_PAGE_NO)
                        setAreas(response.cityArrayList as ArrayList<Any>)
                    else addAreas(response.cityArrayList as ArrayList<Any>)
                    notifyDataSetChanged()
                    loadingList(
                        isOn = false,
                        isGetData = true,
                        isOverloadData = false
                    )
                }
            }
            MENU_AREA_SUB_DISTRICT_ID -> {
                val response = getModel(any, SubDistrictResponse::class.java)
                page = response.page
                areaAdapter?.apply {
                    if (page?.pageNo == INITIALIZE_PAGE_NO)
                        setAreas(response.subDistrictArrayList as ArrayList<Any>)
                    else addAreas(response.subDistrictArrayList as ArrayList<Any>)
                    notifyDataSetChanged()
                    loadingList(
                        isOn = false,
                        isGetData = true,
                        isOverloadData = false
                    )
                }
            }
            MENU_AREA_URBAN_VILLAGE_ID -> {
                val response = getModel(any, UrbanVillageResponse::class.java)
                page = response.page
                areaAdapter?.apply {
                    if (page?.pageNo == INITIALIZE_PAGE_NO)
                        setAreas(response.urbanVillageArrayList as ArrayList<Any>)
                    else addAreas(response.urbanVillageArrayList as ArrayList<Any>)
                    notifyDataSetChanged()
                    loadingList(
                        isOn = false,
                        isGetData = true,
                        isOverloadData = false
                    )
                }
            }
            else -> {}
        }
    }

    private fun disableAccess() {
        disableAccess(
            arrayOf(
                binding.iAppBarListOfArea.ivBtnRightAppBar,
                binding.iSearchListOfArea.ibSearch
            )
        )
        disableAccess(arrayOf(binding.btnBackListOfArea))
    }

    private fun enableAccess() {
        enableAccess(
            arrayOf(
                binding.iAppBarListOfArea.ivBtnRightAppBar,
                binding.iSearchListOfArea.ibSearch
            )
        )
        enableAccess(arrayOf(binding.btnBackListOfArea))
    }
}