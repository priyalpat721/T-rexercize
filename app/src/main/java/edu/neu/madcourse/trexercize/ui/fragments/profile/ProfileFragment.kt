package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val profileList: ArrayList<ProfileCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    var adapter: ProfileAdapter? = null
    private var db = Firebase.database.reference
    private var label: TextView? = null
    private var value: TextView? = null
    private lateinit var editButton: Button
    private lateinit var name : TextView
    private lateinit var gym : TextView
    private lateinit var profile : ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.profile_recycler_view)
        label = view.findViewById(R.id.label)
        value = view.findViewById(R.id.value)
        name = view.findViewById(R.id.name)
        gym = view.findViewById(R.id.gym_name)
        profile = view.findViewById(R.id.profile_image)
        setUpResources()

        listenForChanges()
        editButton = view.findViewById(R.id.edit_btn)
        editButton.setOnClickListener {
            val action: NavDirections =
                ProfileFragmentDirections.actionProfileFragmentToEditFragment()
            view.findNavController().navigate(action)
        }
    }


    private fun listenForChanges() {
        db.child("users").addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                profileList.clear()
                for (snap in snapshot.children) {
                    if (snap.key == Firebase.auth.currentUser?.uid.toString()) {
                        val userInfo = snap.value as Map<String, String>
                        name.text = userInfo["name"]
                        gym.text = userInfo["gym"]
                        context?.let { Glide.with(it).load(userInfo["profilePicture"]).into(profile) }
                        val inches = userInfo["inches"]
                        val feet = userInfo["feet"]
                        var height = "${feet}ft ${inches}in"
                        var bmi = 0.0
                        if (inches.isNullOrEmpty() || feet.isNullOrEmpty()) {
                            bmi = 0.0
                            height = "0ft 0in"
                        }else{
                            val totalInches = (Integer.parseInt(feet) * 12) + Integer.parseInt(inches)
                            bmi = (userInfo["weight"]?.let { Integer.parseInt(it).toDouble() }
                                ?.div(totalInches.toDouble()) ?: 0.0) / totalInches.toDouble() * 703
                        }

                        profileList.add(ProfileCard("Age", userInfo["age"].toString()))
                        profileList.add(ProfileCard("Height", height))
                        profileList.add(ProfileCard("Weight", userInfo["weight"].toString()))
                        profileList.add(ProfileCard("BMI", String.format("%.2f", bmi)))
                        if (userInfo["goals"].isNullOrEmpty()) {
                            profileList.add(ProfileCard("Goals", ""))
                        }
                        else {
                            profileList.add(ProfileCard("Goals", userInfo["goals"].toString()))
                        }

                        if (userInfo["targetAreas"].isNullOrEmpty()) {
                            profileList.add(ProfileCard("Target Areas", ""))
                        }
                        else{
                            profileList.add(ProfileCard("Target Areas", userInfo["targetAreas"].toString()))
                        }
                        profileList.add(ProfileCard("Longest Streak", userInfo["streak"].toString()))
                        adapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //Not implemented
            }
        })
    }

    private fun setUpResources() {
        adapter = ProfileAdapter(profileList, this.requireContext())
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)
    }
}