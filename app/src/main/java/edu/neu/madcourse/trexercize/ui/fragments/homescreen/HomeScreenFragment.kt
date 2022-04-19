package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    private var user = Firebase.auth.currentUser?.uid
    private var db = Firebase.database.reference
    private var favoritesList: MutableList<FavoriteExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var homeScreenAdapter: HomeScreenAdapter? = null
    var streak_counter : TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.favorites_rec_view)
        val navBar: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation_bar)
        navBar?.visibility = View.VISIBLE
        streak_counter = view.findViewById(R.id.streak_counter)
        setUpResources()
        setUpData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData() {
        var musclesMap = HashMap<String, Int>()
        musclesMap.put("arms", R.drawable.arms)
        musclesMap.put("back", R.drawable.back)
        musclesMap.put("chest", R.drawable.chest)
        musclesMap.put("core", R.drawable.abs)
        musclesMap.put("legs", R.drawable.legs)

        favoritesList.clear()
        Firebase.auth.currentUser?.uid?.let {
            db.child("users").child(it).child("favorites")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            var data = snap.value as Map<String, String>
                            var favoriteExerciseCard = FavoriteExerciseCard(
                                snap.key,
                                data.getValue("muscle groups"),
                                musclesMap.get(data.getValue("muscle groups"))
                            )
                            Log.i("New Favorite", favoriteExerciseCard.toString())
                            favoritesList.add(favoriteExerciseCard)
                            homeScreenAdapter?.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
        homeScreenAdapter?.notifyDataSetChanged()

        var streak = Firebase.auth.currentUser?.uid?.let { db.child("users").child(it).child("streak").get().addOnSuccessListener { streak_counter?.text = "\uD83D\uDD25 ${it.value} day streak... Hit the gym!" } }
    }

    private fun setUpResources() {
        homeScreenAdapter = this.context?.let { HomeScreenAdapter(favoritesList, it) }
        recyclerView?.adapter = homeScreenAdapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}