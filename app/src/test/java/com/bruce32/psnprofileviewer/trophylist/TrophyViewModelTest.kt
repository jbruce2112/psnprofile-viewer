package com.bruce32.psnprofileviewer.trophylist

import com.bruce32.psnprofileviewer.model.Trophy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.net.URL

class TrophyViewModelTest {

    @Test
    fun `title is trophy name`() {
        val viewModel = TrophyViewModel(
            trophy = fakeTrophy(name = "My Great Trophy")
        )
        assertEquals("My Great Trophy", viewModel.title)
    }

    @Test
    fun `description is trophy description`() {
        val viewModel = TrophyViewModel(
            trophy = fakeTrophy(description = "Collect 200 coins")
        )
        assertEquals("Collect 200 coins", viewModel.description)
    }

    @Test
    fun `leadingIconURL is trophy url`() {
        val viewModel = TrophyViewModel(
            trophy = fakeTrophy(imageURL = URL("https://www.psnprofiles.com"))
        )
        assertEquals("https://www.psnprofiles.com", viewModel.leadingIconURL.toString())
    }

    @Test
    fun `trailingText is trophy grade`() {
        val viewModel = TrophyViewModel(
            trophy = fakeTrophy(grade = "Silver")
        )
        assertEquals("Silver", viewModel.trailingText)
    }

    @Test
    fun `highlighted is trophy earned`() {
        val viewModel = TrophyViewModel(
            trophy = fakeTrophy(earned = true)
        )
        assertEquals(true, viewModel.highlighted)
    }

    @Test
    fun `id is null`() {
        val viewModel = TrophyViewModel(
            trophy = fakeTrophy()
        )
        assertNull(viewModel.id)
    }

    @Test
    fun `secondary is null`() {
        val viewModel = TrophyViewModel(
            trophy = fakeTrophy()
        )
        assertNull(viewModel.secondary)
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
