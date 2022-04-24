package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.EachExerciseCardListener

class HomeScreenAdapter(
    private var favoritesList: MutableList<FavoriteExerciseCard>,
    private var context: Context,
    private var listener: EachExerciseCardListener
): RecyclerView.Adapter<HomeScreenViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeScreenViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.favorite_exercise_card, parent, false)
        return HomeScreenViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: HomeScreenViewHolder, position: Int) {
        holder.favoriteExercise.text = favoritesList[position].favoriteExercise.toString()
        holder.exerciseCategory.text = favoritesList[position].exerciseCategory.toString()
        favoritesList[position].categoryImage?.let { holder.categoryImage.setImageResource(it)
            if (position % 2 == 0) {
                holder.background.setBackgroundColor(Color.rgb(92,188,179))
            }}
    }

    override fun getItemCount(): Int {
        return favoritesList.size
    }

    fun setEachOnClickListener(listener: EachExerciseCardListener){
        this.listener = listener
    }
}