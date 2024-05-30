package com.alcorp.githubuser.di

import com.alcorp.githubuser.data.AppRepository
import com.alcorp.githubuser.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): AppRepository {
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(apiService)
    }
}