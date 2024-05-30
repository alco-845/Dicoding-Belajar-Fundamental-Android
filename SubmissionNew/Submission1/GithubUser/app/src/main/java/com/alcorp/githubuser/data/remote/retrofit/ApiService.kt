package com.alcorp.githubuser.data.remote.retrofit

import com.alcorp.githubuser.data.remote.response.User
import com.alcorp.githubuser.data.remote.response.UserDetail
import com.alcorp.githubuser.data.remote.response.UserSearch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val authToken: String = "github-token"

interface ApiService {
    @GET("users")
    fun getUser(): Call<List<User>>

    @GET("search/users")
    @Headers("Authorization: $authToken")
    fun searchUser(@Query("q") q: String): Call<UserSearch>

    @GET("users/{username}")
    @Headers("Authorization: $authToken")
    fun getUserDetail(@Path("username") username: String): Call<UserDetail>

    @GET("users/{username}/followers")
    @Headers("Authorization: $authToken")
    fun getUserFollowers(@Path("username") username: String): Call<List<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: $authToken")
    fun getUserFollowing(@Path("username") username: String): Call<List<User>>
}