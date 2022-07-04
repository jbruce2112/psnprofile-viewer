package com.bruce32.psnprofileviewer.model

import java.net.URL

data class Game(
    val name: String,
    val coverURL: URL,
    val platform: String,
    val platinum: Int?,
    val id: String,
    val gold: Int,
    val silver: Int,
    val bronze: Int,
    val completionPercent: Double,
    val earnedTrophies: Int,
    val totalTrophies: Int
)
