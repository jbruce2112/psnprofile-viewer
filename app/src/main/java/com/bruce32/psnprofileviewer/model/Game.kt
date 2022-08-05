package com.bruce32.psnprofileviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity
data class Game(
    val name: String,
    val coverURL: URL,
    val platform: String,
    val platinum: Int?,
    @PrimaryKey val id: String,
    val gold: Int,
    val silver: Int,
    val bronze: Int,
    val completionPercent: Double,
    val earnedTrophies: Int,
    val totalTrophies: Int,
    val playerPsnId: String
)

