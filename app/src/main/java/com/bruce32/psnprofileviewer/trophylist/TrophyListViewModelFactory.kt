package com.bruce32.psnprofileviewer.trophylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TrophyListViewModelFactory(
    private val gameId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrophyListViewModel(gameId = gameId) as T
    }
}
