package com.harifrizki.crimemapsapps.ui.module.password

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.utils.DataResource

class PasswordViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun adminUpdatePassword(adminId: String?, oldPassword: String?, newPassword: String?):
            LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdatePassword(adminId, oldPassword, newPassword)
}