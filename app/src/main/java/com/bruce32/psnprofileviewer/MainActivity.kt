package com.bruce32.psnprofileviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.bruce32.psnprofileviewer.api.PSNProfileScraper
import com.bruce32.psnprofileviewer.api.PSNProfileScraperImpl
import com.bruce32.psnprofileviewer.api.PSNProfileService
import com.bruce32.psnprofileviewer.api.PSNProfileServiceImpl
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}