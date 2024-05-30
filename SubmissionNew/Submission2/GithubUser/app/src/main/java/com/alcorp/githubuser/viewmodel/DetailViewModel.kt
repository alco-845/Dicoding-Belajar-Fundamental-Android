package com.alcorp.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.githubuser.data.AppRepository
import com.alcorp.githubuser.data.remote.response.User
import com.alcorp.githubuser.data.remote.response.UserDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: AppRepository) : ViewModel() {

    private val _userDetailList = MutableLiveData<UserDetail>()
    val userDetailList: LiveData<UserDetail> = _userDetailList

    private val _followersList = MutableLiveData<List<User>>()
    val followersList: LiveData<List<User>> = _followersList

    private val _followingList = MutableLiveData<List<User>>()
    val followingList: LiveData<List<User>> = _followingList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    val errorMessage = MutableLiveData<String>()

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val response: Call<UserDetail> = repository.getUserDetail(username)
        response.enqueue(object : Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                _isLoading.value = false
                _userDetailList.value = response.body()
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                _isLoading.value = false
                errorMessage.value = t.message
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        val response: Call<List<User>> = repository.getUserFollowers(username)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false
                _followersList.value = response.body()
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                errorMessage.value = t.message
            }
        })
    }

    fun getUserFollowing(username: String) {
        _isLoading.value = true
        val response: Call<List<User>> = repository.getUserFollowing(username)
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                _isLoading.value = false
                _followingList.value = response.body()
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                errorMessage.value = t.message
            }
        })
    }
}