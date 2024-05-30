package com.alcorp.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    val login: String?,
    val avatar_url: String?
) : Parcelable