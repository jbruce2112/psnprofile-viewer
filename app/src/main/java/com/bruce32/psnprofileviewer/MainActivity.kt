package com.bruce32.psnprofileviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bruce32.psnprofileviewer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNavigationDrawer()
    }

    override fun onSupportNavigateUp() =
        navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    private fun setupNavigationDrawer() {
        appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
        binding.navigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private val navController: NavController
        get() {
            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.fragment_container_view) as? NavHostFragment
            return checkNotNull(navHostFragment?.navController) {
                "Couldn't get navigation controller"
            }
        }

}
