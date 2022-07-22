package com.bruce32.psnprofileviewer.application

import android.util.Log
import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.database.ProfilePersistence

class ProfileRepository(
    private val service: PSNProfileService = PSNProfileServiceImpl(),
    private val persistence: ProfilePersistence = ProfilePersistence.get()
) {

    val profile = persistence.getProfile()

    val games = persistence.getGames()

    fun trophies(gameId: String) = persistence.getTrophies(gameId)

    suspend fun refreshProfileAndGames() {
        val userName = persistence.getCurrentUser()
        userName?.let {
            val result = service.profileAndGames(it)
            Log.d("Repository", "Insert profile ${result.profile}")
            persistence.insertProfile(result.profile)
            Log.d("Repository", "Insert ${result.games.size} games ${result.games}")
            persistence.insertGames(result.games)
        }
    }

    suspend fun refreshTrophies(gameId: String) {
        val userName = persistence.getCurrentUser()
        userName?.let {
            val gameDetails = service.gameDetails(gameId, it)
            persistence.insertTrophies(gameDetails.trophies)
        }
    }
}
