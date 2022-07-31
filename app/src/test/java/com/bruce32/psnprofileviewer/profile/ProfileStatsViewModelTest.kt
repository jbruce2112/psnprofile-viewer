package com.bruce32.psnprofileviewer.profile

import com.bruce32.psnprofileviewer.model.Profile
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URL

class ProfileStatsViewModelTest {

    @Test
    fun `id is psn id`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(psnId = "myPsnId")
        )
        assertEquals("myPsnId", viewModel.id)
    }

    @Test
    fun `imageURL is avatar url`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(avatarURL = URL("https://www.psnprofiles.com"))
        )
        assertEquals("https://www.psnprofiles.com", viewModel.imageURL.toString())
    }

    @Test
    fun `trophies is formatted trophy list`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(totalPlatinum = 22, totalGold = 4344, totalSilver = 4333, totalBronze = 224345)
        )
        assertEquals("22 Platinum, 4,344 Gold, 4,333 Silver, 224,345 Bronze", viewModel.trophies)
    }

    @Test
    fun `gamesPlayedValue is gamesPlayed`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(gamesPlayed = 55)
        )
        assertEquals("55", viewModel.gamesPlayedValue)
    }

    @Test
    fun `percentCompleteValue is percentComplete`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(completionPercent = 55.42)
        )
        assertEquals("55.42%", viewModel.percentCompleteValue)
    }

    @Test
    fun `completedGamesValue is completedGames`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(completedGames = 32)
        )
        assertEquals("32", viewModel.completedGamesValue)
    }

    @Test
    fun `unearnedTrophiesValue is unearnedTrophies`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(unearnedTrophies = 14500)
        )
        assertEquals("14,500", viewModel.unearnedTrophiesValue)
    }

    @Test
    fun `worldRankValue is worldRank`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(worldRank = 202500)
        )
        assertEquals("202,500", viewModel.worldRankValue)
    }

    @Test
    fun `countryRankValue is countryRank`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(countryRank = 500123)
        )
        assertEquals("500,123", viewModel.countryRankValue)
    }

    @Test
    fun `levelValue is level`() {
        val viewModel = ProfileStatsViewModel(
            profile = fakeProfile(level = 250)
        )
        assertEquals("250", viewModel.levelValue)
    }
}

private fun fakeProfile(
    psnId: String = "somePsnId",
    level: Int = 3,
    avatarURL: URL = URL("https://www.psnprofiles.com"),
    levelProgressPercent: Double = 24.42,
    totalTrophies: Int = 100,
    totalPlatinum: Int = 1,
    totalGold: Int = 124,
    totalSilver: Int = 1234,
    totalBronze: Int = 123,
    gamesPlayed: Int = 24,
    completedGames: Int = 2,
    completionPercent: Double = 42.00,
    unearnedTrophies: Int = 1500,
    trophiesPerDay: Double = 24.42,
    worldRank: Int = 123222,
    countryRank: Int = 12344
) = Profile(
    psnId = psnId,
    level = level,
    avatarURL = avatarURL,
    levelProgressPercent = levelProgressPercent,
    totalTrophies = totalTrophies,
    totalPlatinum = totalPlatinum,
    totalGold = totalGold,
    totalSilver = totalSilver,
    totalBronze = totalBronze,
    gamesPlayed = gamesPlayed,
    completedGames = completedGames,
    completionPercent = completionPercent,
    unearnedTrophies = unearnedTrophies,
    trophiesPerDay = trophiesPerDay,
    worldRank = worldRank,
    countryRank = countryRank
)