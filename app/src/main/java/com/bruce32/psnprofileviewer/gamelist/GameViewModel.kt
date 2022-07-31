package com.bruce32.psnprofileviewer.gamelist

import com.bruce32.psnprofileviewer.common.ListItemViewModel
import com.bruce32.psnprofileviewer.model.Game

class GameViewModel(
    private val game: Game
) : ListItemViewModel {
    override val id = game.id
    override val title = game.name
    override val secondary = game.platform
    override val description = trophies().joinToString(", ") {
        "${it.first.withSeparator()} ${it.second}"
    }

    override val leadingIconURL = game.coverURL

    override val trailingText = "${game.completionPercent.toInt()}%"

    override val highlighted = game.platinum == 1 || game.earnedTrophies == game.totalTrophies

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