package com.snobos.githubuser.di

import android.content.Context
import com.snobos.githubuser.data.repository.UserRepository
import com.snobos.githubuser.data.local.room.FavoriteUserRoomDatabase
import com.snobos.githubuser.data.remote.retrofit.ApiConfig
import java.util.concurrent.Executors

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteUserRoomDatabase.getInstance(context)
        val dao = database.favoriteUserDao()
        val executorService = Executors.newSingleThreadExecutor()
        return UserRepository.getInstance(apiService, dao, executorService)

    }

}