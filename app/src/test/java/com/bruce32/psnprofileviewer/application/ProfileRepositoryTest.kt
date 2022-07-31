package com.bruce32.psnprofileviewer.application

import android.util.Log
import com.bruce32.psnprofileviewer.network.PSNProfileService
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import com.bruce32.psnprofileviewer.model.CurrentUser
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
import io.mockk.slot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URL

class ProfileRepositoryTest {

    private lateinit var mockCurrentUserFlow: MutableStateFlow<CurrentUser?>
    private lateinit var mockPersistenceTrophyFlow: MutableStateFlow<List<Trophy>>
    private lateinit var mockPersistenceProfileFlow: MutableStateFlow<Profile?>
    private lateinit var mockPersistenceGameFlow: MutableStateFlow<List<Game>>

    private lateinit var mockPersistence: ProfilePersistence

    private lateinit var mockService: PSNProfileService

    private lateinit var repository: ProfileRepository

    @Before
    fun setup() {
        mockPersistenceProfileFlow = MutableStateFlow(null)
        mockPersistenceGameFlow = MutableStateFlow(emptyList())
        mockPersistenceTrophyFlow = MutableStateFlow(emptyList())
        mockCurrentUserFlow = MutableStateFlow(null)
        mockPersistence = mockk {
            every { getProfile() } returns mockPersistenceProfileFlow.asStateFlow()
            every { getGames() } returns mockPersistenceGameFlow.asStateFlow()
            every { getTrophies(any()) } returns mockPersistenceTrophyFlow.asStateFlow()
            every { getCurrentUser() } returns mockCurrentUserFlow.asStateFlow()
            coEvery { insertProfile(any()) } returns Unit
            coEvery { insertGames(any()) } returns Unit
            coEvery { insertTrophies(any()) } returns Unit
        }

        mockService = mockk {
            coEvery { profileAndGames(any()) } returns ProfileWithGames(fakeProfile(), emptyList())
            coEvery { gameDetails(any(), any()) } returns GameDetails(emptyList())
        }

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        repository = ProfileRepository(
            service = mockService,
            persistence = mockPersistence
        )
    }

    @Test
    fun `games emitted by persistence are emitted by repository`() {
        runBlocking {
            mockPersistenceGameFlow.emit(
                listOf(
                    fakeGame(name = "game1"),
                    fakeGame(name = "game2"),
                    fakeGame(name = "game3")
                )
            )
        }

        val games = runBlocking { repository.games.first() }
        assertEquals(3, games.size)
        assertEquals("game1", games[0].name)
        assertEquals("game2", games[1].name)
        assertEquals("game3", games[2].name)
    }

    @Test
    fun `profile emitted by persistence is emitted by repository`() {
        runBlocking {
            mockPersistenceProfileFlow.emit(fakeProfile(psnId = "1234"))
        }

        val profile = runBlocking { repository.profile.first() }
        assertEquals("1234", profile?.psnId)
    }

    @Test
    fun `trophies returns flow from persistence`() {
        runBlocking {
            mockPersistenceTrophyFlow.emit(
                listOf(
                    fakeTrophy(name = "trophy1"),
                    fakeTrophy(name = "trophy2"),
                    fakeTrophy(name = "trophy3")
                )
            )
        }

        val trophies = runBlocking { repository.trophies("someGame").first() }
        assertEquals(3, trophies.size)
    }

    @Test
    fun `gameId from trophies is passed to persistence`() {
        val gameIdSlot = slot<String>()
        coEvery {
            mockPersistence.getTrophies(capture(gameIdSlot))
        } returns flowOf(emptyList())

        runBlocking { repository.trophies("someGameId") }
        assertEquals("someGameId", gameIdSlot.captured)
    }

    @Test
    fun `refreshProfileAndGames calls service with current user`() {
        coEvery {
            mockPersistence.getCurrentUser()
        } returns flowOf(CurrentUser("somePsnId"))

        runBlocking { repository.refreshProfileAndGames() }

        val psnIdSlot = slot<String>()
        coVerify(exactly=1) { mockService.profileAndGames(capture(psnIdSlot)) }

        assertEquals("somePsnId", psnIdSlot.captured)
    }

