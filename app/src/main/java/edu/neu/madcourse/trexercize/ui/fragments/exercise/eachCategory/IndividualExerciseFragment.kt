package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.EachExerciseCardListener
import edu.neu.madcourse.trexercize.ui.fragments.exercise.ExerciseAdapter
import edu.neu.madcourse.trexercize.ui.fragments.exercise.ExerciseCard

class IndividualExerciseFragment : Fragment(R.layout.each_category_screen) {
    private val exerciseList: MutableList<IndividualExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var exerciseAdapter: IndividualExerciseAdapter? = null
    private lateinit var title: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.each_category_exercises)
        title = view.findViewById(R.id.category_name)

//        val bundle =  arguments
//        if (bundle != null) {
//            title.text = bundle.getString("title")
//        }

        setUpResources()
        setUpData()
    }

    private fun setUpResources(){

        val listener: IndividualExerciseListener = object : IndividualExerciseListener {
            override fun onItemClick(position: Int) {
//                val exercise: IndividualExerciseCard = exerciseList[position]
//                val exerciseName = exercise.exerciseName
//                Toast.makeText(context, "This is the category: $exerciseName", Toast.LENGTH_SHORT).show()
            }
        }
        exerciseAdapter = this.context?.let { IndividualExerciseAdapter(exerciseList, it, listener) }
        exerciseAdapter?.setEachOnClickListener(listener)
        recyclerView!!.adapter = exerciseAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData(){

        exerciseList.add(IndividualExerciseCard("Name: Dummy Exercise Name"))
        exerciseList.add(IndividualExerciseCard("Name: Dummy Exercise Name"))
        exerciseList.add(IndividualExerciseCard("Name: Dummy Exercise Name"))
        exerciseList.add(IndividualExerciseCard("Name: Dummy Exercise Name"))
        exerciseList.add(IndividualExerciseCard("Name: Dummy Exercise Name"))
        exerciseAdapter?.notifyDataSetChanged()

    }
}