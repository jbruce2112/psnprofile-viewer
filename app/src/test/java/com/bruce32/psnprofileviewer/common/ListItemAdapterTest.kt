package com.bruce32.psnprofileviewer.common

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bruce32.psnprofileviewer.databinding.ListItemTemplateBinding
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
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
        val adapter = ListItemAdapter(
            itemViewModels = viewModels
        )
        adapter.onBindViewHolder(listItemHolder, 1)

        assertEquals("title2", binding.titleView.text)
    }

    @Test
    fun `getItemCount() returns size of initial viewModels`() {
        val threeViewModels = listOf(
            fakeListItemViewModel(),
            fakeListItemViewModel(),
            fakeListItemViewModel()
        )
        val adapter = ListItemAdapter(
            itemViewModels = threeViewModels
        )

        assertEquals(3, adapter.itemCount)
    }

    @Test
    fun `getItemCount() returns size of viewModels after update`() {
        val threeViewModels = listOf(
            fakeListItemViewModel(),
            fakeListItemViewModel(),
            fakeListItemViewModel()
        )
        val adapter = ListItemAdapter(
            itemViewModels = threeViewModels
        )

        adapter.update(listOf(fakeListItemViewModel()))

        assertEquals(1, adapter.itemCount)
    }

    @Test
    fun `notifyDataSetChanged is called after update`() {
        val spyAdapter = spyk(ListItemAdapter(
            itemViewModels = emptyList()
        ))

        verify(exactly=0) { spyAdapter.notifyDataSetChanged() }

        spyAdapter.update(listOf(fakeListItemViewModel()))

        verify(exactly=1) { spyAdapter.notifyDataSetChanged() }
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