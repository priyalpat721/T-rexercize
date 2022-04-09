package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachCategory

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IndividualExerciseCard: IndividualExerciseListener {

    @SerializedName("title")
    @Expose
    var exerciseName: String? = null

    constructor()
    constructor(exerciseName: String?) {
        this.exerciseName = exerciseName

    }
    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }
}