package com.bruce32.psnprofileviewer.application

import android.app.Application
import com.bruce32.psnprofileviewer.database.ProfilePersistence

class PSNProfileViewerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ProfilePersistence.initialize(this)
    }
}
