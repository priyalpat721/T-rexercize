package edu.neu.madcourse.trexercize.ui.helper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.fragments.userAuth.CarouselAdapter
import edu.neu.madcourse.trexercize.ui.fragments.userAuth.CarouselItem

class CarouselData {

    @SuppressLint("NotifyDataSetChanged")
    fun getCarouselData(carouselList : ArrayList<CarouselItem>, viewPager : ViewPager2?, run : Runnable, carouselHandler : Handler, context : Context) {
        if (viewPager != null) {
            viewPager.adapter?.notifyDataSetChanged()

            viewPager.adapter =
                CarouselAdapter(carouselList, viewPager, context, R.layout.fragment_sign_up)
            viewPager.clipToPadding = false
            viewPager.clipChildren = false
            viewPager.offscreenPageLimit = 3
            viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            val composite = CompositePageTransformer()
            composite.addTransformer(MarginPageTransformer(40))
            composite.addTransformer { page, position ->
                page.apply {
                    val r = 1 - kotlin.math.abs(position)
                    page.alpha = 0.25f + r
                    page.scaleY = 0.85f + r * 0.15f
                }
            }

            viewPager.setPageTransformer(composite)
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    carouselHandler.removeCallbacks(run)
                    carouselHandler.postDelayed(run, 1500)
                }
            })

        }
    }
}