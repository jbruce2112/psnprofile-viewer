package com.bruce32.psnprofileviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URL

@Entity
data class Trophy(
    val name: String,
    val description: String,
    val grade: String,
    val imageURL: URL,
    val earned: Boolean,
    val gameId: String,
    val playerPsnId: String
) {
    @PrimaryKey
    var id = "$name-$gameId-$playerPsnId"
}
