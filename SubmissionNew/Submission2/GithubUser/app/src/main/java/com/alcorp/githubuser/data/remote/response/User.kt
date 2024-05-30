package com.alcorp.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class User(
	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("login")
	val login: String
)