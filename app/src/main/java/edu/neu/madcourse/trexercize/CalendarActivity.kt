package edu.neu.madcourse.trexercize

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class CalendarActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        navBar = findViewById(R.id.nav_bar_bottom)
        val menu = navBar.menu
        val tab = menu.getItem(3)
        tab.isChecked = true
        navBar.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item: MenuItem ->
            val itemId = item.itemId
            if (itemId == R.id.home) {
                val home = Intent(this@CalendarActivity, MainActivity::class.java)
                home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(home)
                finish()
            } else if (itemId == R.id.exercise) {
                val exercise = Intent(this@CalendarActivity, ExerciseActivity::class.java)
                exercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(exercise)
                finish()
            } else if (itemId == R.id.favorites) {
                val favorites = Intent(
                    this@CalendarActivity,
                    edu.neu.madcourse.trexercize.FavoritesActivity::class.java
                )
                favorites.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(favorites)
                finish()
            } else if (itemId == R.id.calendar) {
            } else if (itemId == R.id.profile) {
                val profile = Intent(this@CalendarActivity, ProfileActivity::class.java)
                profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(profile)
                finish()
            }
            true
        })
    }
}