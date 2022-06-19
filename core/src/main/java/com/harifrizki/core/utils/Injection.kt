package com.harifrizki.core.utils

import com.harifrizki.core.data.*
import com.harifrizki.core.data.remote.*

object Injection {
    fun provideRepository(): CrimeMapsRepository {
        return CrimeMapsRepository.getInstance(
            RemoteDataSource.getInstance(),
            AppExecutor())
    }
}