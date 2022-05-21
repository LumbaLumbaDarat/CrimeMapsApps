package com.harifrizki.crimemapsapps.ui.module.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.LoginResponse
import com.harifrizki.crimemapsapps.databinding.ActivityLoginBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.ui.module.dashboard.DashboardActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginActivity : BaseActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[LoginViewModel::class.java]
    }

    private var isHidePassword: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)
        binding.apply {
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
            resetFormLogin(false)
    }

    private fun onClickListener() = View.OnClickListener {
        when (it.id) {
            binding.ibShowPasswordLogin.id -> isHidePassword()
            binding.btnLogin.id -> validateLogin()
            binding.btnCancelLogin.id -> resetFormLogin(true)
        }
    }

    private val login = Observer<DataResource<LoginResponse>> {
        when (it.responseStatus)
        {
            LOADING -> {
                showLoading()
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
                }
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
                    getString(
                        R.string.message_error_empty,
                        getString(R.string.label_password_login)))
            } else showWarning(
                getString(
                    R.string.message_error_not_valid,
                    getString(R.string.label_username_login)))
        } else showWarning(
            getString(
                R.string.message_error_empty,
                getString(R.string.label_username_login)))
    }

    private fun isHidePassword() {
        binding.ibShowPasswordLogin.apply {
            if (!isHidePassword) {
                isHidePassword = true
                setImageResource(
                    R.drawable.ic_round_visibility_24
                )
                showAndHidePassword(binding.tiePasswordLogin, true)
            } else {
                isHidePassword = false
                setImageResource(
                    R.drawable.ic_round_visibility_off_24
                )
                showAndHidePassword(binding.tiePasswordLogin, false)
            }
        }
    }

    private fun resetFormLogin(clearAll: Boolean?) {
        binding.apply {
            if (clearAll!!) {
                tieUsernameLogin.text?.clear()
                tiePasswordLogin.text?.clear()
            } else tiePasswordLogin.text?.clear()
        }
    }
}