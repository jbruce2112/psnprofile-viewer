package com.bruce32.psnprofileviewer.gamelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameListViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _games: MutableStateFlow<List<GameViewModel>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<GameViewModel>>
        get() = _games.asStateFlow()

    init {
        viewModelScope.launch {
            async {
                repository.games.collect { games ->
                    Log.d("GameListViewModel", "Got update with ${games.size} games")
                    _games.value = games.map { GameViewModel(it) }
                }
            }
            async {
                repository.refreshProfileAndGames()
            }
        }
    }
}
