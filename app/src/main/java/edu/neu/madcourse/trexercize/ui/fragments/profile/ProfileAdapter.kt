package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ProfileAdapter(
    private val profileList: ArrayList<ProfileCard> = ArrayList<ProfileCard>(),
    private val context: Context

) : RecyclerView.Adapter<ProfileViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.profile_card_layout, parent, false)
        return ProfileViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        holder.label.text = profileList[position].label.toString()
        holder.value.text = profileList[position].value.toString()
    }

}