package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.calendar.CalendarFragmentDirections

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val profileList: ArrayList<ProfileCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var adapter: ProfileAdapter? = null
    private var db = Firebase.database.reference
    private var label: TextView? = null
    private var value:TextView? = null
    private lateinit var editButton : Button;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.profile_recycler_view)
        label = view.findViewById(R.id.label)
        value = view.findViewById(R.id.value)

        setUpResources()

        setupData()

        editButton = view.findViewById(R.id.edit_btn)
        editButton.setOnClickListener {
            val action: NavDirections = ProfileFragmentDirections.actionProfileFragmentToEditFragment()
            view.findNavController().navigate(action)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupData() {
        profileList.clear()
        profileList.add(ProfileCard("Age", 0.toString()))
        profileList.add(ProfileCard("Height", 0.toString()))
        profileList.add(ProfileCard("Weight", "${0} lbs"))
        profileList.add(ProfileCard("BMI", 0.toString()))
        profileList.add(ProfileCard("Goals", "Goals"))
        profileList.add(ProfileCard("Target Areas", "All"))
        profileList.add(ProfileCard("Longest Streak", 0.toString()))
        adapter?.notifyDataSetChanged()
    }

    private fun setUpResources(){
        adapter = ProfileAdapter(profileList, this.requireContext())
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}