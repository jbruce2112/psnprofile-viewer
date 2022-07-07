package com.bruce32.psnprofileviewer.database

import android.content.Context
import androidx.room.Room
import com.bruce32.psnprofileviewer.model.CurrentUser
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.Trophy

class ProfilePersistence private constructor(context: Context) {

    private val database: ProfileDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ProfileDatabase::class.java,
            "profile"
        )
        .fallbackToDestructiveMigration()
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

    fun getProfile() = database.profileDao().getProfile()

    fun getGames() = database.profileDao().getGames()

    fun getTrophies(gameId: String) = database.profileDao().getTrophies(gameId)

    suspend fun insertProfile(profile: Profile) = database.profileDao().insertProfile(profile)

    suspend fun insertGames(games: List<Game>) = database.profileDao().insertGames(games)

    suspend fun insertTrophies(trophies: List<Trophy>) = database.profileDao().insertTrophies(trophies)

    fun getCurrentUser() = database.profileDao().getCurrentUser()?.psnId

    suspend fun setCurrentUser(psnId: String) = database.profileDao().setCurrentUser(CurrentUser(psnId))
}
