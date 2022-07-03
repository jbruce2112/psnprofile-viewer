package com.bruce32.psnprofileviewer.api

import retrofit2.http.GET
import retrofit2.http.Path

interface PSNProfileAPI {
    @GET("/{username}")
    suspend fun profile(
        @Path("username")
        userName: String
    ): String
}
