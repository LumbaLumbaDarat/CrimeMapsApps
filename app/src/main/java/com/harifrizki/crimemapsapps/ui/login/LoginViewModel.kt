package com.harifrizki.crimemapsapps.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.LoginResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.Resource

class LoginViewModel (private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun login(admin: Admin?): LiveData<Resource<LoginResponse>> = crimeMapsRepository.login(admin!!)
}