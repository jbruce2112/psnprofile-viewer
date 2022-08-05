package com.bruce32.psnprofileviewer.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PSNProfileServiceImpl(
    private val scraper: PSNProfileScraper = PSNProfileScraperImpl(),
    private val psnProfileApi: PSNProfileAPI = PSNProfileAPISource.get()
) : PSNProfileService {

    override suspend fun profileAndGames(userName: String) = withContext(Dispatchers.IO) {
        try {
            val profileHtml = psnProfileApi.profile(userName)
            scraper.profileAndGames(html = profileHtml)
        } catch (e: Exception) {
            Log.e("ProfileService", "Exception fetching profile and games : '${e.message}'")
            null
        }
    }

    override suspend fun gameDetails(gameId: String, userName: String) = withContext(Dispatchers.IO) {
        try {
            val gameHtml = psnProfileApi.game(gameId, userName)
            scraper.gameDetails(html = gameHtml, gameId, userName)
        } catch (e: Exception) {
            Log.e("ProfileService", "Exception fetching gameDetails for gameId $gameId : '${e.message}'")
            null
        }
    }
}
