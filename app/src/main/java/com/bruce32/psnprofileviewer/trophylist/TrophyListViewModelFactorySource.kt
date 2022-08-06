package com.bruce32.psnprofileviewer.trophylist

interface TrophyListViewModelFactorySource {
    fun factory(gameId: String): TrophyListViewModelFactory
}

class TrophyListViewModelFactorySourceImpl : TrophyListViewModelFactorySource {
   override fun factory(gameId: String) : TrophyListViewModelFactory {
       return TrophyListViewModelFactory(gameId)
   }
}