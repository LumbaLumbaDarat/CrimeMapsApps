package com.harifrizki.crimemapsapps.module.login

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.LoginResponse
import com.harifrizki.core.model.Admin
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class LoginViewModel (private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun login(admin: Admin?): LiveData<DataResource<LoginResponse>> =
        crimeMapsRepository.login(admin!!)
}