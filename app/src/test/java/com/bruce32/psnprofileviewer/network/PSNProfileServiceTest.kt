package com.bruce32.psnprofileviewer.network

import android.util.Log
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.ProfileWithGames
import com.bruce32.psnprofileviewer.model.Trophy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.net.URL

class PSNProfileServiceTest {

    private lateinit var mockScraper: PSNProfileScraper
    private lateinit var mockPSNProfileAPI: PSNProfileAPI

    private lateinit var service: PSNProfileService

    @Before
    fun setup() {
        mockScraper = mockk {
            every { profileWithGames(any()) } returns mockk()
            every { gameDetails(any(), any(), any()) } returns mockk()
        }
        mockPSNProfileAPI = mockk {
            coEvery { profile(any()) } returns ""
            coEvery { game(any(), any()) } returns ""
        }

        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0

        service = PSNProfileServiceImpl(
            scraper = mockScraper,
            psnProfileApi = mockPSNProfileAPI
        )
    }

    @Test
    fun `profileAndGames calls api with userName`() {
        runBlocking { service.profileAndGames("someUserName") }
        coVerify(exactly=1) { mockPSNProfileAPI.profile("someUserName") }
    }

    @Test
    fun `profileAndGames calls scraper with response from api`() {
        coEvery { mockPSNProfileAPI.profile(any()) } returns "someHTMLString"
        runBlocking { service.profileAndGames("") }
        coVerify(exactly=1) { mockScraper.profileWithGames("someHTMLString") }
    }

    @Test
    fun `profileAndGames returns response from scraper`() {
        val fakeScraperResponse = ProfileWithGames(
            profile = fakeProfile("fetchedPsnId"),
            games = listOf(fakeGame("fetchedGameId"))
        )
        coEvery { mockScraper.profileWithGames("") } returns fakeScraperResponse
        val serviceResponse = runBlocking { service.profileAndGames("") }

        assertEquals(serviceResponse, fakeScraperResponse)
    }

    @Test
    fun `profileAndGames returns null when api throws`() {
        coEvery { mockPSNProfileAPI.profile(any()) } throws RuntimeException("whoops")
        val serviceResponse = runBlocking { service.profileAndGames("") }

        assertNull(serviceResponse)
    }

    @Test
    fun `profileAndGames returns null when scraper throws`() {
        coEvery { mockScraper.profileWithGames(any()) } throws RuntimeException("whoops")
        val serviceResponse = runBlocking { service.profileAndGames("") }

        assertNull(serviceResponse)
    }

    @Test
    fun `gameDetails calls api with gameId and userName`() {
        runBlocking { service.gameDetails("someGameId", "someUserName") }
        coVerify(exactly=1) { mockPSNProfileAPI.game("someGameId", "someUserName") }
    }

    @Test
    fun `gameDetails calls scraper with response from api and userName and gameId`() {
        coEvery { mockPSNProfileAPI.game(any(), any()) } returns "someHTMLString"
        runBlocking { service.gameDetails("someGameId", "someUserName") }
        coVerify(exactly=1) { mockScraper.gameDetails("someHTMLString", "someGameId", "someUserName") }
    }

    @Test
    fun `gameDetails returns response from scraper`() {
        val fakeScraperResponse = GameDetails(
            trophies = listOf(fakeTrophy("fetchedTrophyName"))
        )
        coEvery { mockScraper.gameDetails(any(), any(), any()) } returns fakeScraperResponse
        val serviceResponse = runBlocking { service.gameDetails("", "") }

        assertEquals(serviceResponse, fakeScraperResponse)
    }

    @Test
    fun `gameDetails returns null when api throws`() {
        coEvery { mockPSNProfileAPI.game(any(), any()) } throws RuntimeException("whoops")
        val serviceResponse = runBlocking { service.gameDetails("", "") }

        assertNull(serviceResponse)
    }

    @Test
    fun `gameDetails returns null when scraper throws`() {
        coEvery { mockScraper.gameDetails(any(), any(), any()) } throws RuntimeException("whoops")
        val serviceResponse = runBlocking { service.gameDetails("", "") }

        assertNull(serviceResponse)
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

private fun fakeGame(id: String) = Game(
    name = "someName",
    coverURL = URL("https://www.psnprofiles.com"),
    platform = "somePlatform",
    platinum = null,
    id = id,
    gold = 0,
    silver = 0,
    bronze = 0,
    completionPercent = 0.0,
    earnedTrophies = 0,
    totalTrophies = 0,
    playerPsnId = ""
)

private fun fakeTrophy(
    name: String = "someName",
    description: String = "someDescrption",
    grade: String = "someGrade",
    imageURL: URL = URL("https://www.psnprofiles.com"),
    earned: Boolean = false,
    gameId: String = "someGameId",
    playerPsnId: String = "somePlayerId"
) = Trophy(
    name = name,
    description = description,
    grade = grade,
    imageURL = imageURL,
    earned = earned,
    gameId = gameId,
    playerPsnId = playerPsnId
)
