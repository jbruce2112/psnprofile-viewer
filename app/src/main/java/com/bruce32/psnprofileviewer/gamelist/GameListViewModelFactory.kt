package com.bruce32.psnprofileviewer.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameListViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameListViewModel() as T
    }
}
