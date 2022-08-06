package com.bruce32.psnprofileviewer.profile

import com.bruce32.psnprofileviewer.model.Profile

class ProfileStatsViewModelImpl(
    private val profile: Profile
): ProfileStatsViewModel {
    override val heading = profile.psnId
    override val imageURL = profile.avatarURL
    override val subheading = formattedTrophies()

    override val stats = listOf(
        ProfileStatViewModel(profile.gamesPlayed.toString(), "Games Played"),
        ProfileStatViewModel("${profile.completionPercent.padded()}%", "Overall Completion"),
        ProfileStatViewModel(profile.completedGames.withSeparator(), "Completed Games"),
        ProfileStatViewModel(profile.unearnedTrophies.withSeparator(), "Unearned Trophies"),
        ProfileStatViewModel(profile.worldRank.withSeparator(), "World Rank"),
        ProfileStatViewModel(profile.countryRank.withSeparator(), "Country Rank"),
        ProfileStatViewModel(profile.level.toString(), "PSN Level")
    )

    private fun formattedTrophies() = trophies().joinToString(", ") {
        "${it.first.withSeparator()} ${it.second}"
    }

    private fun trophies() = listOf(
        Pair(profile.totalPlatinum, "Platinum"),
        Pair(profile.totalGold, "Gold"),
        Pair(profile.totalSilver, "Silver"),
        Pair(profile.totalBronze, "Bronze")
    )

}

private fun Int.withSeparator() = String.format("%,d", this)
private fun Double.padded() = String.format("%.2f", this)
