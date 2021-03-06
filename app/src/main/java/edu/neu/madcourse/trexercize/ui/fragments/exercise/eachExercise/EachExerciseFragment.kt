package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachExercise

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
import edu.neu.madcourse.trexercize.ui.fragments.calendar.DayFragmentDirections
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class EachExerciseFragment : Fragment(R.layout.each_exercise_layout) {

    private val exerciseDescription: MutableList<EachExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var eachExerciseAdapter: EachExerciseAdapter?  = null
    private lateinit var exerciseTitle: TextView
    private lateinit var exerciseCategory: String
    private lateinit var exerciseVideo: WebView
    private var videoLink: String? = null
    private var currentCalendarId: String? = null
    private lateinit var exerciseMuscleGroup: TextView
    private lateinit var actualMuscleGroup: TextView
    private lateinit var exerciseDescriptionText: TextView
    private lateinit var exerciseEquipment: TextView
    private lateinit var actualEquipment: TextView
    private lateinit var addToFavorites: Button
    private lateinit var addToTodayWorkout: Button
    private var db = Firebase.database.reference
    private val args : EachExerciseFragmentArgs by navArgs()
    private val currentExercise = hashMapOf<String, Any?>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.exercise_description_recycler_view)
        exerciseVideo = view.findViewById(R.id.exercise_video)
        exerciseTitle = view.findViewById(R.id.exercise_title)
        exerciseMuscleGroup = view.findViewById(R.id.exercise_muscle_group)
        actualMuscleGroup = view.findViewById(R.id.actual_muscle)
        exerciseDescriptionText = view.findViewById(R.id.exercise_description)
        addToFavorites = view.findViewById(R.id.add_to_favorites_btn)
        addToTodayWorkout = view.findViewById(R.id.add_to_today_workout)
        exerciseEquipment = view.findViewById(R.id.exercise_equipment)
        actualEquipment = view.findViewById(R.id.actual_equipment_text_view)


        // gets the exercise name, and category from the action object's arguments
        exerciseTitle.text = args.exerciseName
        exerciseMuscleGroup.text = "Muscle Group:"
        exerciseCategory = args.exerciseCategory
        exerciseEquipment.text = "Equipment needed"

        // back button takes the user back to the exericse list.
        val backBtn = view.findViewById<ImageButton>(R.id.back_to_exercise_list)
        backBtn.setOnClickListener {
//            val action: NavDirections = EachExerciseFragmentDirections.actionEachExerciseFragmentToIndividualExerciseFragment3(
//                equipmentList as Array<out String>
//            )
//
//            view.findNavController().navigate(action)
            findNavController().popBackStack()
        }

        // checks if the current exercise already exists in the user's favorites list and changes
        // the button text accordingly
        if(addToFavorites.text.toString() == "Add to Favorites") {
            Firebase.auth.uid?.let {
                db.child("users").child(it).child("favorites").addValueEventListener(object :
                    ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children) {

                            if(snap.key == exerciseTitle.text.toString()){

                                addToFavorites.text = "Already in Favorites"
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

        // adds the current exercise to the current day for the user.
        addToTodayWorkout.setOnClickListener {
            Firebase.auth.uid?.let { it1->
                db.child("users").child(it1).addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(snap in snapshot.children){
                            if(snap.key == "calendar") {
                                currentCalendarId = snap.value.toString()
                                var date = SimpleDateFormat("M-d-yyyy", Locale.getDefault()).format(
                                    Date()
                                )
                                db.child("calendars").child(currentCalendarId.toString()).child(date).child("workout").child(exerciseTitle.text.toString()).setValue(currentExercise)
                                db.child("calendars").child(currentCalendarId.toString()).child(date).child("rest day").setValue("false")
                                addToTodayWorkout.text = "Added!"

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

        // This button adds the current exercise to the favorites. If the exercise is already in
        // favorites list, then it displays a toast which doesn't let the user add it again
        addToFavorites.setOnClickListener {
            if(addToFavorites.text.toString() == "Already in Favorites") {
                Toast.makeText(context, "This exercise already exists in your favorites", Toast.LENGTH_SHORT).show()

            } else {
                Firebase.auth.uid?.let { it1 -> db.child("users").child(it1).child("favorites").child(exerciseTitle.text.toString()).setValue(currentExercise) }
            }
        }

        setUpResources()
        listenForChanges()
        setUpData()

    }

    // This method is responsible for setting up the recycler view and its adapter
    private fun setUpResources(){

        eachExerciseAdapter = this.context?.let { EachExerciseAdapter(exerciseDescription, it) }
        recyclerView!!.adapter = eachExerciseAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    // This method is responsible for querying the DB to populate the fragment with video info
    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData(){

        db.child(exerciseCategory.lowercase()).child(exerciseTitle.text.toString()).addValueEventListener(object :
            ValueEventListener {
            @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {

                    // initializes a web view with the proper video
                    if(snap.key == "video") {
                        videoLink = snap.value.toString()
                        exerciseVideo.webViewClient = WebViewClient()
                        exerciseVideo.apply{

                            videoLink?.let { loadUrl(it) }
                            settings.javaScriptEnabled = true
                            settings.safeBrowsingEnabled = true

                        }

                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // not implemented
            }
        })

    }

    // This method is responsible for querying the DB with the relevant information to populate the
    // the recycler view with the description.
    @SuppressLint("NotifyDataSetChanged")
    private fun listenForChanges() {
        exerciseDescription.clear()
        db.child(exerciseCategory.lowercase()).child(exerciseTitle.text.toString()).addValueEventListener(object :
            ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                exerciseDescription.clear()
                for (snap in snapshot.children) {
//                    println(snap)

                    if(snap.key == "muscle groups") {
                        actualMuscleGroup.text = snap.value.toString()
                        currentExercise["muscle groups"] = snap.value.toString()
                    }
                    if(snap.key == "equipment") {
                        val equipmentList = snap.value as ArrayList<String>
                        val equipment = equipmentList[0]
                        actualEquipment.text = equipment
                        currentExercise["equipment"] = equipmentList
                    }

                    if(snap.key == "description") {
                        var description:String = snap.value as String
                        currentExercise["description"] = description
                        var startIndex = 0
                        var endIndex = 0

                        if(description.indexOf("1", 0) != -1) {

                            startIndex = description.indexOf("1", 0)
                            endIndex = description.indexOf("." , startIndex + 2)
                            if(endIndex != -1) {
                                val pointOne = description.slice(startIndex..endIndex)
                                exerciseDescription.add(EachExerciseCard(pointOne))
                            }
                        }
                        if(description.indexOf("2", 0) != -1) {

                            startIndex = description.indexOf("2", 0)
                            endIndex = description.indexOf("." , startIndex + 2)
                            if(endIndex != -1) {
                                val pointTwo = description.slice(startIndex..endIndex)
                                exerciseDescription.add(EachExerciseCard(pointTwo))
                            }
                        }
                        if(description.indexOf("3", 0) != -1) {

                            startIndex = description.indexOf("3", 0)
                            endIndex = description.indexOf("." , startIndex + 2)
                            if(endIndex != -1) {
                                val pointThree = description.slice(startIndex..endIndex)
                                exerciseDescription.add(EachExerciseCard(pointThree))
                            }
                        }
                        if(description.indexOf("4", 0) != -1) {

                            startIndex = description.indexOf("4", 0)
                            endIndex = description.indexOf("." , startIndex + 2)
                            if(endIndex != -1) {
                                val pointFour = description.slice(startIndex..endIndex)
                                exerciseDescription.add(EachExerciseCard(pointFour))
                            }
                        }

                    }
                    if(snap.key == "video") {
                        currentExercise["video"] = snap.value.toString()
                        videoLink = snap.value.toString()

                    }
                }
//                println(currentExercise)
                eachExerciseAdapter?.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
                // not implemented
            }
        })
    }

}