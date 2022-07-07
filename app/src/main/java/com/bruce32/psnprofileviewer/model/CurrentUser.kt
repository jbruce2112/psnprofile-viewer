package com.bruce32.psnprofileviewer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrentUser(
    val psnId: String
) {
    @PrimaryKey var id: Int = 1
}
