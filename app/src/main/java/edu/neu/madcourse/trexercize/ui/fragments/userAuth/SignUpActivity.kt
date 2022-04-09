package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.HomeActivity
import edu.neu.madcourse.trexercize.ui.helper.CarouselData

class SignUpActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var signUp: Button
    private lateinit var switchToLogIn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var warningSignUp: TextView
    private var db = Firebase.database.reference
    private lateinit var viewPager: ViewPager2
    private var carouselList = ArrayList<CarouselItem>()
    var carouselHandler : Handler = Handler(Looper.getMainLooper())

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val title = findViewById<TextView>(R.id.welcome)
        title.visibility = View.VISIBLE
        warningSignUp = findViewById(R.id.warning_sign_up)
        warningSignUp.text = ""
        warningSignUp = findViewById(R.id.warning_sign_up)
        warningSignUp.text = ""
        name = findViewById(R.id.name)
        name.visibility = View.VISIBLE
        email = findViewById(R.id.email)
        email.visibility = View.VISIBLE
        signUp = findViewById(R.id.sign_up)
        signUp.visibility = View.VISIBLE
        switchToLogIn = findViewById(R.id.to_login_page)
        switchToLogIn.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progress_bar)
        viewPager = findViewById(R.id.carousel_up)

        populateList()
        val carouselData = CarouselData()
        carouselData.getCarouselData(carouselList, viewPager, run, carouselHandler, this)

        switchToLogIn.setOnClickListener {
            carouselList.clear()
            viewPager.adapter?.notifyDataSetChanged()
            val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
            finish()
            startActivity(intent)
        }

        signUp.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val emailAddress = email.text.toString()
            val nameText = name.text.toString()
            signup(emailAddress, nameText)
        }
        auth = Firebase.auth
    }

    private val run = Runnable {
        viewPager.currentItem = viewPager.currentItem + 1
    }

    override fun onResume() {
        super.onResume()
        carouselHandler.postDelayed(run, 1500)
    }

    override fun onPause() {
        super.onPause()
        carouselHandler.removeCallbacks(run)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

        })
    }

    @SuppressLint("SetTextI18n")
    private fun signup(emailAddress: String, nameText: String) {
        warningSignUp.text = ""

        if (emailAddress == "" || nameText == "") {
            warningSignUp.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            Thread.sleep(1000)
            progressBar.visibility = View.INVISIBLE
            warningSignUp.visibility = View.VISIBLE
            if (nameText == "" && emailAddress == "") {
                warningSignUp.text = "Name and email fields empty. Please provide above information"
            } else if (emailAddress == "") {
                warningSignUp.text = "Email Field empty. Please enter your Email."
            } else {
                warningSignUp.text = "Name Field empty. Please enter your Name."
            }

            return
        }

        auth.createUserWithEmailAndPassword(emailAddress, "password")
            .addOnCompleteListener(this@SignUpActivity) { task ->
                val calendar = arrayListOf(
                    ""
                )
                progressBar.visibility = View.VISIBLE

                if (task.isSuccessful) {
                    Thread.sleep(1000)
                    progressBar.visibility = View.INVISIBLE
                    val calendarDoc = db.child("calendars").push().key.toString()
                    db.child("calendars").child(calendarDoc).setValue(calendar)
                    Log.i("Calendar", calendarDoc)
                    val time = Timestamp.now()


                    val newUser = hashMapOf(
                        "name" to nameText,
                        "password" to "password",
                        "email" to emailAddress,
                        "age" to "0",
                        "dateJoined" to time.toDate().toString(),
                        "favoriteRoutines" to arrayListOf(
                            ""
                        ),
                        "foot" to "0",
                        "inches" to "0",
                        "streak" to "0",
                        "weight" to "0",
                        "equipment" to arrayListOf(
                            ""
                        ),
                        "calendar" to calendarDoc,
                        "targetAreas" to "",
                        "goals" to "",
                        "profilePicture" to "https://firebasestorage.googleapis.com/v0/b/t-rexercize.appspot.com/o/exercisedino.png?alt=media&token=9ef0f90d-80fa-4e5d-9747-856baf0024c8"
                    )
                    Firebase.auth.currentUser?.uid?.let {
                        db.child("users").child(it).setValue(newUser)
                    }


                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        baseContext, "Account successfully made.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                    finish()
                    progressBar.visibility = View.INVISIBLE
                    startActivity(intent)

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
}