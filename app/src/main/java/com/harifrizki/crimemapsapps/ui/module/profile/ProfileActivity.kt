package com.harifrizki.crimemapsapps.ui.module.profile

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.databinding.ActivityProfileBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.ui.module.password.PasswordActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import com.lumbalumbadrt.colortoast.ColorToast

class ProfileActivity : BaseActivity() {

    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[ProfileViewModel::class.java]
    }
    private val admin: Admin? by lazy {
        PreferencesManager.getInstance(this)
            .getPreferences(LOGIN_MODEL, Admin::class.java)
    }

    private var adminFromResponse: Admin? = null
    private var map: HashMap<String, Any>? = null
    private var appBarTitle: String? = null
    private var fromActivity: ActivityName? = null
    private var crud: CRUD? = null
    private var isAfterCRUD: CRUD? = NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        crud = map!![OPERATION_CRUD] as CRUD
        fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        when (crud)
        {
            READ -> {
                when (fromActivity)
                {
                    DASHBOARD -> {
                        appBarTitle = getString(R.string.setting_profile_menu)
                        initializeDetailProfile()
                        initializeCreatedAndUpdated()
                        adminById(admin)
                    }
                    else -> {}
                }
            }
            CREATE -> {
                appBarTitle = getString(
                    R.string.label_add,
                    getString(R.string.admin_menu))
                initializeAddProfile()
                binding.btnSubmitProfile.text = getString(
                    R.string.label_add_on,
                    getString(R.string.admin_menu))
            }
            else -> {}
        }

        binding.apply {
            appBar(iAppBarProfile,
                appBarTitle,
                R.drawable.ic_round_admin_panel_settings_24,
                R.color.primary,
                R.drawable.frame_background_secondary)
            srlProfile.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@ProfileActivity, this)
                setOnRefreshListener(this@ProfileActivity)
            }
            initializePhotoProfile()
            btnSubmitProfile.setOnClickListener(onClickListener)
            btnBackProfile.setOnClickListener(onClickListener)
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK)
        {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
            {
                when (crud)
                {
                    READ -> {
                        when (fromActivity)
                        {
                            DASHBOARD -> {
                                adminById(admin)
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
            else showMessage(getMap(it.data))
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlProfile.isRefreshing = false
        if (crud == READ) adminById(admin)
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(PROFILE),
            isAfterCRUD!!.name)
        super.onBackPressed()
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_submit_profile -> {}
            R.id.btn_back_profile -> {
                onBackPressed()
            }
        }
    }

    private val adminById = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus)
        {
            LOADING -> {
                loadingProfile(true)
                loadingCreatedAndUpdate(true)
                loadingButtonBack(true)
            }
            SUCCESS -> {
                if (isResponseSuccess(it.data?.message))
                {
                    setAdmin(it.data?.admin)
                    loadingProfile(false)
                    loadingCreatedAndUpdate(false)
                    loadingButtonBack(false)
                }
            }
            ERROR -> {
                loadingProfile(false)
                loadingCreatedAndUpdate(false)
                loadingButtonBack(false)
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun adminById(admin: Admin?) {
        if (networkConnected()) {
            viewModel.adminById(admin?.adminId).observe(this, adminById)
        }
    }

    private val adminUpdate = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus)
        {
            LOADING -> {
                showLoading()
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message))
                {
                    setAdmin(it.data?.admin)
                    if (it.data?.admin?.adminId.equals(admin?.adminId))
                        PreferencesManager.getInstance(this).
                        setPreferences(LOGIN_MODEL, it.data?.admin)
                    isAfterCRUD = UPDATE
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun adminUpdate(admin: Admin?) {
        if (networkConnected()) {
            viewModel.adminUpdate(admin).observe(this, adminUpdate)
        }
    }

    private fun initializePhotoProfile() {
        binding.iPhotoProfile.ivChangePhotoProfile.setOnClickListener {
            showBottomOption(
                getString(R.string.label_get_image_from),
                imageMenus(),
                onClickMenu = {

                })
        }
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iPhotoProfileShimmer.ivAdminPhotoProfile,
                binding.iPhotoProfileShimmer.ivChangePhotoProfile
            ), this
        )
    }

    private fun initializeDetailProfile() {
        binding.apply {
            iDetailProfile.apply {
                root.visibility = View.VISIBLE
                ivBtnEditAdminName.setOnClickListener {
                    showBottomInput(
                        this@ProfileActivity,
                        getString(R.string.label_plus_two_string,
                            getString(R.string.label_name),
                            getString(R.string.setting_profile_menu)),
                        adminFromResponse?.adminName,
                        getString(R.string.change),
                        getString(R.string.cancel),
                        onPositive = {
                            adminUpdate(Admin().apply {
                                adminId = adminFromResponse?.adminId
                                adminName = it
                                updatedByUUID = admin?.adminId
                            })
                            dismissBottomInput()
                        }
                    )
                }
                btnChangePassword.setOnClickListener {
                    goTo(PasswordActivity())
                }
            }
            iDetailProfileShimmer.root.visibility = View.VISIBLE
        }
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iDetailProfileShimmer.ivBtnEditAdminName
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iDetailProfileShimmer.tvAdminName,
                binding.iDetailProfileShimmer.tvEmailAdminName,

                binding.iDetailProfileShimmer.btnChangePassword
            ), this
        )
    }

    private fun initializeAddProfile() {
        binding.apply {
            iAddProfile.root.visibility = View.VISIBLE
            iAddProfileShimmer.root.visibility = View.VISIBLE
            tilEmailProfile.apply {
                hint = getString(
                    R.string.label_enter_of,
                    getString(R.string.label_name),
                    getString(R.string.admin_menu))
                visibility = View.VISIBLE
            }
            tilNameProfile.apply {
                hint = getString(
                    R.string.label_enter_of,
                    getString(R.string.label_username),
                    getString(R.string.admin_menu))
                visibility = View.VISIBLE
            }
            btnSubmitProfile.visibility = View.VISIBLE
            iCreatedAndUpdatedProfile.root.visibility = View.GONE
        }
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iAddProfileShimmer.tvOptionActive,
                binding.iAddProfileShimmer.tvOptionAdmin,
                binding.iAddProfileShimmer.tvOptionRoot,
                binding.iAddProfileShimmer.tvTitleRoleState
            ), this
        )
    }

    private fun initializeCreatedAndUpdated() {
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iCreatedAndUpdatedProfileShimmer.tvCreated,
                binding.iCreatedAndUpdatedProfileShimmer.tvUpdated,

                binding.btnBackProfileShimmer
            ), this
        )
    }

    private fun loadingProfile(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.llContentProfile.visibility = View.GONE
            shimmerOn(
                binding.sflContentProfile,
                true
            )
        } else {
            shimmerOn(
                binding.sflContentProfile,
                false
            )
            binding.llContentProfile.visibility = View.VISIBLE
        }
    }

    private fun loadingCreatedAndUpdate(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.iCreatedAndUpdatedProfile.root.visibility = View.GONE
            shimmerOn(
                binding.sflCreatedAndUpdatedProfile,
                true
            )
        } else {
            shimmerOn(
                binding.sflCreatedAndUpdatedProfile,
                false
            )
            binding.iCreatedAndUpdatedProfile.root.visibility = View.VISIBLE
        }
    }

    private fun loadingButtonBack(isOn: Boolean?) {
        if (isOn!!)
        {
            binding.btnBackProfile.visibility = View.GONE
            shimmerOn(
                binding.sflBackProfile,
                true
            )
        } else {
            shimmerOn(
                binding.sflBackProfile,
                false
            )
            binding.btnBackProfile.visibility = View.VISIBLE
        }
    }

    private fun setAdmin(admin: Admin?) {
        adminFromResponse = admin
        when (crud)
        {
            READ -> {
                when (fromActivity) {
                    DASHBOARD -> {
                        binding.apply {
                            doGlide(
                                this@ProfileActivity,
                                iPhotoProfile.ivAdminPhotoProfile,
                                admin?.adminImage,
                                R.drawable.ic_round_account_box_primary_24
                            )
                            iDetailProfile.apply {
                                tvAdminName.text = admin?.adminName
                                tvEmailAdminName.text = admin?.adminUsername
                            }
                            iCreatedAndUpdatedProfile.apply {
                                tvCreated.text = makeSpannable(
                                    true,
                                    getCreated(admin),
                                    SPAN_REGEX,
                                    Color.BLACK
                                )
                                tvUpdated.text = makeSpannable(
                                    true,
                                    getUpdated(admin),
                                    SPAN_REGEX,
                                    Color.BLACK
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
            else -> {}
        }
    }
}