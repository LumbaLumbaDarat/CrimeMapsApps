package com.harifrizki.crimemapsapps.ui.module.admin.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.DataResource

class ListOfAdminViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun admin(pageNo: String?, admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.admin(pageNo, admin)
    fun adminResetPassword(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminResetPassword(admin)
    fun adminUpdateActive(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdateActive(admin)
    fun adminDelete(admin: Admin?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.adminDelete(admin)
}