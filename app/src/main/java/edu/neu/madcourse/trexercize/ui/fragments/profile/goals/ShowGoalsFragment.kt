package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
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
    private val goalList: ArrayList<ShowGoalCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var adapter: ShowGoalAdapter? = null
    var db = Firebase.database.reference
    private val doneList: ArrayList<ShowGoalCard> = ArrayList()
    private val favList: ArrayList<ShowGoalCard> = ArrayList()
    private val pendingList: ArrayList<ShowGoalCard> = ArrayList()
    private lateinit var showMotivatedDino : ConstraintLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.back_btn4)

        backButton.setOnClickListener {
            val action: NavDirections = ShowGoalsFragmentDirections.actionShowGoalsFragmentToProfileFragment()
            view.findNavController().navigate(action)
        }

        recyclerView = view.findViewById(R.id.recycler_view_show)
        showMotivatedDino = view.findViewById(R.id.no_recycler_shown)
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

                            var favImage = ""

                            if (value["favorite"] as Boolean) {
                                favImage = "https://firebasestorage.googleapis.com/v0/b" +
                                        "/t-rexercize.appspot.com/o/dino%20selected.JPG?" +
                                        "alt=media&token=96a20159-face-466a-9df0-c8d51b204e3a"
                            }
                            val card = value["goal"]?.let { it1 ->
                                value["time"]?.let { it2 ->
                                    ShowGoalCard(
                                        snap.key.toString(), it1 as String,
                                        it2 as String, favImage, value["done"] as Boolean
                                    )
                                }
                            }
                            if (card != null) {
                                if (card.favorite != "") {
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

                        if (goalList.isNullOrEmpty()) {
                            recyclerView?.visibility = View.GONE
                            showMotivatedDino.visibility = View.VISIBLE
                        }
                        else{
                            recyclerView?.visibility = View.VISIBLE
                            showMotivatedDino.visibility = View.GONE
                        }
                        adapter?.notifyDataSetChanged()

                    }
                    override fun onCancelled(error: DatabaseError) {
                        // not implemented
                    }
                })
        }
    }
}