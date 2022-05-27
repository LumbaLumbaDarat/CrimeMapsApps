package com.harifrizki.crimemapsapps.ui.module.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.setPadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.databinding.ActivityProfileBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.ui.module.cropphoto.CropPhotoActivity
import com.harifrizki.crimemapsapps.ui.module.password.PasswordActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.ImageType.*
import com.harifrizki.crimemapsapps.utils.MenuSetting.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import com.orhanobut.logger.Logger
import java.io.File
import java.io.IOException

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

    private var appBarTitle: String? = null
    private var appBarIcon: Int? = null
    private var role: String? = null
    private var isActive: Boolean? = false
    private var map: HashMap<String, Any>? = null

    private var adminFromResponse: Admin? = null
    private var fromActivity: ActivityName? = null
    private var crud: CRUD? = null
    private var isAfterCRUD: CRUD? = NONE
    private var getImageFrom: MenuSetting? = MENU_NONE

    private var latestTempUri: Uri? = null
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        map = getMap(intent)
        crud = map!![OPERATION_CRUD] as CRUD
        fromActivity = getEnumActivityName(map!![FROM_ACTIVITY].toString())
        when (crud) {
            READ -> {
                when (fromActivity) {
                    DASHBOARD -> {
                        appBarTitle = getString(R.string.setting_profile_menu)
                        appBarIcon = R.drawable.ic_round_admin_panel_settings_24
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
                    getString(R.string.admin_menu)
                )
                appBarIcon = R.drawable.ic_round_account_circle_24
                initializeAddProfile()
                binding.btnSubmitProfile.text = getString(
                    R.string.label_add_on,
                    getString(R.string.admin_menu)
                )
            }
            else -> {}
        }

        binding.apply {
            appBar(
                iAppBarProfile,
                appBarTitle,
                appBarIcon,
                R.color.primary,
                R.drawable.frame_background_secondary
            )
            srlProfile.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@ProfileActivity, this
                )
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
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!) {
                when (crud) {
                    READ -> {
                        when (fromActivity) {
                            DASHBOARD -> {
                                adminById(admin)
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            } else {
                map = getMap(it.data)
                when (getEnumActivityName(map!![FROM_ACTIVITY].toString())) {
                    CROP_PHOTO -> {
                        try {
                            when (crud)
                            {
                                READ -> {
                                    adminUpdatePhotoProfile(
                                        Admin().apply {
                                            adminId = adminFromResponse?.adminId
                                            updatedByUUID = admin?.adminId
                                        },
                                        File(Uri.parse(map!![URI_IMAGE].toString()).path)
                                    )
                                }
                                CREATE -> {
                                    val uri = Uri.parse(map!![URI_IMAGE].toString())
                                    doGlide(
                                        this,
                                        binding.iPhotoProfile.ivAdminPhotoProfile,
                                        uri,
                                        R.drawable.ic_round_account_box_primary_24)
                                    file = File(uri.path)
                                }
                                else -> {}
                            }
                        } catch (e: IOException) {
                            Logger.e(e.message.toString())
                            showError(
                                message = e.message.toString(),
                                onClick = { onBackPressed() })
                        }
                    }
                    else -> {
                        showMessage(getMap(it.data))
                    }
                }
            }
        }
    }

    private val openCamera =
        registerForActivityResult(ActivityResultContracts.TakePicture())
        {
            if (it) {
                goTo(
                    CropPhotoActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(PROFILE),
                        URI_IMAGE to latestTempUri!!
                    )
                )
            }
        }

    private val openGallery =
        registerForActivityResult(ActivityResultContracts.GetContent())
        { uri: Uri? ->
            goTo(
                CropPhotoActivity(),
                hashMapOf(
                    FROM_ACTIVITY to getNameOfActivity(PROFILE),
                    URI_IMAGE to uri!!
                )
            )
        }

    private val resultLauncherPermission =
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
                    message = getString(R.string.message_error_permission_change_image_profile),
                    onClick = { onBackPressed() })
            else {
                when (getImageFrom) {
                    MENU_CAMERA -> {
                        try {
                            lifecycleScope.launchWhenStarted {
                                getTempFileUri(IMAGE_PROFILE).let { uri ->
                                    latestTempUri = uri
                                    openCamera.launch(uri)
                                }
                            }
                        } catch (e: IOException) {
                            Logger.e(e.message.toString())
                            showError(
                                message = e.message.toString(),
                                onClick = { onBackPressed() })
                        }
                    }
                    MENU_GALLERY -> {
                        openGallery.launch(IMAGE_FORMAT_GALLERY)
                    }
                    else -> {}
                }
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
            isAfterCRUD!!.name
        )
        super.onBackPressed()
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_submit_profile -> {
                if (!textInputEditTextIsEmpty(
                        binding.tieNameProfile
                    )
                ) {
                    if (!textInputEditTextIsEmpty(
                            binding.tieEmailProfile
                        )
                    ) {
                        if (isValidEmail(
                                binding.tieEmailProfile.text.toString().trim()
                            )
                        ) {
                            if (file != null) {
                                adminAdd(
                                    Admin().apply {
                                        adminName = binding.tieNameProfile.text.toString().trim()
                                        adminUsername =
                                            binding.tieEmailProfile.text.toString().trim()
                                        adminRole = role
                                        adminImage = PreferencesManager
                                            .getInstance(this@ProfileActivity)
                                            .getPreferences(DEFAULT_IMAGE_ADMIN)
                                        this.isActive = this@ProfileActivity.isActive
                                        createdByUUID = admin?.adminId
                                    },
                                    file
                                )
                            } else showWarning(
                                message = getString(
                                    R.string.message_error_empty,
                                    getString(
                                        R.string.label_plus_two_string,
                                        getString(R.string.label_photo_profile),
                                        getString(R.string.admin_menu)
                                    )
                                )
                            )
                        } else showWarning(
                            message = getString(
                                R.string.message_error_not_valid,
                                getString(
                                    R.string.label_plus_two_string,
                                    getString(R.string.label_username),
                                    getString(R.string.admin_menu)
                                )
                            )
                        )
                    } else showWarning(
                        message = getString(
                            R.string.message_error_empty,
                            getString(
                                R.string.label_plus_two_string,
                                getString(R.string.label_username),
                                getString(R.string.admin_menu)
                            )
                        )
                    )
                } else showWarning(
                    message = getString(
                        R.string.message_error_empty,
                        getString(
                            R.string.label_plus_two_string,
                            getString(R.string.label_name),
                            getString(R.string.admin_menu)
                        )
                    )
                )
            }
            R.id.btn_back_profile -> {
                onBackPressed()
            }
        }
    }

    private val adminById = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                loadingProfile(true)
                loadingCreatedAndUpdate(true)
                loadingButtonBack(true)
            }
            SUCCESS -> {
                if (isResponseSuccess(it.data?.message)) {
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

    private val adminAdd = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading()
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = CREATE
                    onBackPressed()
                }
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun adminAdd(admin: Admin?, file: File?) {
        if (networkConnected()) {
            viewModel.adminAdd(admin, file).observe(this, adminAdd)
        }
    }

    private val adminUpdate = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading()
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    setAdmin(it.data?.admin)
                    if (it.data?.admin?.adminId.equals(admin?.adminId))
                        PreferencesManager.getInstance(this)
                            .setPreferences(LOGIN_MODEL, it.data?.admin)
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

    private val adminUpdatePhotoProfile = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading()
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    setAdmin(it.data?.admin)
                    if (it.data?.admin?.adminId.equals(admin?.adminId))
                        PreferencesManager.getInstance(this)
                            .setPreferences(LOGIN_MODEL, it.data?.admin)
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

    private fun adminUpdatePhotoProfile(admin: Admin?, file: File?) {
        if (networkConnected()) {
            viewModel.adminUpdatePhotoProfile(admin, file).observe(this, adminUpdatePhotoProfile)
        }
    }

    private fun initializePhotoProfile() {
        binding.iPhotoProfile.ivChangePhotoProfile.setOnClickListener {
            showBottomOption(
                getString(R.string.label_get_image_from),
                imageMenus(),
                onClickMenu = {
                    getImageFrom = it.menuSetting
                    resultLauncherPermission.launch(APP_PERMISSION_GET_IMAGE)
                    dismissBottomOption()
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
                        getString(
                            R.string.label_plus_two_string,
                            getString(R.string.label_name),
                            getString(R.string.setting_profile_menu)
                        ),
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
            iAddProfile.apply {
                root.visibility = View.VISIBLE
                setRole(
                    PreferencesManager
                        .getInstance(this@ProfileActivity)
                        .getPreferences(ROLE_ADMIN),
                    tvOptionAdmin,
                    R.drawable.button_primary_ripple_white,
                    R.color.white,
                    tvOptionRoot,
                    R.drawable.button_transparent_ripple_primary,
                    R.color.primary
                )
                tvOptionAdmin.setOnClickListener {
                    setRole(
                        PreferencesManager
                            .getInstance(this@ProfileActivity)
                            .getPreferences(ROLE_ADMIN),
                        tvOptionAdmin,
                        R.drawable.button_primary_ripple_white,
                        R.color.white,
                        tvOptionRoot,
                        R.drawable.button_transparent_ripple_primary,
                        R.color.primary
                    )
                }
                tvOptionRoot.setOnClickListener {
                    setRole(
                        PreferencesManager
                            .getInstance(this@ProfileActivity)
                            .getPreferences(ROLE_ROOT),
                        tvOptionRoot,
                        R.drawable.button_primary_ripple_white,
                        R.color.white,
                        tvOptionAdmin,
                        R.drawable.button_transparent_ripple_primary,
                        R.color.primary
                    )
                }
                setActive()
                tvOptionActive.setOnClickListener { setActive() }
            }
            iAddProfileShimmer.root.visibility = View.VISIBLE
            tilNameProfile.apply {
                hint = getString(
                    R.string.label_enter_of,
                    getString(R.string.label_name),
                    getString(R.string.admin_menu)
                )
                visibility = View.VISIBLE
            }
            tilEmailProfile.apply {
                hint = getString(
                    R.string.label_enter_of,
                    getString(R.string.label_username),
                    getString(R.string.admin_menu)
                )
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
        if (isOn!!) {
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
        if (isOn!!) {
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
        if (isOn!!) {
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
        when (crud) {
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
                                    isSpanBold = true,
                                    getCreated(admin),
                                    color = Color.BLACK
                                )
                                tvUpdated.text = makeSpannable(
                                    isSpanBold = true,
                                    getUpdated(admin),
                                    color = Color.BLACK
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setRole(
        role: String?,
        optionPositive: TextView?,
        backgroundPositive: Int?,
        textColorPositive: Int?,
        optionNegative: TextView?,
        backgroundNegative: Int?,
        textColorNegative: Int?
    ) {
        this.role = role
        optionPositive?.apply {
            background = backgroundPositive?.let { resources.getDrawable(it, null) }
            setTextColor(getColor(textColorPositive!!))
            setPadding(resources.getDimensionPixelOffset(R.dimen.padding_half_default))
        }
        optionNegative?.apply {
            background = backgroundNegative?.let { resources.getDrawable(it, null) }
            setTextColor(getColor(textColorNegative!!))
            setPadding(resources.getDimensionPixelOffset(R.dimen.padding_half_default))
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setActive() {
        if (isActive!!) {
            binding.iAddProfile.tvOptionActive.apply {
                isActive = false
                text = getString(R.string.label_active_admin_now)
                background = resources.getDrawable(
                    R.drawable.button_dark_green_ripple_white, null
                )
                setTextColor(getColor(R.color.white))
                setPadding(resources.getDimensionPixelOffset(R.dimen.padding_half_default))
            }
        } else {
            binding.iAddProfile.tvOptionActive.apply {
                isActive = true
                text = getString(R.string.label_non_active_admin_now)
                background = resources.getDrawable(
                    R.drawable.button_red_ripple_white, null
                )
                setTextColor(getColor(R.color.white))
                setPadding(resources.getDimensionPixelOffset(R.dimen.padding_half_default))
            }
        }
    }
}