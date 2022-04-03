package edu.neu.madcourse.trexercize.ui.fragments.exercise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.profile.ProfileCard

class ExerciseFragment : Fragment(R.layout.fragment_exercise) {
    // TODO: Rename and change types of parameters
    private val exerciseList: MutableList<ExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var exerciseAdapter: ExerciseAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.exercise_recycler_view)

        setUpResources()
        setUpData()
    }

    private fun setUpData(){
        exerciseList.add(ExerciseCard("Chest", R.drawable.chest))
        exerciseList.add(ExerciseCard("Arms", R.drawable.arms))
        exerciseList.add(ExerciseCard("Back", R.drawable.back))
        exerciseList.add(ExerciseCard("Abs", R.drawable.abs))
        exerciseList.add(ExerciseCard("Legs", R.drawable.legs))
        exerciseList.add(ExerciseCard("Full Body", R.drawable.full_body))
        exerciseList.add(ExerciseCard("HIIT", R.drawable.dumbell))
        exerciseList.add(ExerciseCard("Cardio", R.drawable.dumbell))
        exerciseAdapter?.notifyDataSetChanged()

    }
    private fun setUpResources(){
        exerciseAdapter = this.context?.let { ExerciseAdapter(exerciseList, it) }
        recyclerView!!.adapter = exerciseAdapter
        recyclerView!!.layoutManager = GridLayoutManager(context, 2)
    }
}