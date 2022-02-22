package edu.neu.madcourse.trexercize

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalendarActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        navBar = findViewById(R.id.nav_bar_bottom)
        val menu = navBar.menu
        val tab = menu.getItem(3)
        tab.isChecked = true
        navBar.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    val home = Intent(this@CalendarActivity, MainActivity::class.java)
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(home)
                    finish()
                }
                R.id.exercise -> {
                    val exercise = Intent(this@CalendarActivity, ExerciseActivity::class.java)
                    exercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(exercise)
                    finish()
                }
                R.id.favorites -> {
                    val favorites = Intent(
                        this@CalendarActivity,
                        FavoritesActivity::class.java
                    )
                    favorites.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(favorites)
                    finish()
                }
                R.id.calendar -> {
                }
                R.id.profile -> {
                    val profile = Intent(this@CalendarActivity, ProfileActivity::class.java)
                    profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(profile)
                    finish()
                }
            }
            true
        }
    }
}