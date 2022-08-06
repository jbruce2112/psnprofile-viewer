package com.bruce32.psnprofileviewer.common

import androidx.annotation.StringRes

interface ResourceStringSource {
    fun getString(@StringRes resId: Int, vararg args: Any): String
}