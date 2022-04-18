package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.annotation.SuppressLint
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.helper.ImageUploaderFunctions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DayFragment : Fragment(R.layout.fragment_day) {
    private val args : DayFragmentArgs by navArgs()
    private val exerciseList: MutableList<ExerciseTextCard> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var stickerScroll: HorizontalScrollView
    private lateinit var stickerBack: ImageButton
    private lateinit var stickerForward: ImageButton
    private lateinit var changeSnapButton: Button
    private lateinit var saveSnapButton: Button
    private lateinit var saveMoodButton: Button
    private lateinit var snapImage: ImageView
    private lateinit var moodImage: ImageView
    private lateinit var noWorkout: ConstraintLayout
    lateinit var adapter: ExerciseTextAdapter
    private var path: Uri? = null
    private var pathFromCloud : Uri? = null
    private lateinit var storageRef: StorageReference
    private var db = Firebase.database.reference
    private lateinit var storage: FirebaseStorage
    private var mood: String = "none"
    private lateinit var userCalendar : String

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
        //adapter = ExerciseTextAdapter(view.context)
        adapter = ExerciseTextAdapter(exerciseList, this.requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        moodImage = view.findViewById(R.id.moodSticker)

        Firebase.auth.currentUser?.uid?.let { it1 ->
            db.child("users").child(
                it1
            ).child("calendar").get().addOnSuccessListener {
                userCalendar = it.value.toString()
                println("Calendar " + userCalendar)
                // populate the page with data from database
                db.child("calendars").child(userCalendar).child(args.date)
                    .addValueEventListener(object: ValueEventListener {
                        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            exerciseList.clear()
                            for (snap in snapshot.children){
                                if (snap.key == "dailySnap") {
                                    // set pic image view
                                    changeSnapButton.visibility = GONE
                                    snapImage = view.findViewById(R.id.snapImage)
                                    context?.let { it2 -> Glide.with(it2).load(snap.value).into(snapImage) }
                                }
                                if (snap.key == "workout") {
                                    noWorkout = view.findViewById(R.id.noRecycler)
                                    noWorkout.visibility = GONE
                                    // set the workout items to exercise list
                                    val workoutInfo = snap.value as Map<String, String>
                                    for (exercise in workoutInfo) {
                                        //println("exercise $exercise")
                                        val exerciseInfo = exercise.value as Map<String, String>
                                        for (info in exerciseInfo) {
                                            //println("info $info")
                                            if (info.key == "muscle groups") {
                                                val exerciseString = exercise.key
                                                val muscleString = info.value
                                                //exerciseList.add(ExerciseTextCard("exerciseString", "muscleString"))
                                                exerciseList.add(ExerciseTextCard(exerciseString, muscleString))
                                                println("EXERCISE: $exerciseString MUSCLE: $muscleString")
                                                adapter?.notifyDataSetChanged()
                                            }
                                        }

                                    }
                                }
                                if (snap.key == "mood") {
                                    // set the mode image view
                                    saveMoodButton.visibility = GONE
                                    stickerScroll.visibility = GONE
                                    stickerBack.visibility = GONE
                                    stickerForward.visibility = GONE
                                    moodImage.visibility = VISIBLE
                                    mood = snap.value.toString()
                                    if (mood == "motivated"){
                                        moodImage.setImageResource(R.drawable.stickermotivatedino)
                                    }
                                    if (mood == "hungry"){
                                        moodImage.setImageResource(R.drawable.stickerbubbledino)
                                    }
                                    if (mood == "happy"){
                                        moodImage.setImageResource(R.drawable.stickerhappydino)
                                    }
                                    if (mood == "frustrated"){
                                        moodImage.setImageResource(R.drawable.stickerfrustratedino)
                                    }
                                    if (mood == "energized"){
                                        moodImage.setImageResource(R.drawable.stickerexercisedino)
                                    }
                                    if (mood == "sad"){
                                        moodImage.setImageResource(R.drawable.stickersaddino)
                                    }
                                    if (mood == "sleepy"){
                                        moodImage.setImageResource(R.drawable.stickersleepdino)
                                    }
                                }

                            }

                        }
                        override fun onCancelled(error: DatabaseError) {
                            // NOT IMPLEMENTED
                        }
                    })
            }
        }

        var currentDay = SimpleDateFormat("M-d-yyyy", Locale.getDefault()).format(
            Date()
        )
        // change daily snap image
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val imageUploader = ImageUploaderFunctions()
        snapImage = view.findViewById(R.id.snapImage)
        changeSnapButton = view.findViewById(R.id.change_snap)
        saveSnapButton = view.findViewById(R.id.save_snap)

        changeSnapButton.setOnClickListener {
            path = imageUploader.getImagePathFromCamera(this.context, request, this.requireActivity())
            changeSnapButton.visibility = GONE
            saveSnapButton.visibility = VISIBLE
            Log.i("FILE PATH PIC 1", path.toString())
        }

        saveSnapButton.setOnClickListener {
            var calendar : String
            imageUploader.uploadImageFromCameraToDb(path!!, storageRef,  db, this.context)
            saveSnapButton.visibility = GONE
        }

        stickerScroll = view.findViewById(R.id.stickerScroll)
        stickerBack = view.findViewById(R.id.stickerBack)
        stickerForward = view.findViewById(R.id.stickerForward)
        saveMoodButton = view.findViewById(R.id.save_mood)
        moodImage = view.findViewById(R.id.moodSticker)

        saveMoodButton.setOnClickListener {
            if (mood == "none") {
                Toast.makeText(context, "No mood selected",Toast.LENGTH_SHORT).show()
            } else {
                var calendar: String
                Firebase.auth.currentUser?.uid?.let { it1 ->
                    db.child("users").child(
                        it1
                    ).child("calendar").get().addOnSuccessListener {
                        calendar = it.value.toString()
                        db.child("calendars").child(calendar).child(args.date).child("mood").setValue(mood)
                    }
                }
                saveMoodButton.visibility = GONE
                stickerScroll.visibility = GONE
                stickerBack.visibility = GONE
                stickerForward.visibility = GONE
                moodImage.visibility = VISIBLE
            }
        }

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
        val motivatedSticker: ImageView = view.findViewById(R.id.motivatedSticker)
        val hungrySticker: ImageView = view.findViewById(R.id.hungrySticker)
        val happySticker: ImageView = view.findViewById(R.id.happySticker)
        val frustratedSticker: ImageView = view.findViewById(R.id.frustratedSticker)
        val energizedSticker: ImageView = view.findViewById(R.id.energizedSticker)
        val sadSticker: ImageView = view.findViewById(R.id.sadSticker)
        val sleepySticker: ImageView = view.findViewById(R.id.sleepySticker)

        fun clearStickerSelection() {
            motivatedSticker.setBackgroundResource(0)
            hungrySticker.setBackgroundResource(0)
            happySticker.setBackgroundResource(0)
            frustratedSticker.setBackgroundResource(0)
            energizedSticker.setBackgroundResource(0)
            sadSticker.setBackgroundResource(0)
            sleepySticker.setBackgroundResource(0)
        }

        // listeners for stickers
        motivatedSticker.setOnClickListener {
            mood = "motivated"
            clearStickerSelection()
            motivatedSticker.setBackgroundResource(R.drawable.rectangle_bg_teal_100_radius_15)
        }

        hungrySticker.setOnClickListener {
            mood = "hungry"
            clearStickerSelection()
            hungrySticker.setBackgroundResource(R.drawable.rectangle_bg_teal_100_radius_15)
        }

        happySticker.setOnClickListener {
            mood = "happy"
            clearStickerSelection()
            happySticker.setBackgroundResource(R.drawable.rectangle_bg_teal_100_radius_15)
        }

        frustratedSticker.setOnClickListener {
            mood = "frustrated"
            clearStickerSelection()
            frustratedSticker.setBackgroundResource(R.drawable.rectangle_bg_teal_100_radius_15)
        }

        energizedSticker.setOnClickListener {
            mood = "energized"
            clearStickerSelection()
            energizedSticker.setBackgroundResource(R.drawable.rectangle_bg_teal_100_radius_15)
        }

        sadSticker.setOnClickListener {
            mood = "sad"
            clearStickerSelection()
            sadSticker.setBackgroundResource(R.drawable.rectangle_bg_teal_100_radius_15)
        }

        sleepySticker.setOnClickListener {
            mood = "sleepy"
            clearStickerSelection()
            sleepySticker.setBackgroundResource(R.drawable.rectangle_bg_teal_100_radius_15)
        }

        if (args.date != currentDay) {
            changeSnapButton.visibility = GONE
            saveMoodButton.visibility = GONE
        }
    }

    private val request =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                snapImage.setImageURI(path)
            }
        }
}