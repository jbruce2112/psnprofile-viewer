package com.bruce32.psnprofileviewer.trophylist

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.common.ListItemAdapter
import com.bruce32.psnprofileviewer.common.ListItemAdapterSource
import com.bruce32.psnprofileviewer.common.ListItemViewModel
import com.bruce32.psnprofileviewer.model.Trophy
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class TrophyListFragmentTest {

    private lateinit var mockTrophiesFlow: MutableStateFlow<List<TrophyViewModel>>

    private lateinit var mockViewModel: TrophyListViewModel
    private lateinit var spyAdapter: ListItemAdapter

    private lateinit var scenario: FragmentScenario<TrophyListFragment>

    @Before
    fun setup() {
        mockTrophiesFlow = MutableStateFlow(emptyList())
        mockViewModel = mockk {
            every { trophies } returns mockTrophiesFlow.asStateFlow()
        }

        spyAdapter = spyk()
        val spyAdapterSource = mockk<ListItemAdapterSource> {
            every { adapter(any()) } returns spyAdapter
        }

        val mockFactory: TrophyListViewModelFactory = mockk {
            every { create<TrophyListViewModel>(any(), any()) } returns mockViewModel
        }
        val mockFactorySource: TrophyListViewModelFactorySource = mockk {
            every { factory("someGameId") } returns mockFactory
        }
        val args = Bundle()
        args.putString("gameId", "someGameId")
        scenario = launchFragmentInContainer(
            fragmentArgs = args,
            initialState = Lifecycle.State.CREATED
        ) {
            TrophyListFragment(
                viewModelFactorySource = mockFactorySource,
                adapterSource = spyAdapterSource
            )
        }
    }

    @Test
    fun `submitList is called on recyclerView's ListItemAdapter adapter with result from viewModel`() {
        runBlocking { mockTrophiesFlow.emit(emptyList()) }
        scenario.moveToState(Lifecycle.State.RESUMED)

        val viewModelsSlot = slot<List<ListItemViewModel>>()
        every { spyAdapter.submitList(capture(viewModelsSlot)) } returns Unit

        val oneViewModel = listOf(TrophyViewModel(fakeTrophy("someTrophy")))
        runBlocking { mockTrophiesFlow.emit(oneViewModel) }

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
