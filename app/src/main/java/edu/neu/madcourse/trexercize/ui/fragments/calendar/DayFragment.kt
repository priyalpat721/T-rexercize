package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.neu.madcourse.trexercize.R


class DayFragment : Fragment(R.layout.fragment_day) {
    private val args : DayFragmentArgs by navArgs()
    private val exerciseList: MutableList<ExerciseTextCard> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var stickerScroll: HorizontalScrollView
    private lateinit var stickerBack: ImageButton
    private lateinit var stickerForward: ImageButton
    lateinit var adapter: ExerciseTextAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateBox = view.findViewById<TextView>(R.id.currentDateText)
        dateBox.text = args.date

        val backBtn = view.findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            val action: NavDirections = DayFragmentDirections.actionDayFragmentToCalendarFragment()
            view.findNavController().navigate(action)
        }
        // recycler view
        recyclerView = view.findViewById(R.id.workoutRecycler)
        adapter = ExerciseTextAdapter(view.context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        // some dummy exercises for now
        exerciseList.add(ExerciseTextCard("pushup", "arm"))
        exerciseList.add(ExerciseTextCard("potato", "leg"))
        exerciseList.add(ExerciseTextCard("presses", "chest"))
        exerciseList.add(ExerciseTextCard("run", "leg"))
        exerciseList.add(ExerciseTextCard("swim", "heart"))
        adapter.setDataList(exerciseList)

        stickerScroll = view.findViewById(R.id.stickerScroll)
        stickerBack = view.findViewById(R.id.stickerBack)
        stickerForward = view.findViewById(R.id.stickerForward)

        /*stickerBack.setOnClickListener {
            stickerScroll.smoothScrollTo(stickerScroll.scrollX - 100, stickerScroll.scrollY)
        }
        stickerForward.setOnClickListener {
            stickerScroll.smoothScrollTo(stickerScroll.scrollX + 100, stickerScroll.scrollY)
        }*/

        // scroll through stickers with arrow buttons
        // https://stackoverflow.com/questions/16079486/scrolling-a-horizontalscrollview-by-clicking-buttons-on-its-sides
        stickerBack.setOnTouchListener(object : OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            private var mHandler: Handler? = null
            private val mInitialDelay: Long = 300
            private val mRepeatDelay: Long = 100
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true
                        mHandler = Handler(Looper.getMainLooper())
                        mHandler!!.postDelayed(mAction, mInitialDelay)
                    }
                    MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true
                        mHandler!!.removeCallbacks(mAction)
                        mHandler = null
                    }
                }
                return false
            }

            var mAction: Runnable = object : Runnable {
                override fun run() {
                    stickerScroll.smoothScrollTo(stickerScroll.scrollX - 50, stickerScroll.scrollY)
                    mHandler?.postDelayed(this, mRepeatDelay)
                }
            }
        })
        stickerForward.setOnTouchListener(object : OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            private var mHandler: Handler? = null
            private val mInitialDelay: Long = 300
            private val mRepeatDelay: Long = 100
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true
                        mHandler = Handler(Looper.getMainLooper())
                        mHandler!!.postDelayed(mAction, mInitialDelay)
                    }
                    MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true
                        mHandler!!.removeCallbacks(mAction)
                        mHandler = null
                    }
                }
                return false
            }

            var mAction: Runnable = object : Runnable {
                override fun run() {
                    stickerScroll.smoothScrollTo(stickerScroll.scrollX + 50, stickerScroll.scrollY)
                    mHandler?.postDelayed(this, mRepeatDelay)
                }
            }
        })

        // listeners for stickers
        val motivatedSticker: ImageView = view.findViewById(R.id.motivatedSticker)
        motivatedSticker.setOnClickListener {
            Toast.makeText(
                this.context, "motivated sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val hungrySticker: ImageView = view.findViewById(R.id.hungrySticker)
        hungrySticker.setOnClickListener {
            Toast.makeText(
                this.context, "hungry sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val happySticker: ImageView = view.findViewById(R.id.happySticker)
        happySticker.setOnClickListener {
            Toast.makeText(
                this.context, "happy sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val frustratedSticker: ImageView = view.findViewById(R.id.frustratedSticker)
        frustratedSticker.setOnClickListener {
            Toast.makeText(
                this.context, "frustrated sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val energizedSticker: ImageView = view.findViewById(R.id.energizedSticker)
        energizedSticker.setOnClickListener {
            Toast.makeText(
                this.context, "energized sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val sadSticker: ImageView = view.findViewById(R.id.sadSticker)
        sadSticker.setOnClickListener {
            Toast.makeText(
                this.context, "sad sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val sleepySticker: ImageView = view.findViewById(R.id.sleepySticker)
        sleepySticker.setOnClickListener {
            Toast.makeText(
                this.context, "sleepy sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}