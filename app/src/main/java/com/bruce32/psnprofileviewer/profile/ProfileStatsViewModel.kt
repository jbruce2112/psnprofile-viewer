package com.bruce32.psnprofileviewer.profile

import com.bruce32.psnprofileviewer.model.Profile

class ProfileStatsViewModel(
    private val profile: Profile
) {
    val id = profile.psnId
    val imageURL = profile.avatarURL
    val trophies = trophies().joinToString(", ") {
        "${it.first.withSeparator()} ${it.second}"
    }
    val gamesPlayedValue = profile.gamesPlayed.toString()
    val percentCompleteValue = "${profile.completionPercent}%"
    val completedGamesValue = profile.completedGames.toString()
    val unearnedTrophiesValue = profile.unearnedTrophies.toString()
    val worldRankValue = String.format("%,d", profile.worldRank)
    val countryRankValue = String.format("%,d", profile.countryRank)
    val levelValue = profile.level.toString()

    private fun trophies() = listOf(
        Pair(profile.totalPlatinum, "Platinum"),
        Pair(profile.totalGold, "Gold"),
        Pair(profile.totalSilver, "Silver"),
        Pair(profile.totalBronze, "Bronze")
    )
}

private fun Int.withSeparator() = String.format("%,d", this)