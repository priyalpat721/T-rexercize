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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.EachExerciseCardListener
import edu.neu.madcourse.trexercize.ui.fragments.exercise.ExerciseCard
import edu.neu.madcourse.trexercize.ui.fragments.exercise.ExerciseFragmentDirections

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
        manageSwipes()
        setUpData()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData() {
        var musclesMap = HashMap<String, Int>()
        musclesMap.put("arms", R.drawable.arms)
        musclesMap.put("back", R.drawable.back)
        musclesMap.put("chest", R.drawable.chest)
        musclesMap.put("core", R.drawable.abs)
        musclesMap.put("full body", R.drawable.full_body)
        musclesMap.put("legs", R.drawable.legs)

        favoritesList.clear()
        Firebase.auth.currentUser?.uid?.let {
            db.child("users").child(it).child("favorites")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        favoritesList.clear()
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

    private fun manageSwipes() {
        // deletes the url card
        val deleteOnLeft =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                // should only delete, not move the cards around
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.layoutPosition
                    Firebase.auth.currentUser?.let {
                        db.child("users").child(it.uid)
                            .child("favorites").child(favoritesList[position].favoriteExercise!!).removeValue()
                    }
                    favoritesList.removeAt(position)
                    homeScreenAdapter!!.notifyItemRemoved(position)
                    val success = Snackbar.make(
                        recyclerView!!,
                        "Successfully deleted!", Snackbar.LENGTH_LONG
                    )
                    success.show()
                }
            })
        deleteOnLeft.attachToRecyclerView(recyclerView)
    }
}