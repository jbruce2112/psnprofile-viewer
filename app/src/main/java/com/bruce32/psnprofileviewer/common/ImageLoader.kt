package com.bruce32.psnprofileviewer.common

import android.widget.ImageView
import java.net.URL

interface ImageLoader {
    fun load(url: URL, view: ImageView)
}
