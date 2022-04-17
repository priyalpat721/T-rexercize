package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class GoalViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var task: TextView = itemView.findViewById(R.id.task_name)
    var time: TextView = itemView.findViewById(R.id.time)
    var done: CheckBox = itemView.findViewById(R.id.done_box)
    var favorite: CheckBox = itemView.findViewById(R.id.favorites)
}