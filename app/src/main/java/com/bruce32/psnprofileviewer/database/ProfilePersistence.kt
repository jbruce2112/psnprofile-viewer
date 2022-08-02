package com.bruce32.psnprofileviewer.database

import android.content.Context
import androidx.room.Room
import com.bruce32.psnprofileviewer.model.CurrentUser
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.Trophy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfilePersistence(context: Context) {

    private val database: ProfileDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profile"
        )
        .fallbackToDestructiveMigration()
        .build()

    fun getProfile() = database.profileDao().getProfile()

    fun getGames() = database.profileDao().getGames()

    fun getTrophies(gameId: String) = database.profileDao().getTrophies(gameId)

    fun getCurrentUser() = database.profileDao().getCurrentUser()

    suspend fun insertProfile(profile: Profile) = withContext(Dispatchers.IO) {
        database.profileDao().insertProfile(profile)
    }

    suspend fun insertGames(games: List<Game>) = withContext(Dispatchers.IO) {
        database.profileDao().insertGames(games)
    }

    suspend fun insertTrophies(trophies: List<Trophy>) = withContext(Dispatchers.IO) {
        database.profileDao().insertTrophies(trophies)
    }

    suspend fun setCurrentUser(psnId: String) = withContext(Dispatchers.IO) {
        database.profileDao().setCurrentUser(CurrentUser(psnId))
    }
}
