package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ExerciseTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
    var exerciseMuscleGroups: TextView = itemView.findViewById(R.id.exerciseGroup)
    var button: Button = itemView.findViewById(R.id.exerciseBack)
}