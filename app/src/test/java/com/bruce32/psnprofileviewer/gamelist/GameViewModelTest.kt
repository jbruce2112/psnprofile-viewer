package com.bruce32.psnprofileviewer.gamelist

import com.bruce32.psnprofileviewer.model.Game
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.URL

class GameViewModelTest {

    @Test
    fun `id is game id`() {
        val viewModel = GameViewModel(
            game = fakeGame(id = "MyGameId")
        )
        assertEquals("MyGameId", viewModel.id)
    }

    @Test
    fun `title is game name`() {
        val viewModel = GameViewModel(
            game = fakeGame(name = "The Cat Game")
        )
        assertEquals("The Cat Game", viewModel.title)
    }

    @Test
    fun `secondary is game platform`() {
        val viewModel = GameViewModel(
            game = fakeGame(platform = "PS2")
        )
        assertEquals("PS2", viewModel.secondary)
    }

    @Test
    fun `description is formatted trophy counts`() {
        val viewModel = GameViewModel(
            game = fakeGame(platinum = 1, gold = 1234, silver = 5432, bronze = 88734)
        )
        assertEquals("1 Platinum, 1,234 Gold, 5,432 Silver, 88,734 Bronze", viewModel.description)
    }

    @Test
    fun `leadingIconURL is game cover URL`() {
        val viewModel = GameViewModel(
            game = fakeGame(coverURL = URL("https://www.psnprofiles.com"))
        )
        assertEquals("https://www.psnprofiles.com", viewModel.leadingIconURL.toString())
    }

    @Test
    fun `trailingText is rounded completion percentage`() {
        val viewModel = GameViewModel(
            game = fakeGame(completionPercent = 23.52)
        )
        assertEquals("23%", viewModel.trailingText)
    }

    @Test
    fun `highlighted is true when platinum is earned and earned is not equal to total`() {
        val viewModel = GameViewModel(
            game = fakeGame(platinum = 1, earnedTrophies = 20, totalTrophies = 42)
        )
        assertEquals(true, viewModel.highlighted)
    }

    @Test
    fun `highlighted is true when platinum is not earned and earned is equal to total`() {
        val viewModel = GameViewModel(
            game = fakeGame(platinum = 0, earnedTrophies = 42, totalTrophies = 42)
        )
        assertEquals(true, viewModel.highlighted)
    }

    @Test
    fun `highlighted is false when platinum is not earned and earned is not equal to total`() {
        val viewModel = GameViewModel(
            game = fakeGame(platinum = 0, earnedTrophies = 20, totalTrophies = 42)
        )
        assertEquals(false, viewModel.highlighted)
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