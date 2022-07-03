package com.bruce32.psnprofileviewer.model

data class Profile(
    val psnId: String,
    val level: Int,
    val levelProgressPercent: Double,
    val totalTrophies: Int,
    val totalPlatinum: Int,
    val totalGold: Int,
    val totalSilver: Int,
    val totalBronze: Int,
    val stats: ProfileStats,
    val games: List<Game>
)
