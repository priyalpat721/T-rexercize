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
        // recycler view for the Exercise Fragment page. It will display everything in grid view
        recyclerView = view.findViewById(R.id.exercise_recycler_view)

        // Button to launch the equipment dialogue to select available equipment
        equipmentButton = view.findViewById(R.id.select_equipment)

        // The list of equipment the app supports currently
        EQUIPMENT_ARRAY = arrayOf("dumbbell", "barbell", "yoga mat", "ab wheel", "resistance bands", "cable", "pull up bar", "none")

        // on click listener for equipment button
        // when the button is pressed, the code in this listener is executed
        equipmentButton?.setOnClickListener{

            //clears the equipment list every time this fragment is loaded onto the screen
            // in other words, the equipment selection is reset
            selectedIndices.clear()
            equipmentList.clear()

            // Building an alert dialogue for the selecting equipment. A user can select multiple
            // equipment from the given list which will then filter out the exercises in the next
            // fragment. Android documentation was used to implement this dialogue.
            val builder = AlertDialog.Builder(context)
            // Sets the title of the dialogue
            builder.setTitle("Select your equipment")

                //takes in a equipment list array, what items are already checked initially,
                // and a listener that updates the indices of selected equipment.
                .setMultiChoiceItems(EQUIPMENT_ARRAY, null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->
                        if (isChecked) {
                            // If the user checked the item, add it to the selectedIndices
                            selectedIndices.add(which)
                            // if the user checked the item, add it to the equiupmentList
                            // equipmentList is a string arraylist that keeps track of the string
                            // at the indices of selectedIndices[]
                            equipmentList.add(EQUIPMENT_ARRAY!![which])
                        } else if (selectedIndices.contains(which)) {

                            var targetIndex = selectedIndices.indexOf(which)

                            // Else, if the item is already in the array, we remove it
                            selectedIndices.removeAt(targetIndex)

                            equipmentList.remove(EQUIPMENT_ARRAY!![which])
                        }
                    })
                // positive and negative buttons
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, id ->
                        // When user clicks OK, we save the selectedIndices results
                        // in the equipmentList array and pass that to the next fragment
                        // when a category is clicked.
                        Toast.makeText(context, "# of equipment: ${selectedIndices.size}", Toast.LENGTH_SHORT).show()
                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->

                        // when the user clicks on cancel, we clear both the lists
                        // to reset the selection so exercises don't get filtered.
                        selectedIndices.clear()
                        equipmentList.clear()
                    })
            builder.create().show()
        }

        setUpResources()
        setUpData()
    }

    // This method is responsible for housing the onClick listener of each card of the recycler view
    // and launching the next fragment when an exercise category is clicked.
    private fun setUpResources(){

        val listener: EachExerciseCardListener = object : EachExerciseCardListener {
            override fun onItemClick(position: Int) {
                val category: ExerciseCard = exerciseList[position]
                val title = category.title

                // object of type NavDirections. we will pass in the equipment List so
                // only the exercises that use that equipment are displayed and the others
                // are filtered out.
                val action: NavDirections
                action = ExerciseFragmentDirections.actionExerciseFragmentToIndividualExerciseFragment(
                    equipmentList.toTypedArray()
                )

                // sending the "title"/category into the action object's arguments so it can be
                // used to query the correct collection.
                action.also {
                    if (title != null) {
                        action.title = title
                    }
                    action.equipmentList = equipmentList.toTypedArray()
                }
                // launches the next fragment which is IndividualExerciseFragment
                view?.findNavController()?.navigate(action)

            }
        }
        exerciseAdapter = this.context?.let { ExerciseAdapter(exerciseList, it, listener) }
        exerciseAdapter?.setEachOnClickListener(listener)
        recyclerView!!.adapter = exerciseAdapter
        recyclerView!!.layoutManager = GridLayoutManager(context, 2)

    }

    // This method is responsible for initializing the recyclerview that uses a Grid Layout
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