package com.bruce32.psnprofileviewer.common

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.net.URL

class GlideImageLoader: ImageLoader {
    override fun load(url: URL, view: ImageView) {
        Glide.with(view)
            .load(url.toString())
            .into(view)
    }
}