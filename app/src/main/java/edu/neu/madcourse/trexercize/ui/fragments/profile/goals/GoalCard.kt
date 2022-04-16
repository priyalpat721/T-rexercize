package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

class GoalCard(
    var task: String,
    var time: String,
    var favorite: Boolean,
    var done: Boolean
) : IDoneCheckBoxListener, IFavCheckBoxListener{

    override fun toString(): String {
        return "GoalCard(title='$task', time='$time', favorite=$favorite, done=$done)"
    }

    override fun onFavBoxClick(position: Int) {
        favorite = !favorite
    }

    override fun onDoneBoxClick(position: Int) {
        done = !done
    }
}