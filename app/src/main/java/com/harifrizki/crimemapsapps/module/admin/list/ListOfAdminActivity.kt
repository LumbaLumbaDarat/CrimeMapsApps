package com.harifrizki.crimemapsapps.module.admin.list

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.data.remote.response.AdminResponse
import com.harifrizki.core.data.remote.response.MessageResponse
import com.harifrizki.core.model.Admin
import com.harifrizki.core.model.Page
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ActivityName.*
import com.harifrizki.core.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.core.utils.CRUD.*
import com.harifrizki.core.utils.ResponseStatus.*
import com.harifrizki.crimemapsapps.adapter.AdminAdapter
import com.harifrizki.crimemapsapps.databinding.ActivityListOfAdminBinding
import com.harifrizki.crimemapsapps.module.admin.profile.ProfileActivity
import com.lumbalumbadrt.colortoast.ColorToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.harifrizki.core.R

class ListOfAdminActivity : BaseActivity() {

    private val binding by lazy {
        ActivityListOfAdminBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<ListOfAdminViewModel>()

    private var adminAdapter: AdminAdapter? = null

    private var page: Page? = null
    private var isAfterCRUD: CRUD? = NONE
    private var doNotLoadData: Boolean? = true
    private var searchName: String? = EMPTY_STRING
    private var stateActive: String? = EMPTY_STRING

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        appBar(binding.iAppBarListOfAdmin,
            getString(R.string.admin_menu),
            R.drawable.ic_round_account_circle_24,
            R.color.primary,
            R.drawable.frame_background_secondary,
            R.drawable.ic_round_add_24,
            R.color.white,
            R.drawable.button_primary_ripple_white,
            onClick = {
                goTo(
                    ProfileActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(LIST_OF_ADMIN),
                        OPERATION_CRUD to CREATE
                    )
                )
            })
        binding.apply {
            initializeAdmin()
            createSearch(iSearchListOfAdmin)
            createRootView(rvListOfAdmin, sflListOfAdmin)
            createEmpty(iEmptyListOfAdmin)
            createNotificationAndOptionMessage(iNotificationListOfAdmin)
            srlListOfAdmin.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@ListOfAdminActivity, this
                )
                setOnRefreshListener(this@ListOfAdminActivity)
            }
            btnBackListOfAdmin.setOnClickListener { onBackPressed() }
        }
        search(
            getString(
                R.string.label_title_search_by,
                getString(R.string.admin_menu),
                getString(R.string.label_name)
            ),
            onSearch = {
                searchName = it
                admin()
            }
        )
        admin()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                admin()
            else {
                if (showMessage(getMap(it.data))) {
                    isAfterCRUD = getMap(it.data)[OPERATION_CRUD] as CRUD
                    admin()
                }
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlListOfAdmin.isRefreshing = false
        admin()
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(LIST_OF_ADMIN),
            isAfterCRUD
        )
        super.onBackPressed()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val admin = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                disableAccess()
                loadingList(true)
            }
            SUCCESS -> {
                if (isResponseSuccess(it.data?.message)) {
                    if (it.data?.adminArrayList?.size!! > getMinItemForValidList()) {
                        if (it.data!!.page?.totalContentSize!! >= getMaxItemInList()) {
                            if (doNotLoadData!!) {
                                showNotificationAndOptionMessageInformation(
                                    title = titleOverloadData(getString(R.string.admin_menu)),
                                    message = messageOverloadData(
                                        it.data!!.page?.totalContentSize,
                                        getString(R.string.admin_menu),
                                        getString(R.string.label_name)
                                    ),
                                    useOption = true,
                                    buttonPositive = getString(
                                        R.string.label_title_search_by,
                                        getString(R.string.admin_menu),
                                        getString(
                                            R.string.label_plus_two_string,
                                            getString(R.string.label_name),
                                            getString(R.string.admin_menu)
                                        )
                                    ),
                                    buttonNegative = getString(
                                        R.string.label_show_overload
                                    ),
                                    onPositive = {
                                        binding.iSearchListOfAdmin.tieSearch.requestFocus()
                                    },
                                    onNegative = {
                                        doNotLoadData = false
                                        setAdminAdapter(it.data)
                                    }
                                )
                                loadingList(
                                    isOn = false,
                                    isGetData = true,
                                    isOverloadData = true
                                )
                            }
                            else setAdminAdapter(it.data)
                        }
                        else setAdminAdapter(it.data)
                    }
                    else {
                        showEmpty(
                            getString(R.string.label_not_found),
                            getString(
                                R.string.message_empty_list,
                                getString(R.string.admin_menu)
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

    private fun admin() {
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
            viewModel.admin(pageNo, Admin().apply { adminName = searchName }).observe(this, admin)
        }
    }

    private val adminResetPassword = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                disableAccess()
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
                            admin()
                        }
                    )
                }
                enableAccess()
            }
            ERROR -> {
                enableAccess()
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
                disableAccess()
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_plus_two_string,
                            stateActive,
                            getString(R.string.admin_menu)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success,
                            getString(
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
                            admin()
                        }
                    )
                }
                enableAccess()
            }
            ERROR -> {
                enableAccess()
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
                disableAccess()
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
                            getString(R.string.admin_menu)),
                        message = it.data?.message?.message,
                        onClick = {
                            dismissNotification()
                            admin()
                        }
                    )
                }
                enableAccess()
            }
            ERROR -> {
                enableAccess()
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

    private fun initializeAdmin() {
        adminAdapter = AdminAdapter(
            context = this,
            isShimmer = false
        ).apply {
            onClickAdmin = {
                goTo(
                    ProfileActivity(),
                    hashMapOf(
                        FROM_ACTIVITY to getNameOfActivity(LIST_OF_ADMIN),
                        OPERATION_CRUD to READ,
                        ADMIN_MODEL to it!!
                    )
                )
            }
            onClickResetPassword = {
                showOption(
                    titleOption = getString(
                        R.string.message_title_reset_password,
                        getString(R.string.admin_menu)
                    ),
                    message = getString(
                        R.string.message_reset_password,
                        getString(R.string.admin_menu), it?.adminName
                    ),
                    titlePositive = getString(R.string.yes_reset),
                    titleNegative = getString(R.string.no),
                    onPositive = {
                        dismissOption()
                        adminResetPassword(Admin().apply {
                            adminId = it?.adminId
                            updatedByUUID = PreferencesManager
                                .getInstance(this@ListOfAdminActivity)
                                .getPreferences(LOGIN_MODEL, Admin::class.java)
                                .adminId
                        })
                    },
                )
            }
            onClickLockAndUnlock = {
                stateActive = getLabelActivate(it, true)
                showOption(
                    titleOption = getString(
                        R.string.label_plus_two_string,
                        getLabelActivate(it),
                        getString(R.string.admin_menu)
                    ),
                    message = getString(
                        R.string.message_activate,
                        getLabelActivate(it),
                        getString(R.string.admin_menu),
                        it?.adminName
                    ),
                    titlePositive = getString(
                        R.string.yes_activate,
                        getLabelActivate(it)
                    ),
                    titleNegative = getString(R.string.no),
                    colorButtonPositive = getColorActivate(it),
                    onPositive = {
                        dismissOption()
                        adminUpdateActive(Admin().apply {
                            adminId = it?.adminId
                            isActive = !it?.isActive!!
                            updatedByUUID = PreferencesManager
                                .getInstance(this@ListOfAdminActivity)
                                .getPreferences(LOGIN_MODEL, Admin::class.java)
                                .adminId
                        })
                    },
                )
            }
            onClickDelete = {
                showOption(
                    titleOption = getString(
                        R.string.message_title_delete,
                        getString(R.string.admin_menu)
                    ),
                    message = getString(
                        R.string.message_delete,
                        getString(R.string.admin_menu), it?.adminName
                    ),
                    titlePositive = getString(R.string.yes_delete),
                    titleNegative = getString(R.string.no),
                    colorButtonPositive = R.color.red,
                    onPositive = {
                        dismissOption()
                        adminDelete(
                            Admin().apply { adminId = it?.adminId })
                    },
                )
            }
        }
        binding.apply {
            rvListOfAdmin.apply {
                layoutManager = LinearLayoutManager(this@ListOfAdminActivity)
                adapter = adminAdapter
            }
            rvListOfAdminShimmer.apply {
                layoutManager = LinearLayoutManager(this@ListOfAdminActivity)
                adapter = AdminAdapter(
                    context = this@ListOfAdminActivity,
                    isShimmer = true
                )
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdminAdapter(adminResponse: AdminResponse?) {
        page = adminResponse?.page
        adminAdapter?.apply {
            if (adminResponse?.page?.pageNo == INITIALIZE_PAGE_NO)
                setAdmins(adminResponse.adminArrayList)
            else addAdmins(adminResponse?.adminArrayList)
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
                binding.iAppBarListOfAdmin.ivBtnRightAppBar,
                binding.iSearchListOfAdmin.ibSearch
            )
        )
        disableAccess(arrayOf(binding.btnBackListOfAdmin))
    }

    private fun enableAccess() {
        enableAccess(
            arrayOf(
                binding.iAppBarListOfAdmin.ivBtnRightAppBar,
                binding.iSearchListOfAdmin.ibSearch
            )
        )
        enableAccess(arrayOf(binding.btnBackListOfAdmin))
    }
}