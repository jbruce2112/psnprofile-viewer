package com.bruce32.psnprofileviewer.gamelist

import com.bruce32.psnprofileviewer.common.StringResource

sealed class GameListUpdate {
    class Empty(val message: StringResource?): GameListUpdate()
    class Items(val viewModels: List<GameViewModel>): GameListUpdate()
}