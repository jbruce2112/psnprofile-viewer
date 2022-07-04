package com.bruce32.psnprofileviewer.api

import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.ProfileWithGames
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

    override suspend fun profileAndGames(userName: String) =
        scraper.profileWithGames(html = psnProfileApi.profile(userName))

    override suspend fun gameDetails(gameId: String, userName: String) =
        scraper.gameDetails(html = psnProfileApi.game(gameId, userName), gameId, userName)
}
