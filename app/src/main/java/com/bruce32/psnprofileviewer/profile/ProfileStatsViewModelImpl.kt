package com.bruce32.psnprofileviewer.profile

import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.common.ResourceStringSource
import com.bruce32.psnprofileviewer.model.Profile

class ProfileStatsViewModelImpl(
    private val profile: Profile,
    private val stringSource: ResourceStringSource
): ProfileStatsViewModel {
    override val heading = profile.psnId
    override val imageURL = profile.avatarURL
    override val subheading = formattedTrophies()

    override val stats = listOf(
        ProfileStatViewModel(profile.gamesPlayed.toString(), stringSource.getString(R.string.games_played)),
        ProfileStatViewModel("${profile.completionPercent.padded()}%", stringSource.getString(R.string.overall_completion)),
        ProfileStatViewModel(profile.completedGames.withSeparator(), stringSource.getString(R.string.completed_games)),
        ProfileStatViewModel(profile.unearnedTrophies.withSeparator(), stringSource.getString(R.string.unearned_trophies)),
        ProfileStatViewModel(profile.worldRank.withSeparator(), stringSource.getString(R.string.world_rank)),
        ProfileStatViewModel(profile.countryRank.withSeparator(), stringSource.getString(R.string.country_rank)),
        ProfileStatViewModel(profile.level.toString(), stringSource.getString(R.string.psn_level))
    )

    private fun formattedTrophies() = trophies().joinToString(", ") {
        "${it.first.withSeparator()} ${it.second}"
    }

    private fun trophies() = listOf(
        Pair(profile.totalPlatinum, stringSource.getString(R.string.platinum)),
        Pair(profile.totalGold, stringSource.getString(R.string.gold)),
        Pair(profile.totalSilver, stringSource.getString(R.string.silver)),
        Pair(profile.totalBronze, stringSource.getString(R.string.bronze))
    )

}

private fun Int.withSeparator() = String.format("%,d", this)
private fun Double.padded() = String.format("%.2f", this)
