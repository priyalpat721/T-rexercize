package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R


class ShowGoalsFragment : Fragment(R.layout.fragment_show_goals) {
    private lateinit var backButton: ImageButton
    private val goalList: ArrayList<GoalCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var adapter: ShowGoalAdapter? = null
    var db = Firebase.database.reference
    private val doneList: ArrayList<GoalCard> = ArrayList()
    private val favList: ArrayList<GoalCard> = ArrayList()
    private val pendingList: ArrayList<GoalCard> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.back_btn4)

        backButton.setOnClickListener {
            val action: NavDirections = ShowGoalsFragmentDirections.actionShowGoalsFragmentToProfileFragment()
            view.findNavController().navigate(action)
        }

        recyclerView = view.findViewById(R.id.recycler_view_show)
        setUpResources()

        listenForChanges()

    }

    private fun setUpResources() {
        recyclerView?.setHasFixedSize(true)
        adapter = ShowGoalAdapter(goalList, context)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenForChanges() {
        goalList.clear()
        favList.clear()
        doneList.clear()
        pendingList.clear()

        Firebase.auth.currentUser?.uid?.let {
            db.child("users").child(it).child("goals")
                .addValueEventListener(object :
                    ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        goalList.clear()
                        favList.clear()
                        doneList.clear()
                        pendingList.clear()
                        for (snap in snapshot.children) {
                            Log.i("Goals", snap.toString())
                            val value = snap.value as Map<*, *>

                            val card = value["goal"]?.let { it1 ->
                                value["time"]?.let { it2 ->
                                    GoalCard(
                                        snap.key.toString(), it1 as String,
                                        it2 as String, value["favorite"] as Boolean, value["done"] as Boolean
                                    )
                                }
                            }
                            if (card != null) {
                                if (card.favorite) {
                                    favList.add(card)
                                }
                                else if (card.done) {
                                    doneList.add(card)
                                }
                                else {
                                    pendingList.add(card)
                                }
                            }
                        }
                        goalList.clear()
                        goalList.addAll(favList)
                        goalList.addAll(pendingList)
                        goalList.addAll(doneList)
                        adapter?.notifyDataSetChanged()

                    }
                    override fun onCancelled(error: DatabaseError) {
                        // not implemented
                    }
                })
        }
    }
}