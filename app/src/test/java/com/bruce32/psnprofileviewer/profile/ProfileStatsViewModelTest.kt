package com.bruce32.psnprofileviewer.profile

import com.bruce32.psnprofileviewer.FakeResourceStringSource
import com.bruce32.psnprofileviewer.model.Profile
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URL

class ProfileStatsViewModelTest {

    private val fakeStringSource = FakeResourceStringSource()

    @Test
    fun `heading is psn id`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(psnId = "myPsnId"),
            stringSource = fakeStringSource
        )
        assertEquals("myPsnId", viewModel.heading)
    }

    @Test
    fun `imageURL is avatar url`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(avatarURL = URL("https://www.psnprofiles.com")),
            stringSource = fakeStringSource
        )
        assertEquals("https://www.psnprofiles.com", viewModel.imageURL.toString())
    }

    @Test
    fun `subheading is formatted trophy list`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(totalPlatinum = 22, totalGold = 4344, totalSilver = 4333, totalBronze = 224345),
            stringSource = fakeStringSource
        )
        assertEquals("22 Platinum, 4,344 Gold, 4,333 Silver, 224,345 Bronze", viewModel.subheading)
    }

    @Test
    fun `there are seven statViewModels`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(),
            stringSource = fakeStringSource
        )
        assertEquals(7, viewModel.stats.size)
    }

    @Test
    fun `first statViewModel is gamesPlayed`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(gamesPlayed = 55),
            stringSource = fakeStringSource
        )
        assertEquals("55", viewModel.stats[0].value)
        assertEquals("Games Played", viewModel.stats[0].label)
    }

    @Test
    fun `second statViewModel is percentComplete`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(completionPercent = 55.42),
            stringSource = fakeStringSource
        )
        assertEquals("55.42%", viewModel.stats[1].value)
        assertEquals("Overall Completion", viewModel.stats[1].label)
    }

    @Test
    fun `second statViewModel is percentComplete with a padded zero when trailing digit is zero`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(completionPercent = 55.4),
            stringSource = fakeStringSource
        )
        assertEquals("55.40%", viewModel.stats[1].value)
        assertEquals("Overall Completion", viewModel.stats[1].label)
    }

    @Test
    fun `third statViewModel is completedGames`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(completedGames = 32),
            stringSource = fakeStringSource
        )
        assertEquals("32", viewModel.stats[2].value)
        assertEquals("Completed Games", viewModel.stats[2].label)
    }

    @Test
    fun `fourth statViewModel is unearnedTrophies`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(unearnedTrophies = 14500),
            stringSource = fakeStringSource
        )
        assertEquals("14,500", viewModel.stats[3].value)
        assertEquals("Unearned Trophies", viewModel.stats[3].label)
    }

    @Test
    fun `fifth statViewModel is worldRank`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(worldRank = 202500),
            stringSource = fakeStringSource
        )
        assertEquals("202,500", viewModel.stats[4].value)
        assertEquals("World Rank", viewModel.stats[4].label)
    }

    @Test
    fun `sixth statViewModel is countryRank`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(countryRank = 500123),
            stringSource = fakeStringSource
        )
        assertEquals("500,123", viewModel.stats[5].value)
        assertEquals("Country Rank", viewModel.stats[5].label)
    }

    @Test
    fun `seventh statViewModel is level`() {
        val viewModel = ProfileStatsViewModelImpl(
            profile = fakeProfile(level = 250),
            stringSource = fakeStringSource
        )
        assertEquals("250", viewModel.stats[6].value)
        assertEquals("PSN Level", viewModel.stats[6].label)
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
