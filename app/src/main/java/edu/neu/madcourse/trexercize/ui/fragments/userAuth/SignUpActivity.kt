package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.HomeActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var signUp: Button
    private lateinit var switchToLogIn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var warningSignUp: TextView
    private var db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        warningSignUp = findViewById(R.id.warning_sign_up)
        warningSignUp.text = ""

        warningSignUp = findViewById(R.id.warning_sign_up)
        warningSignUp.text = ""
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        signUp = findViewById(R.id.sign_up)
        switchToLogIn = findViewById(R.id.to_login_page)
        progressBar = findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE
        switchToLogIn.setOnClickListener {
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

    @SuppressLint("SetTextI18n")
    private fun signup(emailAddress: String, nameText: String) {
        warningSignUp.text = ""

        if (emailAddress == "" || nameText == "") {
            Thread.sleep(1000)
            progressBar.visibility = View.GONE
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
                val time = Timestamp.now()
                if (task.isSuccessful) {
                    val newUser = hashMapOf(
                        "name" to nameText,
                        "password" to "password",
                        "email" to emailAddress,
                        "age" to 0,
                        "dateJoined" to time.toDate().toString(),
                        "favoriteRoutines" to arrayListOf(
                            ""
                        ),
                        "height" to hashMapOf(
                            "foot" to 0,
                            "inches" to 0
                        ),
                        "streak" to 0,
                        "weight" to 0,
                        "equipment" to arrayListOf(
                            ""
                        )
                    )


                    progressBar.visibility = View.VISIBLE

                    Firebase.auth.currentUser?.let {
                        db.child("users").child(it.uid).setValue(newUser)
                    }

                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(
                        baseContext, "Account successfully made.",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                    finish()
                    progressBar.visibility = View.GONE
                    startActivity(intent)


                } else {
                    Thread.sleep(1000)
                    progressBar.visibility = View.GONE

                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext, "Could not create an account. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}