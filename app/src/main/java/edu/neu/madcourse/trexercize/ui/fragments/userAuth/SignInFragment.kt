package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import java.util.*


class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private lateinit var auth: FirebaseAuth
    private lateinit var warning: TextView
    private lateinit var usernameOrEmail: EditText
    private lateinit var signInButton: Button
    private lateinit var progressBar: ProgressBar
    private var db = Firebase.database.reference
    private lateinit var signUp: Button
    private var carouselList = ArrayList<CarouselItem>()
    var carouselHandler: Handler = Handler(Looper.getMainLooper())
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarouselAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView3)
        setUpResources()

        // makes the nav bar disappear
        val navBar: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation_bar)
        navBar?.visibility = View.INVISIBLE
        val title = view.findViewById<TextView>(R.id.sign_in_sign)
        title.visibility = View.VISIBLE
        signUp = view.findViewById(R.id.to_sign_up_page)
        auth = Firebase.auth
        warning = view.findViewById(R.id.warning_sign)
        usernameOrEmail = view.findViewById(R.id.sign_in_email)
        signInButton = view.findViewById(R.id.sign_in_button)
        signInButton.visibility = View.VISIBLE
        progressBar = view.findViewById(R.id.progress_bar_sign_in)
        signUp.visibility = View.VISIBLE
        usernameOrEmail.visibility = View.VISIBLE

        signInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val emailOrUsername = usernameOrEmail.text.toString()
            signIn(emailOrUsername)
            // get token for the user
            progressBar.visibility = View.INVISIBLE
        }

        signUp.setOnClickListener {
            val action : NavDirections =
                SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            view.findNavController().navigate(action)
        }


    }
    private val run = Runnable {
    }

    override fun onResume() {
        super.onResume()
        carouselHandler.postDelayed(run, 1500)
    }

    override fun onPause() {
        super.onPause()
        carouselHandler.removeCallbacks(run)
    }


    @SuppressLint("SetTextI18n")
    private fun signIn(userNameOrEmail: String) {

        warning.text = ""

        if (userNameOrEmail == "") {
            warning.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            Thread.sleep(1000)
            progressBar.visibility = View.INVISIBLE
            warning.text = "You don't have any text in the Email field. Please try again."
            return
        }

        auth.signInWithEmailAndPassword(userNameOrEmail, "password")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userRef = db.child("users")
                    userRef.get().addOnSuccessListener { document ->
                        if (document != null) {
                            val action : NavDirections =
                                SignInFragmentDirections.actionSignInFragmentToHomeScreenFragment()
                            progressBar.visibility = View.GONE
                            view?.findNavController()?.navigate(action)
                        }
                    }
                        .addOnFailureListener {
                        }
                } else {
                    warning.visibility = View.VISIBLE
                    Thread.sleep(1000)
                    progressBar.visibility = View.INVISIBLE
                    warning.text = "Email is invalid! Check email or sign up by clicking here."
                    warning.setOnClickListener {
                        val action : NavDirections =
                            SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                        progressBar.visibility = View.INVISIBLE
                        view?.findNavController()?.navigate(action)
                    }

                }
            }
    }

    private fun populateList() {
        carouselList.add(CarouselItem("https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/bubbledino.png?alt=media&token=8896212f-cc4e-4677-b7e6-45575e3c3a0f"))
        carouselList.add(CarouselItem("https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/exercisedino.png?alt=media&token=9ef0f90d-80fa-4e5d-9747-856baf0024c8"))
        carouselList.add(CarouselItem("https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/frustratedino.png?alt=media&token=59cbbe30-0715-46e8-910c-9958b14c2a30"))
        carouselList.add(CarouselItem("https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/happydino.png?alt=media&token=112f3cba-0272-4a0d-af18-263b3b65b9fb"))
        carouselList.add(CarouselItem("https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/motivatedino.png?alt=media&token=ccaf03c3-c640-4a7e-8ca1-58cd617313d3"))
        carouselList.add(CarouselItem("https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/saddino.png?alt=media&token=a2944664-2f88-4095-81e3-678c8bb0e316"))
        carouselList.add(CarouselItem("https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/sleepdino2.png?alt=media&token=44d3ce0d-cfac-4f1c-8f3b-484aaba37343"))
    }

    private fun setUpResources() {

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = layoutManager
        val helper = PagerSnapHelper()
        helper.attachToRecyclerView(recyclerView)

        populateList()
        adapter = CarouselAdapter(carouselList, this.requireContext())
        recyclerView.adapter = adapter

        val timer = Timer()
        timer.schedule(CarouselTimerTask(layoutManager, adapter, recyclerView), 0, 1500)
    }
    private class CarouselTimerTask(var layoutManager: LinearLayoutManager,
                                    var adapter: CarouselAdapter, var recyclerView: RecyclerView
    ) : TimerTask() {
        override fun run() {
            if (layoutManager.findLastCompletelyVisibleItemPosition() < (adapter.itemCount - 1)) {
                layoutManager.smoothScrollToPosition(
                    recyclerView, RecyclerView.State(),
                    layoutManager.findLastCompletelyVisibleItemPosition() + 1
                )
            } else {
                layoutManager.smoothScrollToPosition(recyclerView, RecyclerView.State(), 0)
            }
        }
    }
}