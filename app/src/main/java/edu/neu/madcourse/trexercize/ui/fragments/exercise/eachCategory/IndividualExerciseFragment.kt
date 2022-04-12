package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
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
    private val args : IndividualExerciseFragmentArgs by navArgs()
    private var db = Firebase.database.reference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.each_category_exercises)
        title = view.findViewById(R.id.category_name)
        title.text = args.title

        setUpResources()
        //setUpData()
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
                exerciseName.also {
                    if (it != null) {
                        action.exerciseName = it
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

//    @SuppressLint("NotifyDataSetChanged")
//    private fun setUpData(){
//
//
//        exerciseList.clear()
//        exerciseList.add(IndividualExerciseCard("Dummy Exercise Name"))
//        exerciseList.add(IndividualExerciseCard("Dummy Exercise Name"))
//        exerciseList.add(IndividualExerciseCard("Dummy Exercise Name"))
//        exerciseList.add(IndividualExerciseCard("Dummy Exercise Name"))
//        exerciseList.add(IndividualExerciseCard("Dummy Exercise Name"))
//        exerciseAdapter?.notifyDataSetChanged()
//
//    }

    @SuppressLint("NotifyDataSetChanged")
    private fun listenForChanges() {
        exerciseList.clear()
        println(title.text)
        db.child(title.text.toString().lowercase()).addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                exerciseList.clear()
                for (snap in snapshot.children) {
                    exerciseList.add(IndividualExerciseCard(snap.key))
                    println(snap.key)
                }
                exerciseAdapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // not implemented
            }
        })
    }
}