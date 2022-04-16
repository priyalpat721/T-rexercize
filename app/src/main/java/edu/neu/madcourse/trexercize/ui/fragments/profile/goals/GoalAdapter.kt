package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class GoalAdapter(goalList: ArrayList<GoalCard>, context: Context?) : RecyclerView.Adapter<GoalViewHolder>() {
    var goalList: ArrayList<GoalCard> = goalList
    private var checkBoxListener: ICheckBoxListener? = null
    var context: Context? = context

    fun setICheckBoxListener(checkBoxListener : ICheckBoxListener) {
        this.checkBoxListener = checkBoxListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.task_card_layout, parent, false)
        return GoalViewHolder(view, checkBoxListener)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.task.text = goalList[position].task
        holder.time.text = goalList[position].time
        holder.favorite.isChecked = goalList[position].favorite
        holder.done.isChecked = goalList[position].done
    }

    override fun getItemCount(): Int {
        return goalList.size
    }
}