package com.bruce32.psnprofileviewer.gamelist

import android.content.Context
import com.bruce32.psnprofileviewer.common.ResourceStringSourceImpl

interface GameListViewModelFactorySource {
    fun factory(context: Context): GameListViewModelFactory
}

class GameListViewModelFactorySourceImpl : GameListViewModelFactorySource {
    override fun factory(context: Context) = GameListViewModelFactory(ResourceStringSourceImpl(context))
}