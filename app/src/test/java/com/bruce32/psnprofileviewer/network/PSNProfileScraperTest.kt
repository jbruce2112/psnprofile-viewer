package com.bruce32.psnprofileviewer.network

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PSNProfileScraperTest {

    private val gameListHtml: String by lazy {
        loadFileContents("gamelist.html")
    }

    private val trophyListHtml: String by lazy {
        loadFileContents("trophylist-zerodawn.html")
    }

    private lateinit var scraper: PSNProfileScraper

    @Before
    fun setup() {
        scraper = PSNProfileScraperImpl()
    }

    @Test
    fun `profileAndGames returns 70 games total from game list html`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(70, result.games.size)
    }

    @Test
    fun `gameDetails returns 79 trophies total from trophylist html`() {
        val result = scraper.gameDetails(trophyListHtml,"5879-horizon-zero-dawn", "jbruce2112")
        assertEquals(79, result.trophies.size)
    }
}

private fun loadFileContents(filename: String): String {
    val file = ClassLoader.getSystemResource(filename)
    return file.readText()
}