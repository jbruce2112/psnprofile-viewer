package com.bruce32.psnprofileviewer.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruce32.psnprofileviewer.model.Profile

@Database(entities = [Profile::class], version = 1)
@TypeConverters(ProfileTypeConverters::class)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE psnId=(:psnId)")
    suspend fun getProfile(psnId: String): Profile

    @Insert
    suspend fun addProfile(profile: Profile)
}

class ProfilePersistence private constructor(context: Context) {

    private val database: ProfileDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profile"
        )
        .build()

    companion object {
        private var instance: ProfilePersistence? = null

        fun initialize(context: Context) {
            if (instance == null) {
                instance = ProfilePersistence(context)
            }
        }

        fun get() = checkNotNull(instance) {
            "ProfilePersistence must be initialized"
        }
    }

    suspend fun getProfile(psnId: String) = database.profileDao().getProfile(psnId)

    suspend fun addProfile(profile: Profile) = database.profileDao().addProfile(profile)
}