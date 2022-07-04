package com.bruce32.psnprofileviewer.model

import androidx.room.Entity

@Entity
data class GameDetails(
    val trophies: List<Trophy>
)
