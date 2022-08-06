package com.bruce32.psnprofileviewer.trophylist

sealed class TrophyListUpdate {
    object Loading : TrophyListUpdate()
    class Items(val viewModels: List<TrophyViewModel>): TrophyListUpdate()
}
