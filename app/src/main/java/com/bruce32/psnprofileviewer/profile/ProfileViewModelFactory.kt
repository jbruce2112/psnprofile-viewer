package com.bruce32.psnprofileviewer.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bruce32.psnprofileviewer.common.ResourceStringSource

class ProfileViewModelFactory(
    private val stringSource: ResourceStringSource
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(stringSource = stringSource) as T
    }
}