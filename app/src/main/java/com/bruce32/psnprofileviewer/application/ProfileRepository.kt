package com.bruce32.psnprofileviewer.application

import android.util.Log
import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val service: PSNProfileService = PSNProfileServiceImpl(),
    private val persistence: ProfilePersistence = ProfilePersistence.get(),
    private val userSource: UserSource = UserSourceImpl.get()
) {

    suspend fun profile() = userSource.currentPsnId()?.let {
        Log.d("Profile", "fetch profile for $it")
        persistence.getProfile(it)
    }

    suspend fun games() = userSource.currentPsnId()?.let {
        Log.d("Profile", "fetch profile for $it")
        persistence.getGames(it)
    }

    suspend fun trophies(gameId: String) = userSource.currentPsnId()?.let {
        Log.d("Profile", "fetch profile for $gameId by $it")
        persistence.getTrophies(gameId, it)
    }

    suspend fun refreshProfileAndGames() {
        withContext(Dispatchers.IO) {
            val currentPsnId = userSource.currentPsnId()
            currentPsnId?.let {
                val result = service.profileAndGames(it)
                Log.d("Repository", "Insert profile ${result.profile}")
                try {
                    persistence.insertProfile(result.profile)
                } catch (exception: Exception) {
                    Log.d("Repository", exception.toString())
                }
                Log.d("Repository", "Insert ${result.games.size} games ${result.games}")
                try {
                    persistence.insertGames(result.games)
                } catch (exception: Exception) {
                    Log.d("Repository", exception.toString())
                }
            }
        }
    }

    suspend fun refreshTrophies(gameId: String) {
        withContext(Dispatchers.IO) {
            val currentPsnId = userSource.currentPsnId()
            currentPsnId?.let {
                val gameDetails = service.gameDetails(gameId, it)
                persistence.insertTrophies(gameDetails.trophies)
            }
        }
    }
}
