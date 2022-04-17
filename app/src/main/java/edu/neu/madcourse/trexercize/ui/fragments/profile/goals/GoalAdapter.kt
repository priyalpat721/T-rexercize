package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class GoalAdapter(private var goalList: ArrayList<GoalCard>, var context: Context?) : RecyclerView.Adapter<GoalViewHolder>() {
    private var doneCheckBox: IDoneCheckBoxListener? = null
    private var favCheckBox: IFavCheckBoxListener? = null

    fun setDoneCheckBoxListener(doneCheckBoxListener: IDoneCheckBoxListener) {
        this.doneCheckBox = doneCheckBoxListener
    }

    fun setFavCheckBoxListener(favCheckBoxListener: IFavCheckBoxListener) {
        this.favCheckBox = favCheckBoxListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.goal_card_layout, parent, false)
        return GoalViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
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

        if (position % 2 != 0) {
            holder.button.setBackgroundResource(R.drawable.alternate_goal_border)
        } else {
            holder.button.setBackgroundResource(R.drawable.goal_border)
        }

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