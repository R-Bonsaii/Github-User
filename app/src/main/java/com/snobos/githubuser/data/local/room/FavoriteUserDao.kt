package com.snobos.githubuser.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.snobos.githubuser.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favoriteUser: FavoriteUser)

    @Update
    fun update(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * from favoriteUser ORDER BY username ASC")
    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favoriteUser WHERE username = :inputUsername")
    fun getFavoriteUser(inputUsername: String): FavoriteUser?


}