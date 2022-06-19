package com.harifrizki.crimemapsapps.module.admin.list

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.AdminResponse
import com.harifrizki.core.data.remote.response.MessageResponse
import com.harifrizki.core.model.Admin
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource

class ListOfAdminViewModel(private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun admin(pageNo: String?, admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.admin(pageNo, admin)
    fun adminResetPassword(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminResetPassword(admin)
    fun adminUpdateActive(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdateActive(admin)
    fun adminDelete(admin: Admin?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.adminDelete(admin)
}