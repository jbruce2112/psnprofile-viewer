package com.bruce32.psnprofileviewer.gamelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.common.StringResourceImpl
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import com.bruce32.psnprofileviewer.model.Game
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GameListViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val persistence: ProfilePersistence = ProfilePersistence.get()
) : ViewModel() {

    private val _games: MutableStateFlow<GameListUpdate> = MutableStateFlow(GameListUpdate.Empty(null))
    val items: StateFlow<GameListUpdate>
        get() = _games.asStateFlow()

    init {
        viewModelScope.launch {
            async {
                repository.games.collect { games ->
                    Log.d("GameListViewModel", "Got update with ${games.size} games")
                    _games.value = createUpdate(games)
                }
            }
            async {
                repository.refreshProfileAndGames()
            }
        }
    }

    private suspend fun createUpdate(games: List<Game>): GameListUpdate {
        return if (games.isEmpty()) {
            createEmptyState()
        } else {
            val viewModels = games.map { GameViewModel(it) }
            GameListUpdate.Items(viewModels)
        }
    }

    private suspend fun createEmptyState(): GameListUpdate {
        val userId = persistence.getCurrentUser().first()?.psnId
        return if (userId == null) {
            GameListUpdate.Empty(StringResourceImpl(R.string.sign_in_text))
        } else {
            GameListUpdate.Empty(StringResourceImpl(R.string.no_games_found, userId))
        }
    }
}
