package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R

class CarouselViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView = itemView.findViewById(R.id.carousel_images)
}