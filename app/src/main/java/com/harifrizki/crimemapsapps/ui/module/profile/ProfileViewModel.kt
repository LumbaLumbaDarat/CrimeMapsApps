package com.harifrizki.crimemapsapps.ui.module.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.data.remote.response.MessageResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.DataResource
import java.io.File

class ProfileViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
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