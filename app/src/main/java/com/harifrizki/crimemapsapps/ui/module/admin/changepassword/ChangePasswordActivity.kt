package com.harifrizki.crimemapsapps.ui.module.admin.changepassword

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.databinding.ActivityChangePasswordBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.ui.component.activity.BaseActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ActivityName.*
import com.harifrizki.crimemapsapps.utils.ActivityName.Companion.getNameOfActivity
import com.harifrizki.crimemapsapps.utils.CRUD.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*

class ChangePasswordActivity : BaseActivity() {
    private val binding by lazy {
        ActivityChangePasswordBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[ChangePasswordViewModel::class.java]
    }

    private var isHidePassword: Boolean = true
    private var isAfterCRUD: CRUD? = NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        binding.apply {
            appBar(
                iAppBarPassword,
                getString(R.string.label_change_password),
                R.drawable.ic_round_vpn_key_24,
                R.color.primary,
                R.drawable.frame_background_secondary
            )
            prepareResetTextInputEditText(
                arrayOf(
                    tieExistingPassword,
                    tieNewPassword,
                    tieConfirmNewPassword
                )
            )
            prepareShowAndHidePassword(
                arrayOf(
                    tieExistingPassword,
                    tieNewPassword,
                    tieConfirmNewPassword
                )
            )
            srlPassword.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@ChangePasswordActivity, this
                )
                setOnRefreshListener(this@ChangePasswordActivity)
            }
            ivBtnShowHidePassword.setOnClickListener(onClickListener)
            btnSubmitPassword.setOnClickListener(onClickListener)
            btnCancelPassword.setOnClickListener(onClickListener)
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                resetTextInputEditText()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlPassword.isRefreshing = false
    }

    override fun onBackPressed() {
        onBackPressed(
            getNameOfActivity(PASSWORD),
            isAfterCRUD
        )
        super.onBackPressed()
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.iv_btn_show_hide_password -> {
                binding.apply {
                    isHidePassword = showAndHide(
                        ivBtnShowHidePassword,
                        isHidePassword,
                        tvMessageShowHidePassword
                    )
                }
            }
            R.id.btn_submit_password -> {
                validatePassword()
            }
            R.id.btn_cancel_password -> {
                onBackPressed()
            }
        }
    }

    private val adminUpdatePassword = Observer<DataResource<AdminResponse>> {
        when (it.responseStatus) {
            LOADING -> {
                showLoading(
                    getString(
                        R.string.message_loading,
                        getString(
                            R.string.label_edit_append,
                            getString(R.string.label_password)
                        )
                    )
                )
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message)) {
                    isAfterCRUD = UPDATE
                    resetTextInputEditText()
                    showSuccess(
                        titleNotification = getString(
                            R.string.message_success_update,
                            getString(R.string.label_change_password)
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

    private fun adminUpdatePassword(adminId: String?, oldPassword: String?, newPassword: String?) {
        if (networkConnected()) {
            viewModel.adminUpdatePassword(adminId, oldPassword, newPassword)
                .observe(this, adminUpdatePassword)
        }
    }

    private fun validatePassword() {
        if (!textInputEditTextIsEmpty(binding.tieExistingPassword)) {
            if (!textInputEditTextIsEmpty(binding.tieNewPassword)) {
                if (!textInputEditTextIsEmpty(binding.tieConfirmNewPassword)) {
                    if (isValidPasswordAndConfirmPassword(
                            binding.tieNewPassword.text.toString().trim(),
                            binding.tieConfirmNewPassword.text.toString().trim()
                        )
                    ) {
                        adminUpdatePassword(
                            PreferencesManager.getInstance(this)
                                .getPreferences(LOGIN_MODEL, Admin::class.java).adminId,
                            binding.tieExistingPassword.text.toString().trim(),
                            binding.tieNewPassword.text.toString().trim()
                        )
                    } else showWarning(
                        message = getString(
                            R.string.message_error_validate_not_valid,
                            getString(R.string.label_new_password),
                            getString(R.string.label_confirm_new_password)
                        )
                    )
                } else showWarning(
                    message = getString(
                        R.string.message_error_empty,
                        getString(R.string.label_confirm_new_password)
                    )
                )
            } else showWarning(
                message = getString(
                    R.string.message_error_empty,
                    getString(R.string.label_new_password)
                )
            )
        } else showWarning(
            message = getString(
                R.string.message_error_empty,
                getString(R.string.label_existing_password)
            )
        )
    }
}