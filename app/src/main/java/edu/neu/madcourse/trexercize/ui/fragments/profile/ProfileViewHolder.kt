package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var label : TextView = itemView.findViewById(R.id.label)
    var value : TextView = itemView.findViewById(R.id.value)
}