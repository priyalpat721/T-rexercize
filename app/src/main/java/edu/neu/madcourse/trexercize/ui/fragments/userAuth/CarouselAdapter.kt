package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide

class CarouselAdapter(
    private var carouselList: ArrayList<CarouselItem>,
    private var viewPager: ViewPager2,
    private var context: Context,
    private var layout: Int
) : RecyclerView.Adapter<CarouselViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layout, parent, false)
        return CarouselViewHolder(view)
    }

    override fun getItemCount(): Int {
        return carouselList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        Glide.with(context).load(carouselList[position].imageUrl).into(holder.image)
        if (position == carouselList.size - 1) {
            viewPager.post(run)
        }
    }

    val run = Runnable {
        carouselList.addAll(carouselList)
        notifyDataSetChanged()
    }
}