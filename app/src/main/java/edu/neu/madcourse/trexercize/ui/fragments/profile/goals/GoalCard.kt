package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

import kotlin.properties.Delegates

class GoalCard {
    private var title : String
    private  var time : String
    private var favorite by Delegates.notNull<Boolean>()
    private var done by Delegates.notNull<Boolean>()

    constructor(title: String, time: String, favorite: Boolean, done: Boolean) {
        this.title = title
        this.time = time
        this.favorite = favorite
        this.done = done
    }

    override fun toString(): String {
        return "GoalCard(title='$title', time='$time', favorite=$favorite, done=$done)"
    }
}