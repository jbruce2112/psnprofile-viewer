package com.bruce32.psnprofileviewer.network

import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.ProfileAndGames

interface PSNProfileService {
    suspend fun profileAndGames(userName: String): ProfileAndGames?
    suspend fun gameDetails(gameId: String, userName: String): GameDetails?
}
