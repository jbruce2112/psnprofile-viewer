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

    val stats = listOf(
        ProfileStatViewModel(profile.gamesPlayed.toString(), "Games Played"),
        ProfileStatViewModel("${profile.completionPercent.padded()}%", "Overall Completion"),
        ProfileStatViewModel(profile.completedGames.withSeparator(), "Completed Games"),
        ProfileStatViewModel(profile.unearnedTrophies.withSeparator(), "Unearned Trophies"),
        ProfileStatViewModel(profile.worldRank.withSeparator(), "World Rank"),
        ProfileStatViewModel(profile.countryRank.withSeparator(), "Country Rank"),
        ProfileStatViewModel(profile.level.toString(), "PSN Level")
    )

    private fun trophies() = listOf(
        Pair(profile.totalPlatinum, "Platinum"),
        Pair(profile.totalGold, "Gold"),
        Pair(profile.totalSilver, "Silver"),
        Pair(profile.totalBronze, "Bronze")
    )
}

data class ProfileStatViewModel(
    val value: String,
    val label: String
)

private fun Int.withSeparator() = String.format("%,d", this)
private fun Double.padded() = String.format("%.2f", this)
