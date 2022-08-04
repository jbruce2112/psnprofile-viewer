package com.bruce32.psnprofileviewer.profile

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.R
import com.bruce32.psnprofileviewer.common.ImageLoader
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class ProfileFragmentTest {

    private lateinit var mockProfileStateFlow: MutableStateFlow<ProfileStatsViewModel>
    private lateinit var mockStatsViewModel: ProfileStatsViewModel
    private lateinit var mockViewModel: ProfileViewModel
    private lateinit var mockImageLoader: ImageLoader

    private lateinit var scenario: FragmentScenario<ProfileFragment>

    @Before
    fun setup() {
        mockStatsViewModel = mockk {
            every { imageURL } returns URL("https://www.psnprofiles.com")
            every { heading } returns ""
            every { subheading } returns ""
            every { stats } returns emptyList()
        }
        mockProfileStateFlow = MutableStateFlow(mockStatsViewModel)
        mockViewModel = mockk {
            every { profile } returns mockProfileStateFlow.asStateFlow()
        }
        mockImageLoader = mockk {
            every { load(any(), any()) } returns Unit
        }
        val mockFactory: ProfileViewModelFactory = mockk {
            every { create<ProfileViewModel>(any(), any()) } returns mockViewModel
        }
        scenario = launchFragmentInContainer(initialState = Lifecycle.State.CREATED) {
            ProfileFragment(
                viewModelFactory = mockFactory,
                imageLoader = mockImageLoader
            )
        }
    }

    @Test
    fun `new imageURL emitted from viewModel is bound to imageView via imageLoader`() {
        val mockURL = URL("https://www.psnprofiles.com")
        every { mockStatsViewModel.imageURL } returns mockURL
        runBlocking { mockProfileStateFlow.emit(mockStatsViewModel) }

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment ->
            val imageView = fragment.view?.findViewById<ImageView>(R.id.headingImageView)
            checkNotNull(imageView)
            verify(exactly=1) { mockImageLoader.load(mockURL, imageView) }
        }
    }

    @Test
    fun `new heading emitted from viewModel is bound to headingView`() {
        every { mockStatsViewModel.heading } returns "someHeading"
        runBlocking { mockProfileStateFlow.emit(mockStatsViewModel) }

        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.headingView)).check(matches(withText("someHeading")))
    }

    @Test
    fun `new subheading emitted from viewModel is bound to subheadingView`() {
        every { mockStatsViewModel.subheading } returns "someSubheading"
        runBlocking { mockProfileStateFlow.emit(mockStatsViewModel) }

        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.subheadingView)).check(matches(withText("someSubheading")))
    }

    @Test
    fun `new statViewModel emitted from viewModel is bound to child of statsLayout`() {
        val newStatViewModel = ProfileStatViewModel("someValue", "someLabel")
        every { mockStatsViewModel.stats } returns listOf(newStatViewModel)
        runBlocking { mockProfileStateFlow.emit(mockStatsViewModel) }

        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onFragment { fragment ->
            val linearLayout = fragment.view?.findViewById<LinearLayout>(R.id.statsLayout)
            val statView = linearLayout?.children?.first() as? ProfileStatItemView
            checkNotNull(statView)
            assert(statView.findViewById<TextView>(R.id.statValue).text.toString() == "someValue")
            assert(statView.findViewById<TextView>(R.id.statLabel).text.toString() == "someLabel")
        }
    }

    @Test
    fun `old statViews are cleared out when new statViews are created on viewModel emit`() {
        val fakeStatViewModel = ProfileStatViewModel("someValue", "someLabel")
        val threeStatViewModels = listOf(fakeStatViewModel, fakeStatViewModel, fakeStatViewModel)
        every { mockStatsViewModel.stats } returns threeStatViewModels
        runBlocking { mockProfileStateFlow.emit(mockStatsViewModel) }

        scenario.moveToState(Lifecycle.State.RESUMED)
        scenario.onFragment { fragment ->
            val linearLayout = fragment.view?.findViewById<LinearLayout>(R.id.statsLayout)
            assert(linearLayout?.childCount == 3)
        }

        mockStatsViewModel = mockk(relaxed=true) {
            every { stats } returns listOf(fakeStatViewModel)
        }
        runBlocking { mockProfileStateFlow.emit(mockStatsViewModel) }

        scenario.onFragment { fragment ->
            val linearLayout = fragment.view?.findViewById<LinearLayout>(R.id.statsLayout)
            assert(linearLayout?.childCount == 1)
        }
    }
}
