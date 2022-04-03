package edu.neu.madcourse.trexercize.ui.fragments.exercise

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExerciseCard: EachExerciseCardListener {

    @SerializedName("image")
    @Expose
    var image: Int? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    constructor()
    constructor(title: String?, image:Int?){
        this.image = image
        this.title = title
    }

    override fun onItemClick(position: Int) {

    }

}