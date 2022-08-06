package com.bruce32.psnprofileviewer.switchaccount

import android.content.Context
import com.bruce32.psnprofileviewer.common.ResourceStringSourceImpl

interface SwitchAccountViewModelFactorySource {
    fun factory(context: Context): SwitchAccountViewModelFactory
}

class SwitchAccountViewModelFactorySourceImpl : SwitchAccountViewModelFactorySource {
    override fun factory(context: Context) = SwitchAccountViewModelFactory(ResourceStringSourceImpl(context))
}