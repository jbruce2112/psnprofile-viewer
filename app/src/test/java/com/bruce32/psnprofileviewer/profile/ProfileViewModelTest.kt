package com.bruce32.psnprofileviewer.profile

import android.util.Log
import com.bruce32.psnprofileviewer.MainCoroutineRule
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.model.Profile
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URL

class ProfileViewModelTest {
    private lateinit var mockProfileFlow: MutableStateFlow<Profile?>

    private lateinit var mockRepository: ProfileRepository

    private lateinit var viewModel: ProfileViewModel

    @get:Rule
    @OptIn(ExperimentalCoroutinesApi::class)
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        mockProfileFlow = MutableStateFlow(null)
        mockRepository = mockk {
            every { profile } returns mockProfileFlow.asStateFlow()
            coEvery { refreshProfileAndGames() } returns Unit
        }

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        viewModel = ProfileViewModel(
            repository = mockRepository
        )
    }

    @Test
    fun `refreshProfileAndGames is called on repository on init`() {
        coVerify(exactly=1) { mockRepository.refreshProfileAndGames() }
    }

    @Test
    fun `profile flow emits null when repository emits null profile`() {
        runBlocking { mockProfileFlow.emit(null) }

        val statsViewModel = runBlocking { viewModel.profile.first() }
        assertNull(statsViewModel)
    }

    @Test
    fun `profile flow emits profileStatsViewModel when repository emits non-null profile`() {
        runBlocking {
            mockProfileFlow.emit(fakeProfile(psnId = "myPsnId"))
        }

        val statsViewModel = runBlocking { viewModel.profile.first() }
        assertEquals("myPsnId", statsViewModel?.id)
    }

    @Test
    fun `statsViewModel is updated when changed in repository`() {
        runBlocking { mockProfileFlow.emit(null) }
        val statsViewModelWhenProfileNullInRepository = runBlocking { viewModel.profile.first() }
        runBlocking { mockProfileFlow.emit(fakeProfile("1234")) }
        val statsViewModelWhenProfileSetInRepository = runBlocking { viewModel.profile.first() }

        assertNull(statsViewModelWhenProfileNullInRepository)
        assertNotNull(statsViewModelWhenProfileSetInRepository)
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