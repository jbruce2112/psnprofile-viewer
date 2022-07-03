package com.bruce32.psnprofileviewer.api

import android.util.Log
import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.ProfileStats
import java.net.URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

interface PSNProfileScraper {
    fun profile(html: String): Profile
    fun game(html: String): CompleteGame
}

class PSNProfileScraperImpl : PSNProfileScraper {

    override fun profile(html: String): Profile {
        val doc = Jsoup.parse(html)
        val username = doc.select("span.username").text()
        val level = doc.select("li.icon-sprite.level").text()
        val levelProgressPercent = doc.select("div.progress-bar.level div")
            .attr("style")
            .replace("width: ", "")
            .replace("%;", "")

        val totalTrophies = doc.select("li.total").text()
        val totalPlatinum = doc.select("li.platinum").text()
        val totalGold = doc.select("li.gold").text()
        val totalSilver = doc.select("li.silver").text()
        val totalBronze = doc.select("li.bronze").text()

        val stats = doc.select("span.stat.grow").map { it.ownText() }
        val worldRank = doc.select("span.rank.stat a").firstOrNull()?.ownText()
        val countryRank = doc.select("span.country-rank.stat a").firstOrNull()?.ownText()
        val profileStats = ProfileStats(
            gamesPlayed = stats[0].toIntOrZero(),
            completedGames = stats[1].toIntOrZero(),
            completionPercent = stats[2].replace("%", "").toDoubleOrZero(),
            unearnedTrophies = stats[3].toIntOrZero(),
            trophiesPerDay = stats[4].toDoubleOrZero(),
            worldRank = worldRank?.toIntOrZero() ?: 0,
            countryRank = countryRank?.toIntOrZero() ?: 0
        )

        val gamesTable = doc.select("[id=\"gamesTable\"] tr")
        val games = gamesTable.map { parseGame(it) }

        return Profile(
            psnId = username,
            level = level.toIntOrZero(),
            levelProgressPercent = levelProgressPercent.toDoubleOrZero(),
            totalTrophies = totalTrophies.toIntOrZero(),
            totalPlatinum = totalPlatinum.toIntOrZero(),
            totalGold = totalGold.toIntOrZero(),
            totalSilver = totalSilver.toIntOrZero(),
            totalBronze = totalBronze.toIntOrZero(),
            games = games,
            stats = profileStats
        )
    }

    override fun game(html: String): CompleteGame {
        val doc = Jsoup.parse(html)

        val trophiesRows = doc.select("[id=\"content\"] table.zebra tr")
        val trophies = trophiesRows.mapNotNull { parseTrophy(it) }
        return CompleteGame(
            trophies = trophies
        )
    }

    private fun parseTrophy(row: Element): Trophy? {
        Log.d("Parse", "going to parse row $row")
        val titleData = row.select("td a.title").first() ?: return null
        val name = titleData.ownText()
        val description = titleData.parents()[0].ownText()
        val grade = row.select("td").last()?.select("span img")?.attr("title").orEmpty()
        return Trophy(
            name = name,
            description = description,
            grade = grade
        )
    }
}

private fun parseGame(game: Element): Game {
    val name = game.select("a.title").text()
    val platform = if (game.select("span.tag.platform").size > 1) {
        game.select("span.tag.platform").joinToString(", ") { it.text() }
    } else {
        game.select("span.tag.platform").text()
    }

    val href = game.select("a.title").attr("href")

    val coverURL = game.select("picture.game img").attr("src")

    val platinumClass = game.select("img.icon-sprite").attr("class")
    val platinum = if (platinumClass[12] == 'c') {
        null
    } else if (platinumClass.endsWith("earned")) {
        1
    } else {
        0
    }

    val completionNode = game.select("div.trophy-count div span")

    val gold = completionNode[1].text()
    val silver = completionNode[3].text()
    val bronze = completionNode[5].text()
    val completionPercent = completionNode[6].text().replace("%", "")

    val earnedTrophies = game.select("div.small-info b")[0].text()
    val gameTotalTrophies = if (game.select("div.small-info")[0].text().startsWith("All")) {
        earnedTrophies
    } else {
        game.select("div.small-info b")[1].text()
    }

    return Game(
        name = name,
        coverURL = URL(coverURL),
        platform = platform,
        platinum = platinum,
        href = href,
        gold = gold.toIntOrZero(),
        silver = silver.toIntOrZero(),
        bronze = bronze.toIntOrZero(),
        completionPercent = completionPercent.toDoubleOrZero(),
        earnedTrophies = earnedTrophies.toIntOrZero(),
        totalTrophies = gameTotalTrophies.toIntOrZero(),
    )
}

data class CompleteGame(
    val trophies: List<Trophy>
)

data class Trophy(
    val name: String,
    val description: String,
    val grade: String
)

private fun String.toIntOrZero() =
    this.replace(",", "")
        .toIntOrNull() ?: 0

private fun String.toDoubleOrZero() =
    this.replace(",", "")
        .toDoubleOrNull() ?: 0.0
