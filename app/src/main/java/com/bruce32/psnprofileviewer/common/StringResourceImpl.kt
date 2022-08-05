package com.bruce32.psnprofileviewer.common

import android.content.Context
import androidx.annotation.StringRes

class StringResourceImpl(
    @StringRes private val resId: Int,
    private vararg val args: Any
) : StringResource {
    override fun getString(context: Context) = context.getString(resId, *args)
}