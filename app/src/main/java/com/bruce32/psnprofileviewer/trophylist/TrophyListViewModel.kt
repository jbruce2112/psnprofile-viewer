package com.bruce32.psnprofileviewer.trophylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrophyListViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val gameId: String
) : ViewModel() {

    private val _trophies: MutableStateFlow<List<TrophyViewModel>> = MutableStateFlow(emptyList())
    val trophies: StateFlow<List<TrophyViewModel>>
        get() = _trophies.asStateFlow()

    init {
        Log.d("TrophyList", "initialized with gameId $gameId")
        viewModelScope.launch {
            async {
                repository.trophies(gameId).collect { trophies ->
                    _trophies.value = trophies.map { TrophyViewModel(it) }
                }
            }
            async {
                repository.refreshTrophies(gameId)
            }
        }
    }
}
