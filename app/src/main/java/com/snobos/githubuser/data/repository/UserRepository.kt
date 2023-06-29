package com.snobos.githubuser.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snobos.githubuser.data.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import com.snobos.githubuser.data.local.entity.FavoriteUser
import com.snobos.githubuser.data.local.room.FavoriteUserDao
import com.snobos.githubuser.data.remote.response.DetailResponse
import com.snobos.githubuser.data.remote.response.ItemsItem
import com.snobos.githubuser.data.remote.response.ResponseGithub
import com.snobos.githubuser.data.remote.retrofit.ApiService

@Suppress("NAME_SHADOWING")
class UserRepository private constructor(
    private val apiService: ApiService,
    private val favoriteUserDao: FavoriteUserDao,
    private val executorService: ExecutorService

) {

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.insert(favoriteUser) }
    }

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { favoriteUserDao.delete(favoriteUser) }
    }

    fun search(favoriteUser: FavoriteUser, callback: (Boolean) -> Unit) {
        executorService.execute {
            val favoriteUser = favoriteUserDao.getFavoriteUser(favoriteUser.username)
            callback(favoriteUser != null)
        }
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>> = favoriteUserDao.getAllFavoriteUsers()


    private fun <T> makeApiCall(apiCall: Call<T>): LiveData<Result<T>> {
        val result = MutableLiveData<Result<T>>()

        result.value = Result.Loading
        apiCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        result.value = Result.Success(items)
                    } else {
                        result.value = Result.Error("Data not found")
                    }
                } else {
                    result.value = Result.Error("Error ${response.code()}")
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                result.value = Result.Error(t.message ?: "Unknown error")
            }
        })

        return result
    }

    fun dataListUser(): LiveData<Result<List<ItemsItem>>> {
        return makeApiCall(apiService.getListUser())
    }

    fun dataGithubUser(user: String): LiveData<Result<ResponseGithub>> {
        return makeApiCall(apiService.getUser(user))
    }

    fun dataGithubDetailUser(user: String): LiveData<Result<DetailResponse>> {
        return makeApiCall(apiService.getDetailUser(user))
    }

    fun dataFollowers(user: String): LiveData<Result<List<ItemsItem>>> {
        return makeApiCall(apiService.getFollowers(user))
    }

    fun dataFollowing(user: String): LiveData<Result<List<ItemsItem>>> {
        return makeApiCall(apiService.getFollowing(user))
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteUserDao: FavoriteUserDao,
            executorService: ExecutorService
        ): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(apiService, favoriteUserDao, executorService)
        }.also { instance = it }
    }

}