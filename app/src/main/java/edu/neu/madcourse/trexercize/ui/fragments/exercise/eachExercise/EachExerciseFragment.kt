package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachExercise

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class EachExerciseFragment : Fragment(R.layout.each_exercise_layout) {

    private val exerciseDescription: MutableList<EachExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var eachExerciseAdapter: EachExerciseAdapter?  = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}