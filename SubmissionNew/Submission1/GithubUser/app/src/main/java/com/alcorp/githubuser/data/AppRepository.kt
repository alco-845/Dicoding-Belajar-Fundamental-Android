package com.alcorp.githubuser.data

import com.alcorp.githubuser.data.remote.retrofit.ApiService

class AppRepository(private val apiService: ApiService) {
    fun getUser() = apiService.getUser()
    fun searchUser(username: String) = apiService.searchUser(username)
    fun getUserDetail(username: String) = apiService.getUserDetail(username)
    fun getUserFollowers(username: String) = apiService.getUserFollowers(username)
    fun getUserFollowing(username: String) = apiService.getUserFollowing(username)

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService)
            }.also { instance = it }
    }
}