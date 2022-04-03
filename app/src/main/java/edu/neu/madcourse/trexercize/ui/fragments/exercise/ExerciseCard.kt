package edu.neu.madcourse.trexercize.ui.fragments.exercise

class ExerciseCard {
    var image: Int? = null
    var title: String? = null

    constructor()
    constructor(title: String?, image:Int?){
        this.image = image
        this.title = title
    }
}