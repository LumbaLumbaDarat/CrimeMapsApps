package com.harifrizki.crimemapsapps.ui.module.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.harifrizki.crimemapsapps.data.CrimeMapsRepository
import com.harifrizki.crimemapsapps.data.remote.response.AdminResponse
import com.harifrizki.crimemapsapps.model.Admin
import com.harifrizki.crimemapsapps.utils.DataResource
import java.io.File

class ProfileViewModel(private val crimeMapsRepository: CrimeMapsRepository) : ViewModel() {
    fun adminById(adminId: String?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminById(adminId)
    fun adminUpdate(admin: Admin?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdate(admin)
    fun adminUpdatePhotoProfile(admin: Admin?, file: File?): LiveData<DataResource<AdminResponse>> =
        crimeMapsRepository.adminUpdatePhotoProfile(admin, file)
}