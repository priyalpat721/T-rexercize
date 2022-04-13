package edu.neu.madcourse.trexercize.ui.fragments.homescreen

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FavoriteExerciseCard {
    @SerializedName("favoriteExercise")
    @Expose
    var favoriteExercise: String? = null

    @SerializedName("exerciseCategory")
    @Expose
    var exerciseCategory: String? = null

    @SerializedName("categoryImage")
    @Expose
    var categoryImage: Int? = null

    constructor(favoriteExercise: String?, exerciseCategory: String?, categoryImage: Int?){
        this.favoriteExercise = favoriteExercise
        this.exerciseCategory = exerciseCategory
        this.categoryImage = categoryImage
    }
}