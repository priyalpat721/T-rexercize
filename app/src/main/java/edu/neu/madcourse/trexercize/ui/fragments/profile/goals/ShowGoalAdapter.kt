package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ShowGoalAdapter(private var goalList: ArrayList<GoalCard>, var context: Context?) : RecyclerView.Adapter<ShowGoalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowGoalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.show_goal_card, parent, false)
        return ShowGoalViewHolder(view)
    }

    override fun getItemCount(): Int {
        return goalList.size
    }

    override fun onBindViewHolder(holder: ShowGoalViewHolder, position: Int) {
        if (goalList[position].done) {
            holder.goal.paintFlags =
                holder.goal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.goal.text = goalList[position].goal
        }
        else {
            holder.goal.paintFlags = 0
            holder.goal.text = goalList[position].goal
        }

        holder.time.text = goalList[position].time
        holder.favorite.isChecked = goalList[position].favorite
        holder.done.isChecked = goalList[position].done
        holder.goal.text = goalList[position].goal

        if (position % 2 != 0) {
            holder.button.setBackgroundResource(R.drawable.alternate_goal_border)
        } else {
            holder.button.setBackgroundResource(R.drawable.goal_border)
        }
    }
}