package com.bruce32.psnprofileviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity
data class Profile(
    @PrimaryKey val psnId: String,
    val level: Int,
    val avatarURL: URL,
    val levelProgressPercent: Double,
    val totalTrophies: Int,
    val totalPlatinum: Int,
    val totalGold: Int,
    val totalSilver: Int,
    val totalBronze: Int,
    val gamesPlayed: Int,
    val completedGames: Int,
    val completionPercent: Double,
    val unearnedTrophies: Int,
    val trophiesPerDay: Double,
    val worldRank: Int,
    val countryRank: Int
)
