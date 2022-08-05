package com.bruce32.psnprofileviewer.network

import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.ProfileAndGames

interface PSNProfileScraper {
    fun profileAndGames(html: String): ProfileAndGames
    fun gameDetails(html: String, gameId: String, psnId: String): GameDetails
}
