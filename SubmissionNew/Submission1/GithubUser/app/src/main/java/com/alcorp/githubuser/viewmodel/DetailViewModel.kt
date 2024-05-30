package com.alcorp.githubuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.githubuser.data.AppRepository
import com.alcorp.githubuser.data.remote.response.User
import com.alcorp.githubuser.data.remote.response.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: AppRepository) : ViewModel() {

    val userDetailList = MutableLiveData<UserDetail>()
    val followersList = MutableLiveData<List<User>>()
    val followingList = MutableLiveData<List<User>>()
    val errorMessage = MutableLiveData<String>()

    fun getUserDetail(username: String) {
        val response: Call<UserDetail> = repository.getUserDetail(username)
        response.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                userDetailList.value = response.body()
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun getUserFollowers(username: String) {
        val response: Call<List<User>> = repository.getUserFollowers(username)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                followersList.value = response.body()
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun getUserFollowing(username: String) {
        val response: Call<List<User>> = repository.getUserFollowing(username)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                followingList.value = response.body()
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}