package edu.neu.madcourse.trexercize

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {
    private lateinit var navBar: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        navBar = findViewById(R.id.nav_bar_bottom)
        val menu = navBar.menu
        val tab = menu.getItem(4)
        tab.isChecked = true
        navBar.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.home -> {
                    val home = Intent(this@ProfileActivity, MainActivity::class.java)
                    home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(home)
                    finish()
                }
                R.id.exercise -> {
                    val exercise = Intent(this@ProfileActivity, ExerciseActivity::class.java)
                    exercise.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(exercise)
                    finish()
                }
                R.id.favorites -> {
                    val favorites = Intent(this@ProfileActivity, FavoritesActivity::class.java)
                    favorites.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(favorites)
                    finish()
                }
                R.id.calendar -> {
                    val calendar = Intent(this@ProfileActivity, CalendarActivity::class.java)
                    calendar.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(calendar)
                    finish()
                }
                R.id.profile -> {
                }
            }
            true
        }
    }
}