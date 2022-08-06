package com.bruce32.psnprofileviewer.trophylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.model.Trophy
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TrophyListViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val gameId: String
) : ViewModel() {

    private val _trophies: MutableStateFlow<TrophyListUpdate> = MutableStateFlow(TrophyListUpdate.Loading)
    val trophies: StateFlow<TrophyListUpdate>
        get() = _trophies.asStateFlow()

    private var refreshTask: Deferred<Unit>? = null

    init {
        Log.d("TrophyList", "initialized with gameId $gameId")
        viewModelScope.launch {
            async {
                repository.trophies(gameId).collect {
                    _trophies.value = createUpdate(it)
                }
            }
            refreshTask = async {
                repository.refreshTrophies(gameId)
            }
        }
    }

    private fun createUpdate(trophies: List<Trophy>): TrophyListUpdate {
        if (trophies.isEmpty() && refreshTask?.isActive == true) {
            return TrophyListUpdate.Loading
        }
        return TrophyListUpdate.Items(
            viewModels = trophies.map { TrophyViewModel(it) }
        )
    }
}
