package com.bruce32.psnprofileviewer.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
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

data class ProfileWithGames(
    @Embedded val profile: Profile,
    @Relation(
        parentColumn = "psnId",
        entityColumn = "playerPsnId"
    )
    val games: List<Game>
)
