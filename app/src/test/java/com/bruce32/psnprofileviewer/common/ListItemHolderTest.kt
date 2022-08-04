package com.bruce32.psnprofileviewer.common

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.databinding.ListItemTemplateBinding
import com.google.android.material.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class ListItemHolderTest {

    private lateinit var binding: ListItemTemplateBinding
    private lateinit var mockImageLoader: ImageLoader

    private lateinit var listItemHolder: ListItemHolder

    @Before
    fun setup() {
        val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
        binding = ListItemTemplateBinding.inflate(inflater)

        mockImageLoader = mockk {
            every { load(any(), any()) } returns Unit
        }

        listItemHolder = ListItemHolder(
            binding = binding,
            imageLoader = mockImageLoader
        )
    }

    @Test
    fun `viewModel title is bound to titleView text`() {
        listItemHolder.bind(
            fakeListItemViewModel(title = "someTitle")
        )
        assertEquals("someTitle", binding.titleView.text)
    }

    @Test
    fun `viewModel secondary is bound to secondaryView text`() {
        listItemHolder.bind(
            fakeListItemViewModel(secondary = "someSecondaryText")
        )
        assertEquals("someSecondaryText", binding.secondaryView.text)
    }

    @Test
    fun `viewModel description is bound to description text`() {
        listItemHolder.bind(
            fakeListItemViewModel(description = "someDescription")
        )
        assertEquals("someDescription", binding.descriptionView.text)
    }

    @Test
    fun `viewModel trailing is bound to trailing text`() {
        listItemHolder.bind(
            fakeListItemViewModel(trailingText = "someTrailingText")
        )
        assertEquals("someTrailingText", binding.trailingView.text)
    }

    @Test
    fun `backgroundColor is set to secondary0 when highlighted is true`() {
        listItemHolder.bind(
            fakeListItemViewModel(highlighted = true)
        )
        val color = binding.root.background as? ColorDrawable
        assertEquals(R.color.material_dynamic_secondary0, color?.color)
    }

    @Test
    fun `backgroundColor is set to transparent when highlighted is false`() {
        listItemHolder.bind(
            fakeListItemViewModel(highlighted = false)
        )
        val color = binding.root.background as? ColorDrawable
        assertEquals(android.R.color.transparent, color?.color)
    }

    @Test
    fun `leadingIconURL is loaded into imageView via imageLoader`() {
        val mockURL = URL("https://www.psnprofiles.com")
        listItemHolder.bind(
            fakeListItemViewModel(leadingIconURL = mockURL)
        )
        verify(exactly=1) { mockImageLoader.load(mockURL, binding.imageView) }
    }

    @Test
    fun `viewModel Id is passed to onClick lambda when root's click handler is invoked`() {
        var lastIdPassedToOnClick: String? = null
        listItemHolder.bind(
            fakeListItemViewModel(id = "someId")
        ) {
            lastIdPassedToOnClick = it
        }

        binding.root.callOnClick()

        assertEquals("someId", lastIdPassedToOnClick)
    }
}

private fun fakeListItemViewModel(
    id: String = "",
    title: String = "",
    secondary: String = "",
    description: String = "",
    leadingIconURL: URL = URL("https://www.google.com"),
    trailingText: String = "",
    highlighted: Boolean = false
) = object : ListItemViewModel {
    override val id = id
    override val title = title
    override val secondary = secondary
    override val description = description
    override val leadingIconURL = leadingIconURL
    override val trailingText = trailingText
    override val highlighted = highlighted
}