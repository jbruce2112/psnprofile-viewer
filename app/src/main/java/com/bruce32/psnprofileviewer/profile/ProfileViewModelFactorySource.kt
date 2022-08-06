package com.bruce32.psnprofileviewer.profile

import android.content.Context
import com.bruce32.psnprofileviewer.common.ResourceStringSourceImpl

interface ProfileViewModelFactorySource {
    fun factory(context: Context): ProfileViewModelFactory
}

class ProfileViewModelFactorySourceImpl : ProfileViewModelFactorySource {
    override fun factory(context: Context) = ProfileViewModelFactory(ResourceStringSourceImpl(context))
}