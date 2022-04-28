package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.profile.ProfileCard

class ExerciseTextAdapter(
    private val exerciseList: MutableList<ExerciseTextCard> = ArrayList<ExerciseTextCard>(),
    var context: Context

) : RecyclerView.Adapter<ExerciseTextViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseTextViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.exercise_text_card_layout, parent, false)
        return ExerciseTextViewHolder(view)
    }
    override fun onBindViewHolder(holder: ExerciseTextViewHolder, position: Int) {
        holder.exerciseName.text = exerciseList[position].exerciseName.toString()
        holder.exerciseMuscleGroups.text = exerciseList[position].exerciseMuscleGroups.toString()
        if (position % 2 == 0) {
            holder.button.setBackgroundColor(Color.rgb(117,197,195))
        }
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

}