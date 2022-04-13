package edu.neu.madcourse.trexercize.ui.fragments.userAuth


class CarouselItem(image: String) {
    var imageUrl : String? = image

    override fun toString(): String {
        return "CarouselItem(image=$imageUrl)"
    }
}