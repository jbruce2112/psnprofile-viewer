package com.bruce32.psnprofileviewer.gamelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.R
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameListFragmentTest {

    private lateinit var mockMessageFlow: MutableStateFlow<String?>
    private lateinit var mockViewModel: GameListViewModel

    private lateinit var scenario: FragmentScenario<GameListFragment>

    @get:Rule
    val coroutineRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        mockMessageFlow = MutableStateFlow(null)
        mockViewModel = mockk(relaxed = true)

        val mockFactory: GameListViewModelFactory = mockk {
            every { create<GameListViewModel>(any()) } returns mockViewModel
        }
        scenario = launchFragmentInContainer(initialState = Lifecycle.State.CREATED) {
            GameListFragment(mockFactory)
        }
    }

    @Test
    fun `messageView has text updated to value emitted from viewModel flow`() {
        runBlocking { mockMessageFlow.emit("Some Message For The UI") }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.messageView)).check(matches(withText("Some Message For The UI")))
    }
}
