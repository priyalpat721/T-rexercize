package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import edu.neu.madcourse.trexercize.R

class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private lateinit var calendar: CalendarView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendar = view.findViewById(R.id.calendar_view)
        calendar.setOnDateChangeListener { calendar, i, i2, i3 ->
            // ignore
            println(calendar)

            // keep
            val day = Integer.valueOf(i2 + 1)
            val d = "$day/$i3/$i"

            val action : NavDirections
            action = CalendarFragmentDirections.actionCalendarFragmentToDayFragment()
            action.date = d
            view.findNavController().navigate(action)
        }
    }
}