package com.harifrizki.crimemapsapps.ui.module.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.data.remote.response.UtilizationResponse
import com.harifrizki.crimemapsapps.databinding.ActivityDashboardBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.model.Utilization
import com.harifrizki.crimemapsapps.ui.adapter.MenuAreaAdapter
import com.harifrizki.crimemapsapps.ui.adapter.MenuAreaDetailAdapter
import com.harifrizki.crimemapsapps.ui.component.activity.BaseActivity
import com.harifrizki.crimemapsapps.ui.component.MenuArea
import com.harifrizki.crimemapsapps.ui.component.MenuAreaDetail
import com.harifrizki.crimemapsapps.ui.module.admin.list.ListOfAdminActivity
import com.harifrizki.crimemapsapps.ui.module.area.list.ListOfAreaActivity
import com.harifrizki.crimemapsapps.ui.module.crimelocation.list.ListOfCrimeLocationActivity
import com.harifrizki.crimemapsapps.ui.module.login.LoginActivity
import com.harifrizki.crimemapsapps.ui.module.admin.profile.ProfileActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.MenuAreaType.*
import com.harifrizki.crimemapsapps.utils.MenuSetting.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import java.util.*
import kotlin.collections.ArrayList

class DashboardActivity : BaseActivity() {

    private val binding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[DashboardViewModel::class.java]
    }

    private var menuAreaCrimeLocation: MenuArea? = null
    private var menuAreaDetailCrimeLocation: MenuAreaDetail? = null
    private var menuAreaAdmin: MenuArea? = null
    private var menuAreaDetailAdmin: MenuAreaDetail? = null

    private var menuAreaAdapter: MenuAreaAdapter? = null
    private var menuAreaDetailAdapter: MenuAreaDetailAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        binding.srlDashboard.apply {
            setThemeForSwipeRefreshLayoutLoadingAnimation(
                this@DashboardActivity, this)
            setOnRefreshListener(this@DashboardActivity)
        }

        initializeAccount(PreferencesManager.
        getInstance(this).
        getPreferences(LOGIN_MODEL, Admin::class.java))

        initializeMenuAreaCrimeLocation()
        initializeMenuAreaAdmin()
        initializeMenuArea()
        initializeMenuAreaDetail()

        utilization()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK)
        {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!! ||
                showMessage(getMap(it.data)))
                utilization()
            else if (showMessage(getMap(it.data)) &&
                getEnumActivityName(getMap(it.data)[FROM_ACTIVITY].toString()) == PROFILE)
            {
                initializeAccount(PreferencesManager.
                getInstance(this).
                getPreferences(LOGIN_MODEL, Admin::class.java),
                    false)
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlDashboard.isRefreshing = false
        utilization()
    }

    private val utilization = Observer<DataResource<UtilizationResponse>> {
        when (it.responseStatus)
        {
            LOADING -> {
                loadingMenuAreaCrimeLocation(true)
                loadingMenuAreaDetailCrimeLocation(true)
                loadingMenuArea(true)
                loadingMenuAreaDetail(true)
                loadingMenuAreaAdmin(true)
                loadingMenuAreaDetailAdmin(true)
            }
            SUCCESS -> {
                if (isResponseSuccess(it.data?.message))
                {
                    initializeAccount(PreferencesManager.
                    getInstance(this).
                    getPreferences(LOGIN_MODEL, Admin::class.java),
                        false)
                    menuAreaCrimeLocation?.apply {
                        countMenuArea = it.data?.utilization?.countCrimeLocation
                        create()
                        loadingMenuAreaCrimeLocation(false)
                    }
                    menuAreaDetailCrimeLocation?.apply {
                        countAreaToday = it.data?.utilization?.countCrimeLocationToday
                        countAreaMonth = it.data?.utilization?.countCrimeLocationMonth
                        create()
                        loadingMenuAreaDetailCrimeLocation(false)
                    }
                    menuAreaAdapter?.apply {
                        addMenuAreas(menuAreas(it.data?.utilization))
                        loadingMenuArea(false)
                    }
                    menuAreaDetailAdapter?.apply {
                        addMenuAreaDetails(menuAreaDetails(it.data?.utilization))
                        loadingMenuAreaDetail(false)
                    }
                    menuAreaAdmin?.apply {
                        countMenuArea = it.data?.utilization?.countAdmin
                        create()
                        loadingMenuAreaAdmin(false)
                    }
                    menuAreaDetailAdmin?.apply {
                        countAreaToday = it.data?.utilization?.countAdminToday
                        countAreaMonth = it.data?.utilization?.countAdminMonth
                        create()
                        loadingMenuAreaDetailAdmin(false)
                    }
                    scrollToUp(binding.nsvDashboard)
                }
            }
            ERROR -> {
                loadingMenuAreaCrimeLocation(false)
                loadingMenuAreaDetailCrimeLocation(false)
                loadingMenuArea(false)
                loadingMenuAreaDetail(false)
                loadingMenuAreaAdmin(false)
                loadingMenuAreaDetailAdmin(false)
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun utilization() {
        if (networkConnected()) {
            viewModel.utilization().observe(this, utilization)
        }
    }

    private val logout = Observer<DataResource<MessageResponse>> {
        when (it.responseStatus)
        {
            LOADING -> {
                showLoading()
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message))
                {
                    PreferencesManager.getInstance(this).removePreferences(LOGIN_MODEL)
                    startActivity(
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    )
                    finish()
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun logout(admin: Admin?) {
        if (networkConnected()) {
            viewModel.logout(admin).observe(this, logout)
        }
    }

    private fun initializeAccount(admin: Admin?, isInitialize: Boolean? = true) {
        binding.iUserAccount.apply {
            tvGreeting.text = getString(R.string.label_welcome_greeting_main)
            tvAccountName.text = admin?.adminName
            admin?.adminImage?.let {
                doGlide(
                    this@DashboardActivity,
                    ivAccountPhotoProfile,
                    it,
                    R.drawable.ic_round_account_box_primary_24) }
            if (isInitialize!!)
            {
                ivIconAccount.setOnClickListener { it ->
                    showOptionList(it,
                        settingMenus(),
                        R.drawable.frame_background_primary,
                        ZERO,
                        TWENTY,
                        onClickMenu = {
                            when (it.menuSetting)
                            {
                                MENU_SETTING_EXIT -> {
                                    dismissOptionList()
                                    showOption(
                                        it.name,
                                        getString(R.string.message_logout_option),
                                        onPositive = {
                                            dismissOption()
                                            logout(
                                                Admin(
                                                    adminId = PreferencesManager.
                                                    getInstance(this@DashboardActivity).
                                                    getPreferences(LOGIN_MODEL, Admin::class.java).adminId
                                                )
                                            )
                                        },
                                        onNegative = { dismissOption() }
                                    )
                                }
                                MENU_SETTING_PROFILE -> {
                                    dismissOptionList()
                                    goTo(
                                        ProfileActivity(),
                                        hashMapOf(
                                            FROM_ACTIVITY to getNameOfActivity(DASHBOARD),
                                            OPERATION_CRUD to READ
                                        )
                                    )
                                }
                                else -> {}
                            }
                        })
                }
            }
        }
    }

    private fun initializeMenuAreaCrimeLocation(utilization: Utilization? = Utilization()) {
        menuAreaCrimeLocation = MenuArea(
            this,
            binding.iMenuAreaCrimeLocation,

            MENU_AREA_CRIME_LOCATION_ID,
            getString(R.string.crime_location_menu),
            R.drawable.ic_round_location_on_24,
            utilization?.countCrimeLocation,

            R.color.white,
            R.color.white,
            R.color.white,
            R.drawable.button_primary_ripple_white
        ).apply {
            create()
            onClickMenuArea = {
                goTo(
                    ListOfCrimeLocationActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(DASHBOARD)
                    )
                )
            }
        }.also {
            menuAreaDetailCrimeLocation = MenuAreaDetail(
                binding.iMenuAreaDetailCrimeLocation,

                Date().localDateTimeToString(),
                utilization?.countCrimeLocationToday,
                utilization?.countCrimeLocationMonth
            ).apply {
                context = this@DashboardActivity
                menuAreaType = it.menuAreaType
                titleMenuArea = it.titleMenuArea
                create()
            }
        }

        layoutStartDrawableShimmer(
            arrayOf(
                binding.iMenuAreaCrimeLocationShimmer.clBackgroundMenuArea,
                binding.iMenuAreaDetailCrimeLocationShimmer.clBackgroundMenuAreaDetail
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iMenuAreaCrimeLocationShimmer.ivIconMenuArea,

                binding.iMenuAreaDetailCrimeLocationShimmer.ivIconToday,
                binding.iMenuAreaDetailCrimeLocationShimmer.ivIconMonth
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iMenuAreaCrimeLocationShimmer.tvTitleMenuArea,
                binding.iMenuAreaCrimeLocationShimmer.tvCountMenuArea,

                binding.iMenuAreaDetailCrimeLocationShimmer.tvNameAreaDetail,
                binding.iMenuAreaDetailCrimeLocationShimmer.tvDateAreaDetail,
                binding.iMenuAreaDetailCrimeLocationShimmer.tvLabelCountToday,
                binding.iMenuAreaDetailCrimeLocationShimmer.tvLabelCountMonth,
                binding.iMenuAreaDetailCrimeLocationShimmer.tvCountDataToday,
                binding.iMenuAreaDetailCrimeLocationShimmer.tvCountDataMonth
            ), this
        )
    }

    private fun initializeMenuAreaAdmin(utilization: Utilization? = Utilization()) {
        menuAreaAdmin = MenuArea(
            this,
            binding.iMenuAreaAdmin,

            MENU_AREA_ADMIN_ID,
            getString(R.string.admin_menu),
            R.drawable.ic_round_supervisor_account_24,
            utilization?.countAdmin,

            R.color.white,
            R.color.white,
            R.color.white,
            R.drawable.button_primary_ripple_white
        ).apply {
            create()
            onClickMenuArea = {
                goTo(
                    ListOfAdminActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(DASHBOARD)
                    )
                )
            }
        }.also {
            menuAreaDetailAdmin = MenuAreaDetail(
                binding.iMenuAreaDetailAdmin,

                Date().localDateTimeToString(),
                utilization?.countAdminToday,
                utilization?.countAdminMonth
            ).apply {
                context = this@DashboardActivity
                menuAreaType = it.menuAreaType
                titleMenuArea = it.titleMenuArea
                create()
            }
        }

        layoutStartDrawableShimmer(
            arrayOf(
                binding.iMenuAreaAdminShimmer.clBackgroundMenuArea,
                binding.iMenuAreaDetailAdminShimmer.clBackgroundMenuAreaDetail
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iMenuAreaAdminShimmer.ivIconMenuArea,

                binding.iMenuAreaDetailAdminShimmer.ivIconToday,
                binding.iMenuAreaDetailAdminShimmer.ivIconMonth
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iMenuAreaAdminShimmer.tvTitleMenuArea,
                binding.iMenuAreaAdminShimmer.tvCountMenuArea,

                binding.iMenuAreaDetailAdminShimmer.tvNameAreaDetail,
                binding.iMenuAreaDetailAdminShimmer.tvDateAreaDetail,
                binding.iMenuAreaDetailAdminShimmer.tvLabelCountToday,
                binding.iMenuAreaDetailAdminShimmer.tvLabelCountMonth,
                binding.iMenuAreaDetailAdminShimmer.tvCountDataToday,
                binding.iMenuAreaDetailAdminShimmer.tvCountDataMonth
            ), this
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeMenuArea() {
        menuAreaAdapter = MenuAreaAdapter(false)
        menuAreaAdapter?.apply {
            menuAreas = menuAreas()
            onClickItem = {
                goTo(
                    ListOfAreaActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(DASHBOARD),
                        AREA to (it.menuAreaType as MenuAreaType)))
            }
            notifyDataSetChanged()
        }
        binding.rvMenuArea.apply {
            layoutManager = GridLayoutManager(
                this@DashboardActivity, ROW_MENU_AREA)
            adapter = menuAreaAdapter
        }
        binding.rvMenuAreaShimmer.apply {
            layoutManager = GridLayoutManager(
                this@DashboardActivity, ROW_MENU_AREA)
            adapter = MenuAreaAdapter(true)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initializeMenuAreaDetail() {
        menuAreaDetailAdapter = MenuAreaDetailAdapter(false)
        menuAreaDetailAdapter?.apply {
            menuAreaDetails = menuAreaDetails()
            notifyDataSetChanged()
        }
        binding.rvMenuAreaDetail.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = menuAreaDetailAdapter
        }
        binding.rvMenuAreaDetailShimmer.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = MenuAreaDetailAdapter( true)
        }
    }

    private fun loadingMenuAreaCrimeLocation(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.iMenuAreaCrimeLocation.root.visibility = View.GONE
            shimmerOn(
                binding.sflMenuAreaCrimeLocation,
                true
            )
        } else {
            shimmerOn(
                binding.sflMenuAreaCrimeLocation,
                false
            )
            binding.iMenuAreaCrimeLocation.root.visibility = View.VISIBLE
        }
    }

    private fun loadingMenuAreaDetailCrimeLocation(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.iMenuAreaDetailCrimeLocation.root.visibility = View.GONE
            shimmerOn(
                binding.sflMenuAreaDetailCrimeLocation,
                true
            )
        } else {
            shimmerOn(
                binding.sflMenuAreaDetailCrimeLocation,
                false
            )
            binding.iMenuAreaDetailCrimeLocation.root.visibility = View.VISIBLE
        }
    }

    private fun loadingMenuAreaAdmin(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.iMenuAreaAdmin.root.visibility = View.GONE
            shimmerOn(
                binding.sflMenuAreaAdmin,
                true
            )
        } else {
            shimmerOn(
                binding.sflMenuAreaAdmin,
                false
            )
            binding.iMenuAreaAdmin.root.visibility = View.VISIBLE
        }
    }

    private fun loadingMenuAreaDetailAdmin(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.iMenuAreaDetailAdmin.root.visibility = View.GONE
            shimmerOn(
                binding.sflMenuAreaDetailAdmin,
                true
            )
        } else {
            shimmerOn(
                binding.sflMenuAreaDetailAdmin,
                false
            )
            binding.iMenuAreaDetailAdmin.root.visibility = View.VISIBLE
        }
    }

    private fun loadingMenuArea(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.rvMenuArea.visibility = View.GONE
            shimmerOn(
                binding.sflMenuArea,
                true
            )
        } else {
            shimmerOn(
                binding.sflMenuArea,
                false
            )
            binding.rvMenuArea.visibility = View.VISIBLE
        }
    }

    private fun loadingMenuAreaDetail(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.rvMenuAreaDetail.visibility = View.GONE
            shimmerOn(
                binding.sflMenuAreaDetail,
                true
            )
        } else {
            shimmerOn(
                binding.sflMenuAreaDetail,
                false
            )
            binding.rvMenuAreaDetail.visibility = View.VISIBLE
        }
    }

    private fun menuAreas(utilization: Utilization? = Utilization()): ArrayList<MenuArea> {
        val menuAreas: ArrayList<MenuArea> = ArrayList()
        menuAreas.apply {
            clear()
            add(
                MenuArea(
                    this@DashboardActivity,
                    null,

                    MENU_AREA_PROVINCE_ID,
                    getString(R.string.province_menu),
                    R.drawable.ic_round_location_city_24,
                    utilization?.countProvince,

                    R.color.white,
                    R.color.white,
                    R.color.white,
                    R.drawable.button_primary_ripple_white
                )
            )
            add(
                MenuArea(
                    this@DashboardActivity,
                    null,

                    MENU_AREA_CITY_ID,
                    getString(R.string.city_menu),
                    R.drawable.ic_round_location_city_24,
                    utilization?.countCity,

                    R.color.primary,
                    R.color.primary,
                    R.color.primary,
                    R.drawable.button_secondary_ripple_primary
                )
            )
            add(
                MenuArea(
                    this@DashboardActivity,
                    null,

                    MENU_AREA_SUB_DISTRICT_ID,
                    getString(R.string.sub_district_menu),
                    R.drawable.ic_round_location_city_24,
                    utilization?.countSubDistrict,

                    R.color.primary,
                    R.color.primary,
                    R.color.primary,
                    R.drawable.button_secondary_ripple_primary
                )
            )
            add(
                MenuArea(
                    this@DashboardActivity,
                    null,

                    MENU_AREA_URBAN_VILLAGE_ID,
                    getString(R.string.urban_village_menu),
                    R.drawable.ic_round_location_city_24,
                    utilization?.countUrbanVillage,

                    R.color.primary,
                    R.color.primary,
                    R.color.primary,
                    R.drawable.button_secondary_ripple_primary
                )
            )
        }
        return menuAreas
    }

    private fun menuAreaDetails(utilization: Utilization? = Utilization()): ArrayList<MenuAreaDetail> {
        val menuAreaDetails: ArrayList<MenuAreaDetail> = ArrayList()
        menuAreaDetails.clear()
        for (menuArea: MenuArea in menuAreas())
        {
            when (menuArea.menuAreaType)
            {
                MENU_AREA_PROVINCE_ID -> {
                    menuAreaDetails.add(
                        MenuAreaDetail(
                            null,

                            Date().localDateTimeToString(),
                            utilization?.countProvinceToday,
                            utilization?.countProvinceMonth
                        ).apply {
                            context = this@DashboardActivity
                            menuAreaType = menuArea.menuAreaType
                            titleMenuArea = menuArea.titleMenuArea
                        }
                    )
                }
                MENU_AREA_CITY_ID -> {
                    menuAreaDetails.add(
                        MenuAreaDetail(
                            null,

                            Date().localDateTimeToString(),
                            utilization?.countCityToday,
                            utilization?.countCityMonth
                        ).apply {
                            context = this@DashboardActivity
                            menuAreaType = menuArea.menuAreaType
                            titleMenuArea = menuArea.titleMenuArea
                        }
                    )
                }
                MENU_AREA_SUB_DISTRICT_ID -> {
                    menuAreaDetails.add(
                        MenuAreaDetail(
                            null,

                            Date().localDateTimeToString(),
                            utilization?.countSubDistrictToday,
                            utilization?.countSubDistrictMonth
                        ).apply {
                            context = this@DashboardActivity
                            menuAreaType = menuArea.menuAreaType
                            titleMenuArea = menuArea.titleMenuArea
                        }
                    )
                }
                MENU_AREA_URBAN_VILLAGE_ID -> {
                    menuAreaDetails.add(
                        MenuAreaDetail(
                            null,

                            Date().localDateTimeToString(),
                            utilization?.countUrbanVillageToday,
                            utilization?.countUrbanVillageMonth
                        ).apply {
                            context = this@DashboardActivity
                            menuAreaType = menuArea.menuAreaType
                            titleMenuArea = menuArea.titleMenuArea
                        }
                    )
                }
            }

        }
        return menuAreaDetails
    }
}