package com.bruce32.psnprofileviewer.profile

import android.content.Context
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.common.StringResource
import com.bruce32.psnprofileviewer.common.StringResourceImpl
import com.bruce32.psnprofileviewer.model.Profile

class ProfileStatsViewModelImpl(
    profile: Profile
): ProfileStatsViewModel {
    override val heading = profile.psnId
    override val imageURL = profile.avatarURL
    override val subheading: StringResource = FormattedTrophyHeading(profile)

    override val stats = listOf(
        ProfileStatViewModel(profile.gamesPlayed.toString(), StringResourceImpl(R.string.games_played)),
        ProfileStatViewModel("${profile.completionPercent.padded()}%", StringResourceImpl(R.string.overall_completion)),
        ProfileStatViewModel(profile.completedGames.withSeparator(), StringResourceImpl(R.string.completed_games)),
        ProfileStatViewModel(profile.unearnedTrophies.withSeparator(), StringResourceImpl(R.string.unearned_trophies)),
        ProfileStatViewModel(profile.worldRank.withSeparator(), StringResourceImpl(R.string.world_rank)),
        ProfileStatViewModel(profile.countryRank.withSeparator(), StringResourceImpl(R.string.country_rank)),
        ProfileStatViewModel(profile.level.toString(), StringResourceImpl(R.string.psn_level))
    )
}

private class FormattedTrophyHeading(
    private val profile: Profile
): StringResource {
    override fun getString(context: Context) = formattedTrophies(profile, context)

    private fun formattedTrophies(profile: Profile, context: Context) = trophies(profile).joinToString(", ") {
        "${it.first.withSeparator()} ${it.second.getString(context)}"
    }

    private fun trophies(profile: Profile) = listOf(
        Pair(profile.totalPlatinum, StringResourceImpl(R.string.platinum)),
        Pair(profile.totalGold, StringResourceImpl(R.string.gold)),
        Pair(profile.totalSilver, StringResourceImpl(R.string.silver)),
        Pair(profile.totalBronze, StringResourceImpl(R.string.bronze))
    )
}

private fun Int.withSeparator() = String.format("%,d", this)
private fun Double.padded() = String.format("%.2f", this)
