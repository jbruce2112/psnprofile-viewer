package com.bruce32.psnprofileviewer.trophylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class TrophyListViewModelFactory(
    private val gameId: String
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return TrophyListViewModel(gameId = gameId) as T
    }
}
