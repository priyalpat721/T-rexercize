package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.CalendarView
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.neu.madcourse.trexercize.R


open class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private lateinit var calendar: CalendarView
    private lateinit var quotesBox: TextView
    private var quoteApi : String = "https://api.quotable.io/random?maxLength=100"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val dino = view.findViewById<ImageView>(R.id.dino)
//        dino.visibility = if (resources.configuration.orientation
//            == Configuration.ORIENTATION_LANDSCAPE) View.GONE else View.VISIBLE

        quotesBox = view.findViewById(R.id.quotes)
        getQuote(quotesBox)

        calendar = view.findViewById(R.id.calendar_view)
        getDayView(calendar)

    }

    private fun getDayView(calendar: CalendarView) {
        calendar.setOnDateChangeListener { cal, i, i2, i3 ->
            // ignore
            println(cal)
            // keep
            val day = Integer.valueOf(i2 + 1)
            val d = "$day/$i3/$i"

            val action: NavDirections
            action = CalendarFragmentDirections.actionCalendarFragmentToDayFragment()
            d.also { action.date = it }
            view?.findNavController()?.navigate(action)
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