package com.bruce32.psnprofileviewer.gamelist

import com.bruce32.psnprofileviewer.model.Game
import kotlin.math.roundToInt

class GameViewModel(private val game: Game) {
    val id = game.id
    val title = game.name
    val secondary = game.platform
    val description = trophies().joinToString(", ") {
        "${it.first.withSeparator()} ${it.second}"
    }

    val leadingIconURL = game.coverURL

    val trailingText = "${game.completionPercent.roundToInt()}%"

    val highlighted = game.platinum == 1 || game.earnedTrophies == game.totalTrophies

    private fun trophies(): List<Pair<Int, String>> {
        var list = mutableListOf<Pair<Int, String>>()
        game.platinum?.let {
            list.add(Pair(it, "Platinum"))
        }
        list.add(Pair(game.gold, "Gold"))
        list.add(Pair(game.silver, "Silver"))
        list.add(Pair(game.bronze, "Bronze"))
        return list
    }
}

private fun Int.withSeparator() = String.format("%,d", this)