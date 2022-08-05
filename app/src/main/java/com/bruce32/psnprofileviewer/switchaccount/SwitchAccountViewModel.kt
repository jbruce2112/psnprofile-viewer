package com.bruce32.psnprofileviewer.switchaccount

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.common.StringResource
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SwitchAccountViewModel (
    private val persistence: ProfilePersistence = ProfilePersistence.get(),
    private val repository: ProfileRepository = ProfileRepository()
): ViewModel() {

    private val _userFieldHint: MutableStateFlow<StringResource> = MutableStateFlow(UserIdResource(null))
    val userFieldHint: StateFlow<StringResource?>
        get() = _userFieldHint.asStateFlow()

    init {
        viewModelScope.launch {
            persistence.getCurrentUser().collect {
                _userFieldHint.value = UserIdResource(it?.psnId)
            }
        }
    }

    suspend fun setCurrentUser(newUserId: String) {
        persistence.setCurrentUser(newUserId)
        repository.refreshProfileAndGames()
    }
}

private class UserIdResource(
    private val currentUserId: String?
): StringResource {
    override fun getString(context: Context): String {
        return currentUserId ?: context.getString(R.string.default_user_hint)
    }
}