package com.snobos.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.snobos.githubuser.data.repository.UserRepository
import com.snobos.githubuser.data.Result
import com.snobos.githubuser.data.remote.response.ItemsItem

class FollowViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var followerUser: LiveData<Result<List<ItemsItem>>>

    lateinit var followingUser: LiveData<Result<List<ItemsItem>>>

    fun getDataFollowers(username: String) {
        followerUser = userRepository.dataFollowers(username)
    }

    fun getDataFollowing(username: String) {
        followingUser = userRepository.dataFollowing(username)
    }

}