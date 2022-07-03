package com.bruce32.psnprofileviewer.api

import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path

interface PSNProfileAPI {
    @GET("/{username}")
    suspend fun profile(
        @Path("username")
        userName: String
    ): String
    suspend fun game(userName: String, game: String): String
}

interface PSNProfileService {
    suspend fun profile(userName: String): String
    suspend fun game(userName: String, game: String): String
}

class PSNProfileServiceImpl: PSNProfileService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://psnprofiles.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val psnProfileApi = retrofit.create<PSNProfileAPI>()

    override suspend fun profile(userName: String): String {
        return psnProfileApi.profile(userName)
    }

    override suspend fun game(userName: String, game: String): String {
        return psnProfileApi.game(userName, game)
    }

}
