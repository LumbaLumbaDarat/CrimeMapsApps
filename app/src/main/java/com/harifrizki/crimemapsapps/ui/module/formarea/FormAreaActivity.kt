package com.harifrizki.crimemapsapps.ui.module.formarea

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.CityResponse
import com.harifrizki.crimemapsapps.data.remote.response.ProvinceResponse
import com.harifrizki.crimemapsapps.data.remote.response.SubDistrictResponse
import com.harifrizki.crimemapsapps.data.remote.response.UrbanVillageResponse
import com.harifrizki.crimemapsapps.databinding.ActivityFormAreaBinding
import com.harifrizki.crimemapsapps.model.*
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.ui.component.ParentArea
import com.harifrizki.crimemapsapps.ui.module.area.ListOfAreaActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.MenuAreaType.*
import com.harifrizki.crimemapsapps.utils.ParentAreaAction.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*

class FormAreaActivity : BaseActivity() {
    private val binding by lazy {
        ActivityFormAreaBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[FormAreaViewModel::class.java]
    }

    private var appBarTitle: String? = null
    private var parent: String? = null
    private var child: String? = null
    private var buttonSeeChildTitle: String? = null
    private var areaId: String? = null

    private var parentAreaProvince: ParentArea? = null
    private var parentAreaCity: ParentArea? = null
    private var parentAreaSubDistrict: ParentArea? = null
    private var parentAreaUrbanVillage: ParentArea? = null

    private var map: HashMap<String, Any>? = null
    private var fromActivity: ActivityName? = null
    private var menuAreaType: MenuAreaType? = null
    private var crud: CRUD? = null
    private var isAfterCRUD: CRUD? = NONE

    private var provinceAsParent: Province? = null
    private var cityAsParent: City? = null
    private var subDistrictAsParent: SubDistrict? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        menuAreaType = map!![AREA] as MenuAreaType
        crud = map!![OPERATION_CRUD] as CRUD
        areaId = map!![AREA_MODEL].toString()

        when (menuAreaType) {
            MENU_AREA_PROVINCE_ID -> {
                appBarTitle = getString(R.string.province_menu)
                child = getString(R.string.city_menu)
                buttonSeeChildTitle = getString(
                    R.string.label_see_child,
                    child,
                    appBarTitle
                )
                initializeForm()
                initializeProvince(null)
            }
            MENU_AREA_CITY_ID -> {
                appBarTitle = getString(R.string.city_menu)
                parent = getString(R.string.province_menu)
                child = getString(R.string.sub_district_menu)
                buttonSeeChildTitle = getString(
                    R.string.label_see_child,
                    child,
                    appBarTitle
                )
                initializeForm()
                initializeCity(null)
            }
            MENU_AREA_SUB_DISTRICT_ID -> {
                appBarTitle = getString(R.string.sub_district_menu)
                parent = getString(R.string.city_menu)
                child = getString(R.string.urban_village_menu)
                buttonSeeChildTitle = getString(
                    R.string.label_see_child,
                    child,
                    appBarTitle
                )
                initializeForm()
                initializeSubDistrict(null)
            }
            MENU_AREA_URBAN_VILLAGE_ID -> {
                appBarTitle = getString(R.string.urban_village_menu)
                parent = getString(R.string.sub_district_menu)
                initializeForm()
                initializeUrbanVillage(null)
            }
            else -> {}
        }

