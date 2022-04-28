package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.annotation.SuppressLint
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
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class DayFragment : Fragment(R.layout.fragment_day) {
    private val args: DayFragmentArgs by navArgs()
    private val exerciseList: MutableList<ExerciseTextCard> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var stickerScroll: HorizontalScrollView
    private lateinit var stickerBack: ImageButton
    private lateinit var stickerForward: ImageButton
    private lateinit var changeSnapButton: Button
    private lateinit var saveSnapButton: Button
    private lateinit var saveMoodButton: Button
    private lateinit var changeMoodButton: Button
    private lateinit var restDayButton: Button
    private lateinit var snapImage: ImageView
    private lateinit var moodImage: ImageView
    private lateinit var noWorkout: ConstraintLayout
    private lateinit var speechText: TextView
    lateinit var adapter: ExerciseTextAdapter
    private var path: Uri? = null
    private lateinit var storageRef: StorageReference
    private var db = Firebase.database.reference
    private lateinit var storage: FirebaseStorage
    private var mood: String = "none"
    private lateinit var userCalendar: String
    private lateinit var currentLocalDate: LocalDate
    private lateinit var selectedLocalDate: LocalDate
    private var snapLocalDate: LocalDate? = null
    private var currentStreak by Delegates.notNull<Int>()
    private var longestStreak by Delegates.notNull<Int>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get the current date
        val currentDay = SimpleDateFormat("M-d-yyyy", Locale.getDefault()).format(
            Date()
        )

        // the title of the page is the date clicked on the calendar
        val dateBox = view.findViewById<TextView>(R.id.currentDateText)
        dateBox.text = args.date

        // parse the dates into LocalDate
        currentLocalDate = LocalDate.parse(currentDay, DateTimeFormatter.ofPattern("M-d-yyyy"))
        selectedLocalDate = LocalDate.parse(args.date, DateTimeFormatter.ofPattern("M-d-yyyy"))

        // get the date of most recent snap and streak info
        db.child("users").child(Firebase.auth.currentUser?.uid.toString())
            .child("streakInfo")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children) {
                        if (snap.key == "last snap date") {
                            if (snap.value.toString() != "none") {
                                snapLocalDate = LocalDate.parse(
                                    snap.value.toString(),
                                    DateTimeFormatter.ofPattern("M-d-yyyy")
                                )
                            }
                        }
                        if (snap.key == "current streak count") {
                            currentStreak = parseInt(snap.value.toString())
                        }
                        if (snap.key == "longest streak count") {
                            longestStreak = parseInt(snap.value.toString())
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // not implemented
                }
            })

        val backBtn = view.findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            val action: NavDirections = DayFragmentDirections.actionDayFragmentToCalendarFragment()
            view.findNavController().navigate(action)
        }
        // recycler view to show exercises for the day
        recyclerView = view.findViewById(R.id.workoutRecycler)
        //adapter = ExerciseTextAdapter(view.context)
        adapter = ExerciseTextAdapter(exerciseList, this.requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        moodImage = view.findViewById(R.id.moodSticker)
        speechText = view.findViewById(R.id.noWorkout)

        // populate day view with snap, workouts, and mood
        Firebase.auth.currentUser?.uid?.let { it1 ->
            db.child("users").child(
                it1
            ).child("calendar").get().addOnSuccessListener {
                userCalendar = it.value.toString()
                // populate the page with data from database
                db.child("calendars").child(userCalendar).child(args.date)
                    .addValueEventListener(object : ValueEventListener {
                        @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
                        override fun onDataChange(snapshot: DataSnapshot) {
                            exerciseList.clear()
                            for (snap in snapshot.children) {
                                if (snap.key == "dailySnap") {
                                    // set pic image view
                                    snapImage = view.findViewById(R.id.snapImage)
                                    context?.let { it2 ->
                                        Glide.with(it2).load(snap.value).into(snapImage)
                                    }
                                    if (args.date == currentDay) {
                                        changeSnapButton.text = "Edit snap"
                                        changeSnapButton.visibility = VISIBLE
                                    }
                                }
                                if (snap.key == "workout") {
                                    noWorkout = view.findViewById(R.id.noRecycler)
                                    noWorkout.visibility = GONE
                                    // set the workout items to exercise list
                                    val workoutInfo = snap.value as Map<String, String>
                                    for (exercise in workoutInfo) {
                                        val exerciseInfo = exercise.value as Map<String, String>
                                        for (info in exerciseInfo) {
                                            if (info.key == "muscle groups") {
                                                val exerciseString = exercise.key
                                                val muscleString = info.value
                                                exerciseList.add(
                                                    ExerciseTextCard(
                                                        exerciseString,
                                                        muscleString
                                                    )
                                                )
                                                adapter.notifyDataSetChanged()
                                            }
                                        }

                                    }
                                }
                                if (snap.key == "mood") {
                                    // set the mode image view
                                    if (args.date == currentDay) {
                                        changeMoodButton.visibility = VISIBLE
                                    }
                                    saveMoodButton.visibility = GONE
                                    stickerScroll.visibility = GONE
                                    stickerBack.visibility = GONE
                                    stickerForward.visibility = GONE
                                    moodImage.visibility = VISIBLE
                                    mood = snap.value.toString()
                                    if (mood == "motivated") {
                                        moodImage.setImageResource(R.drawable.stickermotivatedino)
                                    }
                                    if (mood == "hungry") {
                                        moodImage.setImageResource(R.drawable.stickerbubbledino)
                                    }
                                    if (mood == "happy") {
                                        moodImage.setImageResource(R.drawable.stickerhappydino)
                                    }
                                    if (mood == "frustrated") {
                                        moodImage.setImageResource(R.drawable.stickerfrustratedino)
                                    }
                                    if (mood == "energized") {
                                        moodImage.setImageResource(R.drawable.stickerexercisedino)
                                    }
                                    if (mood == "sad") {
                                        moodImage.setImageResource(R.drawable.stickersaddino)
                                    }
                                    if (mood == "sleepy") {
                                        moodImage.setImageResource(R.drawable.stickersleepdino)
                                    }
                                }
                                if (snap.key == "rest day") {
                                    if (snap.value == "true") {
                                        noWorkout = view.findViewById(R.id.noRecycler)
                                        noWorkout.visibility = VISIBLE
                                        val speechDino: ImageView =
                                            view.findViewById(R.id.speechdino)
                                        speechDino.setImageResource(R.drawable.sleepdino2)
                                        speechText.text = "Have a nice rest day! Make sure to take a rest snap!"
                                    }
                                    restDayButton.visibility = GONE
                                }

                            }
                            if (mood == "none") {
                                if (args.date == currentDay) {
                                    saveMoodButton.visibility = VISIBLE
                                    stickerScroll.visibility = VISIBLE
                                    stickerBack.visibility = VISIBLE
                                    stickerForward.visibility = VISIBLE
                                } else {
                                    moodImage.visibility = VISIBLE
                                    moodImage.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                                }
                            }
                            snapImage.visibility = VISIBLE
                            if (exerciseList.isEmpty()) {
                                noWorkout = view.findViewById(R.id.noRecycler)
                                noWorkout.visibility = VISIBLE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // NOT IMPLEMENTED
                        }
                    })
            }
        }

        // change daily snap image
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val imageUploader = ImageUploaderFunctions()
        snapImage = view.findViewById(R.id.snapImage)
        changeSnapButton = view.findViewById(R.id.change_snap)
        saveSnapButton = view.findViewById(R.id.save_snap)

        changeSnapButton.setOnClickListener {
            path =
                imageUploader.getImagePathFromCamera(this.context, request, this.requireActivity())
            changeSnapButton.visibility = GONE
            saveSnapButton.visibility = VISIBLE
            Log.i("FILE PATH PIC 1", path.toString())
        }

        saveSnapButton.setOnClickListener {
            //var calendar: String
            imageUploader.uploadImageFromCameraToDb(path!!, storageRef, db, this.context)
            // increase streak counter
            // change bool that shows if user saved a snap this day
            // also need longest streak info

            // update the current streak based on when last snap was taken
            // if the previously taken pic was before yesterday
            if (snapLocalDate == null
                || snapLocalDate!!.isBefore(currentLocalDate.minusDays(1))) {
                db.child("users")
                    .child(Firebase.auth.currentUser?.uid.toString())
                    .child("streakInfo").child("current streak count")
                    .setValue("1")
                currentStreak = 1
            }
            // if the previously taken pic was yesterday
            else if (snapLocalDate!!.isEqual(currentLocalDate.minusDays(1))) {
                currentStreak += 1
                db.child("users")
                    .child(Firebase.auth.currentUser?.uid.toString())
                    .child("streakInfo").child("current streak count")
                    .setValue(currentStreak.toString())
            }
            // update longest streak
            if (currentStreak > longestStreak) {
                db.child("users")
                    .child(Firebase.auth.currentUser?.uid.toString())
                    .child("streakInfo").child("longest streak count")
                    .setValue(currentStreak.toString())
            }
            // update last snap date
            db.child("users").child(Firebase.auth.currentUser?.uid.toString())
            .child("streakInfo").child("last snap date")
            .setValue(currentDay)

            saveSnapButton.visibility = GONE
            changeSnapButton.text = "Edit snap"
            changeSnapButton.visibility = VISIBLE
        }

        stickerScroll = view.findViewById(R.id.stickerScroll)
        stickerBack = view.findViewById(R.id.stickerBack)
        stickerForward = view.findViewById(R.id.stickerForward)
        saveMoodButton = view.findViewById(R.id.save_mood)
        changeMoodButton = view.findViewById(R.id.change_mood)
        moodImage = view.findViewById(R.id.moodSticker)

        // update the mood to the selected mood
        saveMoodButton.setOnClickListener {
            if (mood == "none") {
                Toast.makeText(context, "No mood selected", Toast.LENGTH_SHORT).show()
            } else {
                var calendar: String
                Firebase.auth.currentUser?.uid?.let { it1 ->
                    db.child("users").child(
                        it1
                    ).child("calendar").get().addOnSuccessListener {
                        calendar = it.value.toString()
                        db.child("calendars").child(calendar).child(args.date).child("mood")
                            .setValue(mood)
                    }
                }
                saveMoodButton.visibility = GONE
                stickerScroll.visibility = GONE
                stickerBack.visibility = GONE
                stickerForward.visibility = GONE
                moodImage.visibility = VISIBLE
                changeMoodButton.visibility = VISIBLE
            }
        }

        // to edit the mood
        changeMoodButton.setOnClickListener {
            stickerScroll.visibility = VISIBLE
            stickerBack.visibility = VISIBLE
            stickerForward.visibility = VISIBLE
            moodImage.visibility = GONE
            changeMoodButton.visibility = GONE
            saveMoodButton.visibility = VISIBLE
        }

        // rest day button for rest days
        restDayButton = view.findViewById(R.id.rest_day)
        if (args.date == currentDay) {
            restDayButton.visibility = VISIBLE
        }

        // for days where we need to rest
        restDayButton.setOnClickListener {
            // save that this day was a rest day
            var calendar: String
            Firebase.auth.currentUser?.uid?.let { it1 ->
                db.child("users").child(
                    it1
                ).child("calendar").get().addOnSuccessListener {
                    calendar = it.value.toString()
                    db.child("calendars").child(calendar).child(args.date)
                        .child("rest day")
                        .setValue("true")
                }
            }

            // display dino saying it is a rest day
            val speechDino: ImageView = view.findViewById(R.id.speechdino)
            speechDino.setImageResource(R.drawable.sleepdino2)
            speechText.text = getString(R.string.restSnap)

            restDayButton.visibility = GONE
        }

        // scroll through stickers with arrow buttons
        // https://stackoverflow.com/questions/16079486/scrolling-a-horizontalscrollview-by-clicking-buttons-on-its-sides
        stickerBack.setOnTouchListener(object : OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            private var mHandler: Handler? = null
            private val mInitialDelay: Long = 100
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
            private val mInitialDelay: Long = 100
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
            restDayButton.visibility = GONE
        }
    }

    private val request =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                snapImage.setImageURI(path)
            }
        }
}