package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ShowGoalViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    var goal: TextView = itemView.findViewById(R.id.goal_name2)
    var time: TextView = itemView.findViewById(R.id.time2)
    var done: CheckBox = itemView.findViewById(R.id.done_box2)
    var favorite: CheckBox = itemView.findViewById(R.id.favorites2)
    var button : TextView = itemView.findViewById(R.id.goalBack)
}