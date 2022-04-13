package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class HomeScreenAdapter(private var favoritesList: MutableList<FavoriteExerciseCard>, private var context: Context):
    RecyclerView.Adapter<HomeScreenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeScreenViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.favorite_exercise_card, parent, false)
        return HomeScreenViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeScreenViewHolder, position: Int) {
        holder.favoriteExercise.text = favoritesList[position].favoriteExercise.toString()
        holder.exerciseCategory.text = favoritesList[position].exerciseCategory.toString()
        favoritesList[position].categoryImage?.let { holder.categoryImage.setImageResource(it) }
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

}