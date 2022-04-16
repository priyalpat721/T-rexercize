package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.content.DialogInterface
import android.os.Bundle
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
import edu.neu.madcourse.trexercize.R

class GoalFragment : Fragment(R.layout.fragment_goal) {
    private lateinit var backButton: ImageButton
    private val goalList: ArrayList<GoalCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var adapter: GoalAdapter? = null
    lateinit var addGoalBtn: FloatingActionButton;

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
    }

    private fun setUpResources() {
        recyclerView?.setHasFixedSize(true)
        adapter = GoalAdapter(goalList, context)
        val doneCheckBox = object : IDoneCheckBoxListener {
            override fun onDoneBoxClick(position: Int) {
                goalList[position].onDoneBoxClick(position)
                adapter?.notifyItemChanged(position)
            }
        }
        adapter?.setDoneCheckBoxListener(doneCheckBox)

        val favCheckBox = object : IFavCheckBoxListener {
            override fun onFavBoxClick(position: Int) {
                goalList[position].onFavBoxClick(position)
                adapter?.notifyItemChanged(position)
            }
        }
        adapter?.setFavCheckBoxListener(favCheckBox)
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
        val goalText = view.findViewById<EditText>(R.id.goal_name)
        timeText.text = Timestamp.now().toDate().toString()

        // this is if the user hits done
        dialogBox.setCancelable(false)
            .setPositiveButton("Done") { dialog: DialogInterface, which: Int ->
                // create a new goal card to hold new values
                val card =
                    GoalCard(goalText.text.toString(), timeText.text.toString(), false, false)

                goalList.add(goalList.size, card)
                adapter!!.notifyItemInserted(goalList.size)
                val success = Snackbar.make(
                    recyclerView!!,
                    "Successfully created!", Snackbar.LENGTH_LONG
                )
                success.show()

                dialog.dismiss()
            }
        dialogBox.setCancelable(false)
            .setNegativeButton("Cancel") { dialog: DialogInterface, which: Int ->
                val canceled = Snackbar.make(
                    recyclerView!!,
                    "Action canceled!", Snackbar.LENGTH_SHORT
                )
                canceled.show()
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
                    val goalText = view.findViewById<EditText>(R.id.goal_name)
                    timeText.text = Timestamp.now().toDate().toString()

                    // this is if the user hits done
                    dialogBox.setCancelable(false).setPositiveButton(
                        "Done"
                    ) { dialog: DialogInterface, which: Int ->

                        val newCard = GoalCard(
                            goalText.text.toString(),
                            timeText.text.toString(),
                            card.favorite,
                            card.done
                        )


                        goalList[position] = newCard
                        adapter!!.notifyItemChanged(position)
                        val success = Snackbar.make(
                            recyclerView!!,
                            "Successfully edited!", Snackbar.LENGTH_LONG
                        )
                        success.show()

                        dialog.dismiss()
                    }
                    dialogBox.setCancelable(false).setNegativeButton(
                        "Cancel"
                    ) { dialog: DialogInterface, which: Int ->
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
}