package com.harifrizki.crimemapsapps.utils

import com.harifrizki.crimemapsapps.data.*
import com.harifrizki.crimemapsapps.data.remote.*

object Injection {
    fun provideRepository(): CrimeMapsRepository {
        return CrimeMapsRepository.getInstance(
            RemoteDataSource.getInstance(),
            AppExecutor())
    }
}