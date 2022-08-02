package com.bruce32.psnprofileviewer.gamelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameListViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val persistence: ProfilePersistence
) : ViewModel() {

    private val _games: MutableStateFlow<List<GameViewModel>> = MutableStateFlow(emptyList())
    val items: StateFlow<List<GameViewModel>>
        get() = _games.asStateFlow()

    private val _message: MutableStateFlow<String?> = MutableStateFlow(null)
    val message: StateFlow<String?>
        get() = _message.asStateFlow()

    init {
        viewModelScope.launch {
            async {
                repository.games.collect { games ->
                    Log.d("GameListViewModel", "Got update with ${games.size} games")
                    _games.value = games.map { GameViewModel(it) }
                }
            }
            async {
                persistence.getCurrentUser().collect { currentUser ->
                    if (currentUser == null) {
                        _message.value =
                            "Please enter a PSN ID in the menu to see your progress."
                    } else {
                        _message.value = null
                    }
                }
            }
            async {
                repository.refreshProfileAndGames()
            }
        }
    }
}
