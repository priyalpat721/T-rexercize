package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class HomeScreenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var favoriteExercise: TextView = itemView.findViewById(R.id.favorite_exercise_name)
    var exerciseCategory: TextView = itemView.findViewById(R.id.favorite_exercise_category)
    var categoryImage: ImageView = itemView.findViewById(R.id.favorite_category_image)
}