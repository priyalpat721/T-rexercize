package edu.neu.madcourse.trexercize.ui.fragments.exercise

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ExerciseViewHolder(itemView: View, listener: EachExerciseCardListener) : RecyclerView.ViewHolder(itemView) {
    var title: TextView = itemView.findViewById(R.id.body_part_title)
    var image: ImageView = itemView.findViewById(R.id.body_part)

    init {
        itemView.setOnClickListener {
            val position = layoutPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }
}