package com.bruce32.psnprofileviewer.gamelist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameListViewModelFactorySource {
    fun factory(context: Context) = GameListViewModelFactory(StringSource(context))
}

class GameListViewModelFactory(
    private val stringSource: StringSource
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameListViewModel(stringSource = stringSource) as T
    }
}
