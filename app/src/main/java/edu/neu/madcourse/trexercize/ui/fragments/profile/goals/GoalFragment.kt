package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class GoalFragment : Fragment(R.layout.fragment_goal) {
    private lateinit var backButton: ImageButton
    private val goalList: ArrayList<GoalCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var adapter: GoalAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.back_btn2)

        backButton.setOnClickListener {
            val action: NavDirections = GoalFragmentDirections.actionGoalFragmentToProfileFragment()
            view.findNavController().navigate(action)
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        setUpResources()
    }

    private fun setUpResources() {
        recyclerView?.setHasFixedSize(true)
        adapter = GoalAdapter(goalList, context)
        val checkBoxListener = object : ICheckBoxListener {
            override fun onBoxClick(position: Int) {
                goalList[position].onBoxClick(position)
                adapter?.notifyItemChanged(position)
            }
        }
        adapter?.setICheckBoxListener(checkBoxListener)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}