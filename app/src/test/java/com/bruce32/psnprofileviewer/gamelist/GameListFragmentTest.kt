package com.bruce32.psnprofileviewer.gamelist

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.R
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameListFragmentTest {

    private lateinit var mockItemsFlow: MutableStateFlow<GameListUpdate>
    private lateinit var mockViewModel: GameListViewModel

    private lateinit var scenario: FragmentScenario<GameListFragment>

    @Before
    fun setup() {
        mockItemsFlow = MutableStateFlow(GameListUpdate.Empty(""))
        mockViewModel = mockk {
            every { items } returns mockItemsFlow
        }

        val mockFactory: GameListViewModelFactory = mockk {
            every { create<GameListViewModel>(any(), any()) } returns mockViewModel
        }
        scenario = launchFragmentInContainer(initialState = Lifecycle.State.CREATED) {
            GameListFragment(mockFactory)
        }
    }

    @Test
    fun `messageView has text updated to message emitted from update of type Empty`() {
        runBlocking { mockItemsFlow.emit(GameListUpdate.Empty("Some Message For The UI")) }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.messageView)).check(matches(withText("Some Message For The UI")))
    }

    @Test
    fun `messageView is set to GONE when update is emitted of type Items`() {
        runBlocking { mockItemsFlow.emit(GameListUpdate.Items(emptyList())) }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.messageView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun `messageView is set to VISIBLE when update is emitted of type Empty`() {
        runBlocking { mockItemsFlow.emit(GameListUpdate.Empty("")) }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.messageView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun `recyclerView is set to GONE when update is emitted of type Empty`() {
        runBlocking { mockItemsFlow.emit(GameListUpdate.Empty("")) }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.list_recycler_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun `recyclerView is set to VISIBLE when update is emitted of type Items`() {
        runBlocking { mockItemsFlow.emit(GameListUpdate.Items(emptyList())) }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.list_recycler_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}
