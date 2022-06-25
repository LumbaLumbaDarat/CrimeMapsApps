package com.harifrizki.crimemapsappsuser.module.splash

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import com.harifrizki.core.R
import com.harifrizki.core.component.activity.BaseActivity
import com.harifrizki.core.data.remote.response.HandshakeResponse
import com.harifrizki.core.utils.*
import com.harifrizki.crimemapsappsuser.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.harifrizki.crimemapsappsuser.BuildConfig
import com.harifrizki.crimemapsappsuser.module.dashboard.DashboardActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseActivity() {
    private val binding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    private val viewModel by viewModel<SplashViewModel>()

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
            ResponseStatus.LOADING -> {
                loadingMessage(View.VISIBLE)
            }
            ResponseStatus.SUCCESS -> {
                loadingMessage(View.GONE)
                if (isResponseSuccess(it.data?.message))
                {
                    PreferencesManager.getInstance(this).apply {
                        setPreferences(
                            URL_CONNECTION_API_IMAGE_CRIME_LOCATION,
                            it.data?.handshake?.urlImageStorageApi?.get(ZERO)
                        )
                        setPreferences(DISTANCE_UNIT, it.data?.handshake?.distanceUnit)
                        setPreferences(MAX_DISTANCE, it.data?.handshake?.maxDistance)
                    }

                    val intent = Intent(this, DashboardActivity::class.java)
                    runBlocking {
                        launch {
                            delay(WAIT_FOR_RUN_HANDLER_500_MS.toLong())
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            ResponseStatus.ERROR -> {
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
        BuildConfig.APPLICATION_ID
    }
}