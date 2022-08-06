package com.bruce32.psnprofileviewer.switchaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.common.ResourceStringSource
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SwitchAccountViewModel (
    private val persistence: ProfilePersistence = ProfilePersistence.get(),
    private val repository: ProfileRepository = ProfileRepository(),
    stringSource: ResourceStringSource
): ViewModel() {

    private val defaultUserHint = stringSource.getString(R.string.default_user_hint)
    private val _userFieldHint: MutableStateFlow<String> = MutableStateFlow(defaultUserHint)
    val userFieldHint: StateFlow<String>
        get() = _userFieldHint.asStateFlow()

    init {
        viewModelScope.launch {
            persistence.getCurrentUser().collect {
                _userFieldHint.value = it?.psnId ?: defaultUserHint
            }
        }
    }

    suspend fun setCurrentUser(newUserId: String) {
        persistence.setCurrentUser(newUserId)
        repository.refreshProfileAndGames()
    }
}