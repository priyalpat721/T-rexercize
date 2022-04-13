package edu.neu.madcourse.trexercize.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.neu.madcourse.trexercize.R

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // makes the nav bar appear
        val navBar : BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation_bar)
        navBar?.visibility = View.VISIBLE
    }
}