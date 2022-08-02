package com.bruce32.psnprofileviewer.trophylist

import dagger.assisted.AssistedFactory

@AssistedFactory
interface TrophyListViewModelAssistedFactory {
    fun create(gameId: String): TrophyListViewModel
}