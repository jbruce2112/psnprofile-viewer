package com.bruce32.psnprofileviewer.common

import android.content.Context
import androidx.annotation.StringRes

class ResourceStringSourceImpl(
    private val context: Context
) : ResourceStringSource {
    override fun getString(@StringRes resId: Int, vararg args: Any) = context.getString(resId, *args)
}