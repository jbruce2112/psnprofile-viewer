package com.bruce32.psnprofileviewer.gamelist

sealed class GameListUpdate {
    class Empty(val message: String): GameListUpdate()
    object Loading : GameListUpdate()
    class Items(val viewModels: List<GameViewModel>): GameListUpdate()
}
