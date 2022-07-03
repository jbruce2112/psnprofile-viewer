package com.bruce32.psnprofileviewer.api

import com.bruce32.psnprofileviewer.model.Profile
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

interface PSNProfileService {
    suspend fun profile(userName: String): Profile
    suspend fun game(gameId: String, userName: String): CompleteGame
}

class PSNProfileServiceImpl(
    private val scraper: PSNProfileScraper = PSNProfileScraperImpl()
) : PSNProfileService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://psnprofiles.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val psnProfileApi = retrofit.create<PSNProfileAPI>()

    override suspend fun profile(userName: String) = scraper.profile(html = psnProfileApi.profile(userName))

    override suspend fun game(gameId: String, userName: String) = scraper.game(html = psnProfileApi.game(gameId, userName))
}
