package com.alcorp.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alcorp.githubuser.model.User
import com.alcorp.githubuser.model.UserDetail
import com.alcorp.githubuser.view.MainActivity
import com.alcorp.githubuser.view.ProfileActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class UserViewModel : ViewModel() {

    val list = arrayListOf<User>()
    val listDetail = arrayListOf<UserDetail>()
    val listAllUser = MutableLiveData<ArrayList<User>>()
    val listAllUserDetail = MutableLiveData<ArrayList<UserDetail>>()

    private var profileActivity: ProfileActivity = ProfileActivity()

    fun setUser(){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("Authorization", "token github-token")
        client.addHeader("User-Agent", "request")
        client.get(url, object: TextHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: String?) {
                try {
                    val jsonArray = JSONArray(responseBody)
                    for (position in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(position)
                        val user = User(
                            jsonObject.getString("login"),
                            jsonObject.getString("avatar_url")
                        )
                        list.add(user)
                    }
                    listAllUser.postValue(list)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: String?, error: Throwable?) {
                Log.d("onFailure", error?.message.toString())
            }

        })
    }

    fun searchUser(query: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$query"
        client.addHeader("Authorization", "token github-token")
        client.addHeader("User-Agent", "request")
        client.get(url, object: TextHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String) {
                try {
                    list.clear()

                    val responObject = JSONObject(responseString)
                    val list_items = responObject.getJSONArray("items")
                    for (position in 0 until list_items.length()){
                        val jsonObject = list_items.getJSONObject(position)
                        val user = User(
                            jsonObject.getString("login"),
                            jsonObject.getString("avatar_url")
                        )
                        list.add(user)
                    }
                    listAllUser.postValue(list)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.d("onFailure", throwable?.message.toString())
            }
        })
    }

    fun setUserDetail(user: String?, place: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$user"
        client.addHeader("Authorization", "token github-token")
        client.addHeader("User-Agent", "request")
        client.get(url, object: TextHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String) {
                try {
                    val jsonObject = JSONObject(responseString)
                    for (position in 0 until jsonObject.length()){

                        profileActivity.name = jsonObject.getString("name")
                        if (jsonObject.getString("location").equals("null") && jsonObject.getString("location").equals("null")) {
                            profileActivity.location = place
                            profileActivity.company = place
                        } else if (jsonObject.getString("location").equals("null")){
                            profileActivity.location = place
                            profileActivity.company = jsonObject.getString("company")
                        } else if (jsonObject.getString("company").equals("null")){
                            profileActivity.location = jsonObject.getString("location")
                            profileActivity.company = place
                        } else {
                            profileActivity.location = jsonObject.getString("location")
                            profileActivity.company = jsonObject.getString("company")
                        }

                        val userDetail = UserDetail(
                            jsonObject.getString("login"),
                            profileActivity.name,
                            profileActivity.location,
                            profileActivity.company,
                            jsonObject.getString("public_repos"),
                            jsonObject.getString("followers"),
                            jsonObject.getString("following"),
                            jsonObject.getString("avatar_url")
                        )

                        listDetail.add(userDetail)
                    }
                    listAllUserDetail.postValue(listDetail)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.d("onFailure", throwable?.message.toString())
            }

        })
    }

    fun setFollowers(value: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$value/followers"

        client.addHeader("Authorization", "token github-token")
        client.addHeader("User-Agent", "request")
        client.get(url, object: TextHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                try {
                    val jsonArray = JSONArray(responseString)
                    for (position in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(position)
                        val user = User(
                            jsonObject.getString("login"),
                            jsonObject.getString("avatar_url")
                        )
                        list.add(user)
                    }
                    listAllUser.postValue(list)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.d("onFailure", throwable?.message.toString())
            }

        })
    }

    fun setFollowing(value: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$value/following"

        client.addHeader("Authorization", "token github-token")
        client.addHeader("User-Agent", "request")
        client.get(url, object: TextHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseString: String?) {
                try {
                    val jsonArray = JSONArray(responseString)
                    for (position in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(position)
                        val user = User(
                            jsonObject.getString("login"),
                            jsonObject.getString("avatar_url")
                        )
                        list.add(user)
                    }
                    listAllUser.postValue(list)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseString: String?, throwable: Throwable?) {
                Log.d("onFailure", throwable?.message.toString())
            }

        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listAllUser
    }

    fun getUserDetail(): LiveData<ArrayList<UserDetail>> {
        return listAllUserDetail
    }
}