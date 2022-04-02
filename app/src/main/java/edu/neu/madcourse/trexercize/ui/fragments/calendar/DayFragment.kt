package edu.neu.madcourse.trexercize.ui.fragments.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.neu.madcourse.trexercize.R
import android.widget.TextView
import androidx.navigation.fragment.navArgs

/**
 * A simple [Fragment] subclass.
 * Use the [DayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayFragment : Fragment(R.layout.fragment_day) {
    private val args : DayFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        val dateBox = view.findViewById<TextView>(R.id.date)
        dateBox.text = args.date

        return view
    }
}