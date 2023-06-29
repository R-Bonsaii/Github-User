package com.snobos.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.snobos.githubuser.data.repository.UserRepository
import com.snobos.githubuser.data.remote.response.ResponseGithub
import com.snobos.githubuser.data.Result
import com.snobos.githubuser.data.remote.response.ItemsItem

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var githubUser: LiveData<Result<ResponseGithub>>

    var githubListUser: LiveData<Result<List<ItemsItem>>> = userRepository.dataListUser()

    fun getSearchDataUser(username: String) {
        githubUser = userRepository.dataGithubUser(username)
    }

    fun getAllFavoriteUser() = userRepository.getAllFavoriteUser()
}