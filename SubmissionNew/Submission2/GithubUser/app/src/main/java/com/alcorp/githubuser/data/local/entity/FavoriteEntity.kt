package com.alcorp.githubuser.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_entity")
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_fav")
    var idFav: Int = 0,

    @ColumnInfo(name = "login")
    var login: String? = null,

    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null
)