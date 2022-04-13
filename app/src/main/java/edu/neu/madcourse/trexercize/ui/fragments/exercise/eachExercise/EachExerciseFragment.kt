package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachExercise

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory.IndividualExerciseCard
import edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory.IndividualExerciseFragmentArgs
import kotlin.math.E


class EachExerciseFragment : Fragment(R.layout.each_exercise_layout) {

    private val exerciseDescription: MutableList<EachExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var eachExerciseAdapter: EachExerciseAdapter?  = null
    private lateinit var exerciseTitle: TextView
    private lateinit var exerciseCategory: String
    private lateinit var exerciseVideo: VideoView
    private lateinit var exerciseMuscleGroup: TextView
    private lateinit var exerciseDescriptionText: TextView
    private lateinit var exerciseEquipment: TextView
    private lateinit var addToFavorites: Button
    private lateinit var addToTodayWorkout: Button
    private var db = Firebase.database.reference
    private val args : EachExerciseFragmentArgs by navArgs()


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

        exerciseCategory = args.exerciseCategory
        setUpResources()
        setUpData()
        listenForChanges()

//        "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    }

    private fun setUpResources(){

        eachExerciseAdapter = this.context?.let { EachExerciseAdapter(exerciseDescription, it) }
        recyclerView!!.adapter = eachExerciseAdapter
        recyclerView!!.layoutManager = LinearLayoutManager(context)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUpData(){

        exerciseDescription.add(EachExerciseCard("1. Keep your back straight"))
        exerciseDescription.add(EachExerciseCard("2. Keep your chest up"))
        exerciseDescription.add(EachExerciseCard("3. Keep your feet shoulder width apart"))
        exerciseDescription.add(EachExerciseCard("4. Breathe in and breathe out"))
        eachExerciseAdapter?.notifyDataSetChanged()

        exerciseTitle.text = args.exerciseName
        exerciseMuscleGroup.text = "Muscle Group: Sample muscle group"

//        exerciseVideo.setVideoURI(Uri.parse("https://vimeo.com/123135208"))
//        exerciseVideo.requestFocus()
//        exerciseVideo.start()

//        val mediaController: MediaController = MediaController(context)
//        exerciseVideo.setMediaController(mediaController)
//        mediaController.setAnchorView(exerciseVideo)



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
                    println(snap)

                    if(snap.key == "muscle groups") {
                        exerciseMuscleGroup.text = "Muscle Group: " + snap.value.toString()
                    }
                    if(snap.key == "equipment") {
                        val equipmentList = snap.value as ArrayList<String>
                        val equipment = equipmentList[0]
                        exerciseEquipment.text = "Equipment needed: $equipment"
                    }

                    if(snap.key == "description") {
                        var description:String = snap.value as String

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
                }
                eachExerciseAdapter?.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
                // not implemented
            }
        })
    }

}