package com.bruce32.psnprofileviewer.switchaccount

import android.util.Log
import com.bruce32.psnprofileviewer.MainCoroutineRule
import com.bruce32.psnprofileviewer.application.ProfileRepository
import com.bruce32.psnprofileviewer.database.ProfilePersistence
import com.bruce32.psnprofileviewer.model.CurrentUser
import io.mockk.Ordering
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

class SwitchAccountViewModelTest {

    private lateinit var mockCurrentUserFlow: MutableStateFlow<CurrentUser?>

    private lateinit var mockRepository: ProfileRepository
    private lateinit var mockPersistence: ProfilePersistence

    private lateinit var viewModel: SwitchAccountViewModel

    @get:Rule
    @OptIn(ExperimentalCoroutinesApi::class)
    val coroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        mockCurrentUserFlow = MutableStateFlow(null)
        mockRepository = mockk {
            coEvery { refreshProfileAndGames() } returns Unit
        }
        mockPersistence = mockk {
            coEvery { setCurrentUser(any()) } returns Unit
            every { getCurrentUser() } returns mockCurrentUserFlow.asStateFlow()
        }

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        viewModel = SwitchAccountViewModel(
            repository = mockRepository,
            persistence = mockPersistence
        )
    }

    @Test
    fun `new currentUser values emitted from persistence are present in userFieldHint`() {
        runBlocking { mockCurrentUserFlow.emit(CurrentUser("SomeUserId")) }
        val userFieldHint = runBlocking { viewModel.userFieldHint.first() }
        assertEquals("SomeUserId", userFieldHint)
    }

    @Test
    fun `userFieldHint is PSN ID when no currentUser is null in persistence`() {
        runBlocking { mockCurrentUserFlow.emit(null) }
        val userFieldHint = runBlocking { viewModel.userFieldHint.first() }
        assertEquals("PSN ID", userFieldHint)
    }

    @Test
    fun `setCurrentUser calls setCurrentUser on persistence with new userId`() {
        runBlocking { viewModel.setCurrentUser("someNewUserId") }
        coVerify(exactly=1) { mockPersistence.setCurrentUser("someNewUserId") }
    }

    @Test
    fun `setCurrentUser calls refreshProfileAndGames on repository after updating current user`() {
        runBlocking { viewModel.setCurrentUser("someNewUserId") }
        coVerify(ordering=Ordering.ORDERED) {
            mockPersistence.setCurrentUser(any())
            mockRepository.refreshProfileAndGames()
        }
    }
}
