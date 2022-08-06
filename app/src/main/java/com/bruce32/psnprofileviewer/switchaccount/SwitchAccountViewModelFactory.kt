package com.bruce32.psnprofileviewer.switchaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bruce32.psnprofileviewer.common.ResourceStringSource

class SwitchAccountViewModelFactory(
    private val stringSource: ResourceStringSource
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SwitchAccountViewModel(stringSource = stringSource) as T
    }
}