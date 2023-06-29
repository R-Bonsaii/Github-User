package com.snobos.githubuser.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.snobos.githubuser.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteUserRoomDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var instance: FavoriteUserRoomDatabase? = null
        fun getInstance(context: Context): FavoriteUserRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): FavoriteUserRoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                FavoriteUserRoomDatabase::class.java, "favorite_user.db"
            ).build()
        }
    }

}
