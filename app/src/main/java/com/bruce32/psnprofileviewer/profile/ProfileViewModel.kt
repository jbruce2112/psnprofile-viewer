package com.bruce32.psnprofileviewer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.common.ResourceStringSource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository(),
    private val stringSource: ResourceStringSource
) : ViewModel() {

    private val _profile: MutableStateFlow<ProfileStatsViewModel?> = MutableStateFlow(null)
    val profile: StateFlow<ProfileStatsViewModel?>
        get() = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            async {
                repository.profile.collect {
                    _profile.value = it?.let {
                        ProfileStatsViewModelImpl(it, stringSource)
                    }
                }
            }
            async {
                repository.refreshProfileAndGames()
            }
        }
    }
}
