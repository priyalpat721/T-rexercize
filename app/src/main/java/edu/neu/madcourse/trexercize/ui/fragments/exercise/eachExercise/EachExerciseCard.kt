package edu.neu.madcourse.trexercize.ui.fragments.exercise.eachExercise

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EachExerciseCard {

    @SerializedName("title")
    @Expose
    var description: String? = null

    constructor()
    constructor(description: String?) {
        this.description = description

    }
}