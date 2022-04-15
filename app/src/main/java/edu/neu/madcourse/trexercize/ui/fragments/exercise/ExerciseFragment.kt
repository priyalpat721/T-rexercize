package edu.neu.madcourse.trexercize.ui.fragments.exercise

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class ExerciseFragment : Fragment(R.layout.fragment_exercise) {
    private val exerciseList: MutableList<ExerciseCard> = ArrayList()
    private val selectedIndices: MutableList<Int> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var exerciseAdapter: ExerciseAdapter? = null
    private var equipmentButton: Button? = null
    private var EQUIPMENT_ARRAY: Array<String>? =  null
    private var equipmentList: MutableList<String>  = ArrayList()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.exercise_recycler_view)
        equipmentButton = view.findViewById(R.id.select_equipment)
        EQUIPMENT_ARRAY = arrayOf("dumbbell", "barbell", "yoga mat", "ab wheel", "resistance bands", "cable", "pull up bar")

        equipmentButton?.setOnClickListener{

            //clears the equipment list every time this fragment is loaded onto the screen
            // in other words, the equipment selection is reset
            selectedIndices.clear()
            equipmentList.clear()
            val builder = AlertDialog.Builder(context)
            // Set the dialog title
            builder.setTitle("Select your equipment")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(EQUIPMENT_ARRAY, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        if (isChecked) {
                            println("in the if block $which")
                            // If the user checked the item, add it to the selected items
                            selectedIndices.add(which)
                            equipmentList.add(EQUIPMENT_ARRAY!![which])
                        } else if (selectedIndices.contains(which)) {
                            // Else, if the item is already in the array, remove it
                                println("in the else block $which")
                            selectedIndices.removeAt(which)
                            equipmentList.remove(EQUIPMENT_ARRAY!![which])
                        }
                    })
                // Set the action buttons
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User clicked OK, so save the selectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Toast.makeText(context, "# of equipment: ${selectedIndices.size}", Toast.LENGTH_SHORT).show()
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
                println(equipmentList.toTypedArray().contentToString())
                action = ExerciseFragmentDirections.actionExerciseFragmentToIndividualExerciseFragment(
                    equipmentList.toTypedArray()
                )

                action.also {
                    if (title != null) {
                        action.title = title
                    }
                    action.equipmentList = equipmentList.toTypedArray()
                }
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

}