package com.bruce32.psnprofileviewer.common

interface ListItemAdapterSource {
    fun adapter(onClick: ((itemId: String) -> Unit)? = null): ListItemAdapter
}