package com.bruce32.psnprofileviewer.switchaccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SwitchAccountViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SwitchAccountViewModel() as T
    }
}