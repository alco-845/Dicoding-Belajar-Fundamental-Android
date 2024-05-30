package com.alcorp.githubuser.model

data class UserDetail (
    val login: String?,
    val name: String?,
    val location: String?,
    val company: String?,
    val public_repos: String?,
    val followers: String?,
    val following: String?,
    val avatar_url: String?
)