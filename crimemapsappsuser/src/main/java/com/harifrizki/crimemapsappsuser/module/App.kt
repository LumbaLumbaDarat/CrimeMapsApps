package com.harifrizki.crimemapsappsuser.module

import android.os.Build
import androidx.multidex.MultiDexApplication
import com.harifrizki.core.utils.Injection
import com.harifrizki.crimemapsappsuser.helper.NotificationHelper
import com.harifrizki.crimemapsappsuser.module.dashboard.DashboardViewModel
import com.harifrizki.crimemapsappsuser.module.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

@FlowPreview
@ExperimentalCoroutinesApi
@KoinReflectAPI
class App : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    viewModelModule
                )
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationHelper(this).createChannels()
    }

    @KoinReflectAPI
    @ExperimentalCoroutinesApi
    @FlowPreview
    val viewModelModule = module {
        viewModel { _ -> SplashViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> DashboardViewModel(crimeMapsRepository = Injection.provideRepository()) }
    }
}