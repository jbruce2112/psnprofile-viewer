package com.bruce32.psnprofileviewer.network

import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.ProfileWithGames

interface PSNProfileService {
    suspend fun profileAndGames(userName: String): ProfileWithGames?
    suspend fun gameDetails(gameId: String, userName: String): GameDetails?
}
