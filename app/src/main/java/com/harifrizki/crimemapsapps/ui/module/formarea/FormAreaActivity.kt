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
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.Province
import com.harifrizki.crimemapsapps.model.Province.Companion.jsonObject
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.ui.component.ParentArea
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.MenuAreaType.*
import com.harifrizki.crimemapsapps.utils.ParentAreaAction.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import com.orhanobut.logger.Logger

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
    private var buttonSeeChildTitle: String? = null
    private var areaId: String? = null

    private var parentAreaOne: ParentArea? = null

    private var map: HashMap<String, Any>? = null
    private var fromActivity: ActivityName? = null
    private var menuAreaType: MenuAreaType? = null
    private var crud: CRUD? = null
    private var isAfterCRUD: CRUD? = NONE

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
                buttonSeeChildTitle = getString(
                    R.string.label_see_child,
                    getString(R.string.city_menu),
                    appBarTitle
                )
                initializeProvince(ProvinceResponse())
            }
            MENU_AREA_CITY_ID -> {
                appBarTitle = getString(R.string.city_menu)
                initializeCity(CityResponse())
            }
            MENU_AREA_SUB_DISTRICT_ID -> {
                appBarTitle = getString(R.string.sub_district_menu)
                initializeSubDistrict(SubDistrictResponse())
            }
            MENU_AREA_URBAN_VILLAGE_ID -> {
                appBarTitle = getString(R.string.urban_village_menu)
                initializeUrbanVillage(UrbanVillageResponse())
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
                                areaById()
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
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlFormArea.isRefreshing = false
        if (crud == READ) areaById()
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(FORM_AREA),
            isAfterCRUD,
            menuAreaType
        )
        super.onBackPressed()
    }

    private val provinceById = Observer<DataResource<ProvinceResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                loadingArea(true, ProvinceResponse())
                loadingTextInput(true)
                loadingCreatedAndUpdate(true)
            }
            SUCCESS -> {
                if (isResponseSuccess(it.data?.message)) {
                    loadingCreatedAndUpdate(false)
                    loadingTextInput(false)
                    loadingArea(false, it.data)
                }
            }
            ERROR -> {
                loadingArea(false, ProvinceResponse())
                loadingTextInput(false)
                loadingCreatedAndUpdate(false)
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun areaById() {
        if (networkConnected()) {
            when (menuAreaType) {
                MENU_AREA_PROVINCE_ID -> {
                    viewModel.provinceById(areaId)
                        .observe(this, provinceById)
                }
                MENU_AREA_CITY_ID -> {

                }
                MENU_AREA_SUB_DISTRICT_ID -> {

                }
                MENU_AREA_URBAN_VILLAGE_ID -> {

                }
                else -> {}
            }
        }
    }

    private val provinceAdd = Observer<DataResource<ProvinceResponse>> {
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
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = CREATE
                    showSuccess(
                        titleNotification = getString(R.string.message_success_add, appBarTitle),
                        message = it.data?.message?.message,
                        onClick = {
                            binding.tieNameArea.text?.clear()
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

    private fun provinceAdd(province: Province?) {
        if (networkConnected()) {
            viewModel.provinceAdd(province).observe(this, provinceAdd)
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
        initializeForm()
        initializeCreatedAndUpdated()
        when (crud) {
            READ -> {
                parentAreaOne = ParentArea().apply {
                    init(this@FormAreaActivity, binding.iParentAreaOne)
                    title = getString(
                        R.string.label_plus_two_string,
                        getString(R.string.label_id),
                        appBarTitle
                    )
                    content = provinceResponse?.province?.provinceId
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
                if (provinceResponse?.province == null) areaById()
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
                        provinceAdd(
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
        initializeForm()
        initializeCreatedAndUpdated()
    }

    private fun initializeSubDistrict(subDistrictResponse: SubDistrictResponse?) {
        initializeForm()
        initializeCreatedAndUpdated()
    }

    private fun initializeUrbanVillage(urbanVillageResponse: UrbanVillageResponse?) {
        initializeForm()
        initializeCreatedAndUpdated()
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
                    initializeProvince(getModel(any, ProvinceResponse::class.java))
                }
                MENU_AREA_CITY_ID -> {
                    initializeCity(getModel(any, CityResponse::class.java))
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    initializeSubDistrict(getModel(any, SubDistrictResponse::class.java))
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    initializeUrbanVillage(getModel(any, UrbanVillageResponse::class.java))
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