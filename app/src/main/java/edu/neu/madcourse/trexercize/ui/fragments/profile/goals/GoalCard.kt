package edu.neu.madcourse.trexercize.ui.fragments.profile.goals

class GoalCard(
    var id : String,
    var goal: String,
    var time: String,
    var favorite: Boolean,
    var done: Boolean
) : IDoneCheckBoxListener, IFavCheckBoxListener{

    override fun onFavBoxClick(position: Int) {
        favorite = !favorite
    }

    override fun onDoneBoxClick(position: Int) {
        done = !done
    }

    override fun toString(): String {
        return "GoalCard(id='$id', task='$goal', time='$time', favorite=$favorite, done=$done)"
    }
}