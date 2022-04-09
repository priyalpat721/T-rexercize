package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.HomeActivity
import edu.neu.madcourse.trexercize.ui.helper.CarouselData

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var warning: TextView
    private lateinit var usernameOrEmail: EditText
    private lateinit var signInButton: Button
    private lateinit var progressBar: ProgressBar
    private var db = Firebase.database.reference
    private lateinit var signUp: Button
    private lateinit var viewPager: ViewPager2
    private var carouselList = ArrayList<CarouselItem>()
    var carouselHandler : Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        populateList()

        val title = findViewById<TextView>(R.id.sign_in_sign)
        title.visibility = View.VISIBLE
        signUp = findViewById(R.id.to_sign_up_page)
        auth = Firebase.auth
        warning = findViewById(R.id.warning_sign)
        usernameOrEmail = findViewById(R.id.sign_in_email)
        signInButton = findViewById(R.id.sign_in_button)
        signInButton.visibility = View.VISIBLE
        progressBar = findViewById(R.id.progress_bar_sign_in)
        viewPager = findViewById(R.id.carousel)
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
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            finish()
            startActivity(intent)

        }
        val carouselData = CarouselData()
        carouselData.getCarouselData(carouselList, viewPager, run, carouselHandler, this)

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
            .addOnCompleteListener(this@SignInActivity) { task ->
                if (task.isSuccessful) {
                    val userRef = db.child("users")
                    userRef.get().addOnSuccessListener { document ->
                        if (document != null) {
                            val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                            finish()
                            progressBar.visibility = View.GONE
                            startActivity(intent)
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
                        val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
                        finish()
                        progressBar.visibility = View.INVISIBLE
                        startActivity(intent)

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
}