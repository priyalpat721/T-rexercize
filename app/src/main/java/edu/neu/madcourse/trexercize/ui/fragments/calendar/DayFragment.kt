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
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.helper.ImageUploaderFunctions


class DayFragment : Fragment(R.layout.fragment_day) {
    private val args : DayFragmentArgs by navArgs()
    private val exerciseList: MutableList<ExerciseTextCard> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var stickerScroll: HorizontalScrollView
    private lateinit var stickerBack: ImageButton
    private lateinit var stickerForward: ImageButton
    private lateinit var changeSnapButton: Button
    private lateinit var snapImage: ImageView
    lateinit var adapter: ExerciseTextAdapter
    private var path: Uri? = null
    private lateinit var storageRef: StorageReference
    private var db = Firebase.database.reference
    private lateinit var storage: FirebaseStorage
    private var mood: String = "none"

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

        // change daily snap image
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val imageUploader = ImageUploaderFunctions()
        snapImage = view.findViewById(R.id.snapImage)
        changeSnapButton = view.findViewById(R.id.change_snap)
        changeSnapButton.setOnClickListener {
            path = imageUploader.getImagePathFromCamera(this.context, request, this.requireActivity())
            imageUploader.uploadImageFromCameraToDb(path!!, storageRef,  db, this.context)

            var calendar: String = ""
            Firebase.auth.currentUser?.uid?.let { it1 ->
                db.child("users").child(
                    it1
                ).child("calendar").get().addOnSuccessListener {
                    calendar = it.value.toString()
                    val currentDay = hashMapOf(
                        "dailySnap" to path.toString(),
                        "workout" to arrayListOf(exerciseList),
                        "mood" to mood
                    )
                    Log.i("CALENDAR", calendar)
                    db.child("calendars").child(calendar).child(args.date).setValue(currentDay)

                }
            }


        }

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
            mood = "motivated"
            Toast.makeText(
                this.context, "motivated sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val hungrySticker: ImageView = view.findViewById(R.id.hungrySticker)
        hungrySticker.setOnClickListener {
            mood = "hungry"
            Toast.makeText(
                this.context, "hungry sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val happySticker: ImageView = view.findViewById(R.id.happySticker)
        happySticker.setOnClickListener {
            mood = "happy"
            Toast.makeText(
                this.context, "happy sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val frustratedSticker: ImageView = view.findViewById(R.id.frustratedSticker)
        frustratedSticker.setOnClickListener {
            mood = "frustrated"
            Toast.makeText(
                this.context, "frustrated sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val energizedSticker: ImageView = view.findViewById(R.id.energizedSticker)
        energizedSticker.setOnClickListener {
            mood = "energized"
            Toast.makeText(
                this.context, "energized sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val sadSticker: ImageView = view.findViewById(R.id.sadSticker)
        sadSticker.setOnClickListener {
            mood = "sad"
            Toast.makeText(
                this.context, "sad sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
        val sleepySticker: ImageView = view.findViewById(R.id.sleepySticker)
        sleepySticker.setOnClickListener {
            mood = "sleepy"
            Toast.makeText(
                this.context, "sleepy sticker pressed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val request =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                snapImage.setImageURI(path)
            }
        }
}