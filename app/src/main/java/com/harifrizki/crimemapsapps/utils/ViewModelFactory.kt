package com.harifrizki.crimemapsapps.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.ui.module.admin.list.ListOfAdminViewModel
import com.harifrizki.crimemapsapps.ui.module.area.list.ListOfAreaViewModel
import com.harifrizki.crimemapsapps.ui.module.crimelocation.list.ListOfCrimeLocationViewModel
import com.harifrizki.crimemapsapps.ui.module.dashboard.DashboardViewModel
import com.harifrizki.crimemapsapps.ui.module.area.form.FormAreaViewModel
import com.harifrizki.crimemapsapps.ui.module.login.LoginViewModel
import com.harifrizki.crimemapsapps.ui.module.admin.changepassword.ChangePasswordViewModel
import com.harifrizki.crimemapsapps.ui.module.admin.profile.ProfileViewModel
import com.harifrizki.crimemapsapps.ui.module.crimelocation.detail.DetailCrimeLocationViewModel
import com.harifrizki.crimemapsapps.ui.module.crimelocation.form.FormCrimeLocationViewModel
import com.harifrizki.crimemapsapps.ui.module.splash.SplashViewModel
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class ViewModelFactory private constructor (private val crimeMapsRepository: CrimeMapsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }
    }

    init {
        Logger.addLogAdapter(AndroidLogAdapter());
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                ChangePasswordViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(ListOfAdminViewModel::class.java) -> {
                ListOfAdminViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(ListOfAreaViewModel::class.java) -> {
                ListOfAreaViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(FormAreaViewModel::class.java) -> {
                FormAreaViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(ListOfCrimeLocationViewModel::class.java) -> {
                ListOfCrimeLocationViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(DetailCrimeLocationViewModel::class.java) -> {
                DetailCrimeLocationViewModel(crimeMapsRepository) as T
            }
            modelClass.isAssignableFrom(FormCrimeLocationViewModel::class.java) -> {
                FormCrimeLocationViewModel(crimeMapsRepository) as T
            }
            else -> {
                Logger.e("Unknown ViewModel class: " + modelClass.name)
                throw Throwable("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
}