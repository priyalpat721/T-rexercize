package edu.neu.madcourse.trexercize.ui.fragments.userAuth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import edu.neu.madcourse.trexercize.R


class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private lateinit var auth: FirebaseAuth
    private lateinit var warning: TextView
    private lateinit var usernameOrEmail: EditText
    private lateinit var signInButton: Button
    private lateinit var progressBar: ProgressBar
    private var db = Firebase.database.reference
    private lateinit var signUp: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUp = view.findViewById(R.id.to_sign_up_page)
        auth = Firebase.auth
        warning = view.findViewById(R.id.warning_sign)
        usernameOrEmail = view.findViewById(R.id.sign_in_email)
        signInButton = view.findViewById(R.id.sign_in_button)
        progressBar = view.findViewById(R.id.progress_bar_sign_in)
        progressBar.visibility = View.INVISIBLE

        signInButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val emailOrUsername = usernameOrEmail.text.toString()
            signIn(emailOrUsername)
            // get token for the user
        }

        signUp.setOnClickListener {


        }
    }

    @SuppressLint("SetTextI18n")
    private fun signIn(userNameOrEmail: String) {

        warning.text = ""

        if (userNameOrEmail.equals("")) {
            progressBar.visibility = View.VISIBLE
            Thread.sleep(1000)
            progressBar.visibility = View.GONE
            warning.text = "You don't have any text in the Email field. Please try again."
            return
        }

        auth.signInWithEmailAndPassword(userNameOrEmail, "password")
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {

                    val userRef = db.child("users")
                    userRef.get().addOnSuccessListener { document ->
                        if (document != null) {

                            progressBar.visibility = View.GONE
                        }
                    }
                        .addOnFailureListener { e ->
                        }
                } else {
                    Thread.sleep(1000)
                    progressBar.visibility = View.GONE
                    warning.text = "Email is invalid! Check email or sign up by clicking here."
                    warning.setOnClickListener {

                        progressBar.visibility = View.GONE

                    }
                }
            }
    }
}