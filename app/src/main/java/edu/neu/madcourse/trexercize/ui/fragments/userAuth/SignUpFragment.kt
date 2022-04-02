package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R
import edu.neu.madcourse.trexercize.ui.HomeActivity


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var signUp: Button
    private lateinit var switchToLogIn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var auth: FirebaseAuth
    private lateinit var warningSignUp: TextView
    private var db = Firebase.database.reference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        warningSignUp = view.findViewById(R.id.warning_sign_up)
        warningSignUp.text = ""

        warningSignUp = view.findViewById(R.id.warning_sign_up)
        warningSignUp.text = ""
        name = view.findViewById(R.id.name)
        email = view.findViewById(R.id.email)
        signUp = view.findViewById(R.id.sign_up)
        switchToLogIn = view.findViewById(R.id.to_login_page)
        progressBar = view.findViewById(R.id.progress_bar)
        progressBar.visibility = View.GONE
        switchToLogIn.setOnClickListener {

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
            .addOnCompleteListener() { task ->
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
                        this.context, "Account successfully made.",
                        Toast.LENGTH_SHORT
                    ).show()

                    progressBar.visibility = View.GONE


                } else {
                    Thread.sleep(1000)
                    progressBar.visibility = View.GONE

                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        this.context, "Could not create an account. Please try again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}