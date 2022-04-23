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
    private lateinit var showGoals : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.profile_recycler_view)
        label = view.findViewById(R.id.label)
        value = view.findViewById(R.id.value)
        name = view.findViewById(R.id.name)
        gym = view.findViewById(R.id.gym_name)
        profile = view.findViewById(R.id.profile_image)
        showGoals = view.findViewById(R.id.show_goal_btn)
        setUpResources()

        listenForChanges()
        editButton = view.findViewById(R.id.edit_btn)
        editButton.setOnClickListener {
            val action: NavDirections =
                ProfileFragmentDirections.actionProfileFragmentToEditFragment()
            view.findNavController().navigate(action)
        }

        showGoals.setOnClickListener {
            val action: NavDirections =
                ProfileFragmentDirections.actionProfileFragmentToShowGoalsFragment()
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
                        val userInfo = snap.value as Map<*, *>
                        name.text = userInfo["name"] as CharSequence?
                        gym.text = userInfo["gym"] as CharSequence?
                        context?.let { Glide.with(it).load(userInfo["profilePicture"]).into(profile) }
                        val inches = userInfo["inches"] as CharSequence?
                        val feet = userInfo["feet"] as CharSequence?
                        var height = "$feet ft $inches in"
                        var totalInches = 0
                        if (inches.isNullOrEmpty() || feet.isNullOrEmpty()) {
                            height = "0 ft 0 in"
                        }else{
                             totalInches = (Integer.parseInt(feet as String) * 12) + Integer.parseInt(
                                inches as String)
                        }

                        profileList.add(ProfileCard("  Age", userInfo["age"].toString()))
                        profileList.add(ProfileCard(" Height", height))
                        profileList.add(ProfileCard(" Weight", userInfo["weight"].toString() + " lbs"))
                        if (userInfo["weight"].toString() == "") {
                            profileList.add(ProfileCard(" BMI", String.format("%.2f", 0.0)))
                        }
                        else{
                            val bmi = (userInfo["weight"]?.let { Integer.parseInt(it as String).toDouble() }
                                ?.div(totalInches.toDouble()) ?: 0.0) / totalInches.toDouble() * 703
                            profileList.add(ProfileCard(" BMI", String.format("%.2f", bmi)))
                        }

                        if (userInfo["targetAreas"].toString().isEmpty()) {
                            profileList.add(ProfileCard(" Target Areas", "None"))
                        }
                        else{
                            profileList.add(ProfileCard(" Target Areas", userInfo["targetAreas"].toString()))
                        }
                        for (info in userInfo) {
                            if (info.key == "streakInfo") {
                                val streakInfo = info.value as Map<*, *>
                                for (sInfo in streakInfo) {
                                    if (sInfo.key == "longest streak count") {
                                        profileList.add(
                                            ProfileCard(
                                                " Longest Streak",
                                                sInfo.value as String?
                                            )
                                        )
                                    }

                                    if (sInfo.key == "current streak count") {
                                        profileList.add(
                                            ProfileCard(
                                                " Current Streak",
                                                sInfo.value as String?
                                            )
                                        )
                                    }
                                    if (sInfo.key == "last snap date") {
                                        profileList.add(
                                            ProfileCard("Last Snap Date",
                                                sInfo.value as String?
                                            )
                                        )
                                    }
                                }
                            }
                        }
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