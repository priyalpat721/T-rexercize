package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import android.widget.ImageButton
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import edu.neu.madcourse.trexercize.R

class GoalFragment : Fragment(R.layout.fragment_goal) {
   private lateinit var backButton : ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton = view.findViewById(R.id.back_btn2)

        backButton.setOnClickListener {
            val action: NavDirections = GoalFragmentDirections.actionGoalFragmentToProfileFragment()
            view.findNavController().navigate(action)
        }
    }
}