    @Test
    fun `refreshProfileAndGames persists profile from service`() {
        coEvery {
            mockPersistence.getCurrentUser()
        } returns flowOf(CurrentUser("somePsnId"))
        coEvery {
            mockService.profileAndGames("somePsnId")
        } returns ProfileWithGames(fakeProfile(psnId = "somePsnIdFromService"), emptyList())

        runBlocking { repository.refreshProfileAndGames() }

        val profileSlot = slot<Profile>()
        coVerify(exactly=1) { mockPersistence.insertProfile(capture(profileSlot)) }

        assertEquals("somePsnIdFromService", profileSlot.captured.psnId)
    }

    @Test
    fun `refreshProfileAndGames persists games from service`() {
        coEvery {
            mockPersistence.getCurrentUser()
        } returns flowOf(CurrentUser("somePsnId"))

        val fakeGames = listOf(
            fakeGame(name = "game1"),
            fakeGame(name = "game2"),
            fakeGame(name = "game3")
        )
        coEvery {
            mockService.profileAndGames("somePsnId")
        } returns ProfileWithGames(fakeProfile(), fakeGames)

        runBlocking { repository.refreshProfileAndGames() }

        val gamesSlot = slot<List<Game>>()
        coVerify(exactly=1) { mockPersistence.insertGames(capture(gamesSlot)) }

        assertEquals(3, gamesSlot.captured.size)
        assertEquals("game1", gamesSlot.captured[0].name)
        assertEquals("game2", gamesSlot.captured[1].name)
        assertEquals("game3", gamesSlot.captured[2].name)
    }

    @Test
    fun `refreshTrophies calls service with gameId and current user`() {
        coEvery {
            mockPersistence.getCurrentUser()
        } returns flowOf(CurrentUser("somePsnId"))

        runBlocking { repository.refreshTrophies("someGameId") }

        val psnIdSlot = slot<String>()
        val gameIdSlot = slot<String>()
        coVerify(exactly=1) { mockService.gameDetails(capture(gameIdSlot), capture(psnIdSlot)) }

        assertEquals("someGameId", gameIdSlot.captured)
        assertEquals("somePsnId", psnIdSlot.captured)
    }

    @Test
    fun `refreshTrophies persists trophies from service`() {
        coEvery {
            mockPersistence.getCurrentUser()
        } returns flowOf(CurrentUser("somePsnId"))

        val fakeGameDetails = GameDetails(
            trophies = listOf(
                fakeTrophy(name = "trophy1"),
                fakeTrophy(name = "trophy2"),
                fakeTrophy(name = "trophy3")
            )
        )
        coEvery {
            mockService.gameDetails("someGameId", "somePsnId")
        } returns fakeGameDetails

        runBlocking { repository.refreshTrophies("someGameId") }

        val trophiesSlot = slot<List<Trophy>>()
        coVerify(exactly=1) { mockPersistence.insertTrophies(capture(trophiesSlot)) }

        assertEquals(3, trophiesSlot.captured.size)
        assertEquals("trophy1", trophiesSlot.captured[0].name)
        assertEquals("trophy2", trophiesSlot.captured[1].name)
        assertEquals("trophy3", trophiesSlot.captured[2].name)
    }
}

private fun fakeGame(
    name: String = "someName",
    coverURL: URL = URL("https://www.psnprofiles.com"),
    platform: String = "somePlatform",
    platinum: Int? = null,
    id: String = "someGameId",
    gold: Int = 0,
    silver: Int = 0,
    bronze: Int = 0,
    completionPercent: Double = 0.0,
    earnedTrophies: Int = 0,
    totalTrophies: Int = 0,
    playerPsnId: String = ""
) = Game(
    name = name,
    coverURL = coverURL,
    platform = platform,
    platinum = platinum,
    id = id,
    gold = gold,
    silver = silver,
    bronze = bronze,
    completionPercent = completionPercent,
    earnedTrophies = earnedTrophies,
    totalTrophies = totalTrophies,
    playerPsnId = playerPsnId
)

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

private fun fakeTrophy(name: String) = Trophy(
    name = name,
    description = "someDescription",
    grade = "someGrade",
    imageURL = URL("https://www.psnprofiles.com"),
    earned = false,
    gameId = "someId",
    playerPsnId = "somePlayerId"
)
