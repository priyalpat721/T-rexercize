package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.neu.madcourse.trexercize.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.time.format.DateTimeFormatter


open class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private lateinit var calendar: CalendarView
    private lateinit var quotesBox: TextView
    private var quoteApi : String = "https://api.quotable.io/random?maxLength=150"
    private lateinit var currentLocalDate: LocalDate
    private lateinit var selectedLocalDate: LocalDate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quotesBox = view.findViewById(R.id.quotes)
        getQuote(quotesBox)
        // calendar view
        calendar = view.findViewById(R.id.calendar_view)
        getDayView(calendar)

    }

    private fun getDayView(calendar: CalendarView) {
        calendar.setOnDateChangeListener { _, i, i2, i3 ->
            // keep
            val month = Integer.valueOf(i2 + 1)
            val d = "$month-$i3-$i"

            val action: NavDirections
            action = CalendarFragmentDirections.actionCalendarFragmentToDayFragment()
            d.also { action.date = it }
            // check if the date clicked is after current date
            // get the current day
            val currentDay = SimpleDateFormat("M-d-yyyy", Locale.getDefault()).format(
                Date()
            )
            // parse the dates into LocalDate
            currentLocalDate = LocalDate.parse(currentDay, DateTimeFormatter.ofPattern("M-d-yyyy"))
            selectedLocalDate = LocalDate.parse(d, DateTimeFormatter.ofPattern("M-d-yyyy"))

            if (selectedLocalDate.isBefore(currentLocalDate)
                || selectedLocalDate.isEqual(currentLocalDate)) {
                view?.findNavController()?.navigate(action)
            }
        }
    }

    private fun getQuote(quotesBox: TextView?) {
        val volley = Volley.newRequestQueue(activity)
        val response = JsonObjectRequest(
            Request.Method.GET, quoteApi, null,
            {
                    res ->
                val quote = res.getString("content")
                val author = res.getString("author")
                "$quote\n~ $author".also {
                    if (quotesBox != null) {
                        quotesBox.text = it
                    }
                }

            },
            {
                "You can do it!".also {
                    if (quotesBox != null) {
                        quotesBox.text = it
                    }
                }
            }
        )
        volley.add(response)
    }


}