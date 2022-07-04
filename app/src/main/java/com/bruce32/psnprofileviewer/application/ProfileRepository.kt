package com.bruce32.psnprofileviewer.application

import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val service: PSNProfileService = PSNProfileServiceImpl(),
    private val persistence: ProfilePersistence = ProfilePersistence.get()
) {

    suspend fun profile() = persistence.getProfile(currentPsnId)
    fun games() = persistence.getGames(currentPsnId)
    fun trophies(gameId: String) = persistence.getTrophies(gameId, currentPsnId)

    suspend fun refreshProfileAndGames() {
        withContext(Dispatchers.IO) {
            val result = service.profileAndGames(currentPsnId)
            persistence.insertProfile(result.profile)
            persistence.insertGames(result.games)
        }
    }

    suspend fun refreshTrophies(gameId: String) {
        withContext(Dispatchers.IO) {
            val gameDetails = service.gameDetails(gameId, currentPsnId)
            persistence.insertTrophies(gameDetails.trophies)
        }
    }

    private val currentPsnId = "jbruce2112"
}
