package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R

class GoalFragment : Fragment(R.layout.fragment_goal) {
    private lateinit var backButton: ImageButton
    private val goalList: ArrayList<GoalCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var adapter: GoalAdapter? = null
    private lateinit var addGoalBtn: FloatingActionButton
    var db = Firebase.database.reference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.back_btn2)

        backButton.setOnClickListener {
            val action: NavDirections = GoalFragmentDirections.actionGoalFragmentToProfileFragment()
            view.findNavController().navigate(action)
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        setUpResources()
        manageSwipes()

        addGoalBtn = view.findViewById(R.id.addGoalBtn)
        addGoalBtn.setOnClickListener {
            getTaskName()
        }
        listenForChanges()

        val doneCheckBox = object : IDoneCheckBoxListener {
            override fun onDoneBoxClick(position: Int) {
                goalList[position].onDoneBoxClick(position)

                adapter?.notifyItemChanged(position)

                Firebase.auth.currentUser?.let {
                    db.child("users").child(it.uid)
                        .child("goals").child(goalList[position].id)
                        .child("done").setValue(goalList[position].done)
                }
            }
        }
        adapter?.setDoneCheckBoxListener(doneCheckBox)

        val favCheckBox = object : IFavCheckBoxListener {
            override fun onFavBoxClick(position: Int) {
                goalList[position].onFavBoxClick(position)
                adapter?.notifyItemChanged(position)

                Firebase.auth.currentUser?.let {
                    db.child("users").child(it.uid)
                        .child("goals").child(goalList[position].id)
                        .child("favorite").setValue(goalList[position].favorite)
                }
            }
        }
        adapter?.setFavCheckBoxListener(favCheckBox)
    }

    private fun setUpResources() {
        recyclerView?.setHasFixedSize(true)
        adapter = GoalAdapter(goalList, context)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }

    private fun getTaskName() {
        val inflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.activity_pop_up_dialog_box, null, false)
        val dialogBox = AlertDialog.Builder(this.requireContext())
        dialogBox.setTitle("Enter your goal")
        dialogBox.setView(view)
        val timeText = view.findViewById<TextView>(R.id.time_entered)
        val goalText = view.findViewById<EditText>(R.id.goal_name_dialog)
        timeText.text = Timestamp.now().toDate().toString()

        // this is if the user hits done
        dialogBox.setCancelable(false)
            .setPositiveButton("Done") { dialog: DialogInterface, _: Int ->
                // create a new goal card to hold new values

                val goalId = Firebase.auth.uid?.let { it1 ->
                    db.child("users").child(it1).child("goals").push().key.toString()
                }
                val card =
                    hashMapOf(
                        "goal" to goalText.text.toString(),
                        "time" to timeText.text.toString(),
                        "favorite" to false,
                        "done" to false
                    )

                Firebase.auth.uid?.let { it1 ->
                    db.child("users").child(it1).child("goals").child(goalId.toString()).setValue(card)
                }

                val success = recyclerView?.let {
                    Snackbar.make(
                        it,
                        "Successfully created!", Snackbar.LENGTH_LONG
                    )
                }
                success?.show()

                dialog.dismiss()
            }
        dialogBox.setCancelable(false)
            .setNegativeButton("Cancel") { dialog: DialogInterface, _: Int ->
                val canceled = recyclerView?.let {
                    Snackbar.make(
                        it,
                        "Action canceled!", Snackbar.LENGTH_SHORT
                    )
                }
                canceled?.show()
                dialog.dismiss()
            }
        dialogBox.show()
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
                            .child("goals").child(goalList[position].id).removeValue()
                    }
                    goalList.removeAt(position)
                    adapter!!.notifyItemRemoved(position)
                    val success = Snackbar.make(
                        recyclerView!!,
                        "Successfully deleted!", Snackbar.LENGTH_LONG
                    )
                    success.show()
                }
            })
        deleteOnLeft.attachToRecyclerView(recyclerView)

        // edits the url card
        val editOnRight =
            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                // should only edit, not move the cards around
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.layoutPosition
                    val card: GoalCard = goalList[position]
                    val inflater = LayoutInflater.from(context)
                    val view: View =
                        inflater.inflate(R.layout.activity_pop_up_dialog_box, null, false)
                    val dialogBox = AlertDialog.Builder(
                        context!!
                    )
                    dialogBox.setTitle("Edit Goal")
                    dialogBox.setView(view)
                    val timeText = view.findViewById<TextView>(R.id.time_entered)
                    val goalText = view.findViewById<EditText>(R.id.goal_name_dialog)
                    timeText.text = Timestamp.now().toDate().toString()

                    // this is if the user hits done
                    dialogBox.setCancelable(false).setPositiveButton(
                        "Done"
                    ) { dialog: DialogInterface, _: Int ->

                        val newCard = GoalCard(
                            card.id,
                            goalText.text.toString(),
                            timeText.text.toString(),
                            card.favorite,
                            card.done
                        )


                        goalList[position] = newCard
                        adapter!!.notifyItemChanged(position)

                        Firebase.auth.currentUser?.let {
                            db.child("users").child(it.uid)
                                .child("goals").child(goalList[position].id)
                                .child("goal").setValue(goalList[position].goal)
                        }

                        val success = Snackbar.make(
                            recyclerView!!,
                            "Successfully edited!", Snackbar.LENGTH_LONG
                        )
                        success.show()

                        dialog.dismiss()
                    }
                    dialogBox.setCancelable(false).setNegativeButton(
                        "Cancel"
                    ) { dialog: DialogInterface, _: Int ->
                        goalList[position] = card
                        adapter!!.notifyItemChanged(position)
                        val canceled = Snackbar.make(
                            recyclerView!!,
                            "Action canceled!", Snackbar.LENGTH_SHORT
                        )
                        canceled.show()
                        dialog.dismiss()
                    }
                    dialogBox.show()
                }
            })
        editOnRight.attachToRecyclerView(recyclerView)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenForChanges() {
        goalList.clear()
        Firebase.auth.currentUser?.uid?.let {
            db.child("users").child(it).child("goals")
                .addValueEventListener(object :
                    ValueEventListener {
                    @SuppressLint("SetTextI18n")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        goalList.clear()
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
                                goalList.add(card)
                            }
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