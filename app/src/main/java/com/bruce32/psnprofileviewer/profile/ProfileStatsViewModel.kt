package com.bruce32.psnprofileviewer.profile

import java.net.URL

interface ProfileStatsViewModel {
    val heading: String
    val imageURL: URL
    val subheading: String
    val stats: List<ProfileStatViewModel>
}