package com.bruce32.psnprofileviewer.network

import android.util.Log
import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.ProfileWithGames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

interface PSNProfileService {
    suspend fun profileAndGames(userName: String): ProfileWithGames?
    suspend fun gameDetails(gameId: String, userName: String): GameDetails?
}

class PSNProfileServiceImpl(
    private val scraper: PSNProfileScraper = PSNProfileScraperImpl()
) : PSNProfileService {

    private val psnProfileApi: PSNProfileAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://psnprofiles.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create()
    }

    override suspend fun profileAndGames(userName: String) = withContext(Dispatchers.IO) {
        try {
            val profileHtml = psnProfileApi.profile(userName)
            scraper.profileWithGames(html = profileHtml)
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
