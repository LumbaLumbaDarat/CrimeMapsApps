package com.harifrizki.crimemapsapps.module.area.form

import FormAreaViewModel
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.harifrizki.core.component.ParentArea
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.data.remote.response.CityResponse
import com.harifrizki.core.data.remote.response.ProvinceResponse
import com.harifrizki.core.data.remote.response.SubDistrictResponse
import com.harifrizki.core.data.remote.response.UrbanVillageResponse
import com.harifrizki.core.model.*
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ActivityName.*
import com.harifrizki.core.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.core.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.core.utils.CRUD.*
import com.harifrizki.core.utils.MenuAreaType.*
import com.harifrizki.core.utils.ParentAreaAction.*
import com.harifrizki.core.utils.ResponseStatus.*
import com.harifrizki.crimemapsapps.databinding.ActivityFormAreaBinding
import com.harifrizki.crimemapsapps.module.area.list.ListOfAreaActivity
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.harifrizki.core.R

class FormAreaActivity : BaseActivity() {
    private val binding by lazy {
        ActivityFormAreaBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<FormAreaViewModel>()

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
    private var operationBefore: CRUD? = null
    private var isAfterCRUD: CRUD? = NONE

    private var provinceAsParent: Province? = null
    private var cityAsParent: City? = null
    private var subDistrictAsParent: SubDistrict? = null

    private var isReadOnly: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        menuAreaType = map!![AREA] as MenuAreaType
        crud = map!![OPERATION_CRUD] as CRUD
        operationBefore = map!![OPERATION_CRUD] as CRUD
        areaId = map!![AREA_MODEL].toString()
        isReadOnly = map?.getOrElse(IS_READ_ONLY) { false } as Boolean?

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

        viewModel.operation.value = crud
        viewModel.operation.observe(this, {
            crud = it
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    appBarTitle = getString(R.string.province_menu)
                    child = getString(R.string.city_menu)
                    buttonSeeChildTitle = getString(
                        R.string.label_see_child,
                        child,
                        appBarTitle
                    )
                    initializeAppBar()
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
                    initializeAppBar()
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
                    initializeAppBar()
                    initializeSubDistrict(null)
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    appBarTitle = getString(R.string.urban_village_menu)
                    parent = getString(R.string.sub_district_menu)
                    initializeAppBar()
                    initializeUrbanVillage(null)
                }
                else -> {}
            }
        })
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
            }
            else {
                val map = getMap(it.data)
                when (getEnumActivityName(map[FROM_ACTIVITY].toString())) {
                    LIST_OF_AREA -> {
                        when (map[AREA] as MenuAreaType) {
                            MENU_AREA_PROVINCE_ID -> {
                                provinceAsParent = getModel(map[AREA_MODEL]!!, Province::class.java)
                                when (crud) {
                                    CREATE -> {
                                        resetParam(
                                            MENU_AREA_PROVINCE_ID,
                                            provinceAsParent?.provinceName,
                                            resetCity = true,
                                            resetSubDistrict = true
                                        )
                                    }
                                    UPDATE -> {
                                        when (menuAreaType) {
                                            MENU_AREA_CITY_ID -> {
                                                resetParam(
                                                    MENU_AREA_PROVINCE_ID,
                                                    provinceAsParent?.provinceName,
                                                    resetCity = false,
                                                    resetSubDistrict = false
                                                )
                                            }
                                            MENU_AREA_SUB_DISTRICT_ID -> {
                                                resetParam(
                                                    MENU_AREA_PROVINCE_ID,
                                                    provinceAsParent?.provinceName,
                                                    resetCity = true,
                                                    resetSubDistrict = false
                                                )
                                            }
                                            MENU_AREA_URBAN_VILLAGE_ID -> {
                                                resetParam(
                                                    MENU_AREA_PROVINCE_ID,
                                                    provinceAsParent?.provinceName,
                                                    resetCity = true,
                                                    resetSubDistrict = true
                                                )
                                            }
                                            else -> {}
                                        }
                                    }
                                    else -> {}
                                }
                            }
                            MENU_AREA_CITY_ID -> {
                                cityAsParent = getModel(map[AREA_MODEL], City::class.java)
                                when (crud) {
                                    CREATE -> {
                                        resetParam(
                                            MENU_AREA_CITY_ID,
                                            cityAsParent?.cityName,
                                            resetCity = false,
                                            resetSubDistrict = true
                                        )
                                    }
                                    UPDATE -> {
                                        when (menuAreaType) {
                                            MENU_AREA_SUB_DISTRICT_ID -> {
                                                resetParam(
                                                    MENU_AREA_CITY_ID,
                                                    cityAsParent?.cityName,
                                                    resetCity = false,
                                                    resetSubDistrict = false
                                                )
                                            }
                                            MENU_AREA_URBAN_VILLAGE_ID -> {
                                                resetParam(
                                                    MENU_AREA_CITY_ID,
                                                    cityAsParent?.cityName,
                                                    resetCity = false,
                                                    resetSubDistrict = true
                                                )
                                            }
                                            else -> {}
                                        }
                                    }
                                    else -> {}
                                }
                            }
                            MENU_AREA_SUB_DISTRICT_ID -> {
                                subDistrictAsParent =
                                    getModel(map[AREA_MODEL]!!, SubDistrict::class.java)
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
        when (viewModel.operation.value) {
            UPDATE -> {
                resetParam()
            }
            else -> {
                onBackPressed(
                    getNameOfActivity(FORM_AREA),
                    isAfterCRUD,
                    menuAreaType
                )
                super.onBackPressed()
            }
        }
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

    private val provinceUpdate = Observer<DataResource<ProvinceResponse>> {
        areaUpdate(it as DataResource<Any?>)
    }

    private val cityUpdate = Observer<DataResource<CityResponse>> {
        areaUpdate(it as DataResource<Any?>)
    }

    private val subDistrictUpdate = Observer<DataResource<SubDistrictResponse>> {
        areaUpdate(it as DataResource<Any?>)
    }

    private val urbanVillageUpdate = Observer<DataResource<UrbanVillageResponse>> {
        areaUpdate(it as DataResource<Any?>)
    }

    private fun areaUpdate(it: DataResource<Any?>) {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_edit_append,
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
                    isAfterCRUD = UPDATE
                    showSuccess(
                        titleNotification = getString(R.string.message_success_update, appBarTitle),
                        message = message?.message,
                        onClick = {
                            resetParam()
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

    private fun areaUpdate(any: Any?) {
        if (networkConnected()) {
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    viewModel.provinceUpdate(getModel(any, Province::class.java))
                        .observe(this, provinceUpdate)
                }
                MENU_AREA_CITY_ID -> {
                    viewModel.cityUpdate(getModel(any, City::class.java))
                        .observe(this, cityUpdate)
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    viewModel.subDistrictUpdate(getModel(any, SubDistrict::class.java))
                        .observe(this, subDistrictUpdate)
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    viewModel.urbanVillageUpdate(getModel(any, UrbanVillage::class.java))
                        .observe(this, urbanVillageUpdate)
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

    private fun initializeAppBar() {
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
                    when (isReadOnly) {
                        true -> {
                            appBar(
                                iAppBarFormArea,
                                appBarTitle,
                                R.drawable.ic_round_location_city_24,
                                R.color.primary,
                                R.drawable.frame_background_secondary
                            )
                        }
                        else -> {
                            Logger.i("LARI KESINI ANJING....")
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
                                    viewModel.operation.value = UPDATE
                                })
                        }
                    }
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
            UPDATE -> {
                binding.apply {
                    appBar(
                        iAppBarFormArea,
                        getString(R.string.label_edit, appBarTitle),
                        R.drawable.ic_round_location_city_24,
                        R.color.primary,
                        R.drawable.frame_background_secondary,
                    )
                    btnChildArea.visibility = View.GONE
                    btnSubmitArea.text = getString(R.string.label_edit, appBarTitle)
                    btnBackArea.text = getString(R.string.cancel)
                }
            }
            else -> {}
        }
    }

    private fun initializeForm(any: Any?, isEdit: Boolean?) {
        when (menuAreaType) {
            MENU_AREA_PROVINCE_ID -> {
                var prepareProvince: ProvinceResponse? = null
                if (any != null) prepareProvince = getModel(any, ProvinceResponse::class.java)
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = prepareProvince?.province?.provinceId?.uppercase()
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
                        setText(prepareProvince?.province?.provinceName)
                        isEnabled = isEdit!!
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                prepareProvince?.province?.createdBy,
                                prepareProvince?.province?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                prepareProvince?.province?.updatedBy,
                                prepareProvince?.province?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
            }
            MENU_AREA_CITY_ID -> {
                var prepareCity: CityResponse? = null
                if (any != null) {
                    prepareCity = getModel(any, CityResponse::class.java)
                    provinceAsParent = prepareCity.city?.province
                }
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        parent
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_PROVINCE_ID,
                        prepareCity?.city?.province,
                        getString(R.string.province_menu)
                    )
                    when (isEdit) {
                        true -> {
                            iconLeftAction = R.drawable.ic_round_edit_24
                            parentAreaAction = PARENT_AREA_ACTION_LEFT
                            onActionLeft = {
                                if (provinceAsParent != null)
                                    getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
                                else showWarning(
                                    message = getString(
                                        R.string.message_error_empty,
                                        getString(R.string.province_menu)
                                    )
                                )
                            }
                        }
                        else -> {
                            iconLeftAction = R.drawable.ic_round_location_city_24
                            parentAreaAction = PARENT_AREA_ICON_LEFT
                        }
                    }
                    create()
                }
                parentAreaCity?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = prepareCity?.city?.cityId?.uppercase()
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
                        setText(prepareCity?.city?.cityName)
                        isEnabled = isEdit!!
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                prepareCity?.city?.createdBy,
                                prepareCity?.city?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                prepareCity?.city?.updatedBy,
                                prepareCity?.city?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
            }
            MENU_AREA_SUB_DISTRICT_ID -> {
                var prepareSubDistrict: SubDistrictResponse? = null
                if (any != null) {
                    prepareSubDistrict = getModel(any, SubDistrictResponse::class.java)
                    provinceAsParent = prepareSubDistrict.subDistrict?.province
                    cityAsParent = prepareSubDistrict.subDistrict?.city
                }
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        getString(R.string.province_menu)
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_PROVINCE_ID,
                        prepareSubDistrict?.subDistrict?.province,
                        getString(R.string.province_menu)
                    )
                    when (isEdit) {
                        true -> {
                            iconLeftAction = R.drawable.ic_round_edit_24
                            parentAreaAction = PARENT_AREA_ACTION_LEFT
                            onActionLeft = {
                                getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
                            }
                        }
                        else -> {
                            iconLeftAction = R.drawable.ic_round_location_city_24
                            parentAreaAction = PARENT_AREA_ICON_LEFT
                        }
                    }
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
                        prepareSubDistrict?.subDistrict?.city,
                        getString(R.string.city_menu)
                    )
                    when (isEdit) {
                        true -> {
                            iconRightAction = R.drawable.ic_round_edit_24
                            parentAreaAction = PARENT_AREA_ACTION_RIGHT
                            onActionRight = {
                                if (provinceAsParent != null)
                                    getParentArea(
                                        MENU_AREA_CITY_ID,
                                        provinceAsParent?.provinceId)
                                else showWarning(
                                    message = getString(
                                        R.string.message_error_empty,
                                        getString(R.string.province_menu)
                                    )
                                )
                            }
                        }
                        else -> {
                            iconRightAction = R.drawable.ic_round_location_city_24
                            parentAreaAction = PARENT_AREA_ICON_RIGHT
                        }
                    }
                    create()
                }
                parentAreaSubDistrict?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = prepareSubDistrict?.subDistrict?.subDistrictId?.uppercase()
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
                        setText(prepareSubDistrict?.subDistrict?.subDistrictName)
                        isEnabled = isEdit!!
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                prepareSubDistrict?.subDistrict?.createdBy,
                                prepareSubDistrict?.subDistrict?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                prepareSubDistrict?.subDistrict?.updatedBy,
                                prepareSubDistrict?.subDistrict?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
            }
            MENU_AREA_URBAN_VILLAGE_ID -> {
                var prepareUrbanVillage: UrbanVillageResponse? = null
                if (any != null) {
                    prepareUrbanVillage = getModel(any, UrbanVillageResponse::class.java)
                    provinceAsParent = prepareUrbanVillage.urbanVillage?.province
                    cityAsParent = prepareUrbanVillage.urbanVillage?.city
                    subDistrictAsParent = prepareUrbanVillage.urbanVillage?.subDistrict
                }
                parentAreaProvince?.apply {
                    title = getString(
                        R.string.message_area_registration,
                        appBarTitle,
                        getString(R.string.province_menu)
                    )
                    content = getContentArea(
                        this@FormAreaActivity,
                        MENU_AREA_PROVINCE_ID,
                        prepareUrbanVillage?.urbanVillage?.province,
                        getString(R.string.province_menu)
                    )
                    when (isEdit) {
                        true -> {
                            iconLeftAction = R.drawable.ic_round_edit_24
                            parentAreaAction = PARENT_AREA_ACTION_LEFT
                            onActionLeft = {
                                getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
                            }
                        }
                        else -> {
                            iconLeftAction = R.drawable.ic_round_location_city_24
                            parentAreaAction = PARENT_AREA_ICON_LEFT
                        }
                    }
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
                        prepareUrbanVillage?.urbanVillage?.city,
                        getString(R.string.city_menu)
                    )
                    when (isEdit) {
                        true -> {
                            iconRightAction = R.drawable.ic_round_edit_24
                            parentAreaAction = PARENT_AREA_ACTION_RIGHT
                            onActionRight = {
                                if (provinceAsParent != null)
                                    getParentArea(
                                        MENU_AREA_CITY_ID,
                                        provinceAsParent?.provinceId)
                                else showWarning(
                                    message = getString(
                                        R.string.message_error_empty,
                                        getString(R.string.province_menu)
                                    )
                                )
                            }
                        }
                        else -> {
                            iconRightAction = R.drawable.ic_round_location_city_24
                            parentAreaAction = PARENT_AREA_ICON_RIGHT
                        }
                    }
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
                        prepareUrbanVillage?.urbanVillage?.subDistrict,
                        getString(R.string.sub_district_menu)
                    )
                    when (isEdit) {
                        true -> {
                            iconLeftAction = R.drawable.ic_round_edit_24
                            parentAreaAction = PARENT_AREA_ACTION_LEFT
                            onActionLeft = {
                                if (cityAsParent != null)
                                    getParentArea(
                                        MENU_AREA_SUB_DISTRICT_ID,
                                        cityAsParent?.cityId)
                                else showWarning(
                                    message = getString(
                                        R.string.message_error_empty,
                                        getString(R.string.city_menu)
                                    )
                                )
                            }
                        }
                        else -> {
                            iconLeftAction = R.drawable.ic_round_location_city_24
                            parentAreaAction = PARENT_AREA_ICON_LEFT
                        }
                    }
                    create()
                }
                parentAreaUrbanVillage?.apply {
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = prepareUrbanVillage?.urbanVillage?.urbanVillageId?.uppercase()
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
                        setText(prepareUrbanVillage?.urbanVillage?.urbanVillageName)
                        isEnabled = isEdit!!
                    }
                    iCreatedAndUpdateArea.apply {
                        tvCreated.text = makeSpannable(
                            isSpanBold = true,
                            getCreated(
                                prepareUrbanVillage?.urbanVillage?.createdBy,
                                prepareUrbanVillage?.urbanVillage?.createdDate
                            ),
                            color = Color.BLACK
                        )
                        tvUpdated.text = makeSpannable(
                            isSpanBold = true,
                            getUpdated(
                                prepareUrbanVillage?.urbanVillage?.updatedBy,
                                prepareUrbanVillage?.urbanVillage?.updatedDate
                            ),
                            color = Color.BLACK
                        )
                    }
                }
            }
            else -> {}
        }
    }

    private fun initializeProvince(provinceResponse: ProvinceResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                initializeForm(provinceResponse, false)
                if (provinceResponse?.province == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea,
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnSubmitArea
                        ), View.GONE
                    )
                }
                binding.btnChildArea.setOnClickListener {
                    getChildArea(
                        MENU_AREA_CITY_ID,
                        provinceResponse?.province?.provinceId
                    )
                }
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
                    submitProvince(Province().apply {
                        provinceName = binding.tieNameArea.text.toString().trim()
                        createdBy = Admin().apply {
                            adminId = PreferencesManager
                                .getInstance(this@FormAreaActivity)
                                .getPreferences(
                                    LOGIN_MODEL,
                                    Admin::class.java
                                ).adminId
                        }
                    }, false)
                }
            }
            UPDATE -> {
                initializeForm(provinceResponse, true)
                if (provinceResponse?.province == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnSubmitArea,
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea
                        ), View.GONE
                    )
                }
                binding.btnSubmitArea.setOnClickListener {
                    submitProvince(Province().apply {
                        provinceId = provinceResponse?.province?.provinceId
                        provinceName = binding.tieNameArea.text.toString().trim()
                        updatedBy = Admin().apply {
                            adminId = PreferencesManager
                                .getInstance(this@FormAreaActivity)
                                .getPreferences(
                                    LOGIN_MODEL,
                                    Admin::class.java
                                ).adminId
                        }
                    }, true)
                }
            }
            else -> {}
        }
    }

    private fun initializeCity(cityResponse: CityResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                initializeForm(cityResponse, false)
                if (cityResponse?.city == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea,
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnSubmitArea
                        ), View.GONE
                    )
                }
                binding.btnChildArea.setOnClickListener {
                    getChildArea(
                        MENU_AREA_SUB_DISTRICT_ID,
                        cityResponse?.city?.cityId
                    )
                }
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
                        getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
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
                    submitCity(City().apply {
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
                    }, false)
                }
            }
            UPDATE -> {
                initializeForm(cityResponse, true)
                if (cityResponse?.city == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnSubmitArea,
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea
                        ), View.GONE
                    )
                }
                binding.btnSubmitArea.setOnClickListener {
                    submitCity(City().apply {
                        cityId = cityResponse?.city?.cityId
                        province = Province().apply {
                            provinceId = provinceAsParent?.provinceId
                        }
                        cityName = binding.tieNameArea.text.toString().trim()
                        updatedBy = Admin().apply {
                            adminId = PreferencesManager
                                .getInstance(this@FormAreaActivity)
                                .getPreferences(
                                    LOGIN_MODEL,
                                    Admin::class.java
                                ).adminId
                        }
                    }, true)
                }
            }
            else -> {}
        }
    }

    private fun initializeSubDistrict(subDistrictResponse: SubDistrictResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                initializeForm(subDistrictResponse, false)
                if (subDistrictResponse?.subDistrict == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea,
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnSubmitArea
                        ), View.GONE
                    )
                }
                binding.btnChildArea.setOnClickListener {
                    getChildArea(
                        MENU_AREA_URBAN_VILLAGE_ID,
                        subDistrictResponse?.subDistrict?.subDistrictId
                    )
                }
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
                        getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
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
                            getParentArea(
                                MENU_AREA_CITY_ID,
                                provinceAsParent?.provinceId
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
                    submitSubDistrict(SubDistrict().apply {
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
                    }, false)
                }
            }
            UPDATE -> {
                initializeForm(subDistrictResponse, true)
                if (subDistrictResponse?.subDistrict == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnSubmitArea,
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea
                        ), View.GONE
                    )
                }
                binding.btnSubmitArea.setOnClickListener {
                    submitSubDistrict(SubDistrict().apply {
                        subDistrictId = subDistrictResponse?.subDistrict?.subDistrictId
                        province = Province().apply {
                            provinceId = provinceAsParent?.provinceId
                        }
                        city = City().apply {
                            cityId = cityAsParent?.cityId
                        }
                        subDistrictName = binding.tieNameArea.text.toString().trim()
                        updatedBy = Admin().apply {
                            adminId = PreferencesManager
                                .getInstance(this@FormAreaActivity)
                                .getPreferences(
                                    LOGIN_MODEL,
                                    Admin::class.java
                                ).adminId
                        }
                    }, true)
                }
            }
            else -> {}
        }
    }

    private fun initializeUrbanVillage(urbanVillageResponse: UrbanVillageResponse?) {
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                initializeForm(urbanVillageResponse, false)
                if (urbanVillageResponse?.urbanVillage == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea,
                            binding.btnSubmitArea
                        ), View.GONE
                    )
                }
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
                        getParentArea(MENU_AREA_PROVINCE_ID, EMPTY_STRING)
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
                            getParentArea(
                                MENU_AREA_CITY_ID,
                                provinceAsParent?.provinceId
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
                            getParentArea(
                                MENU_AREA_SUB_DISTRICT_ID,
                                cityAsParent?.cityId
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
                    submitUrbanVillage(UrbanVillage().apply {
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
                    }, false)
                }
            }
            UPDATE -> {
                initializeForm(urbanVillageResponse, true)
                if (urbanVillageResponse?.urbanVillage == null) areaDetail()
                else {
                    buttonVisibility(
                        arrayOf(
                            binding.btnSubmitArea,
                            binding.btnBackArea
                        ), View.VISIBLE
                    )
                    buttonVisibility(
                        arrayOf(
                            binding.btnChildArea
                        ), View.GONE
                    )
                }
                binding.btnSubmitArea.setOnClickListener {
                    submitUrbanVillage(UrbanVillage().apply {
                        urbanVillageId = urbanVillageResponse?.urbanVillage?.urbanVillageId
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
                        updatedBy = Admin().apply {
                            adminId = PreferencesManager
                                .getInstance(this@FormAreaActivity)
                                .getPreferences(
                                    LOGIN_MODEL,
                                    Admin::class.java
                                ).adminId
                        }
                    }, true)
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
                    if (any != null)
                        initializeProvince(getModel(any, ProvinceResponse::class.java))
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

    private fun submitProvince(province: Province?, isEdit: Boolean?) {
        if (!textInputEditTextIsEmpty(binding.tieNameArea)) {
            when (isEdit) {
                true -> {
                    areaUpdate(province)
                }
                else -> {
                    areaAdd(province)
                }
            }
        } else showWarning(
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

    private fun submitCity(city: City?, isEdit: Boolean?) {
        if (provinceAsParent != null) {
            if (!textInputEditTextIsEmpty(binding.tieNameArea)) {
                when (isEdit) {
                    true -> {
                        areaUpdate(city)
                    }
                    else -> {
                        areaAdd(city)
                    }
                }
            } else showWarning(
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

    private fun submitSubDistrict(subDistrict: SubDistrict?, isEdit: Boolean?) {
        if (provinceAsParent != null) {
            if (cityAsParent != null) {
                if (!textInputEditTextIsEmpty(binding.tieNameArea)) {
                    when (isEdit) {
                        true -> {
                            areaUpdate(subDistrict)
                        }
                        else -> {
                            areaAdd(subDistrict)
                        }
                    }
                } else showWarning(
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

    private fun submitUrbanVillage(urbanVillage: UrbanVillage?, isEdit: Boolean?) {
        if (provinceAsParent != null) {
            if (cityAsParent != null) {
                if (subDistrictAsParent != null) {
                    if (!textInputEditTextIsEmpty(binding.tieNameArea)) {
                        when (isEdit) {
                            true -> { areaUpdate(urbanVillage) }
                            else -> { areaAdd(urbanVillage) }
                        }
                    } else showWarning(
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

    private fun getParentArea(menuAreaType: MenuAreaType?, parentId: String?) {
        goTo(
            ListOfAreaActivity(),
            hashMapOf(
                FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                AREA to menuAreaType!!,
                PARENT_AREA to parentId!!
            )
        )
    }

    private fun getChildArea(menuAreaType: MenuAreaType?, parentId: String?) {
        goTo(
            ListOfAreaActivity(),
            hashMapOf(
                FROM_ACTIVITY to getNameOfActivity(FORM_AREA),
                AREA to menuAreaType!!,
                PARENT_AREA to parentId!!,
                IS_READ_ONLY to true
            )
        )
    }

    private fun resetParam() {
        binding.tieNameArea.text?.clear()
        provinceAsParent = null
        cityAsParent = null
        subDistrictAsParent = null
        viewModel.operation.value = operationBefore
    }

    private fun resetParam(
        menuAreaType: MenuAreaType?,
        areaName: String?,
        resetCity: Boolean?,
        resetSubDistrict: Boolean?
    ) {
        when (menuAreaType) {
            MENU_AREA_PROVINCE_ID -> {
                parentAreaProvince?.apply {
                    setContent(areaName)
                }
            }
            MENU_AREA_CITY_ID -> {
                parentAreaCity?.apply {
                    setContent(areaName)
                }
            }
            MENU_AREA_SUB_DISTRICT_ID -> {
                parentAreaSubDistrict?.apply {
                    setContent(areaName)
                }
            }
            else -> {}
        }

        if (resetCity!!) {
            cityAsParent = null
            parentAreaCity?.apply {
                setContent(
                    getString(
                        R.string.label_not_yet,
                        getString(R.string.city_menu)
                    )
                )
            }
        }

        if (resetSubDistrict!!) {
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
    }
}