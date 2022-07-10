package com.bruce32.psnprofileviewer.common

import java.net.URL

interface ListItemViewModel {
    val id: String?
    val title: String
    val secondary: String?
    val description: String
    val leadingIconURL: URL
    val trailingText: String
    val highlighted: Boolean
}