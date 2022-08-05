package com.bruce32.psnprofileviewer.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bruce32.psnprofileviewer.model.CurrentUser
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.Trophy

@Database(
    entities = [Profile::class, Game::class, Trophy::class, CurrentUser::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ProfileTypeConverters::class)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}
