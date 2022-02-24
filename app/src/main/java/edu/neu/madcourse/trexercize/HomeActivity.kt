package edu.neu.madcourse.trexercize

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.neu.madcourse.trexercize.ui.fragments.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val homeScreenFragment = HomeScreenFragment()
        val exerciseFragment = ExerciseFragment()
        val favoritesFragment = FavoritesFragment()
        val profileFragment = ProfileFragment()
        val calendarFragment = CalendarFragment()

        setCurrentFragment(homeScreenFragment)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        bottomNavigationView.background = null
        bottomNavigationView.menu.getItem(2).isEnabled = false
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeScreenFragment -> setCurrentFragment(homeScreenFragment)
                R.id.favoritesFragment -> setCurrentFragment(favoritesFragment)
                R.id.profileFragment -> setCurrentFragment(profileFragment)
                R.id.calendarFragment -> setCurrentFragment(calendarFragment)
            }
            true
        }

        val exerciseButton = findViewById<FloatingActionButton>(R.id.exerciseButton)
        exerciseButton.setOnClickListener {
            setCurrentFragment(exerciseFragment)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.homeFragment, fragment)
            commit()
        }
    }
}

