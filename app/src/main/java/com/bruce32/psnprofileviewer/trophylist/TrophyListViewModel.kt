package com.bruce32.psnprofileviewer.trophylist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// @@TODO HiltViewModel
class TrophyListViewModel @AssistedInject constructor(
    private val repository: ProfileRepository,
    @Assisted private val gameId: String
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

    companion object {
        fun provideFactory(
            assistedFactory: TrophyListViewModelAssistedFactory,
            gameId: String
        ): ViewModelProvider.NewInstanceFactory = object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(gameId) as T
            }
        }
    }
}