        binding.apply {
            prepareResetTextInputEditText(
                arrayOf(
                    tieNameArea
                )
            )
            srlFormArea.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@FormAreaActivity, this
                )
                setOnRefreshListener(this@FormAreaActivity)
            }
            btnBackArea.setOnClickListener { onBackPressed() }
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!) {
                when (menuAreaType) {
                    MENU_AREA_PROVINCE_ID -> {
                        when (crud) {
                            READ -> {
                                areaDetail()
                            }
                            CREATE -> {
                                resetTextInputEditText()
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            } else {
                val map = getMap(it.data)
                when (getEnumActivityName(map[FROM_ACTIVITY].toString())) {
                    LIST_OF_AREA -> {
                        when (map[AREA] as MenuAreaType) {
                            MENU_AREA_PROVINCE_ID -> {
                                provinceAsParent = getModel(map[AREA_MODEL], Province::class.java)
                                parentAreaProvince?.apply {
                                    setContent(provinceAsParent?.provinceName)
                                }
                                cityAsParent = null
                                parentAreaCity?.apply {
                                    setContent(
                                        getString(
                                            R.string.label_not_yet,
                                            getString(R.string.city_menu)
                                        )
                                    )
                                }
                                subDistrictAsParent = null
                                parentAreaSubDistrict?.apply {
                                    setContent(
                                        getString(
                                            R.string.label_not_yet,
                                            getString(R.string.sub_district_menu)
                                        )
                                    )
                                }
                            }
                            MENU_AREA_CITY_ID -> {
                                cityAsParent = getModel(map[AREA_MODEL], City::class.java)
                                parentAreaCity?.apply {
                                    setContent(cityAsParent?.cityName)
                                }
                                subDistrictAsParent = null
                                parentAreaSubDistrict?.apply {
                                    setContent(
                                        getString(
                                            R.string.label_not_yet,
                                            getString(R.string.sub_district_menu)
                                        )
                                    )
                                }
                            }
                            MENU_AREA_SUB_DISTRICT_ID -> {
                                subDistrictAsParent =
                                    getModel(map[AREA_MODEL], SubDistrict::class.java)
                                parentAreaSubDistrict?.apply {
                                    setContent(subDistrictAsParent?.subDistrictName)
                                }
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlFormArea.isRefreshing = false
        if (crud == READ) areaDetail()
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(FORM_AREA),
            isAfterCRUD,
            menuAreaType
        )
        super.onBackPressed()
    }

    private val provinceDetail = Observer<DataResource<ProvinceResponse>> {
        areaDetail(it as DataResource<Any?>)
    }

    private val cityDetail = Observer<DataResource<CityResponse>> {
        areaDetail(it as DataResource<Any?>)
    }

    private val subDistrictDetail = Observer<DataResource<SubDistrictResponse>> {
        areaDetail(it as DataResource<Any?>)
    }

    private val urbanVillageDetail = Observer<DataResource<UrbanVillageResponse>> {
        areaDetail(it as DataResource<Any?>)
    }

    private fun areaDetail(it: DataResource<Any?>) {
        when (it.responseStatus) {
            LOADING -> {
                loadingArea(true, null)
                loadingTextInput(true)
                loadingCreatedAndUpdate(true)
            }
            SUCCESS -> {
                var message: Message? = null
                when (menuAreaType) {
                    MENU_AREA_PROVINCE_ID -> {
                        message = getModel(it.data, ProvinceResponse::class.java).message
                    }
                    MENU_AREA_CITY_ID -> {
                        message = getModel(it.data, CityResponse::class.java).message
                    }
                    MENU_AREA_SUB_DISTRICT_ID -> {
                        message = getModel(it.data, SubDistrictResponse::class.java).message
                    }
                    MENU_AREA_URBAN_VILLAGE_ID -> {
                        message = getModel(it.data, UrbanVillageResponse::class.java).message
                    }
                    else -> {}
                }
                if (isResponseSuccess(message)) {
                    loadingCreatedAndUpdate(false)
                    loadingTextInput(false)
                    loadingArea(false, it.data)
                }
            }
            ERROR -> {
                loadingArea(false, null)
                loadingTextInput(false)
                loadingCreatedAndUpdate(false)
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun areaDetail() {
        if (networkConnected()) {
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    viewModel.provinceDetail(Province().apply { provinceId = areaId })
                        .observe(this, provinceDetail)
                }
                MENU_AREA_CITY_ID -> {
                    viewModel.cityDetail(City().apply { cityId = areaId })
                        .observe(this, cityDetail)
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    viewModel.subDistrictDetail(SubDistrict().apply { subDistrictId = areaId })
                        .observe(this, subDistrictDetail)
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    viewModel.urbanVillageDetail(UrbanVillage().apply { urbanVillageId = areaId })
                        .observe(this, urbanVillageDetail)
                }
                else -> {}
            }
        }
    }

    private val provinceAdd = Observer<DataResource<ProvinceResponse>> {
        areaAdd(it as DataResource<Any?>)
    }

    private val cityAdd = Observer<DataResource<CityResponse>> {
        areaAdd(it as DataResource<Any?>)
    }

    private val subDistrictAdd = Observer<DataResource<SubDistrictResponse>> {
        areaAdd(it as DataResource<Any?>)
    }

    private val urbanVillageAdd = Observer<DataResource<UrbanVillageResponse>> {
        areaAdd(it as DataResource<Any?>)
    }

    private fun areaAdd(it: DataResource<Any?>) {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_add_append,
                            appBarTitle
                        )
                    )
                )
            }
            SUCCESS -> {
                var message: Message? = null
                when (menuAreaType) {
                    MENU_AREA_PROVINCE_ID -> {
                        message = getModel(it.data, ProvinceResponse::class.java).message
                    }
                    MENU_AREA_CITY_ID -> {
                        message = getModel(it.data, CityResponse::class.java).message
                    }
                    MENU_AREA_SUB_DISTRICT_ID -> {
                        message = getModel(it.data, SubDistrictResponse::class.java).message
                    }
                    MENU_AREA_URBAN_VILLAGE_ID -> {
                        message = getModel(it.data, UrbanVillageResponse::class.java).message
                    }
                    else -> {}
                }
                dismissLoading()
                if (isResponseSuccess(message)) {
                    isAfterCRUD = CREATE
                    showSuccess(
                        titleNotification = getString(R.string.message_success_add, appBarTitle),
                        message = message?.message,
                        onClick = {
                            binding.tieNameArea.text?.clear()
                            provinceAsParent = null
                            cityAsParent = null
                            subDistrictAsParent = null

                            parentAreaProvince?.apply {
                                setContent(
                                    getString(
                                        R.string.label_not_yet,
                                        getString(R.string.province_menu)
                                    )
                                )
                            }
                            parentAreaCity?.apply {
                                setContent(
                                    getString(
                                        R.string.label_not_yet,
                                        getString(R.string.city_menu)
                                    )
                                )
                            }
                            parentAreaSubDistrict?.apply {
                                setContent(
                                    getString(
                                        R.string.label_not_yet,
                                        getString(R.string.sub_district_menu)
                                    )
                                )
                            }

                            dismissNotification()
                        })
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun areaAdd(any: Any?) {
        if (networkConnected()) {
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    viewModel.provinceAdd(getModel(any, Province::class.java))
                        .observe(this, provinceAdd)
                }
                MENU_AREA_CITY_ID -> {
                    viewModel.cityAdd(getModel(any, City::class.java))
                        .observe(this, cityAdd)
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    viewModel.subDistrictAdd(getModel(any, SubDistrict::class.java))
                        .observe(this, subDistrictAdd)
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    viewModel.urbanVillageAdd(getModel(any, UrbanVillage::class.java))
                        .observe(this, urbanVillageAdd)
                }
                else -> {}
            }
        }
    }

    private fun initializeForm() {
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iParentAreaOneShimmer.tvParentArea,
                binding.iParentAreaOneShimmer.tvTitleParentArea,

                binding.iParentAreaTwoShimmer.tvParentArea,
                binding.iParentAreaTwoShimmer.tvTitleParentArea,

                binding.iParentAreaThirdShimmer.tvParentArea,
                binding.iParentAreaThirdShimmer.tvTitleParentArea,

                binding.iParentAreaFourthShimmer.tvParentArea,
                binding.iParentAreaFourthShimmer.tvTitleParentArea,
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iParentAreaOneShimmer.ivActionLeftParentArea,
                binding.iParentAreaOneShimmer.ivActionRightParentArea,

                binding.iParentAreaTwoShimmer.ivActionLeftParentArea,
                binding.iParentAreaTwoShimmer.ivActionRightParentArea,

                binding.iParentAreaThirdShimmer.ivActionLeftParentArea,
                binding.iParentAreaThirdShimmer.ivActionRightParentArea,

                binding.iParentAreaFourthShimmer.ivActionLeftParentArea,
                binding.iParentAreaFourthShimmer.ivActionRightParentArea
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.tieNameAreaShimmer
            )
        )
        layoutStartDrawableShimmer(
            arrayOf(
                binding.iParentAreaOneShimmer.llBackgroundContent,
                binding.iParentAreaTwoShimmer.llBackgroundContent,
                binding.iParentAreaThirdShimmer.llBackgroundContent,
                binding.iParentAreaFourthShimmer.llBackgroundContent
            ), this
        )

        parentAreaProvince = ParentArea().apply {
            init(this@FormAreaActivity, binding.iParentAreaOne)
        };
        parentAreaCity = ParentArea().apply {
            init(this@FormAreaActivity, binding.iParentAreaTwo)
        }
        parentAreaSubDistrict = ParentArea().apply {
            init(this@FormAreaActivity, binding.iParentAreaThird)
        }
        parentAreaUrbanVillage = ParentArea().apply {
            init(this@FormAreaActivity, binding.iParentAreaFourth)
        }

        when (crud) {
            READ -> {
                binding.apply {
                    appBar(
                        iAppBarFormArea,
                        appBarTitle,
                        R.drawable.ic_round_location_city_24,
                        R.color.primary,
                        R.drawable.frame_background_secondary,
                        R.drawable.ic_round_edit_24,
                        R.color.white,
                        R.drawable.button_primary_ripple_white,
                        onClick = {

                        })
                    tilNameArea.hint = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_name),
                        appBarTitle
                    )
                    btnChildArea.text = buttonSeeChildTitle
                    btnSubmitArea.visibility = View.GONE
                    btnBackArea.text = getString(R.string.back)
                }
            }
            CREATE -> {
                binding.apply {
                    appBar(
                        iAppBarFormArea,
                        getString(R.string.label_add, appBarTitle),
                        R.drawable.ic_round_location_city_24,
                        R.color.primary,
                        R.drawable.frame_background_secondary
                    )
                    btnChildArea.visibility = View.GONE
                    iCreatedAndUpdateArea.root.visibility = View.GONE
                    tilNameArea.hint = getString(
                        R.string.label_enter_of,
                        getString(R.string.label_name),
                        appBarTitle
                    )
                    btnSubmitArea.text = getString(R.string.label_add, appBarTitle)
                    btnBackArea.text = getString(R.string.cancel)
                }
            }
            else -> {}
        }
    }

    private fun initializeProvince(provinceResponse: ProvinceResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = provinceResponse?.province?.provinceId?.uppercase()
                    iconRightAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_RIGHT
                    create()
                }
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaOne,
                    ), View.VISIBLE
                )
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaTwo,
                        binding.iParentAreaThird,
                        binding.iParentAreaFourth,
                    ), View.GONE
                )

                imageViewVisibility(
                    arrayOf(
                        binding.iParentAreaOneShimmer.ivActionLeftParentArea
                    ), View.GONE
                )
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaTwoShimmer,
                        binding.iParentAreaThirdShimmer,
                        binding.iParentAreaFourthShimmer,
                    ), View.GONE
                )

                binding.apply {
                    tieNameArea.apply {
                        setText(provinceResponse?.province?.provinceName)
                        isEnabled = false
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                provinceResponse?.province?.createdBy,
                                provinceResponse?.province?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                provinceResponse?.province?.updatedBy,
                                provinceResponse?.province?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
                if (provinceResponse?.province == null) areaDetail()
                else buttonVisibility(
                    arrayOf(
                        binding.btnChildArea,
                        binding.btnBackArea
                    ), View.VISIBLE
                )
            }
            CREATE -> {
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaOne,
                        binding.iParentAreaTwo,
                        binding.iParentAreaThird,
                        binding.iParentAreaFourth,
                    ), View.GONE
                )
                binding.btnSubmitArea.setOnClickListener {
                    if (!textInputEditTextIsEmpty(binding.tieNameArea))
                        areaAdd(
                            Province().apply {
                                provinceName = binding.tieNameArea.text.toString().trim()
                                createdBy = Admin().apply {
                                    adminId = PreferencesManager
                                        .getInstance(this@FormAreaActivity)
                                        .getPreferences(
                                            LOGIN_MODEL,
                                            Admin::class.java
                                        ).adminId
                                }
                            })
                    else showWarning(
                        message = getString(
                            R.string.message_error_empty,
                            getString(
                                R.string.label_plus_two_string,
                                getString(R.string.label_name),
                                appBarTitle
                            )
                        )
                    )
                }
            }
            else -> {}
        }
    }

    private fun initializeCity(cityResponse: CityResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        parent
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_PROVINCE_ID,
                        cityResponse?.city?.province,
                        getString(R.string.province_menu)
                    )
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_LEFT
                    create()
                }
                parentAreaCity?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = cityResponse?.city?.cityId?.uppercase()
                    iconRightAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_RIGHT
                    create()
                }
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaOne,
                        binding.iParentAreaTwo,
                    ), View.VISIBLE
                )
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaThird,
                        binding.iParentAreaFourth,
                    ), View.GONE
                )

                imageViewVisibility(
                    arrayOf(
                        binding.iParentAreaOneShimmer.ivActionRightParentArea,
                        binding.iParentAreaTwoShimmer.ivActionLeftParentArea
                    ), View.GONE
                )
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaThirdShimmer,
                        binding.iParentAreaFourthShimmer,
                    ), View.GONE
                )
                binding.apply {
                    tieNameArea.apply {
                        setText(cityResponse?.city?.cityName)
                        isEnabled = false
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                cityResponse?.city?.createdBy,
                                cityResponse?.city?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                cityResponse?.city?.updatedBy,
                                cityResponse?.city?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
                if (cityResponse?.city == null) areaDetail()
                else buttonVisibility(
                    arrayOf(
                        binding.btnChildArea,
                        binding.btnBackArea
                    ), View.VISIBLE
                )
            }
            CREATE -> {
                parentAreaProvince?.apply {
                    init(this@FormAreaActivity, binding.iParentAreaOne)
                    title = getString(
                        R.string.message_choose_parent_for_this_area,
                        parent,
                        appBarTitle
                    )
                    content = getString(R.string.label_not_yet, parent)
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ACTION_LEFT
                    onActionLeft = {
                        goTo(
                            ListOfAreaActivity(),
                            hashMapOf(
                                FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                                AREA to MENU_AREA_PROVINCE_ID
                            )
                        )
                    }
                    create()
                }
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaTwo,
                        binding.iParentAreaThird,
                        binding.iParentAreaFourth,
                    ), View.GONE
                )
                binding.btnSubmitArea.setOnClickListener {
                    if (provinceAsParent != null) {
                        if (!textInputEditTextIsEmpty(binding.tieNameArea))
                            areaAdd(
                                City().apply {
                                    province = Province().apply {
                                        provinceId = provinceAsParent?.provinceId
                                    }
                                    cityName = binding.tieNameArea.text.toString().trim()
                                    createdBy = Admin().apply {
                                        adminId = PreferencesManager
                                            .getInstance(this@FormAreaActivity)
                                            .getPreferences(
                                                LOGIN_MODEL,
                                                Admin::class.java
                                            ).adminId
                                    }
                                })
                        else showWarning(
                            message = getString(
                                R.string.message_error_empty,
                                getString(
                                    R.string.label_plus_two_string,
                                    getString(R.string.label_name),
                                    appBarTitle
                                )
                            )
                        )
                    } else showWarning(
                        message = getString(
                            R.string.message_error_empty, parent
                        )
                    )
                }
            }
            else -> {}
        }
    }

    private fun initializeSubDistrict(subDistrictResponse: SubDistrictResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        getString(R.string.province_menu)
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_PROVINCE_ID,
                        subDistrictResponse?.subDistrict?.province,
                        getString(R.string.province_menu)
                    )
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_LEFT
                    create()
                }
                parentAreaCity?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        parent
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_CITY_ID,
                        subDistrictResponse?.subDistrict?.city,
                        getString(R.string.city_menu)
                    )
                    iconRightAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_RIGHT
                    create()
                }
                parentAreaSubDistrict?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = subDistrictResponse?.subDistrict?.subDistrictId?.uppercase()
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_LEFT
                    create()
                }
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaOne,
                        binding.iParentAreaTwo,
                        binding.iParentAreaThird,
                    ), View.VISIBLE
                )
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaFourth,
                    ), View.GONE
                )

                imageViewVisibility(
                    arrayOf(
                        binding.iParentAreaOneShimmer.ivActionRightParentArea,
                        binding.iParentAreaTwoShimmer.ivActionLeftParentArea,
                        binding.iParentAreaThirdShimmer.ivActionRightParentArea,
                    ), View.GONE
                )
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaFourthShimmer,
                    ), View.GONE
                )
                binding.apply {
                    tieNameArea.apply {
                        setText(subDistrictResponse?.subDistrict?.subDistrictName)
                        isEnabled = false
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                subDistrictResponse?.subDistrict?.createdBy,
                                subDistrictResponse?.subDistrict?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                subDistrictResponse?.subDistrict?.updatedBy,
                                subDistrictResponse?.subDistrict?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
                if (subDistrictResponse?.subDistrict == null) areaDetail()
                else buttonVisibility(
                    arrayOf(
                        binding.btnChildArea,
                        binding.btnBackArea
                    ), View.VISIBLE
                )
            }
            CREATE -> {
                parentAreaProvince?.apply {
                    init(this@FormAreaActivity, binding.iParentAreaOne)
                    title = getString(
                        R.string.message_choose_parent_for_this_area,
                        getString(R.string.province_menu),
                        appBarTitle
                    )
                    content = getString(R.string.label_not_yet, getString(R.string.province_menu))
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ACTION_LEFT
                    onActionLeft = {
                        goTo(
                            ListOfAreaActivity(),
                            hashMapOf(
                                FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                                AREA to MENU_AREA_PROVINCE_ID
                            )
                        )
                    }
                    create()
                }
                parentAreaCity?.apply {
                    init(this@FormAreaActivity, binding.iParentAreaTwo)
                    title = getString(
                        R.string.message_choose_parent_for_this_area,
                        parent,
                        appBarTitle
                    )
                    content = getString(R.string.label_not_yet, parent)
                    iconRightAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ACTION_RIGHT
                    onActionRight = {
                        if (provinceAsParent != null)
                            goTo(
                                ListOfAreaActivity(),
                                hashMapOf(
                                    FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                                    AREA to MENU_AREA_CITY_ID,
                                    PARENT_AREA to provinceAsParent?.provinceId!!
                                )
                            )
                        else showWarning(
                            message = getString(
                                R.string.message_error_empty, getString(R.string.province_menu)
                            )
                        )
                    }
                    create()
                }
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaThird,
                        binding.iParentAreaFourth,
                    ), View.GONE
                )
                binding.btnSubmitArea.setOnClickListener {
                    if (provinceAsParent != null) {
                        if (cityAsParent != null) {
                            if (!textInputEditTextIsEmpty(binding.tieNameArea))
                                areaAdd(
                                    SubDistrict().apply {
                                        province = Province().apply {
                                            provinceId = provinceAsParent?.provinceId
                                        }
                                        city = City().apply {
                                            cityId = cityAsParent?.cityId
                                        }
                                        subDistrictName = binding.tieNameArea.text.toString().trim()
                                        createdBy = Admin().apply {
                                            adminId = PreferencesManager
                                                .getInstance(this@FormAreaActivity)
                                                .getPreferences(
                                                    LOGIN_MODEL,
                                                    Admin::class.java
                                                ).adminId
                                        }
                                    })
                            else showWarning(
                                message = getString(
                                    R.string.message_error_empty,
                                    getString(
                                        R.string.label_plus_two_string,
                                        getString(R.string.label_name),
                                        appBarTitle
                                    )
                                )
                            )
                        } else showWarning(
                            message = getString(
                                R.string.message_error_empty, parent
                            )
                        )
                    } else showWarning(
                        message = getString(
                            R.string.message_error_empty, getString(R.string.province_menu)
                        )
                    )
                }
            }
            else -> {}
        }
    }

    private fun initializeUrbanVillage(urbanVillageResponse: UrbanVillageResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        getString(R.string.province_menu)
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_PROVINCE_ID,
                        urbanVillageResponse?.urbanVillage?.province,
                        getString(R.string.province_menu)
                    )
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_LEFT
                    create()
                }
                parentAreaCity?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        getString(R.string.city_menu)
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_CITY_ID,
                        urbanVillageResponse?.urbanVillage?.city,
                        getString(R.string.city_menu)
                    )
                    iconRightAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_RIGHT
                    create()
                }
                parentAreaSubDistrict?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        appBarTitle,
                        parent
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_SUB_DISTRICT_ID,
                        urbanVillageResponse?.urbanVillage?.subDistrict,
                        getString(R.string.sub_district_menu)
                    )
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_LEFT
                    create()
                }
                parentAreaUrbanVillage?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = urbanVillageResponse?.urbanVillage?.urbanVillageId?.uppercase()
                    iconRightAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ICON_RIGHT
                    create()
                }
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaOne,
                        binding.iParentAreaTwo,
                        binding.iParentAreaThird,
                        binding.iParentAreaFourth,
                    ), View.VISIBLE
                )

                imageViewVisibility(
                    arrayOf(
                        binding.iParentAreaOneShimmer.ivActionRightParentArea,
                        binding.iParentAreaTwoShimmer.ivActionLeftParentArea,
                        binding.iParentAreaThirdShimmer.ivActionRightParentArea,
                        binding.iParentAreaFourthShimmer.ivActionLeftParentArea,
                    ), View.GONE
                )
                binding.apply {
                    tieNameArea.apply {
                        setText(urbanVillageResponse?.urbanVillage?.urbanVillageName)
                        isEnabled = false
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                urbanVillageResponse?.urbanVillage?.createdBy,
                                urbanVillageResponse?.urbanVillage?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                urbanVillageResponse?.urbanVillage?.updatedBy,
                                urbanVillageResponse?.urbanVillage?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
                if (urbanVillageResponse?.urbanVillage == null) areaDetail()
                else buttonVisibility(
                    arrayOf(
                        binding.btnBackArea
                    ), View.VISIBLE
                )
            }
            CREATE -> {
                parentAreaProvince?.apply {
                    init(this@FormAreaActivity, binding.iParentAreaOne)
                    title = getString(
                        R.string.message_choose_parent_for_this_area,
                        getString(R.string.province_menu),
                        appBarTitle
                    )
                    content = getString(R.string.label_not_yet, getString(R.string.province_menu))
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ACTION_LEFT
                    onActionLeft = {
                        goTo(
                            ListOfAreaActivity(),
                            hashMapOf(
                                FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                                AREA to MENU_AREA_PROVINCE_ID
                            )
                        )
                    }
                    create()
                }
                parentAreaCity?.apply {
                    init(this@FormAreaActivity, binding.iParentAreaTwo)
                    title = getString(
                        R.string.message_choose_parent_for_this_area,
                        getString(R.string.city_menu),
                        appBarTitle
                    )
                    content = getString(R.string.label_not_yet, getString(R.string.city_menu))
                    iconRightAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ACTION_RIGHT
                    onActionRight = {
                        if (provinceAsParent != null)
                            goTo(
                                ListOfAreaActivity(),
                                hashMapOf(
                                    FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                                    AREA to MENU_AREA_CITY_ID,
                                    PARENT_AREA to provinceAsParent?.provinceId!!
                                )
                            )
                        else showWarning(
                            message = getString(
                                R.string.message_error_empty, getString(R.string.province_menu)
                            )
                        )
                    }
                    create()
                }
                parentAreaSubDistrict?.apply {
                    init(this@FormAreaActivity, binding.iParentAreaThird)
                    title = getString(
                        R.string.message_choose_parent_for_this_area,
                        parent,
                        appBarTitle
                    )
                    content = getString(R.string.label_not_yet, parent)
                    iconLeftAction = R.drawable.ic_round_location_city_24
                    parentAreaAction = PARENT_AREA_ACTION_LEFT
                    onActionLeft = {
                        if (cityAsParent != null)
                            goTo(
                                ListOfAreaActivity(),
                                hashMapOf(
                                    FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                                    AREA to MENU_AREA_SUB_DISTRICT_ID,
                                    PARENT_AREA to cityAsParent?.cityId!!
                                )
                            )
                        else showWarning(
                            message = getString(
                                R.string.message_error_empty, getString(R.string.city_menu)
                            )
                        )
                    }
                    create()
                }
                parentAreaVisibility(
                    arrayOf(
                        binding.iParentAreaFourth,
                    ), View.GONE
                )
                binding.btnSubmitArea.setOnClickListener {
                    if (provinceAsParent != null) {
                        if (cityAsParent != null) {
                            if (subDistrictAsParent != null) {
                                if (!textInputEditTextIsEmpty(binding.tieNameArea))
                                    areaAdd(
                                        UrbanVillage().apply {
                                            province = Province().apply {
                                                provinceId = provinceAsParent?.provinceId
                                            }
                                            city = City().apply {
                                                cityId = cityAsParent?.cityId
                                            }
                                            subDistrict = SubDistrict().apply {
                                                subDistrictId = subDistrictAsParent?.subDistrictId
                                            }
                                            urbanVillageName =
                                                binding.tieNameArea.text.toString().trim()
                                            createdBy = Admin().apply {
                                                adminId = PreferencesManager
                                                    .getInstance(this@FormAreaActivity)
                                                    .getPreferences(
                                                        LOGIN_MODEL,
                                                        Admin::class.java
                                                    ).adminId
                                            }
                                        })
                                else showWarning(
                                    message = getString(
                                        R.string.message_error_empty,
                                        getString(
                                            R.string.label_plus_two_string,
                                            getString(R.string.label_name),
                                            appBarTitle
                                        )
                                    )
                                )
                            } else showWarning(
                                message = getString(
                                    R.string.message_error_empty, parent
                                )
                            )
                        } else showWarning(
                            message = getString(
                                R.string.message_error_empty, getString(R.string.city_menu)
                            )
                        )
                    } else showWarning(
                        message = getString(
                            R.string.message_error_empty, getString(R.string.province_menu)
                        )
                    )
                }
            }
            else -> {}
        }
    }

    private fun initializeCreatedAndUpdated() {
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iCreatedAndUpdateAreaShimmer.tvCreated,
                binding.iCreatedAndUpdateAreaShimmer.tvUpdated,
            ), this
        )
    }

    private fun loadingArea(isOn: Boolean?, any: Any?) {
        if (isOn!!) {
            parentAreaVisibility(
                arrayOf(
                    binding.iParentAreaOne,
                    binding.iParentAreaTwo,
                    binding.iParentAreaThird,
                    binding.iParentAreaFourth,
                ), View.GONE
            )
            buttonVisibility(
                arrayOf(
                    binding.btnChildArea,
                    binding.btnSubmitArea,
                    binding.btnBackArea
                ), View.GONE
            )
            shimmerOn(
                binding.sflParentArea,
                true
            )
        } else {
            shimmerOn(
                binding.sflParentArea,
                false
            )
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    if (any != null) initializeProvince(
                        getModel(any, ProvinceResponse::class.java)
                    )
                    else initializeProvince(null)
                }
                MENU_AREA_CITY_ID -> {
                    if (any != null) initializeCity(
                        getModel(any, CityResponse::class.java)
                    )
                    else initializeCity(null)
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    if (any != null) initializeSubDistrict(
                        getModel(any, SubDistrictResponse::class.java)
                    )
                    else initializeSubDistrict(null)
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    if (any != null) initializeUrbanVillage(
                        getModel(any, UrbanVillageResponse::class.java)
                    )
                    else initializeUrbanVillage(null)
                }
                else -> {}
            }
        }
    }

    private fun loadingTextInput(isOn: Boolean?) {
        if (isOn!!) {
            binding.tieNameArea.visibility = View.GONE
            shimmerOn(
                binding.sflNameArea,
                true
            )
        } else {
            shimmerOn(
                binding.sflNameArea,
                false
            )
            binding.tieNameArea.visibility = View.VISIBLE
        }
    }

    private fun loadingCreatedAndUpdate(isOn: Boolean?) {
        if (isOn!!) {
            binding.iCreatedAndUpdateArea.root.visibility = View.GONE
            shimmerOn(
                binding.sflCreateAndUpdateArea,
                true
            )
        } else {
            shimmerOn(
                binding.sflCreateAndUpdateArea,
                false
            )
            binding.iCreatedAndUpdateArea.root.visibility = View.VISIBLE
        }
    }
}