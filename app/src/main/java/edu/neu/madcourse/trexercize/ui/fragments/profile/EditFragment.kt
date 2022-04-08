package edu.neu.madcourse.trexercize.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.firebase.auth.ktx.auth
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

    /**
     * Gets information from the edit texts and populates the current user's information
     * once done is hit. Both buttons take the users back to the profile page
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                    .child("height").child("feet").setValue(feet.text.toString())
                }
            }
            if (!inches.text.isNullOrEmpty()) {
                Firebase.auth.currentUser?.uid?.let { it1 -> db.child("users").child(it1)
                    .child("height").child("inches").setValue(inches.text.toString())
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

    }
}