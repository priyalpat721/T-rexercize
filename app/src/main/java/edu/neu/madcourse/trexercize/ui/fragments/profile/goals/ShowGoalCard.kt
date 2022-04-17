package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

class ShowGoalCard(
    var id: String,
    var goal: String,
    var time: String,
    var favorite: String,
    var done: Boolean
) {

    override fun toString(): String {
        return "GoalCard(id='$id', task='$goal', time='$time', favorite=$favorite, done=$done)"
    }
}