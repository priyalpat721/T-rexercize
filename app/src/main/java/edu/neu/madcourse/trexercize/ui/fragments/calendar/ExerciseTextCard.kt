package edu.neu.madcourse.trexercize.ui.fragments.calendar

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExerciseTextCard{
    var exerciseName: String? = null
    var exerciseMuscleGroups: String? = null

    constructor()
    constructor(name: String?, groups:String?){
        this.exerciseName = name
        this.exerciseMuscleGroups = groups
    }
}