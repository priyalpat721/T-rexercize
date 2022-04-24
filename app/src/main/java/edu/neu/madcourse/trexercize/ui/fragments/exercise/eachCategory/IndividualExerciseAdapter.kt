package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.EachExerciseCardListener
import edu.neu.madcourse.trexercize.ui.fragments.exercise.ExerciseViewHolder

class IndividualExerciseAdapter(
    private var eachCategoryExercises: MutableList<IndividualExerciseCard>,
    private var context: Context,
    private var listener: IndividualExerciseListener) : RecyclerView.Adapter<IndividualExerciseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IndividualExerciseViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.each_exercise_list_card_layout, parent, false)
        return IndividualExerciseViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: IndividualExerciseViewHolder, position: Int) {
        holder.exerciseName.text = eachCategoryExercises[position].exerciseName.toString()
        if (position % 2 == 0) {
            holder.background.setCardBackgroundColor(Color.rgb(117,197,195))
        }
    }

    override fun getItemCount(): Int {
        return eachCategoryExercises.size
    }

    fun setEachOnClickListener(listener: IndividualExerciseListener) {
        this.listener = listener
    }

}