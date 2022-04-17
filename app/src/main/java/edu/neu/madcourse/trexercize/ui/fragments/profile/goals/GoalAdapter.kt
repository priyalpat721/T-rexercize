package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class GoalAdapter(private var goalList: ArrayList<GoalCard>, var context: Context?) : RecyclerView.Adapter<GoalViewHolder>() {
    var doneCheckBox : IDoneCheckBoxListener? = null
    var favCheckBox : IFavCheckBoxListener? = null

    fun setDoneCheckBoxListener(doneCheckBoxListener : IDoneCheckBoxListener) {
        this.doneCheckBox = doneCheckBoxListener
    }

    fun setFavCheckBoxListener(favCheckBoxListener : IFavCheckBoxListener) {
        this.favCheckBox = favCheckBoxListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.goal_card_layout, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        if (holder.done.isChecked) {
            holder.task.text = goalList[position].goal
            holder.task.paintFlags =
                holder.task.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        else {
            holder.task.text = goalList[position].goal
        }
        holder.time.text = goalList[position].time
        holder.favorite.isChecked = goalList[position].favorite
        holder.done.isChecked = goalList[position].done

        holder.favorite.setOnClickListener {
            if (favCheckBox != null) {
                if (position != RecyclerView.NO_POSITION) {
                    favCheckBox?.onFavBoxClick(position)
                }
            }
        }
        holder.done.setOnClickListener {
            if (doneCheckBox != null) {
                if (position != RecyclerView.NO_POSITION) {
                    doneCheckBox?.onDoneBoxClick(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return goalList.size
    }
}