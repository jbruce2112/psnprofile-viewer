package com.bruce32.psnprofileviewer.common

import android.util.Log
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.URL

class ListItemViewModelDifferTest {

    private lateinit var differ: ListItemViewModelDiffer

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        differ = ListItemViewModelDiffer()
    }

    @Test
    fun `areItemsTheSame returns false when ids are different`() {
        val old = fakeListItemViewModel(id = "id1")
        val new = fakeListItemViewModel(id = "id2")

        val result = differ.areItemsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areItemsTheSame returns true when ids are same`() {
        val old = fakeListItemViewModel(id = "id1")
        val new = fakeListItemViewModel(id = "id1")

        val result = differ.areItemsTheSame(old, new)
        assertEquals(true, result)
    }

    @Test
    fun `areItemsTheSame returns false when ids are null and titles are different`() {
        val old = fakeListItemViewModel(id = null, title = "name1")
        val new = fakeListItemViewModel(id = null, title = "name2")

        val result = differ.areItemsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areItemsTheSame returns true when ids are null and titles are same`() {
        val old = fakeListItemViewModel(id = null, title = "name1")
        val new = fakeListItemViewModel(id = null, title = "name1")

        val result = differ.areItemsTheSame(old, new)
        assertEquals(true, result)
    }

    @Test
    fun `areContentsTheSame returns false when titles are different`() {
        val old = fakeListItemViewModel(title = "name1")
        val new = fakeListItemViewModel(title = "name2")

        val result = differ.areContentsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areContentsTheSame returns false when secondaries are different`() {
        val old = fakeListItemViewModel(title = "name1", secondary = "second1")
        val new = fakeListItemViewModel(title = "name1", secondary = "second2")

        val result = differ.areContentsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areContentsTheSame returns false when descriptions are different`() {
        val old = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1"
        )
        val new = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc2"
        )

        val result = differ.areContentsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areContentsTheSame returns false when leadingIconURLs are different`() {
        val old = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.google.com")
        )
        val new = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.psnprofiles.com")
        )

        val result = differ.areContentsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areContentsTheSame returns false when trailingText are different`() {
        val old = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.google.com"),
            trailingText = "trailing1"
        )
        val new = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.google.com"),
            trailingText = "trailing2"
        )

        val result = differ.areContentsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areContentsTheSame returns false when highlighted are different`() {
        val old = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.google.com"),
            trailingText = "trailing1",
            highlighted = true
        )
        val new = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.google.com"),
            trailingText = "trailing1",
            highlighted = false
        )

        val result = differ.areContentsTheSame(old, new)
        assertEquals(false, result)
    }

    @Test
    fun `areContentsTheSame returns true when all properties are same`() {
        val old = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.google.com"),
            trailingText = "trailing1",
            highlighted = true
        )
        val new = fakeListItemViewModel(
            title = "name1",
            secondary = "second1",
            description = "desc1",
            leadingIconURL = URL("https://www.google.com"),
            trailingText = "trailing1",
            highlighted = true
        )

        val result = differ.areContentsTheSame(old, new)
        assertEquals(true, result)
    }
}

private fun fakeListItemViewModel(
    id: String? = null,
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