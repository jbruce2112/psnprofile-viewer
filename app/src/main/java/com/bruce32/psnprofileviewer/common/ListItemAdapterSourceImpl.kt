package com.bruce32.psnprofileviewer.common

class ListItemAdapterSourceImpl: ListItemAdapterSource {
    override fun adapter(onClick: ((itemId: String) -> Unit)?) = ListItemAdapter(onClick)
}