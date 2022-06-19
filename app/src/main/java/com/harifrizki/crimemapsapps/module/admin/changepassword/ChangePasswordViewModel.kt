package com.harifrizki.crimemapsapps.module.admin.changepassword

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.AdminResponse
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class ChangePasswordViewModel(private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun adminUpdatePassword(adminId: String?, oldPassword: String?, newPassword: String?):
            LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdatePassword(adminId, oldPassword, newPassword)
}