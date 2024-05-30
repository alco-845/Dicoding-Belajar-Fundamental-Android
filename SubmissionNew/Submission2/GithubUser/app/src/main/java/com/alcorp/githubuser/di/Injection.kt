package com.alcorp.githubuser.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alcorp.githubuser.data.AppRepository
import com.alcorp.githubuser.data.local.AppDatabase
import com.alcorp.githubuser.data.remote.retrofit.ApiConfig
import com.alcorp.githubuser.utils.SettingPreferences

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val apiService = ApiConfig.getApiService()
        val database = AppDatabase.getInstance(context)
        return AppRepository.getInstance(apiService, database.appDao())
    }

    fun providePreferences(dataStore: DataStore<Preferences>): SettingPreferences {
        return SettingPreferences.getInstance(dataStore)
    }
}