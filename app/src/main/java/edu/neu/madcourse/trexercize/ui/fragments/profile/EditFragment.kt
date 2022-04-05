package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R

class EditFragment : Fragment(R.layout.fragment_edit) {
    private lateinit var age : EditText
    private lateinit var feet : EditText
    private lateinit var inches : EditText
    private lateinit var weight : EditText
    private lateinit var goals : EditText
    private lateinit var targetAreas : EditText
    private lateinit var name: EditText
    private lateinit var gym : EditText
    private lateinit var doneButton : Button
    private lateinit var cancelButton: Button
    private var db = Firebase.database.reference
    private lateinit var profile : ImageView
    private lateinit var changePicture : Button

    /**
     * Gets information from the edit texts and populates the current user's information
     * once done is hit. Both buttons take the users back to the profile page
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenForChanges()
        doneButton = view.findViewById(R.id.done_btn)
        cancelButton = view.findViewById(R.id.cancel_btn)
        name = view.findViewById(R.id.name)
        gym = view.findViewById(R.id.gym_name)
        age = view.findViewById(R.id.age)
        feet = view.findViewById(R.id.feet)
        inches = view.findViewById(R.id.inches)
        goals = view.findViewById(R.id.goals)
        targetAreas = view.findViewById(R.id.target_areas)
        weight = view.findViewById(R.id.weight)
        profile = view.findViewById(R.id.profile_image)
        changePicture = view.findViewById(R.id.change_profile_pic)

        doneButton.setOnClickListener {
            if (!name.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("name").setValue(name.text.toString())
                }
            }
            if (!gym.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("gym").setValue(gym.text.toString())
                }
            }
            if (!age.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("age").setValue(age.text.toString())
                }
            }
            if (!feet.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("feet").setValue(feet.text.toString())
                }
            }
            if (!inches.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("inches").setValue(inches.text.toString())
                }
            }
            if (!weight.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("weight").setValue(weight.text.toString())
                }
            }
            if (!goals.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("goals").setValue(goals.text.toString())
                }
            }
            if (!targetAreas.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("targetAreas").setValue(targetAreas.text.toString())
                }
            }
            val action: NavDirections = EditFragmentDirections.actionEditFragmentToProfileFragment()
            view.findNavController().navigate(action)
        }

        cancelButton.setOnClickListener {
            val action: NavDirections = EditFragmentDirections.actionEditFragmentToProfileFragment()
            view.findNavController().navigate(action)
        }

        changePicture.setOnClickListener {
            Toast.makeText(context, "Change Picture!", Toast.LENGTH_LONG).show()
        }
    }
    private fun listenForChanges() {
        db.child("users").addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    if (snap.key == Firebase.auth.currentUser?.uid.toString()) {
                        val userInfo = snap.value as Map<String, String>
                        context?.let {
                            Glide.with(it).load(userInfo["profilePicture"]).into(profile)
                        }
                        name.setText(userInfo["name"])
                        gym.setText(userInfo["gym"])

                        val ins = userInfo["inches"]
                        val ft = userInfo["feet"]

                        inches.setText(ins)
                        feet.setText(ft)
                        age.setText(userInfo["age"])
                        weight.setText(userInfo["weight"])
                        goals.setText(userInfo["goals"])
                        targetAreas.setText(userInfo["targetAreas"])
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Not implemented
            }
        })
    }
}