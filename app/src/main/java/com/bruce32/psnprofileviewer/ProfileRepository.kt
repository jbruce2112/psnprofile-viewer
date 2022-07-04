package com.bruce32.psnprofileviewer

import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val service: PSNProfileService = PSNProfileServiceImpl(),
    private val persistence: ProfilePersistence = ProfilePersistence.get()
) {

    val profile = persistence.getProfile("jbruce2112")
    val games = persistence.getGames("jbruce2112")
    fun getTrophies(gameId: String) = persistence.getTrophies(gameId, "jbruce2112")

    suspend fun refreshProfileAndGames(psnId: String) {
        withContext(Dispatchers.IO) {
            val result = service.profileAndGames(psnId)
            persistence.insertProfile(result.profile)
            persistence.insertGames(result.games)
        }
    }

    suspend fun refreshTrophies(gameId: String, psnId: String) {
        withContext(Dispatchers.IO) {
            val gameDetails = service.gameDetails(gameId, psnId)
            persistence.insertTrophies(gameDetails.trophies)
        }
    }
}
