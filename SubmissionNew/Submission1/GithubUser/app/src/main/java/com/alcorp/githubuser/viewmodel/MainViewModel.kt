package com.alcorp.githubuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.githubuser.data.AppRepository
import com.alcorp.githubuser.data.remote.response.User
import com.alcorp.githubuser.data.remote.response.UserSearch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: AppRepository) : ViewModel() {

    val userList = MutableLiveData<List<User>>()
    val errorMessage = MutableLiveData<String>()

    fun getUser() {
        val response: Call<List<User>> = repository.getUser()
        response.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                userList.value = response.body()
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }

    fun searchUser(username: String) {
        val response: Call<UserSearch> = repository.searchUser(username)
        response.enqueue(object : Callback<UserSearch> {
            override fun onResponse(call: Call<UserSearch>, response: Response<UserSearch>) {
                userList.value = response.body()!!.items
            }

            override fun onFailure(call: Call<UserSearch>, t: Throwable) {
                errorMessage.value = t.message
            }
        })
    }
}