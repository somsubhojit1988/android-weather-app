package com.example.weatherapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.weatherapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val  navController = findNavController(R.id.defaultNavHost)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
    }

    override fun onSupportNavigateUp(): Boolean {
        val  navController = findNavController(R.id.defaultNavHost)
        return NavigationUI.navigateUp(navController,drawerLayout)
    }
}
