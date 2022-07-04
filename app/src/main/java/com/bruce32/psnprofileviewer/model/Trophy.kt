package com.bruce32.psnprofileviewer.model

import java.net.URL

data class Trophy(
    val name: String,
    val description: String,
    val grade: String,
    val imageURL: URL,
    val earned: Boolean
)
