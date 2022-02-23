package edu.neu.madcourse.trexercize

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.homeFragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        bottomNavigation.background = null
        bottomNavigation.menu.getItem(2).isEnabled = false
        bottomNavigation?.setupWithNavController(navController)

        val exercises = findViewById<FloatingActionButton>(R.id.exerciseButton)
        exercises.setOnClickListener {
           Navigation.findNavController(this, R.id.homeFragment).navigate(R.id.exerciseFragment)
        }
    }
}
