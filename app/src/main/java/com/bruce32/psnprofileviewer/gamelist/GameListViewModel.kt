package com.bruce32.psnprofileviewer.gamelist

import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import com.bruce32.psnprofileviewer.model.Game
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class GameListUpdate {
    class Empty(val message: String): GameListUpdate()
    class Items(val viewModels: List<GameViewModel>): GameListUpdate()
}

class StringSource(
    private val context: Context
) {
    fun getString(@StringRes resId: Int) = context.getString(resId)
    fun getString(@StringRes resId: Int, vararg args: Any) = context.getString(resId, *args)
}

class GameListViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val persistence: ProfilePersistence = ProfilePersistence.get(),
    private val stringSource: StringSource
) : ViewModel() {

    private val _games: MutableStateFlow<GameListUpdate> = MutableStateFlow(GameListUpdate.Empty(""))
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
            GameListUpdate.Empty(stringSource.getString(R.string.sign_in_text))
        } else {
            GameListUpdate.Empty(stringSource.getString(R.string.no_games_found, userId))
        }
    }
}
