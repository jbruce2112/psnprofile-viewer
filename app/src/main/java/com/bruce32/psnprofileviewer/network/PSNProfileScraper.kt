package com.bruce32.psnprofileviewer.network

import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.ProfileWithGames

interface PSNProfileScraper {
    fun profileWithGames(html: String): ProfileWithGames
    fun gameDetails(html: String, gameId: String, psnId: String): GameDetails
}
