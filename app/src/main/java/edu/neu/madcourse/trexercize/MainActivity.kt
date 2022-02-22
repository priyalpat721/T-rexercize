package edu.neu.madcourse.trexercize

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class MainActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navBar = findViewById(R.id.nav_bar_bottom)
        val menu = navBar.menu
        val tab = menu.getItem(0)
        tab.isChecked = true
        navBar.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            if (itemId == R.id.home) {
            } else if (itemId == R.id.exercise) {
                val exercise = Intent(this@MainActivity, ExerciseActivity::class.java)
                exercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(exercise)
            } else if (itemId == R.id.favorites) {
                val favorites = Intent(
                    this@MainActivity,
                    edu.neu.madcourse.trexercize.FavoritesActivity::class.java
                )
                favorites.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(favorites)
            } else if (itemId == R.id.calendar) {
                val calendar = Intent(
                    this@MainActivity,
                    edu.neu.madcourse.trexercize.CalendarActivity::class.java
                )
                calendar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(calendar)
            } else if (itemId == R.id.profile) {
                val profile = Intent(this@MainActivity, ProfileActivity::class.java)
                profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(profile)
            }
            false
        })
    }
}