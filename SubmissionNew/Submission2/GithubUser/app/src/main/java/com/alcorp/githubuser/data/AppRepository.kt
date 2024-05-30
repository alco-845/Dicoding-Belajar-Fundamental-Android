package com.alcorp.githubuser.data

import androidx.lifecycle.LiveData
import com.alcorp.githubuser.data.local.AppDao
import com.alcorp.githubuser.data.local.entity.FavoriteEntity
import com.alcorp.githubuser.data.remote.retrofit.ApiService

class AppRepository(private val apiService: ApiService, private val appDao: AppDao) {
    fun getUser() = apiService.getUser()
    fun searchUser(username: String) = apiService.searchUser(username)
    fun getUserDetail(username: String) = apiService.getUserDetail(username)
    fun getUserFollowers(username: String) = apiService.getUserFollowers(username)
    fun getUserFollowing(username: String) = apiService.getUserFollowing(username)

    fun getFav(): LiveData<List<FavoriteEntity>> = appDao.getFav()
    fun insertFav(favoriteEntity: FavoriteEntity) = appDao.insertFav(favoriteEntity)
    fun deleteFav(login: String) = appDao.deleteFav(login)

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
            database: AppDao
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService, database)
            }.also { instance = it }
    }
}