package com.bruce32.psnprofileviewer.trophylist

import android.util.Log
import com.bruce32.psnprofileviewer.MainCoroutineRule
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.model.Trophy
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
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

class TrophyListViewModelTest {

    private lateinit var mockTrophyFlow: MutableStateFlow<List<Trophy>>
    private lateinit var mockRepository: ProfileRepository

    @get:Rule
    @OptIn(ExperimentalCoroutinesApi::class)
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        mockTrophyFlow = MutableStateFlow(emptyList())
        mockRepository = mockk {
            every { trophies(any()) } returns mockTrophyFlow.asStateFlow()
            coEvery { refreshTrophies(any()) } returns Unit
        }

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `refreshTrophies is called with gameId on init`() {
        TrophyListViewModel(
            repository = mockRepository,
            gameId = "someGameId"
        )

        val gameIdSlot = slot<String>()
        coVerify(exactly=1) { mockRepository.refreshTrophies(capture(gameIdSlot)) }
        assertEquals("someGameId", gameIdSlot.captured)
    }

    @Test
    fun `new trophies emitted from repository are emitted by viewModel as trophyViewModels`() {
        val viewModel = TrophyListViewModel(
            repository = mockRepository,
            gameId = "someGameId"
        )

        runBlocking {
            mockTrophyFlow.emit(
                listOf(
                    fakeTrophy(name = "trophy1"),
                    fakeTrophy(name = "trophy2"),
                    fakeTrophy(name = "trophy3")
                )
            )
        }
        val itemViewModels = runBlocking { viewModel.trophies.first() }
        assertEquals(3, itemViewModels.size)
        assertEquals("trophy1", itemViewModels[0].title)
        assertEquals("trophy2", itemViewModels[1].title)
        assertEquals("trophy3", itemViewModels[2].title)
    }

    @Test
    fun `trophies are collected for gameId viewModel initialized with`() {
        val gameIdSlot = slot<String>()
        coEvery { mockRepository.trophies(capture(gameIdSlot)) }
        TrophyListViewModel(
            repository = mockRepository,
            gameId = "someGameId"
        )
        assertEquals("someGameId", gameIdSlot.captured)
    }
}

private fun fakeTrophy(name: String) = Trophy(
    name = name,
    description = "someDescription",
    grade = "someGrade",
    imageURL = URL("https://www.psnprofiles.com"),
    earned = false,
    gameId = "someId",
    playerPsnId = "somePlayerId"
)
