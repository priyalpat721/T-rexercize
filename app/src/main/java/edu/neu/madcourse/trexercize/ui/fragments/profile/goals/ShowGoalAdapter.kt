package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ShowGoalAdapter(private var goalList: ArrayList<GoalCard>, var context: Context?) : RecyclerView.Adapter<GoalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.goal_card_layout, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.time.text = goalList[position].time
        holder.favorite.isChecked = goalList[position].favorite
        holder.done.isChecked = goalList[position].done

    }

    override fun getItemCount(): Int {
        return goalList.size
    }
}