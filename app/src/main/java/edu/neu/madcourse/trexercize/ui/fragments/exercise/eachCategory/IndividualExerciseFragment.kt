package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R

class IndividualExerciseFragment : Fragment(R.layout.each_category_screen) {
    private val exerciseList: MutableList<IndividualExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var exerciseAdapter: IndividualExerciseAdapter? = null
    private lateinit var title: TextView
    private lateinit var equipmentSelected: TextView
    private val args : IndividualExerciseFragmentArgs by navArgs()
    private var db = Firebase.database.reference
    private var equipmentList: Array<String>? =  null
    private lateinit var noExercises: ConstraintLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noExercises = view.findViewById(R.id.no_exercises_constraint)
        noExercises.visibility = GONE
        recyclerView = view.findViewById(R.id.each_category_exercises)
        title = view.findViewById(R.id.category_name)
        equipmentSelected = view.findViewById(R.id.equipment_selected)
        title.text = args.title
        equipmentList = args.equipmentList
        if(equipmentList!!.isEmpty()) {
            equipmentSelected.text = "Showing all exercises"
        } else {

            equipmentSelected.text = "Showing exercises for the following equipment: \n\n" + equipmentList.contentToString()

        }

        val backBtn = view.findViewById<ImageButton>(R.id.back_to_categories)
        backBtn.setOnClickListener {
            val action: NavDirections = IndividualExerciseFragmentDirections.actionIndividualExerciseFragmentToExerciseFragment(
            )
            view.findNavController().navigate(action)
        }

        setUpResources()
        listenForChanges()
    }

    private fun setUpResources(){

        val listener: IndividualExerciseListener = object : IndividualExerciseListener {
            override fun onItemClick(position: Int) {
                val exercise: IndividualExerciseCard = exerciseList[position]
                val exerciseName = exercise.exerciseName
                Toast.makeText(context, "This is the exercise name: $exerciseName", Toast.LENGTH_SHORT).show()

                val action: NavDirections
                action = IndividualExerciseFragmentDirections.actionIndividualExerciseFragmentToEachExerciseFragment()

                action.also {
                    if (exerciseName != null) {
                        action.exerciseName = exerciseName
                        action.exerciseCategory = title.text.toString()
                    }
                }

                view?.findNavController()?.navigate(action)
            }
        }
        exerciseAdapter = this.context?.let { IndividualExerciseAdapter(exerciseList, it, listener) }
        exerciseAdapter?.setEachOnClickListener(listener)
        recyclerView!!.adapter = exerciseAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenForChanges() {
        exerciseList.clear()
        db.child(title.text.toString().lowercase()).addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                exerciseList.clear()
                for (snap in snapshot.children) {
                    val exerciseName = snap.key
                    println(exerciseName)
                    snap.key?.let { db.child(title.text.toString().lowercase()).child(it).addValueEventListener(object : ValueEventListener {
                        @SuppressLint("SetTextI18n")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children) {
                                if(snap.key == "equipment") {
                                    val equipmentArray = snap.value as ArrayList<String>
                                    val equipment = equipmentArray[0]
                                    println(equipment)
                                    if(equipmentList?.isEmpty() == true) {
                                        exerciseList.add(IndividualExerciseCard(exerciseName))
//                                        noExercises = view!!.findViewById(R.id.no_exercises_constraint)
//                                        noExercises.visibility = GONE

                                        exerciseAdapter?.notifyDataSetChanged()
                                    }
                                     else if(equipmentList?.isNotEmpty() == true && equipmentList!!.contains(equipment)) {
                                        exerciseList.add(IndividualExerciseCard(exerciseName))
//                                        noExercises = view!!.findViewById(R.id.no_exercises_constraint)
//                                        noExercises.visibility = GONE
                                        exerciseAdapter?.notifyDataSetChanged()
                                    }

                                }
                            }

                            println("is it empty: "+ exerciseList.isEmpty())
                            if(exerciseList.isEmpty()) {
                                println("is it coming here")
                                noExercises.visibility = VISIBLE
                            } else {
                                noExercises.visibility = GONE
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            // not implemented
                        }
                    }) }

                    // println(value)
//                    exerciseList.add(IndividualExerciseCard(exerciseName))
                }
//                exerciseAdapter?.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                // not implemented
            }
        })

    }
}