package com.bruce32.psnprofileviewer.network

import com.bruce32.psnprofileviewer.model.Game
import com.bruce32.psnprofileviewer.model.GameDetails
import com.bruce32.psnprofileviewer.model.Profile
import com.bruce32.psnprofileviewer.model.ProfileAndGames
import com.bruce32.psnprofileviewer.model.Trophy
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL

class PSNProfileScraperImpl : PSNProfileScraper {

    override fun profileAndGames(html: String): ProfileAndGames {
        val doc = Jsoup.parse(html)
        val username = doc.select("span.username").text()
        val level = doc.select("li.icon-sprite.level").text()
        val levelProgressPercent = doc.select("div.progress-bar.level div")
            .attr("style")
            .replace("width: ", "")
            .replace("%;", "")

        val avatarURL = doc.select("div.avatar div img").attr("src")

        val totalTrophies = doc.select("li.total").text()
        val totalPlatinum = doc.select("li.platinum").text()
        val totalGold = doc.select("li.gold").text()
        val totalSilver = doc.select("li.silver").text()
        val totalBronze = doc.select("li.bronze").text()

        val stats = doc.select("span.stat.grow").map { it.ownText() }
        val worldRank = doc.select("span.rank.stat a").firstOrNull()?.ownText()
        val countryRank = doc.select("span.country-rank.stat a").firstOrNull()?.ownText()

        val gamesRows = doc.select("#gamesTable tr")
        val games = gamesRows.mapNotNull { parseGame(it, username) }

        val profile = Profile(
            psnId = username,
            level = level.toIntOrZero(),
            avatarURL = URL(avatarURL),
            levelProgressPercent = levelProgressPercent.toDoubleOrZero(),
            totalTrophies = totalTrophies.toIntOrZero(),
            totalPlatinum = totalPlatinum.toIntOrZero(),
            totalGold = totalGold.toIntOrZero(),
            totalSilver = totalSilver.toIntOrZero(),
            totalBronze = totalBronze.toIntOrZero(),
            gamesPlayed = stats[0].toIntOrZero(),
            completedGames = stats[1].toIntOrZero(),
            completionPercent = stats[2].replace("%", "").toDoubleOrZero(),
            unearnedTrophies = stats[3].toIntOrZero(),
            trophiesPerDay = stats[4].toDoubleOrZero(),
            worldRank = worldRank?.toIntOrZero() ?: 0,
            countryRank = countryRank?.toIntOrZero() ?: 0
        )

        return ProfileAndGames(
            profile = profile,
            games = games
        )
    }

    override fun gameDetails(html: String, gameId: String, psnId: String): GameDetails {
        val doc = Jsoup.parse(html)

        val trophiesRows = doc.select("#content table.zebra tr")
        val trophies = trophiesRows.mapNotNull { parseTrophy(it, gameId, psnId) }
        return GameDetails(
            trophies = trophies
        )
    }

    private fun parseTrophy(row: Element, gameId: String, psnId: String): Trophy? {
        val titleData = row.select("td a.title").first() ?: return null
        val name = titleData.ownText()
        val description = titleData.parents().first()?.ownText().orEmpty()
        val grade = row.select("td:last-child span img").attr("title")
        val imageURL = row.select("td a picture img").attr("src")
        val earned = row.hasClass("completed")

        return Trophy(
            name = name,
            description = description,
            grade = grade,
            imageURL = URL(imageURL),
            earned = earned,
            gameId = gameId,
            playerPsnId = psnId
        )
    }
}

private fun parseGame(game: Element, psnId: String): Game? {
    val name = game.select("a.title").text()
    val platform = if (game.select("span.tag.platform").size > 1) {
        game.select("span.tag.platform").joinToString(", ") { it.text() }
    } else {
        game.select("span.tag.platform").text()
    }

    val href = game.select("a.title").attr("href")
    if (href.isBlank()) {
        return null
    }
    val hrefComponents = href.split("/").filter { it.isNotBlank() }
    val psnProfilesId = hrefComponents[1]

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
        id = psnProfilesId,
        gold = gold.toIntOrZero(),
        silver = silver.toIntOrZero(),
        bronze = bronze.toIntOrZero(),
        completionPercent = completionPercent.toDoubleOrZero(),
        earnedTrophies = earnedTrophies.toIntOrZero(),
        totalTrophies = gameTotalTrophies.toIntOrZero(),
        playerPsnId = psnId
    )
}

private fun String.toIntOrZero() =
    this.replace(",", "")
        .toIntOrNull() ?: 0

private fun String.toDoubleOrZero() =
    this.replace(",", "")
        .toDoubleOrNull() ?: 0.0
