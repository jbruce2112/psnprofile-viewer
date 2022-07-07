package com.bruce32.psnprofileviewer.gamelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.model.Game
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameListViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _games: MutableStateFlow<List<Game>> = MutableStateFlow(emptyList())
    val games: StateFlow<List<Game>>
        get() = _games.asStateFlow()

    init {
        Log.d("GameList", "ViewModel Created")
        viewModelScope.launch {
            async {
                repository.games()?.collect {
                    Log.d("GameListViewModel", "Got update with ${it.size} games")
                    Log.d("GameListViewModel", it.map { it.name }.joinToString(","))
                    _games.value = it
                }
            }
            async {
                repository.refreshProfileAndGames()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameList", "ViewModel Cleared")
    }
}
