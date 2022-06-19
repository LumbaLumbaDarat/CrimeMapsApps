package com.harifrizki.crimemapsapps.module.admin.profile

import androidx.lifecycle.LiveData
import com.harifrizki.core.data.CrimeMapsRepository
import com.harifrizki.core.data.remote.response.AdminResponse
import com.harifrizki.core.data.remote.response.MessageResponse
import com.harifrizki.core.model.Admin
import com.harifrizki.core.utils.BaseViewModel
import com.harifrizki.core.utils.DataResource
import java.io.File

class ProfileViewModel(private val crimeMapsRepository: CrimeMapsRepository) : BaseViewModel() {
    fun adminDetail(adminId: String?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminDetail(adminId)
    fun adminAdd(admin: Admin?, file: File?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminAdd(admin, file)
    fun adminUpdate(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdate(admin)
    fun adminUpdatePhotoProfile(admin: Admin?, file: File?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdatePhotoProfile(admin, file)
    fun adminResetPassword(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminResetPassword(admin)
    fun adminUpdateActive(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdateActive(admin)
    fun adminDelete(admin: Admin?): LiveData<DataResource<MessageResponse>> =
        crimeMapsRepository.adminDelete(admin)
}