package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachExercise

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class EachExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var description : TextView = itemView.findViewById(R.id.each_exercise)
}