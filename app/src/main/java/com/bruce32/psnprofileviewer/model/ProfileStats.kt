package com.bruce32.psnprofileviewer.model

data class ProfileStats(
    val gamesPlayed: Int,
    val completedGames: Int,
    val completionPercent: Double,
    val unearnedTrophies: Int,
    val trophiesPerDay: Double,
    val worldRank: Int,
    val countryRank: Int
)