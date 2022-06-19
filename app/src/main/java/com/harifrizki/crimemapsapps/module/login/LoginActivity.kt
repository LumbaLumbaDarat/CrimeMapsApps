package com.harifrizki.crimemapsapps.module.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.data.remote.response.LoginResponse
import com.harifrizki.core.model.Admin
import com.harifrizki.core.utils.*
import com.harifrizki.core.utils.ResponseStatus.*
import com.harifrizki.crimemapsapps.databinding.ActivityLoginBinding
import com.harifrizki.crimemapsapps.module.dashboard.DashboardActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.harifrizki.core.R

class LoginActivity : BaseActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<LoginViewModel>()

    private var isHidePassword: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        binding.apply {
            prepareResetTextInputEditText(arrayOf(
                tieUsernameLogin, tiePasswordLogin
            ))
            prepareShowAndHidePassword(arrayOf(
                tiePasswordLogin
            ))
            srlLogin.apply {
                setThemeForSwipeRefreshLayoutLoadingAnimation(
                    this@LoginActivity, this)
                setOnRefreshListener(this@LoginActivity)
            }
            tvVersionAppsLogin.text = getString(
                R.string.version_apps,
                getVersion(this@LoginActivity)
            )
            ibShowPasswordLogin.setOnClickListener(onClickListener())
            btnLogin.setOnClickListener(onClickListener())
            btnCancelLogin.setOnClickListener(onClickListener())
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    )
    {
        if (it.resultCode == Activity.RESULT_OK)
        {
            if (it.data?.getBooleanExtra(IS_AFTER_ERROR, false)!!)
                resetTextInputEditText()
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        binding.srlLogin.isRefreshing = false
    }

    private fun onClickListener() = View.OnClickListener {
        when (it.id) {
            binding.ibShowPasswordLogin.id -> isHidePassword =
                showAndHide(binding.ibShowPasswordLogin, isHidePassword)
            binding.btnLogin.id -> validateLogin()
            binding.btnCancelLogin.id -> resetTextInputEditText()
        }
    }

    private val login = Observer<DataResource<LoginResponse>> {
        when (it.responseStatus)
        {
            LOADING -> {
                showLoading(getString(R.string.message_loading_login))
            }
            SUCCESS -> {
                dismissLoading()
                if (isResponseSuccess(it.data?.message))
                {
                    PreferencesManager.getInstance(this).setPreferences(
                        LOGIN_MODEL, it.data?.login)
                    runBlocking {
                        launch {
                            delay(WAIT_FOR_RUN_HANDLER_500_MS.toLong())
                            startActivity(
                                Intent(
                                    this@LoginActivity,
                                    DashboardActivity::class.java
                                )
                            )
                            finishAffinity()
                        }
                    }
                } else resetTextInputEditText()
            }
            ERROR -> {
                dismissLoading()
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun login(admin: Admin?) {
        if (networkConnected()) {
            viewModel.login(admin).observe(this, login)
        }
    }

    private fun validateLogin() {
        if (!textInputEditTextIsEmpty(
                binding.tieUsernameLogin
            )
        ) {
            if (isValidEmail(
                    binding.tieUsernameLogin.text.toString().trim()
                )
            ) {
                if (!textInputEditTextIsEmpty(
                        binding.tiePasswordLogin
                    )
                )
                    login(
                        Admin().apply
                        {
                            adminUsername = binding.tieUsernameLogin.text.toString().trim()
                            adminPassword = binding.tiePasswordLogin.text.toString().trim()
                        })
                else showWarning(
                    message = getString(
                        R.string.message_error_empty,
                        getString(R.string.label_password)))
            } else showWarning(
                message = getString(
                    R.string.message_error_not_valid,
                    getString(R.string.label_username)))
        } else showWarning(
            message = getString(
                R.string.message_error_empty,
                getString(R.string.label_username)))
    }
}