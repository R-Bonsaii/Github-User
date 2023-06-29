package com.snobos.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snobos.githubuser.data.repository.UserRepository
import com.snobos.githubuser.data.Result
import com.snobos.githubuser.data.local.entity.FavoriteUser
import com.snobos.githubuser.data.remote.response.DetailResponse
import kotlinx.coroutines.launch

class DetailViewModel(private val userRepository: UserRepository) : ViewModel() {
    lateinit var githubDetailUser: LiveData<Result<DetailResponse>>

    fun getDetailDataUser(username: String) {
        githubDetailUser = userRepository.dataGithubDetailUser(username)
    }

    fun saveFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            userRepository.insert(favoriteUser)
        }
    }

    fun deleteFavoriteUser(favoriteUser: FavoriteUser) {
        viewModelScope.launch {
            userRepository.delete(favoriteUser)
        }
    }

    fun getFavoriteUser(favoriteUser: FavoriteUser, callback: (Boolean) -> Unit) {
        userRepository.search(favoriteUser) { isFavorite ->
            callback(isFavorite)
        }
    }
}