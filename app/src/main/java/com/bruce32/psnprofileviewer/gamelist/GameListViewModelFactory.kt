package com.bruce32.psnprofileviewer.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bruce32.psnprofileviewer.common.ResourceStringSource

class GameListViewModelFactory(
    private val stringSource: ResourceStringSource
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameListViewModel(stringSource = stringSource) as T
    }
}
