package com.bruce32.psnprofileviewer.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.databinding.ListItemTemplateBinding
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.net.URL

@RunWith(AndroidJUnit4::class)
class ListItemAdapterTest {

    private lateinit var viewGroup: ViewGroup
    private lateinit var binding: ListItemTemplateBinding
    private lateinit var listItemHolder: ListItemHolder

    @Before
    fun setup() {
        viewGroup = LinearLayout(
            ApplicationProvider.getApplicationContext()
        )

        val inflater = LayoutInflater.from(ApplicationProvider.getApplicationContext())
        binding = ListItemTemplateBinding.inflate(inflater)

        val mockImageLoader = mockk<ImageLoader> {
            every { load(any(), any()) } returns Unit
        }

        listItemHolder = ListItemHolder(
            binding = binding,
            imageLoader = mockImageLoader
        )
    }

    @Test
    fun `onBindViewHolder binds viewModel to holder for index`() {
        val viewModels = listOf(
            fakeListItemViewModel(title = "title1"),
            fakeListItemViewModel(title = "title2"),
            fakeListItemViewModel(title = "title3")
        )
        val adapter = ListItemAdapter()
        adapter.submitList(viewModels)
        adapter.onBindViewHolder(listItemHolder, 1)

        assertEquals("title2", binding.titleView.text)
    }

    @Test
    fun `onBindViewHolder passes onClick lambda to holder's bind for index`() {
        val viewModels = listOf(
            fakeListItemViewModel(title = "title1"),
            fakeListItemViewModel(title = "title2"),
            fakeListItemViewModel(title = "title3")
        )
        var didCallOnClick = false
        val adapter = ListItemAdapter {
            didCallOnClick = true
        }
        adapter.submitList(viewModels)

        var mockItemHolder = mockk<ListItemHolder> {
            every { bind(any(), captureLambda()) } answers {
                lambda<(String) -> Unit>().captured.invoke("")
            }
        }
        adapter.onBindViewHolder(mockItemHolder, 2)

        assertEquals(true, didCallOnClick)
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