package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachExercise

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory.IndividualExerciseFragmentArgs

class EachExerciseAdapter(
    private var exerciseDescription: MutableList <EachExerciseCard>,
    private var context: Context
) : RecyclerView.Adapter<EachExerciseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EachExerciseViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.each_exercise_card_layout, parent, false)
        return EachExerciseViewHolder(view)
    }

    override fun onBindViewHolder(holder: EachExerciseViewHolder, position: Int) {
        holder.description.text = exerciseDescription[position].description.toString()
    }

    override fun getItemCount(): Int {
        return exerciseDescription.size
    }
}