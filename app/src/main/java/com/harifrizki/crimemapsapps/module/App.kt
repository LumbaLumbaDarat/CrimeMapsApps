package com.harifrizki.crimemapsapps.module

import FormAreaViewModel
import androidx.multidex.MultiDexApplication
import com.harifrizki.core.utils.Injection
import com.harifrizki.crimemapsapps.module.admin.changepassword.ChangePasswordViewModel
import com.harifrizki.crimemapsapps.module.admin.list.ListOfAdminViewModel
import com.harifrizki.crimemapsapps.module.admin.profile.ProfileViewModel
import com.harifrizki.crimemapsapps.module.area.list.ListOfAreaViewModel
import com.harifrizki.crimemapsapps.module.crimelocation.detail.DetailCrimeLocationViewModel
import com.harifrizki.crimemapsapps.module.crimelocation.form.FormCrimeLocationViewModel
import com.harifrizki.crimemapsapps.module.crimelocation.list.ListOfCrimeLocationViewModel
import com.harifrizki.crimemapsapps.module.dashboard.DashboardViewModel
import com.harifrizki.crimemapsapps.module.login.LoginViewModel
import com.harifrizki.crimemapsapps.module.splash.SplashViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module
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
    }

    @KoinReflectAPI
    @ExperimentalCoroutinesApi
    @FlowPreview
    val viewModelModule = module {
        viewModel { _ -> ChangePasswordViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> ListOfAdminViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> ProfileViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> FormAreaViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> ListOfAreaViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> DetailCrimeLocationViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> FormCrimeLocationViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> ListOfCrimeLocationViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> DashboardViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> LoginViewModel(crimeMapsRepository = Injection.provideRepository()) }
        viewModel { _ -> SplashViewModel(crimeMapsRepository = Injection.provideRepository()) }
    }
}