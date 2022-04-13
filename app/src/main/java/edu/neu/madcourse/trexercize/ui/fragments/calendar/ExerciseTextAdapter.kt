package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ExerciseTextAdapter(
    var context: Context

 ) : RecyclerView.Adapter<ExerciseTextViewHolder>() {
    private var dataList = emptyList<ExerciseTextCard>()
    internal fun setDataList(dataList:List<ExerciseTextCard>) {
        this.dataList = dataList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseTextViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.exercise_text_card_layout, parent, false)
        return ExerciseTextViewHolder(view)
    }
    override fun onBindViewHolder(holder: ExerciseTextViewHolder, position: Int) {
        holder.exerciseName.text = dataList[position].exerciseName.toString()
        holder.exerciseMuscleGroups.text = dataList[position].exerciseMuscleGroups.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

 }