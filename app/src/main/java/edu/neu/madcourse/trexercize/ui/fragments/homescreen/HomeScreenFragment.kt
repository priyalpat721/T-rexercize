package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.neu.madcourse.trexercize.R

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    private val favoritesList: MutableList<FavoriteExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var homeScreenAdapter: HomeScreenAdapter? = null
    //TODO: Add DB connection

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.favorites_rec_view)
        val navBar : BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation_bar)
        navBar?.visibility = View.VISIBLE
        setUpResources()
        setUpData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData() {
        favoritesList.clear()
        favoritesList.add(FavoriteExerciseCard("Pushup", "Chest", R.drawable.chest))
        favoritesList.add(FavoriteExerciseCard("Pullup", "Back", R.drawable.back))
        favoritesList.add(FavoriteExerciseCard("Crunches", "Abs", R.drawable.abs))
        homeScreenAdapter?.notifyDataSetChanged()
    }

    private fun setUpResources() {
        homeScreenAdapter = this.context?.let { HomeScreenAdapter(favoritesList, it) }
        recyclerView!!.adapter = homeScreenAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)
    }
}