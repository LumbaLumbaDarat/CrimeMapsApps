package com.harifrizki.crimemapsapps.module.admin.profile
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
import androidx.lifecycle.lifecycleScope
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.component.activity.CropPhotoActivity
import com.harifrizki.core.data.remote.response.AdminResponse
import com.harifrizki.core.data.remote.response.MessageResponse
import com.harifrizki.core.model.Admin
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ActivityName.*
import com.harifrizki.core.utils.ActivityName.Companion.getEnumActivityName
import com.harifrizki.core.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.core.utils.CRUD.*
import com.harifrizki.core.utils.ImageType.*
import com.harifrizki.core.utils.MenuSetting.*
import com.harifrizki.core.utils.ResponseStatus.*
import com.harifrizki.crimemapsapps.BuildConfig
import com.harifrizki.crimemapsapps.databinding.ActivityProfileBinding
import com.harifrizki.crimemapsapps.module.admin.changepassword.ChangePasswordActivity
import com.orhanobut.logger.Logger
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException
import com.harifrizki.core.R

class ProfileActivity : BaseActivity() {

    private val binding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<ProfileViewModel>()
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
                        adminDetail(admin)
                    }
                    LIST_OF_ADMIN -> {
                        appBarTitle = getString(
                            R.string.label_edit,
                            getString(R.string.admin_menu)
                        )
                        appBarIcon = R.drawable.ic_round_account_circle_24
                        initializeUpdateProfile()
                        initializeCreatedAndUpdated()
                        adminDetail(map!![ADMIN_MODEL] as Admin)
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
                                adminDetail(admin)
                            }
                            else -> {}
                        }
                    }
                    else -> {}
                }
            }
            else {
                map = getMap(it.data)
                when (getEnumActivityName(map!![FROM_ACTIVITY].toString())) {
                    CROP_PHOTO -> {
                        try {
                            when (crud) {
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
                                        R.drawable.ic_round_account_box_primary_24
                                    )
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
                                getTempFileUri(IMAGE_PROFILE,
                                    BuildConfig.APPLICATION_ID).let { uri ->
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
        if (crud == READ) adminDetail(admin)
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(PROFILE),
            isAfterCRUD
        )
        super.onBackPressed()
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            com.harifrizki.crimemapsapps.R.id.btn_submit_profile -> {
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
            com.harifrizki.crimemapsapps.R.id.btn_back_profile -> {
                onBackPressed()
            }
        }
    }

    private val adminDetail = Observer<DataResource<AdminResponse>> {
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

    private fun adminDetail(admin: Admin?) {
        if (networkConnected()) {
            viewModel.adminDetail(admin?.adminId).observe(this, adminDetail)
        }
    }

    private val adminAdd = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_add_append,
                            getString(R.string.admin_menu)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = CREATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_add,
                            getString(R.string.admin_menu)
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            onBackPressed()
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

    private fun adminAdd(admin: Admin?, file: File?) {
        if (networkConnected()) {
            viewModel.adminAdd(admin, file).observe(this, adminAdd)
        }
    }

    private val adminUpdate = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_edit_append,
                            getString(R.string.admin_menu)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = UPDATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_update,
                            getString(R.string.admin_menu)
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            setAdmin(it.data?.admin)
                            if (it.data?.admin?.adminId.equals(admin?.adminId))
                                PreferencesManager.getInstance(this)
                                    .setPreferences(LOGIN_MODEL, it.data?.admin)
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

    private fun adminUpdate(admin: Admin?) {
        if (networkConnected()) {
            viewModel.adminUpdate(admin).observe(this, adminUpdate)
        }
    }

    private val adminUpdatePhotoProfile = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_edit_append,
                            getString(R.string.label_photo_profile)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = UPDATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_update,
                            getString(R.string.label_photo_profile)
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            setAdmin(it.data?.admin)
                            if (it.data?.admin?.adminId.equals(admin?.adminId))
                                PreferencesManager.getInstance(this)
                                    .setPreferences(LOGIN_MODEL, it.data?.admin)
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

    private fun adminUpdatePhotoProfile(admin: Admin?, file: File?) {
        if (networkConnected()) {
            viewModel.adminUpdatePhotoProfile(admin, file).observe(this, adminUpdatePhotoProfile)
        }
    }

    private val adminResetPassword = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_reset_append,
                            getString(R.string.label_password)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = UPDATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success,
                            getString(
                                R.string.label_plus_two_string,
                                getString(
                                    R.string.label_reset_append,
                                    getString(R.string.label_password)
                                ),
                                getString(R.string.admin_menu)
                            )
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            adminDetail(it.data?.admin)
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

    private fun adminResetPassword(admin: Admin?) {
        if (networkConnected()) {
            viewModel.adminResetPassword(admin)
                .observe(this, adminResetPassword)
        }
    }

    private val adminUpdateActive = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_plus_two_string,
                            getLabelActivate(adminFromResponse, true),
                            getString(R.string.admin_menu)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = UPDATE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success, getString(
                                R.string.label_plus_two_string,
                                getLabelActivate(
                                    it.data?.admin,
                                    isUseMessageAppend = true,
                                    isReverse = true
                                ),
                                getString(R.string.admin_menu)
                            )
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            adminDetail(it.data?.admin)
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

    private fun adminUpdateActive(admin: Admin?) {
        if (networkConnected()) {
            viewModel.adminUpdateActive(admin)
                .observe(this, adminUpdateActive)
        }
    }

    private val adminDelete = Observer<DataResource<MessageResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_delete_append,
                            getString(R.string.admin_menu)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = DELETE
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_delete,
                            getString(R.string.admin_menu)
                        ),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            onBackPressed()
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

    private fun adminDelete(admin: Admin?) {
        if (networkConnected()) {
            viewModel.adminDelete(admin).observe(this, adminDelete)
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
                    goTo(ChangePasswordActivity())
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

    private fun initializeUpdateProfile() {
        binding.apply {
            iEditProfile.apply {
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
                ivAdminResetPassword.setOnClickListener {
                    showOption(
                        titleOption = getString(
                            R.string.message_title_reset_password,
                            getString(R.string.admin_menu)
                        ),
                        message = getString(
                            R.string.message_reset_password,
                            getString(R.string.admin_menu), adminFromResponse?.adminName
                        ),
                        titlePositive = getString(R.string.yes_reset),
                        titleNegative = getString(R.string.no),
                        onPositive = {
                            dismissOption()
                            adminResetPassword(Admin().apply {
                                adminId = adminFromResponse?.adminId
                                updatedByUUID = PreferencesManager
                                    .getInstance(this@ProfileActivity)
                                    .getPreferences(LOGIN_MODEL, Admin::class.java)
                                    .adminId
                            })
                        },
                    )
                }
                ivAdminLock.setOnClickListener {
                    showOption(
                        titleOption = getString(
                            R.string.label_plus_two_string,
                            getLabelActivate(adminFromResponse),
                            getString(R.string.admin_menu)
                        ),
                        message = getString(
                            R.string.message_activate,
                            getLabelActivate(adminFromResponse),
                            getString(R.string.admin_menu),
                            adminFromResponse?.adminName
                        ),
                        titlePositive = getString(
                            R.string.yes_activate,
                            getLabelActivate(adminFromResponse)
                        ),
                        titleNegative = getString(R.string.no),
                        colorButtonPositive = getColorActivate(adminFromResponse),
                        onPositive = {
                            dismissOption()
                            adminUpdateActive(Admin().apply {
                                adminId = adminFromResponse?.adminId
                                isActive = !adminFromResponse?.isActive!!
                                updatedByUUID = PreferencesManager
                                    .getInstance(this@ProfileActivity)
                                    .getPreferences(LOGIN_MODEL, Admin::class.java)
                                    .adminId
                            })
                        },
                    )
                }
                ivAdminDelete.setOnClickListener {
                    showOption(
                        titleOption = getString(
                            R.string.message_title_delete,
                            getString(R.string.admin_menu)
                        ),
                        message = getString(
                            R.string.message_delete,
                            getString(R.string.admin_menu), adminFromResponse?.adminName
                        ),
                        titlePositive = getString(R.string.yes_delete),
                        titleNegative = getString(R.string.no),
                        colorButtonPositive = R.color.red,
                        onPositive = {
                            dismissOption()
                            adminDelete(
                                Admin().apply { adminId = adminFromResponse?.adminId })
                        },
                    )
                }
            }
            iEditProfileShimmer.root.visibility = View.VISIBLE
        }
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iEditProfileShimmer.tvAdminName,
                binding.iEditProfileShimmer.tvEmailAdmin,
                binding.iEditProfileShimmer.tvAdminRole
            ), this
        )
        widgetStartDrawableShimmer(
            arrayOf(
                binding.iEditProfileShimmer.ivBtnEditAdminName,
                binding.iEditProfileShimmer.ivAdminResetPassword,
                binding.iEditProfileShimmer.ivAdminLock,
                binding.iEditProfileShimmer.ivAdminDelete
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

    @SuppressLint("UseCompatLoadingForDrawables")
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
                    LIST_OF_ADMIN -> {
                        binding.apply {
                            doGlide(
                                this@ProfileActivity,
                                iPhotoProfile.ivAdminPhotoProfile,
                                admin?.adminImage,
                                R.drawable.ic_round_account_box_primary_24
                            )
                            iEditProfile.apply {
                                tvAdminName.text = admin?.adminName
                                tvEmailAdmin.text = admin?.adminUsername
                                tvAdminRole.text = admin?.adminRole
                                setActivate(admin, ivAdminLock)
                                if (admin?.adminRole
                                        .equals(
                                            PreferencesManager.getInstance(this@ProfileActivity)
                                                .getPreferences(ROLE_ROOT)
                                        )
                                ) {
                                    iPhotoProfile.ivChangePhotoProfile.visibility = View.INVISIBLE
                                    ivBtnEditAdminName.visibility = View.INVISIBLE
                                    ivAdminResetPassword.visibility = View.GONE
                                    ivAdminLock.visibility = View.GONE
                                    ivAdminDelete.visibility = View.GONE
                                }
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
                text = getString(R.string.label_active)
                background = resources.getDrawable(
                    R.drawable.button_dark_green_ripple_white, null
                )
                setTextColor(getColor(R.color.white))
                setPadding(resources.getDimensionPixelOffset(R.dimen.padding_half_default))
            }
        } else {
            binding.iAddProfile.tvOptionActive.apply {
                isActive = true
                text = getString(R.string.label_non_active)
                background = resources.getDrawable(
                    R.drawable.button_red_ripple_white, null
                )
                setTextColor(getColor(R.color.white))
                setPadding(resources.getDimensionPixelOffset(R.dimen.padding_half_default))
            }
        }
    }
}