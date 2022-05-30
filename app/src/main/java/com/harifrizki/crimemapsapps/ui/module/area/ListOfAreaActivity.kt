package com.harifrizki.crimemapsapps.ui.module.area

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.ProvinceResponse
import com.harifrizki.crimemapsapps.databinding.ActivityListOfAreaBinding
import com.harifrizki.crimemapsapps.model.Message
import com.harifrizki.crimemapsapps.model.Page
import com.harifrizki.crimemapsapps.ui.adapter.AdminAdapter
import com.harifrizki.crimemapsapps.ui.adapter.AreaAdapter
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.MenuAreaType.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
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
    private var isAfterCRUD: CRUD? = CRUD.NONE
    private var doNotLoadData: Boolean? = true
    private var searchName: String? = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        menuAreaType = map!![AREA] as MenuAreaType
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

        appBar(binding.iAppBarListOfArea,
            appBarTitle,
            R.drawable.ic_round_location_city_24,
            R.color.primary,
            R.drawable.frame_background_secondary,
            R.drawable.ic_round_add_24,
            R.color.white,
            R.drawable.button_primary_ripple_white,
            onClick = {

            })
        binding.apply {
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
    { }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlListOfArea.isRefreshing = false
        area()
    }

    override fun onBackPressed() {
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

    private fun area(it: DataResource<Any?>) {
        when (it.responseStatus) {
            LOADING -> {
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
                    else -> {}
                }
                if (isResponseSuccess(message)) {
                    if (size!! > ZERO) {
                        if (page?.totalContentSize!! >= 1) {
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
                                        R.string.label_title_search_by,
                                        appBarTitle,
                                        getString(
                                            R.string.label_plus_two_string,
                                            appBarTitle,
                                            getString(R.string.admin_menu)
                                        )
                                    ),
                                    buttonNegative = getString(
                                        R.string.label_show_overload,
                                        appBarTitle
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
                            } else setArea(it.data)
                        } else setArea(it.data)
                    } else {
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
            }
            ERROR -> {
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
                    viewModel.province(pageNo, searchName)
                        .observe(this, province)
                }
                MENU_AREA_CITY_ID -> {}
                MENU_AREA_SUB_DISTRICT_ID -> {}
                MENU_AREA_URBAN_VILLAGE_ID -> {}
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
            onClickArea = {}
            onClickDelete = {
                showOption(
                    titleOption = getString(
                        R.string.message_title_delete,
                        appBarTitle
                    ),
                    message = getString(
                        R.string.message_delete,
                        appBarTitle,
                        getAreaName(menuAreaType, it)
                    ),
                    titlePositive = getString(R.string.yes_delete),
                    titleNegative = getString(R.string.no),
                    colorButtonPositive = R.color.red,
                    onPositive = {
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
                val provinceResponse = getModel(any, ProvinceResponse::class.java)
                page = provinceResponse.page
                areaAdapter?.apply {
                    if (page?.pageNo == INITIALIZE_PAGE_NO)
                        setAreas(provinceResponse.provinceArrayList as ArrayList<Any>)
                    else addAreas(provinceResponse.provinceArrayList as ArrayList<Any>)
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
}