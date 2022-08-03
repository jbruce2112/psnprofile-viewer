package com.bruce32.psnprofileviewer.switchaccount

import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.R
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SwitchAccountFragmentTest {

    private lateinit var mockUserFieldHintFlow: MutableStateFlow<String>
    private lateinit var mockViewModel: SwitchAccountViewModel

    private lateinit var scenario: FragmentScenario<SwitchAccountFragment>

    @Before
    fun setup() {
        mockUserFieldHintFlow = MutableStateFlow("someUser")
        mockViewModel = mockk {
            every { userFieldHint } returns mockUserFieldHintFlow.asStateFlow()
        }

        val mockFactory: SwitchAccountViewModelFactory = mockk {
            every { create<SwitchAccountViewModel>(any(), any()) } returns mockViewModel
        }
        scenario = launchFragmentInContainer(initialState = Lifecycle.State.CREATED) {
            SwitchAccountFragment(mockFactory)
        }
    }

    @Test
    fun `psnIdText hint is updated when new values are emitted from userFieldHint flow`() {
        runBlocking { mockUserFieldHintFlow.emit("someCurrentUser") }
        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.setPsnIdText)).check(matches(withHint("someCurrentUser")))
    }

    @Test
    fun `setCurrentUser is called with value from psnIdText field when setId button is tapped`() {
        scenario.moveToState(Lifecycle.State.RESUMED)
        onView(withId(R.id.setPsnIdText)).perform(setTextInTextView("someNewPsnId"))
        onView(withId(R.id.setIdButton)).perform(click())

        coVerify(exactly=1) { mockViewModel.setCurrentUser("someNewPsnId") }
    }
}

private fun setTextInTextView(value: String): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            return CoreMatchers.allOf(ViewMatchers.isDisplayed(), ViewMatchers.isAssignableFrom(TextView::class.java))
        }

        override fun perform(uiController: UiController, view: View) {
            (view as TextView).text = value
        }

        override fun getDescription(): String {
            return "replace text"
        }
    }
}