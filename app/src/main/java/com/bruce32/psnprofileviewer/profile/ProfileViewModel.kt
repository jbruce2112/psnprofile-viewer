package com.bruce32.psnprofileviewer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _profile: MutableStateFlow<ProfileStatsViewModel?> = MutableStateFlow(null)
    val profile: StateFlow<ProfileStatsViewModel?>
        get() = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            async {
                repository.profile.collect {
                    if (it == null) {
                        _profile.value = null
                    } else {
                        _profile.value = ProfileStatsViewModel(it)
                    }
                }
            }
            async {
                repository.refreshProfileAndGames()
            }
        }
    }
}
