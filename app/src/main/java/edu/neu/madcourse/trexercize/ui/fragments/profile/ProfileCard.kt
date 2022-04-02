package edu.neu.madcourse.trexercize.ui.fragments.profile

class ProfileCard {
    var label: String? = null
    var value: String? = null

    constructor()
    constructor(label: String?, value: String?) {
        this.label = label
        this.value = value
    }

    override fun toString(): String {
        return "ProfileCard(label=$label, value=$value)"
    }
}