package com.bruce32.psnprofileviewer.application

import com.bruce32.psnprofileviewer.network.PSNProfileScraper
import com.bruce32.psnprofileviewer.network.PSNProfileScraperImpl
import com.bruce32.psnprofileviewer.network.PSNProfileService
import com.bruce32.psnprofileviewer.network.PSNProfileServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class ActivityModule {

    @Binds
    abstract fun bindProfileService(
        profileServiceImpl: PSNProfileServiceImpl
    ): PSNProfileService

    @Binds
    abstract fun bindProfileScraper(
        profileScraperImpl: PSNProfileScraperImpl
    ): PSNProfileScraper
}