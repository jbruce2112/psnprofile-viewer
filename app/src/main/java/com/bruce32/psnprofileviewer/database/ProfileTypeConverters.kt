package com.bruce32.psnprofileviewer.database

import androidx.room.TypeConverter
import java.net.URL

class ProfileTypeConverters {
    @TypeConverter
    fun fromURL(url: URL): String {
        return url.toString()
    }

    @TypeConverter
    fun toURL(urlString: String): URL {
        return URL(urlString)
    }
}