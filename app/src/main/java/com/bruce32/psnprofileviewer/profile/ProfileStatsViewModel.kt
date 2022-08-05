package com.bruce32.psnprofileviewer.profile

import com.bruce32.psnprofileviewer.common.StringResource
import java.net.URL

interface ProfileStatsViewModel {
    val heading: String
    val imageURL: URL
    val subheading: StringResource
    val stats: List<ProfileStatViewModel>
}