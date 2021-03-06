package com.bruce32.psnprofileviewer.api

import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.ProfileWithGames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

interface PSNProfileService {
    suspend fun profileAndGames(userName: String): ProfileWithGames
    suspend fun gameDetails(gameId: String, userName: String): GameDetails
}

class PSNProfileServiceImpl(
    private val scraper: PSNProfileScraper = PSNProfileScraperImpl()
) : PSNProfileService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://psnprofiles.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val psnProfileApi = retrofit.create<PSNProfileAPI>()

    override suspend fun profileAndGames(userName: String) = withContext(Dispatchers.IO) {
        val profileHtml = psnProfileApi.profile(userName)
        scraper.profileWithGames(html = profileHtml)
    }

    override suspend fun gameDetails(gameId: String, userName: String) = withContext(Dispatchers.IO) {
        val gameHtml = psnProfileApi.game(gameId, userName)
        scraper.gameDetails(html = gameHtml, gameId, userName)
    }
}
