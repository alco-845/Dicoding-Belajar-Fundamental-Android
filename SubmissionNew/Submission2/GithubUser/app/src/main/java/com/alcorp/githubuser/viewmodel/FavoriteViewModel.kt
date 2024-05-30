package com.alcorp.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alcorp.githubuser.data.AppRepository
import com.alcorp.githubuser.data.local.entity.FavoriteEntity

class FavoriteViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getFav(): LiveData<List<FavoriteEntity>> = appRepository.getFav()
    fun insertFav(favoriteEntity: FavoriteEntity) = appRepository.insertFav(favoriteEntity)
    fun deleteFav(login: String) = appRepository.deleteFav(login)
}