package com.bruce32.psnprofileviewer.trophylist

interface TrophyListViewModelFactoryProvider {
    fun factory(gameId: String): TrophyListViewModelFactory
}

class TrophyListViewModelFactoryProviderImpl : TrophyListViewModelFactoryProvider {
   override fun factory(gameId: String) : TrophyListViewModelFactory {
       return TrophyListViewModelFactory(gameId)
   }
}