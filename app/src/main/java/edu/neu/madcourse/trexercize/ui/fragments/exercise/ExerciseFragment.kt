package edu.neu.madcourse.trexercize.ui.fragments.exercise

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

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

        val listener: EachExerciseCardListener = object : EachExerciseCardListener {
            override fun onItemClick(position: Int) {
                val category: ExerciseCard = exerciseList[position]
                val title = category.title
                Toast.makeText(context, "This is the category: $title", Toast.LENGTH_SHORT).show()
            }
        }
        exerciseAdapter = this.context?.let { ExerciseAdapter(exerciseList, it, listener) }
        exerciseAdapter?.setEachOnClickListener(listener)
        recyclerView!!.adapter = exerciseAdapter
        recyclerView!!.layoutManager = GridLayoutManager(context, 2)

    }
}