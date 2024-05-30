package com.alcorp.githubuser.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserSearch(
	@field:SerializedName("items")
	val items: List<User>
)