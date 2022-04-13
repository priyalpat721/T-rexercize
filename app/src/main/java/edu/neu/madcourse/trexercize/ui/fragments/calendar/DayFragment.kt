package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.View.FOCUS_DOWN
import android.view.View.FOCUS_UP
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import edu.neu.madcourse.trexercize.R
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.ui.fragments.profile.ProfileAdapter
import edu.neu.madcourse.trexercize.ui.fragments.profile.ProfileCard

class DayFragment : Fragment(R.layout.fragment_day) {
    private val args : DayFragmentArgs by navArgs()
    private val exerciseList: MutableList<ExerciseTextCard> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var stickerScroll: HorizontalScrollView
    private lateinit var stickerBack: ImageButton
    private lateinit var stickerForward: ImageButton
    lateinit var adapter: ExerciseTextAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateBox = view.findViewById<TextView>(R.id.currentDateText)
        dateBox.text = args.date

        val backBtn = view.findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            val action: NavDirections = DayFragmentDirections.actionDayFragmentToCalendarFragment()
            view.findNavController().navigate(action)
        }
        recyclerView = view.findViewById(R.id.workoutRecycler)
        adapter = ExerciseTextAdapter(view.context)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(context)

        exerciseList.add(ExerciseTextCard("pushup", "arm"))
        exerciseList.add(ExerciseTextCard("potato", "leg"))
        exerciseList.add(ExerciseTextCard("presses", "chest"))
        exerciseList.add(ExerciseTextCard("run", "leg"))
        exerciseList.add(ExerciseTextCard("swim", "heart"))
        adapter?.setDataList(exerciseList)

        stickerScroll = view.findViewById(R.id.stickerScroll)
        stickerBack = view.findViewById(R.id.stickerBack)
        stickerForward = view.findViewById(R.id.stickerForward)

        stickerBack.setOnClickListener {
            stickerScroll.smoothScrollTo(stickerScroll.scrollX - 50, stickerScroll.scrollY)
        }
        stickerForward.setOnClickListener {
            stickerScroll.smoothScrollTo(stickerScroll.scrollX + 50, stickerScroll.scrollY)
        }
    }
}