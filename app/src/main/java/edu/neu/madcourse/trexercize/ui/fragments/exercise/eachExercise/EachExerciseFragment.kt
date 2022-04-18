package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachExercise

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.fragment.app.Fragment
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
    private lateinit var exerciseDescriptionText: TextView
    private lateinit var exerciseEquipment: TextView
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
        exerciseDescriptionText = view.findViewById(R.id.exercise_description)
        addToFavorites = view.findViewById(R.id.add_to_favorites_btn)
        addToTodayWorkout = view.findViewById(R.id.add_to_today_workout)
        exerciseEquipment = view.findViewById(R.id.exercise_equipment)

//        println(addToFavorites.text.toString())

        exerciseTitle.text = args.exerciseName
        exerciseMuscleGroup.text = "Muscle Group: Sample muscle group"
        exerciseCategory = args.exerciseCategory

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
                                Toast.makeText(context, "Workout added!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }
        }

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

    private fun setUpResources(){

        eachExerciseAdapter = this.context?.let { EachExerciseAdapter(exerciseDescription, it) }
        recyclerView!!.adapter = eachExerciseAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData(){

        db.child(exerciseCategory.lowercase()).child(exerciseTitle.text.toString()).addValueEventListener(object :
            ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {

                    if(snap.key == "video") {
                        videoLink = snap.value.toString()
                        exerciseVideo.webViewClient = WebViewClient()
                        exerciseVideo.apply{
//                            println("This is the video link: $videoLink")
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
                        exerciseMuscleGroup.text = "Muscle Group: " + snap.value.toString()
                        currentExercise["muscle groups"] = snap.value.toString()
                    }
                    if(snap.key == "equipment") {
                        val equipmentList = snap.value as ArrayList<String>
                        val equipment = equipmentList[0]
                        exerciseEquipment.text = "Equipment needed: $equipment"
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