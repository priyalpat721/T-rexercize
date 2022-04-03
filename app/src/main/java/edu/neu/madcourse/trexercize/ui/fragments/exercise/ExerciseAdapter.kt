package edu.neu.madcourse.trexercize.ui.fragments.exercise

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ExerciseAdapter(
    private var exerciseList: MutableList<ExerciseCard>,
    private var context: Context,
    private var listener: EachExerciseCardListener
) : RecyclerView.Adapter<ExerciseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.exercise_card_layout, parent, false)
        return ExerciseViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        holder.title.text = exerciseList[position].title.toString()
        exerciseList[position].image?.let { holder.image.setImageResource(it) }
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    fun setEachOnClickListener(listener: EachExerciseCardListener) {
        this.listener = listener
    }

}