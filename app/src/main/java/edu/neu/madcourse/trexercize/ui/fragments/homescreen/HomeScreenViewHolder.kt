package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.EachExerciseCardListener

class HomeScreenViewHolder(itemView: View, listener: EachExerciseCardListener) : RecyclerView.ViewHolder(itemView) {
    var favoriteExercise: TextView = itemView.findViewById(R.id.favorite_exercise_name)
    var exerciseCategory: TextView = itemView.findViewById(R.id.favorite_exercise_category)
    var categoryImage: ImageView = itemView.findViewById(R.id.favorite_category_image)

    init {
        itemView.setOnClickListener {
            val position = layoutPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }

    }
}