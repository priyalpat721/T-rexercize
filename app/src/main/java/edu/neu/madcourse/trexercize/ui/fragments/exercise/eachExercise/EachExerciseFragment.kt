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
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory.IndividualExerciseFragmentArgs


class EachExerciseFragment : Fragment(R.layout.each_exercise_layout) {

    private val exerciseDescription: MutableList<EachExerciseCard> = ArrayList()
    private var recyclerView: RecyclerView? = null
    private var eachExerciseAdapter: EachExerciseAdapter?  = null
    private lateinit var exerciseTitle: TextView
    private lateinit var exerciseVideo: VideoView
    private lateinit var exerciseMuscleGroup: TextView
    private lateinit var exerciseDescriptionText: TextView
    private lateinit var addToFavorites: Button
    private lateinit var addToTodayWorkout: Button
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

        setUpResources()
        setUpData()

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

        exerciseVideo.setVideoURI(Uri.parse("https://vimeo.com/123135208"))
//        exerciseVideo.requestFocus()
//        exerciseVideo.start()

        val mediaController: MediaController = MediaController(context)
        exerciseVideo.setMediaController(mediaController)
        mediaController.setAnchorView(exerciseVideo)



    }
}