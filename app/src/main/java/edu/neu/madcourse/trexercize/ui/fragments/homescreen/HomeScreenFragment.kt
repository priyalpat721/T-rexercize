package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
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
import edu.neu.madcourse.trexercize.ui.fragments.exercise.EachExerciseCardListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {
    private var db = Firebase.database.reference
    private var favoritesList: MutableList<FavoriteExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var homeScreenAdapter: HomeScreenAdapter? = null
    private var streakCounter : TextView? = null
    private lateinit var currentLocalDate: LocalDate
    private lateinit var snapLocalDate: LocalDate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.favorites_rec_view)
        val navBar: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation_bar)
        navBar?.visibility = View.VISIBLE
        streakCounter = view.findViewById(R.id.streak_counter)
        setUpResources()
        setUpData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData() {
        val musclesMap = HashMap<String, Int>()
        musclesMap["arms"] = R.drawable.arms
        musclesMap["back"] = R.drawable.back
        musclesMap["chest"] = R.drawable.chest
        musclesMap["core"] = R.drawable.abs
        musclesMap["legs"] = R.drawable.legs

        favoritesList.clear()
        Firebase.auth.currentUser?.uid?.let {
            db.child("users").child(it).child("favorites")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            val data = snap.value as Map<String, String>
                            val favoriteExerciseCard = FavoriteExerciseCard(
                                snap.key,
                                data.getValue("muscle groups"),
                                musclesMap[data.getValue("muscle groups")]
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
        val currentDay = SimpleDateFormat("M-d-yyyy", Locale.getDefault()).format(
            Date()
        )
        Firebase.auth.currentUser?.uid?.let { it ->
            db.child("users").child(it)
            .child("streakInfo").child("last snap date").get()
            .addOnSuccessListener {
                if (it.value != "none") {
                    snapLocalDate = LocalDate.parse(
                        it.value.toString(),
                        DateTimeFormatter.ofPattern("M-d-yyyy")
                    )
                    currentLocalDate =
                        LocalDate.parse(currentDay, DateTimeFormatter.ofPattern("M-d-yyyy"))
                    if (snapLocalDate.isBefore(currentLocalDate.minusDays(1))) {
                        db.child("users")
                            .child(Firebase.auth.currentUser?.uid.toString())
                            .child("streakInfo").child("current streak count")
                            .setValue(0)
                    }
                }
            } }

        homeScreenAdapter?.notifyDataSetChanged()

        Firebase.auth.currentUser?.uid?.let {
            db.child("users").child(it).child("streakInfo")
                .child("current streak count")
                .addValueEventListener(object : ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                            val count = snapshot.value.toString()
                            if (count == "0") {
                                streakCounter?.text = "0 day streak... Hit the gym!"
                            } else {
                                streakCounter?.text = "\uD83D\uDD25 $count day streak! Keep it up!"
                            }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // not implemented
                    }
                })
        }
//        Firebase.auth.currentUser?.uid?.let { db.child("users").child(it)
//            .child("streakInfo").child("current streak count")
//            .get().addValueEventListener { streak_counter?.text = "\uD83D\uDD25 ${it.value} day streak... Hit the gym!" } }
    }

    private fun setUpResources() {

        val listener: EachExerciseCardListener = object : EachExerciseCardListener {
            override fun onItemClick(position: Int) {
                val exerciseCard: FavoriteExerciseCard = favoritesList[position]
                val favoriteExercise = exerciseCard.favoriteExercise
                val category = exerciseCard.exerciseCategory
                Toast.makeText(context, "This is: $favoriteExercise", Toast.LENGTH_SHORT).show()

                val action: NavDirections
                action = HomeScreenFragmentDirections.actionHomeScreenFragmentToEachExerciseFragment()

                action.also {
                    if (favoriteExercise != null) {
                        action.exerciseName = favoriteExercise
                        action.exerciseCategory = category.toString()
                    }
                }

                view?.findNavController()?.navigate(action)

            }
        }

        homeScreenAdapter = this.context?.let { HomeScreenAdapter(favoritesList, it, listener) }
        homeScreenAdapter?.setEachOnClickListener(listener)
        recyclerView?.adapter = homeScreenAdapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}