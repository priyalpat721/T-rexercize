package edu.neu.madcourse.trexercize

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navBar = findViewById(R.id.nav_bar_bottom)
        val menu = navBar.menu
        val tab = menu.getItem(0)
        tab.isChecked = true
        navBar.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                }
                R.id.exercise -> {
                    val exercise = Intent(this@MainActivity, ExerciseActivity::class.java)
                    exercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(exercise)
                }
                R.id.favorites -> {
                    val favorites = Intent(
                        this@MainActivity,
                        FavoritesActivity::class.java
                    )
                    favorites.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(favorites)
                }
                R.id.calendar -> {
                    val calendar = Intent(
                        this@MainActivity,
                        CalendarActivity::class.java
                    )
                    calendar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(calendar)
                }
                R.id.profile -> {
                    val profile = Intent(this@MainActivity, ProfileActivity::class.java)
                    profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(profile)
                }
            }
            false
        }
    }
}