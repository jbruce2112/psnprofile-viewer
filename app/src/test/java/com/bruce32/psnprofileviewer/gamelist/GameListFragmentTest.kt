package com.bruce32.psnprofileviewer.gamelist

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.common.ListItemAdapter
import com.bruce32.psnprofileviewer.common.ListItemAdapterSource
import com.bruce32.psnprofileviewer.common.ListItemViewModel
import com.bruce32.psnprofileviewer.model.Game
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class GameListFragmentTest {

    private lateinit var mockItemsFlow: MutableStateFlow<GameListUpdate>
    private lateinit var mockViewModel: GameListViewModel

    private lateinit var spyAdapter: ListItemAdapter

    private lateinit var scenario: FragmentScenario<GameListFragment>

    @Before
    fun setup() {
        mockItemsFlow = MutableStateFlow(GameListUpdate.Empty(""))
        mockViewModel = mockk {
            every { items } returns mockItemsFlow
        }

        spyAdapter = spyk()
        val spyAdapterSource = mockk<ListItemAdapterSource> {
            every { adapter(any()) } returns spyAdapter
        }

        val mockFactory: GameListViewModelFactory = mockk {
            every { create<GameListViewModel>(any(), any()) } returns mockViewModel
        }
        val mockFactorySource: GameListViewModelFactorySource = mockk {
            every { factory(any()) } returns mockFactory
        }
        scenario = launchFragmentInContainer(initialState = Lifecycle.State.CREATED) {
            GameListFragment(
                viewModelFactorySource = mockFactorySource,
                adapterSource = spyAdapterSource
            )
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

    @Test
    fun `submitList is called on recyclerView's ListItemAdapter adapter with result from update of type Items`() {
        runBlocking { mockItemsFlow.emit(GameListUpdate.Items(emptyList())) }
        scenario.moveToState(Lifecycle.State.RESUMED)

        val viewModelsSlot = slot<List<ListItemViewModel>>()
        every { spyAdapter.submitList(capture(viewModelsSlot)) } returns Unit

        val oneViewModel = listOf(GameViewModel(fakeGame("someGame")))
        runBlocking { mockItemsFlow.emit(GameListUpdate.Items(oneViewModel)) }

        assertEquals(oneViewModel, viewModelsSlot.captured)
    }

    @Test
    fun `recyclerView's layoutManager is of type linearLayoutManager`() {
        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { fragment ->
            val layoutManager = fragment.view?.findViewById<RecyclerView>(R.id.list_recycler_view)?.layoutManager
            assert(layoutManager is LinearLayoutManager)
        }
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