package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.EachExerciseCardListener

class IndividualExerciseViewHolder(itemView: View, listener: IndividualExerciseListener) : RecyclerView.ViewHolder(itemView) {
    var exerciseName: TextView = itemView.findViewById(R.id.exercise_name)
    var background: CardView = itemView.findViewById(R.id.each_category_exercise)

    init {
        itemView.setOnClickListener {
            val position = layoutPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }
}