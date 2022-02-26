package edu.neu.madcourse.trexercize.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import edu.neu.madcourse.trexercize.R

class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private lateinit var calendar: CalendarView
    private lateinit var date : TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calendar = view.findViewById(R.id.calendar_view)
        date = view.findViewById(R.id.date)
        calendar.setOnDateChangeListener { calendar, i, i2, i3 ->
            // ignore
            println(calendar)

            // keep
            val day = Integer.valueOf(i2 + 1)
            val d = "$day/$i3/$i"
            date.text = d
        }
    }
}