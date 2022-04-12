package edu.neu.madcourse.trexercize.ui.fragments.exercise

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory.IndividualExerciseFragment

class ExerciseFragment : Fragment(R.layout.fragment_exercise) {
    // TODO: Rename and change types of parameters
    private val exerciseList: MutableList<ExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var exerciseAdapter: ExerciseAdapter? = null
    private var equipmentButton: Button? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.exercise_recycler_view)
        equipmentButton = view.findViewById(R.id.select_equipment)
        val equipmentArray = arrayOf("Dumbell", "Barbell", "Yoga Mat", "Ab Roller", "Resistance Bands")

        equipmentButton?.setOnClickListener{
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            val builder = AlertDialog.Builder(context)
            // Set the dialog title
            builder.setTitle("Select your equipment")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(equipmentArray, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        if (isChecked) {
                            // If the user checked the item, add it to the selected items
                            selectedItems.add(which)
                        } else if (selectedItems.contains(which)) {
                            // Else, if the item is already in the array, remove it
                            selectedItems.remove(which)
                        }
                    })
                // Set the action buttons
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Toast.makeText(context, "# of equipment: ${selectedItems.size}", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->

                    })

            builder.create().show()


        }

        setUpResources()
        setUpData()
    }

    private fun setUpResources(){

        val listener: EachExerciseCardListener = object : EachExerciseCardListener {
            override fun onItemClick(position: Int) {
                val category: ExerciseCard = exerciseList[position]
                val title = category.title
                Toast.makeText(context, "This is the category: $title", Toast.LENGTH_SHORT).show()

                val action: NavDirections
                action = ExerciseFragmentDirections.actionExerciseFragmentToIndividualExerciseFragment()

                title.also {
                    if (it != null) {
                        action.title = it
                    }
                }

                view?.findNavController()?.navigate(action)

            }
        }
        exerciseAdapter = this.context?.let { ExerciseAdapter(exerciseList, it, listener) }
        exerciseAdapter?.setEachOnClickListener(listener)
        recyclerView!!.adapter = exerciseAdapter
        recyclerView!!.layoutManager = GridLayoutManager(context, 2)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData(){
        exerciseList.clear()
        exerciseList.add(ExerciseCard("Chest", R.drawable.chest))
        exerciseList.add(ExerciseCard("Arms", R.drawable.arms))
        exerciseList.add(ExerciseCard("Back", R.drawable.back))
        exerciseList.add(ExerciseCard("Core", R.drawable.abs))
        exerciseList.add(ExerciseCard("Legs", R.drawable.legs))
        exerciseList.add(ExerciseCard("Full Body", R.drawable.full_body))
        exerciseAdapter?.notifyDataSetChanged()

    }

    // KEEP THIS FOR NOW-- WILL DELETE LATER
//    equipmentButton?.setOnClickListener{
//
//        val builder = AlertDialog.Builder(context)
//        val equipmentArray = arrayOf("Dumbell", "Barbell", "Yoga Mat", "Ab Roller", "Resistance Bands")
//        val selectedItems = booleanArrayOf(false, false, false, false, false)
//        builder.setTitle("Select your equipment")
//            .setMultiChoiceItems(equipmentArray, selectedItems){
//                    dialog, which, isChecked ->
//                selectedItems[which] = isChecked
//            }
//        builder.setPositiveButton("Ok"){ dialog, id->
//
//
//        }
//        builder.setNegativeButton("Cancel"){ dialog, id->
//
//
//        }
//        builder.create().show()
//
//    }

}