package com.bruce32.psnprofileviewer.application

import android.util.Log
import com.bruce32.psnprofileviewer.network.PSNProfileService
import com.bruce32.psnprofileviewer.network.PSNProfileServiceImpl
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import kotlinx.coroutines.flow.first

class ProfileRepository(
    private val service: PSNProfileService = PSNProfileServiceImpl(),
    private val persistence: ProfilePersistence = ProfilePersistence.get()
) {

    val profile = persistence.getProfile()

    val games = persistence.getGames()

    fun trophies(gameId: String) = persistence.getTrophies(gameId)

    suspend fun refreshProfileAndGames() {
        val currentUser = persistence.getCurrentUser().first()
        currentUser?.let {
            val result = service.profileAndGames(it.psnId)
            Log.d("Repository", "Insert profile ${result.profile}")
            persistence.insertProfile(result.profile)
            Log.d("Repository", "Insert ${result.games.size} games ${result.games}")
            persistence.insertGames(result.games)
        }
    }

    suspend fun refreshTrophies(gameId: String) {
        val userName = persistence.getCurrentUser().first()
        userName?.let {
            val gameDetails = service.gameDetails(gameId, it.psnId)
            persistence.insertTrophies(gameDetails.trophies)
        }
    }
}
