package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class GoalViewHolder(itemView: View, checkBox: ICheckBoxListener?) :
    RecyclerView.ViewHolder(itemView) {

    var checkBoxListener = checkBox
    var task: TextView = itemView.findViewById(R.id.task_name)
    var time: TextView = itemView.findViewById(R.id.time)
    var done: CheckBox = itemView.findViewById(R.id.done_box)
    var favorite: CheckBox = itemView.findViewById(R.id.favorites)

    init{
        favorite.setOnClickListener {
            if (checkBoxListener != null) {
                val position: Int = layoutPosition
                if (position != RecyclerView.NO_POSITION) {
                    checkBoxListener?.onBoxClick(position)
                }
            }
            done.setOnClickListener {
                if (checkBoxListener != null) {
                    val position: Int = layoutPosition
                    if (position != RecyclerView.NO_POSITION) {
                        checkBoxListener?.onBoxClick(position)
                    }
                }
            }
        }
    }
}