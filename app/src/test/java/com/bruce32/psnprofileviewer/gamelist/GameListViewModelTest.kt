package com.bruce32.psnprofileviewer.gamelist

import android.util.Log
import com.bruce32.psnprofileviewer.FakeResourceStringSource
import com.bruce32.psnprofileviewer.MainCoroutineRule
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import com.bruce32.psnprofileviewer.model.CurrentUser
import com.bruce32.psnprofileviewer.model.Game
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.URL

class GameListViewModelTest {

    private lateinit var mockCurrentUserFlow: MutableStateFlow<CurrentUser?>
    private lateinit var mockGameFlow: MutableStateFlow<List<Game>>

    private lateinit var mockRepository: ProfileRepository
    private lateinit var mockPersistence: ProfilePersistence

    private lateinit var viewModel: GameListViewModel

    @get:Rule
    @OptIn(ExperimentalCoroutinesApi::class)
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        mockGameFlow = MutableStateFlow(listOf(fakeGame("1234")))
        mockRepository = mockk {
            every { games } returns mockGameFlow.asStateFlow()
            coEvery { refreshProfileAndGames() } returns Unit
        }
        mockCurrentUserFlow = MutableStateFlow(null)
        mockPersistence = mockk {
            every { getCurrentUser() } returns mockCurrentUserFlow.asStateFlow()
        }

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        viewModel = GameListViewModel(
            repository = mockRepository,
            persistence = mockPersistence,
            stringSource = FakeResourceStringSource
        )
    }

    @Test
    fun `update type is empty with no games message when no games found and currentUser is not null`() {
        runBlocking { mockCurrentUserFlow.emit(CurrentUser("1234")) }
        runBlocking { mockGameFlow.emit(emptyList()) }
        val update = runBlocking { viewModel.items.first() } as? GameListUpdate.Empty
        assertEquals("Couldn't find any games for 1234 on PSNProfiles.com", update?.message)
    }

    @Test
    fun `update type is empty with 'Please enter PSN ID' message when no games found and currentUser is null`() {
        runBlocking { mockCurrentUserFlow.emit(null) }
        runBlocking { mockGameFlow.emit(emptyList()) }
        val update = runBlocking { viewModel.items.first() } as? GameListUpdate.Empty
        assertEquals("Please enter a PSN ID in the menu to see your progress.", update?.message)
    }

    @Test
    fun `refreshProfileAndGames is called on repository on init`() {
        coVerify(exactly = 1) { mockRepository.refreshProfileAndGames() }
    }

    @Test
    fun `new games emitted from repository are emitted by viewModel as gameViewModels`() {
        runBlocking {
            mockGameFlow.emit(
                listOf(
                    fakeGame(id = "game1"),
                    fakeGame(id = "game2"),
                    fakeGame(id = "game3")
                )
            )
        }
        val update = runBlocking { viewModel.items.first() } as? GameListUpdate.Items
        assertEquals(3, update?.viewModels?.size)
        assertEquals("game1", update?.viewModels?.get(0)?.id)
        assertEquals("game2", update?.viewModels?.get(1)?.id)
        assertEquals("game3", update?.viewModels?.get(2)?.id)
    }
}

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
