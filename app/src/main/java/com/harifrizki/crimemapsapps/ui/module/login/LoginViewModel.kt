package com.harifrizki.crimemapsapps.ui.module.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.LoginResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.DataResource

class LoginViewModel (private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun login(admin: Admin?): LiveData<DataResource<LoginResponse>> =
        crimeMapsRepository.login(admin!!)
}