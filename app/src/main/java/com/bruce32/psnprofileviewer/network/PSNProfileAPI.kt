package com.bruce32.psnprofileviewer.network

import retrofit2.http.GET
import retrofit2.http.Path

interface PSNProfileAPI {
    @GET("/{username}")
    suspend fun profile(
        @Path("username")
        userName: String
    ): String

    @GET("/trophies/{gameId}/{username}")
    suspend fun game(
        @Path("gameId")
        gameId: String,
        @Path("username")
        userName: String
    ): String
}
