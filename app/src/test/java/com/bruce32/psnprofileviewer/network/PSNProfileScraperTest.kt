package com.bruce32.psnprofileviewer.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.net.URL

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
    fun `profileAndGames parses psn id`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals("jbruce2112", result.profile.psnId)
    }

    @Test
    fun `profileAndGames parses psn level`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(246, result.profile.level)
    }

    @Test
    fun `profileAndGames parses level progress percent`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(6.00, result.profile.levelProgressPercent, .001)
    }

    @Test
    fun `profileAndGames parses avatar URL`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(URL("https://i.psnprofiles.com/avatars/m/Gf53ede390.png"), result.profile.avatarURL)
    }

    @Test
    fun `profileAndGames parses total trophies`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(1449, result.profile.totalTrophies)
    }

    @Test
    fun `profileAndGames parses total platinum`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(19, result.profile.totalPlatinum)
    }

    @Test
    fun `profileAndGames parses total gold`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(70, result.profile.totalGold)
    }

    @Test
    fun `profileAndGames parses total silver`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(218, result.profile.totalSilver)
    }

    @Test
    fun `profileAndGames parses total bronze`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(1142, result.profile.totalBronze)
    }

    @Test
    fun `profileAndGames parses games played`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(70, result.profile.gamesPlayed)
    }

    @Test
    fun `profileAndGames parses completed games`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(14, result.profile.completedGames)
    }

    @Test
    fun `profileAndGames parses completion percent`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(38.80, result.profile.completionPercent, .001)
    }

    @Test
    fun `profileAndGames parses unearned trophies`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(1748, result.profile.unearnedTrophies)
    }

    @Test
    fun `profileAndGames parses trophies per day`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(0.52, result.profile.trophiesPerDay, .001)
    }

    @Test
    fun `profileAndGames parses world rank`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(629214, result.profile.worldRank)
    }

    @Test
    fun `profileAndGames parses country rank`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(202971, result.profile.countryRank)
    }

    @Test
    fun `profileAndGames returns 70 games total from game list html`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals(70, result.games.size)
    }

    @Test
    fun `profileAndGames parses game title`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals("Killzone: Mercenary", result.games[0].name)
    }

    @Test
    fun `profileAndGames parses game platform when single platform`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals("Vita", result.games[0].platform)
    }

    @Test
    fun `profileAndGames parses game platform when multi platform`() {
        val result = scraper.profileAndGames(gameListHtml)
        val guacamelee = result.games.first { it.name == "Guacamelee!" }
        assertEquals("PS3, Vita", guacamelee.platform)
    }

    @Test
    fun `profileAndGames parses psnProfilesId for game`() {
        val result = scraper.profileAndGames(gameListHtml)
        assertEquals("2111-killzone-mercenary", result.games[0].id)
    }

    @Test
    fun `profileAndGames parses platinum as null for game that doesnt have a platinum`() {
        val result = scraper.profileAndGames(gameListHtml)
        val galaga = result.games.first { it.name == "Galaga Legions DX" }
        assertNull(galaga.platinum)
    }

    @Test
    fun `profileAndGames parses platinum as 0 for game that has an unearned platinum`() {
        val result = scraper.profileAndGames(gameListHtml)
        val guacamelee = result.games.first { it.name == "Guacamelee!" }
        assertEquals(0, guacamelee.platinum)
    }

    @Test
    fun `profileAndGames parses platinum as 1 for game that has an earned platinum`() {
        val result = scraper.profileAndGames(gameListHtml)
        val catQuest = result.games.first { it.name == "Cat Quest" }
        assertEquals(1, catQuest.platinum)
    }

    @Test
    fun `profileAndGames parses gold trophies for game`() {
        val result = scraper.profileAndGames(gameListHtml)
        val infamous = result.games.first { it.name == "inFamous Second Son" }
        assertEquals(2, infamous.gold)
    }

    @Test
    fun `profileAndGames parses silver trophies for game`() {
        val result = scraper.profileAndGames(gameListHtml)
        val infamous = result.games.first { it.name == "inFamous Second Son" }
        assertEquals(11, infamous.silver)
    }

    @Test
    fun `profileAndGames parses bronze trophies for game`() {
        val result = scraper.profileAndGames(gameListHtml)
        val infamous = result.games.first { it.name == "inFamous Second Son" }
        assertEquals(34, infamous.bronze)
    }

    @Test
    fun `profileAndGames parses completion percent for game`() {
        val result = scraper.profileAndGames(gameListHtml)
        val tacoMaster = result.games.first { it.name == "Taco Master" }
        assertEquals(16.00, tacoMaster.completionPercent, .001)
    }

    @Test
    fun `profileAndGames parses earned trophies game`() {
        val result = scraper.profileAndGames(gameListHtml)
        val catQuest = result.games.first { it.name == "Cat Quest" }
        assertEquals(23, catQuest.earnedTrophies)
    }

    @Test
    fun `profileAndGames parses total trophies for game when all trophies have been earned`() {
        val result = scraper.profileAndGames(gameListHtml)
        val catQuest = result.games.first { it.name == "Cat Quest" }
        assertEquals(23, catQuest.totalTrophies)
    }

    @Test
    fun `profileAndGames parses total trophies for game when all trophies have not been earned`() {
        val result = scraper.profileAndGames(gameListHtml)
        val tacoMaster = result.games.first { it.name == "Taco Master" }
        assertEquals(34, tacoMaster.totalTrophies)
    }

    @Test
    fun `gameDetails returns 79 trophies total from trophy list html`() {
        val result = scraper.gameDetails(trophyListHtml,"", "")
        assertEquals(79, result.trophies.size)
    }

    @Test
    fun `gameDetails parses trophy name`() {
        val result = scraper.gameDetails(trophyListHtml,"", "")
        assertEquals("All trophies obtained", result.trophies[0].name)
    }

    @Test
    fun `gameDetails parses trophy description`() {
        val result = scraper.gameDetails(trophyListHtml,"", "")
        assertEquals("Obtained all Horizon Zero Dawn trophies.", result.trophies[0].description)
    }

    @Test
    fun `gameDetails parses trophy grade`() {
        val result = scraper.gameDetails(trophyListHtml,"", "")
        assertEquals("Platinum", result.trophies[0].grade)
    }

    @Test
    fun `gameDetails parses trophy image URL`() {
        val result = scraper.gameDetails(trophyListHtml,"", "")
        assertEquals(URL("https://i.psnprofiles.com/games/1b7ae5/trophies/1S848de1.png"), result.trophies[0].imageURL)
    }

    @Test
    fun `gameDetails parses earned as true for earned trophy`() {
        val result = scraper.gameDetails(trophyListHtml,"", "")
        assertEquals(true, result.trophies[0].earned)
    }

    @Test
    fun `gameDetails parses earned as true for unearned trophy`() {
        val result = scraper.gameDetails(trophyListHtml,"", "")
        val newGamePlus = result.trophies.first { it.name == "New Game+ Completed" }
        assertEquals(false, newGamePlus.earned)
    }

    @Test
    fun `gameDetails sets gameId to passed value`() {
        val result = scraper.gameDetails(trophyListHtml,"someGameId", "")
        assertEquals("someGameId", result.trophies[0].gameId)
    }

    @Test
    fun `gameDetails sets playerPsnId to passed value`() {
        val result = scraper.gameDetails(trophyListHtml,"", "somePsnId")
        assertEquals("somePsnId", result.trophies[0].playerPsnId)
    }
}

private fun loadFileContents(filename: String): String {
    val file = ClassLoader.getSystemResource(filename)
    return file.readText()
}