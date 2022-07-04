package com.bruce32.psnprofileviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.model.Profile
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _profile: MutableStateFlow<Profile?> = MutableStateFlow(null)
    val profile: StateFlow<Profile?>
        get() = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            async {
                _profile.value = repository.profile()
            }
            async {
                repository.refreshProfileAndGames()
            }
        }
    }
}
