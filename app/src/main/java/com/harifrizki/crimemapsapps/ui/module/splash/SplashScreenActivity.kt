package com.harifrizki.crimemapsapps.ui.module.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.harifrizki.crimemapsapps.R
import com.harifrizki.crimemapsapps.data.remote.response.HandshakeResponse
import com.harifrizki.crimemapsapps.databinding.ActivitySplashScreenBinding
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.ui.component.BaseActivity
import com.harifrizki.crimemapsapps.ui.module.dashboard.DashboardActivity
import com.harifrizki.crimemapsapps.ui.module.login.LoginActivity
import com.harifrizki.crimemapsapps.utils.*
import com.harifrizki.crimemapsapps.utils.ResponseStatus.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {

    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater);
    }
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(
                this
            )
        )[SplashViewModel::class.java]
    }
    private val admin: Admin? by lazy {
        PreferencesManager.getInstance(this)
            .getPreferences(LOGIN_MODEL, Admin::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        create(this, resultLauncher)

        binding.tvVersionAppsSplashScreen.apply {
            text = getString(
                R.string.version_apps,
                getVersion(this@SplashScreenActivity)
            )
        }

        handshake()
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK)
            handshake()
    }

    private val handshake = Observer<DataResource<HandshakeResponse>> {
        when (it.responseStatus)
        {
            LOADING -> {
                loadingMessage(View.VISIBLE)
            }
            SUCCESS -> {
                loadingMessage(View.GONE)
                if (isResponseSuccess(it.data?.message))
                {
                    PreferencesManager.getInstance(this).apply {
                        setPreferences(
                            URL_CONNECTION_API_IMAGE_CRIME_LOCATION,
                            it.data?.handshake?.urlImageStorageApi?.get(ZERO)
                        )
                        setPreferences(
                            URL_CONNECTION_API_IMAGE_ADMIN,
                            it.data?.handshake?.urlImageStorageApi?.get(ONE)
                        )
                        setPreferences(ROLE_ROOT, it.data?.handshake?.roleAdminRoot)
                        setPreferences(ROLE_ADMIN, it.data?.handshake?.roleAdmin)
                        setPreferences(DEFAULT_IMAGE_ADMIN, it.data?.handshake?.defaultImageAdmin)
                        setPreferences(DEFAULT_ADMIN_ROOT_USERNAME, it.data?.handshake
                            ?.firstRootAdmin)
                        setPreferences(DISTANCE_UNIT, it.data?.handshake?.distanceUnit)
                        setPreferences(MAX_DISTANCE, it.data?.handshake?.maxDistance)
                        setPreferences(MAX_UPLOAD_IMAGE_CRIME_LOCATION, it.data?.handshake
                            ?.maxUploadImageCrimeLocation)
                    }

                    val intent: Intent = if (admin == null)
                        Intent(
                            this,
                            LoginActivity::class.java
                        )
                    else Intent(
                        this,
                        DashboardActivity::class.java
                    )

                    runBlocking {
                        launch {
                            delay(WAIT_FOR_RUN_HANDLER_500_MS.toLong())
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            ERROR -> {
                loadingMessage(View.GONE)
                goTo(it.errorResponse)
            }
            else -> {}
        }
    }

    private fun handshake() {
        if (networkConnected()) {
            viewModel.handshake().observe(this, handshake)
        }
    }

    private fun loadingMessage(visibility: Int?) {
        binding.tvMessageLoadingSplashScreen.visibility = visibility!!
    }
}