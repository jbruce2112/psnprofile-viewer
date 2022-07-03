package com.bruce32.psnprofileviewer.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL

class PSNProfileScraper {

    fun profile(html: String): Profile {
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

    fun game(html: String): String {
        TODO("Not yet implemented")
    }
}

data class Profile(
    val psnId: String,
    val level: Int,
    val levelProgressPercent: Double,
    val totalTrophies: Int,
    val totalPlatinum: Int,
    val totalGold: Int,
    val totalSilver: Int,
    val totalBronze: Int,
    val stats: ProfileStats,
    val games: List<Game>
)

data class ProfileStats(
    val gamesPlayed: Int,
    val completedGames: Int,
    val completionPercent: Double,
    val unearnedTrophies: Int,
    val trophiesPerDay: Double,
    val worldRank: Int,
    val countryRank: Int
)

data class Game(
    val name: String,
    val coverURL: URL?,
    val platform: String,
    val platinum: Int?,
    val href: String,
    val gold: Int,
    val silver: Int,
    val bronze: Int,
    val completionPercent: Double,
    val earnedTrophies: Int,
    val totalTrophies: Int
)

private fun String.toIntOrZero() =
    this.replace(",","")
        .toIntOrNull() ?: 0

private fun String.toDoubleOrZero() =
    this.replace(",","")
        .toDoubleOrNull() ?: 0.0

private fun parseGame(game: Element): Game {
    val name = game.select("a.title").text()
    val platform = if (game.select("span.tag.platform").size > 1) {
        game.select("span.tag.platform").joinToString(",") { it.text() }
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
