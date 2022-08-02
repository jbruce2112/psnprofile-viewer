package com.bruce32.psnprofileviewer.application

import android.content.Context
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePersistence(@ApplicationContext context: Context) = ProfilePersistence(context)

}