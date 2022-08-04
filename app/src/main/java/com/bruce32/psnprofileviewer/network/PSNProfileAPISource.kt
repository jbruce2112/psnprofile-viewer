package com.bruce32.psnprofileviewer.network

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

object PSNProfileAPISource {
    fun get(): PSNProfileAPI {
        return Retrofit.Builder()
            .baseUrl("https://psnprofiles.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create()
    }
}