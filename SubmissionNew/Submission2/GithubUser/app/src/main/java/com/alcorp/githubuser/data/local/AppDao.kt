package com.alcorp.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alcorp.githubuser.data.local.entity.FavoriteEntity

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFav(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM fav_entity WHERE login = :login")
    fun deleteFav(login: String)

    @Query("SELECT * FROM fav_entity ORDER BY id_fav DESC")
    fun getFav() : LiveData<List<FavoriteEntity>>
}