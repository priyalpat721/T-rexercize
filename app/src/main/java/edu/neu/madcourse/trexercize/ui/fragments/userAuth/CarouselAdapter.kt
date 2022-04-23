package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.neu.madcourse.trexercize.R

class CarouselAdapter(
    private var carouselList: ArrayList<CarouselItem>,
    private var context: Context,
) : RecyclerView.Adapter<CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.carousel_card_layout, parent, false)
        return CarouselViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {

        val pos = position % carouselList.size
        Glide.with(context).load(carouselList[pos].imageUrl).into(holder.image)
    }
}